package com.cjy.qiquan.task;

import com.cjy.qiquan.model.PartnerBankInfo;
import com.cjy.qiquan.model.User;
import com.cjy.qiquan.service.PartnerService;
import com.cjy.qiquan.service.UserService;
import com.cjy.qiquan.utils.CommonUtils;
import com.timevale.esign.sdk.tech.bean.result.AddSealResult;
import com.timevale.esign.sdk.tech.bean.result.GetAccountProfileResult;
import com.timevale.esign.sdk.tech.impl.constants.LicenseQueryType;

import cn.tsign.ching.eSign.SignHelper;

public class UpdateSignAccount extends TaskEvent {

	private int type;

	private int id;

	private int newOrEdit;

	public UpdateSignAccount(final int type, final int id, final int newOrEdit) {
		this.type = type;
		this.id = id;
		this.newOrEdit = newOrEdit;
	}

	@Override
	protected void impl() {
		if (type == 0) {
			// 个人
			User user = UserService.instance().getUserById(id);
			if (user != null && CommonUtils.hasText(user.getIdCard())) {
				if (newOrEdit == 1) {
					// 新增
					GetAccountProfileResult re = SignHelper.getAccountInfoByIdNo(user.getIdCard(),
							LicenseQueryType.MAINLAND);
					if (re.getAccountInfo()!=null && CommonUtils.hasText(re.getAccountInfo().getAccountUid())) {
						return;
					}
					String accountId = SignHelper.addPersonAccount(user);
					System.out.println(accountId);
					if (CommonUtils.hasText(accountId)) {
						UserService.instance().updateColById("f_signAccountId", accountId, user.getUserId());
						AddSealResult userPersonSealData = SignHelper.addPersonTemplateSeal(accountId);
						UserService.instance().updateColById("f_sealData", userPersonSealData.getSealData(),
								user.getUserId());
					}

				} else if (newOrEdit == 2) {
					// 修改
					GetAccountProfileResult re = SignHelper.getAccountInfoByIdNo(user.getIdCard(),
							LicenseQueryType.MAINLAND);
					if (re.getAccountInfo()==null || CommonUtils.isBlankOrNull(re.getAccountInfo().getAccountUid())) {
						String accountId = SignHelper.addPersonAccount(user);
						System.out.println(accountId);
						if (CommonUtils.hasText(accountId)) {
							UserService.instance().updateColById("f_signAccountId", accountId, user.getUserId());
							AddSealResult userPersonSealData = SignHelper.addPersonTemplateSeal(accountId);
							UserService.instance().updateColById("f_sealData", userPersonSealData.getSealData(),
									user.getUserId());
						}
					} else {
						SignHelper.updatePersonAccount(user.getSignAccountId(), user.getRealName(), user.getMobile());
						AddSealResult userPersonSealData = SignHelper.addPersonTemplateSeal(user.getSignAccountId());
						UserService.instance().updateColById("f_sealData", userPersonSealData.getSealData(),
								user.getUserId());
					}
				}
			}

		} else {
			// 合作机构
			
			PartnerBankInfo partner = PartnerService.instance().getPartnerBankInfoById(id);
			
			if (partner != null && CommonUtils.hasText(partner.getOrganCode())) {
				if (newOrEdit == 1) {
					// 新增
					GetAccountProfileResult re = SignHelper.getAccountInfoByIdNo(partner.getOrganCode(),
							LicenseQueryType.MERGE);
					if (re.getAccountInfo()!=null && CommonUtils.hasText(re.getAccountInfo().getAccountUid())) {
						PartnerService.instance().updatePartnerBankInfoColById("f_signAccountId", re.getAccountInfo().getAccountUid(), partner.getId());
						AddSealResult userOrganizeSealData = SignHelper.addOrganizeTemplateSeal(re.getAccountInfo().getAccountUid());
						PartnerService.instance().updatePartnerBankInfoColById("f_sealData", userOrganizeSealData.getSealData(),
								partner.getId());
					}

					String accountId = SignHelper.addOrganizeAccount(partner);
					System.out.println(accountId);
					if (CommonUtils.hasText(accountId)) {
						PartnerService.instance().updatePartnerBankInfoColById("f_signAccountId", accountId, partner.getId());
						SignHelper.updateOrganizeAccount(accountId, partner.getCompanyName(),
								"");
						
						AddSealResult userOrganizeSealData = SignHelper.addOrganizeTemplateSeal(accountId);
						PartnerService.instance().updatePartnerBankInfoColById("f_sealData", userOrganizeSealData.getSealData(),
								partner.getId());
					}
				} else if (newOrEdit == 2) {
					// 修改
					GetAccountProfileResult re = SignHelper.getAccountInfoByIdNo(partner.getOrganCode(),
							LicenseQueryType.MERGE);
					if (re.getAccountInfo()==null || CommonUtils.isBlankOrNull(re.getAccountInfo().getAccountUid())) {
						String accountId = SignHelper.addOrganizeAccount(partner);
						System.out.println(accountId);
						if (CommonUtils.hasText(accountId)) {
							PartnerService.instance().updatePartnerBankInfoColById("f_signAccountId", accountId, partner.getId());
							AddSealResult userOrganizeSealData = SignHelper.addOrganizeTemplateSeal(accountId);
							PartnerService.instance().updatePartnerBankInfoColById("f_sealData", userOrganizeSealData.getSealData(),
									partner.getId());
						}
					} else {
						SignHelper.updateOrganizeAccount(partner.getSignAccountId(), partner.getCompanyName(),
								"");
						AddSealResult userOrganizeSealData = SignHelper
								.addOrganizeTemplateSeal(partner.getSignAccountId());
						PartnerService.instance().updatePartnerBankInfoColById("f_sealData", userOrganizeSealData.getSealData(),
								partner.getId());
					}
				}
			}
		}
	}

}

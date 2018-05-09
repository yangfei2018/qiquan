package com.cjy.qiquan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.qiquan.cache.CacheManager;
import com.cjy.qiquan.dao.PartnerDao;
import com.cjy.qiquan.dao.UserDao;
import com.cjy.qiquan.model.Partner;
import com.cjy.qiquan.model.PartnerBankInfo;
import com.cjy.qiquan.model.User;
import com.cjy.qiquan.po.Page;
import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.SpringApplicationContext;

@Service
public class PartnerService {

	
	@Autowired
	private PartnerDao partnerDao;
	@Autowired
	private UserDao userDao;
	
	
	public static PartnerService instance() {
		return SpringApplicationContext.getBean("partnerService");
	}
	
	public int createPartner(Partner partner) {
		partner.setRole("partner");
		return partnerDao.createPartner(partner);
	}
	
	public int updatePartner(Partner partner) {
		return partnerDao.updatePartner(partner);
	}
	
	public int updateColById(final String col, final Object value, final int id) {
		return partnerDao.updateColById(col, value, id);
	}
	
	public Partner getPartnerByName(final String name) {
		return partnerDao.getPartnerByName(name);
	}
	
	public Partner getPartnerByCode(final String code) {
		return partnerDao.getPartnerByCode(code);
	}
	
	
	public Partner getPartnerById(final int id) {
		return partnerDao.getPartnerById(id);
	}
	
	public PartnerBankInfo getPartnerBankInfoById(final int id) {
		return partnerDao.getPartnerBankInfoById(id);
	}
	
	
	public List<PartnerBankInfo> listPartnerBankByPartnerId(final int partnerId){
		return partnerDao.listPartnerBankByPartnerId(partnerId);
	}

	public Partner getPartnerByNo(final String partnerNo) {
		return partnerDao.getPartnerByNo(partnerNo);
	}
	
	
	public int updatePartnerBankInfoColById(final String col, final Object value, final int id) {
		return partnerDao.updatePartnerBankInfoColById(col, value, id);
	}
	
	public int createPartnerBankInfo(PartnerBankInfo partnerBankInfo) {
		int ref = partnerDao.createPartnerBankInfo(partnerBankInfo);
		if (ref>0) {
			partnerBankInfo.setId(ref);
		}
		
		return ref;
	}
	
	public int updatePartnerBankInfoById(PartnerBankInfo partnerBankInfo) {
		return partnerDao.updatePartnerBankInfoById(partnerBankInfo);
	}
	
	public Page<Partner> listPartnerByGmUserId(final int userid, int index, int size){
		if (index <= 0) {
			index = 1;
		}
		if (size <= 0) {
			size = 20;
		}
		return partnerDao.listPartnerByGmUserId(userid, index, size);
	}
	
	/**
	 * 根据用户获取指定汇款信息
	 * @param user
	 * @return
	 */
	public PartnerBankInfo getPartnerBankInfoOnice(User user) {
		
		PartnerBankInfo partnerBankInfo = getPartnerBankInfoById(user.getPartnerbankinfoId());
		if (partnerBankInfo==null) {
			//获取代理商可用汇款账号
			Partner partner = getPartnerByCode(user.getPartnerCode());
			if (partner!=null) {
				//如果找到代理商
				List<PartnerBankInfo>  list = listPartnerBankByPartnerId(partner.getUserId());
				for(PartnerBankInfo p:list) {
					if (p.getUsedCount()<49) {
						partnerBankInfo = p;
						break;
					}
				}
				if (partnerBankInfo!=null) {
					int pos = getAvailablePos(partnerBankInfo);
					//更新用户选择状态
					userDao.updateColById("f_partnerbankinfoId", partnerBankInfo.getId(), user.getUserId());
					userDao.updateColById("f_partnerposition", pos, user.getUserId());
					//更新使用次数
					List<Integer> exitsList = userDao.listHehuoPos(partnerBankInfo.getId());
					updatePartnerBankInfoColById("f_usedCount", exitsList.size(), partnerBankInfo.getId());
				}				
			}
		}
		return partnerBankInfo;
	}
	
	
	private int getAvailablePos(PartnerBankInfo partnerBankInfo) {
		 List<Integer> exitsList = userDao.listHehuoPos(partnerBankInfo.getId());
		 int pos = 1;
		 for(int i=0;i<exitsList.size();i++) {
			 if (exitsList.get(i)>pos) {
				 break;
			 }
			 pos++;
		 }
		 
		 return pos;
	}
	
	
	public Partner getUserByToken(final String token) {
		if (CommonUtils.isBlankOrNull(token)) {
			return null;
		}
		String id = CacheManager.instance.getCache(Constant.CacheGroup.PARTNERSESSION, token);
		if (CommonUtils.isBlankOrNull(id)) {
			return null;
		}
		return getPartnerById(Integer.valueOf(id));
	}
	
}

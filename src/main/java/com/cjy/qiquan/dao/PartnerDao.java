package com.cjy.qiquan.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cjy.qiquan.dbhelper.JdbcHelper;
import com.cjy.qiquan.model.Partner;
import com.cjy.qiquan.model.PartnerBankInfo;
import com.cjy.qiquan.po.Page;

@Repository
public class PartnerDao {

	@Autowired
	private JdbcHelper jdbcHelper;

	private RowMapper<Partner> PARTNER = new RowMapper<Partner>() {
		@Override
		public Partner mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			Partner user = new Partner(resultSet);
			return user;
		}
	};

	private RowMapper<PartnerBankInfo> PARTNER_BANKINFO = new RowMapper<PartnerBankInfo>() {
		@Override
		public PartnerBankInfo mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			PartnerBankInfo user = new PartnerBankInfo(resultSet);
			return user;
		}
	};

	public int updateColById(final String col, final Object value, final int id) {
		StringBuilder sb = new StringBuilder("UPDATE `t_partner` SET ");
		sb.append(col).append("=? WHERE f_id=?");

		int ref = jdbcHelper.update(sb.toString(), value, id);
		return ref;
	}

	public int createPartner(Partner partner) {
		return jdbcHelper.executeUpdateReturnKeyGenerate("INSERT INTO `t_partner`(f_password_hash,"
				+ "f_realName,f_partnerNo,f_companyName,f_mobile,f_bankOfDeposit,f_idCardFrontPic,"
				+ "f_idCardBackgroundPic,f_businessLicencePic,f_code,f_name,f_belongtogmsuer,f_role,f_bankCardNo,f_signAccountId,f_organCode,f_idCard,f_bankhm) value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[] { partner.getPassword_hash(), partner.getRealName(), partner.getPartnerNo(),
						partner.getCompanyName(), partner.getMobile(), partner.getBankOfDeposit(),
						partner.getIdCardFrontPic(), partner.getIdCardBackgroundPic(), partner.getBusinessLicencePic(),
						partner.getCode(), partner.getName(), partner.getBelongtogmsuer(), partner.getRole(),
						partner.getBankCardNo(), partner.getSignAccountId(), partner.getOrganCode(),
						partner.getIdCard(),partner.getBankhm() });
	}

	public int updatePartner(Partner partner) {
		return jdbcHelper.update("UPDATE `t_partner` SET f_password_hash=?,"
				+ "f_realName=?,f_partnerNo=?,f_companyName=?,f_mobile=?,f_bankOfDeposit=?,f_idCardFrontPic=?,"
				+ "f_idCardBackgroundPic=?,f_businessLicencePic=?,f_code=?,f_name=?,f_belongtogmsuer=?,f_bankCardNo=?,f_organCode=?,f_idCard=?,f_bankhm=? WHERE f_id=?",
				new Object[] { partner.getPassword_hash(), partner.getRealName(), partner.getPartnerNo(),
						partner.getCompanyName(), partner.getMobile(), partner.getBankOfDeposit(),
						partner.getIdCardFrontPic(), partner.getIdCardBackgroundPic(), partner.getBusinessLicencePic(),
						partner.getCode(), partner.getName(), partner.getBelongtogmsuer(), partner.getBankCardNo(),
						partner.getOrganCode(), partner.getIdCard(),partner.getBankhm(), partner.getUserId() });
	}

	public Page<Partner> listPartnerByGmUserId(final int userid, int index, int size) {
		Page<Partner> page = new Page<>();
		page.setIndex(index);
		page.setSize(size);
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM `t_partner` WHERE 1=1");
		if (userid > 0) {
			sql.append(" AND f_belongtogmsuer=").append(userid);
		}

		int total = jdbcHelper.queryForObject("SELECT count(*) " + sql.toString(), Integer.class);
		page.setTotal(total);
		sql.append(" ORDER BY f_createTime DESC");
		sql.append(" LIMIT ").append((page.getIndex() - 1) * page.getSize()).append(" , ").append(page.getSize());
		List<Partner> list = jdbcHelper.query("SELECT * " + sql.toString(), PARTNER);
		page.setList(list);
		return page;

	}

	public Partner getPartnerByName(final String name) {
		return jdbcHelper.queryForObject("SELECT * FROM `t_partner` WHERE f_name=? ", new Object[] { name }, PARTNER);
	}

	public Partner getPartnerById(final int id) {
		return jdbcHelper.queryForObject("SELECT * FROM `t_partner` WHERE f_id=? ", new Object[] { id }, PARTNER);
	}

	public Partner getPartnerByNo(final String partnerNo) {
		return jdbcHelper.queryForObject("SELECT * FROM `t_partner` WHERE f_partnerNo=? ", new Object[] { partnerNo },
				PARTNER);
	}

	public Partner getPartnerByCode(final String code) {
		return jdbcHelper.queryForObject("SELECT * FROM `t_partner` WHERE f_code=? ", new Object[] { code }, PARTNER);
	}

	public PartnerBankInfo getPartnerBankInfoById(final int id) {
		return jdbcHelper.queryForObject("SELECT * FROM `t_partnerbankinfo` WHERE f_id=? ", new Object[] { id },
				PARTNER_BANKINFO);
	}

	public List<PartnerBankInfo> listPartnerBankByPartnerId(final int partnerId) {
		if (partnerId == 0) {
			return jdbcHelper.query("SELECT * FROM `t_partnerBankInfo`", new Object[] {}, PARTNER_BANKINFO);
		} else {
			return jdbcHelper.query("SELECT * FROM `t_partnerBankInfo` WHERE f_partnerId=? ",
					new Object[] { partnerId }, PARTNER_BANKINFO);
		}

	}

	public int createPartnerBankInfo(PartnerBankInfo partnerBankInfo) {
		return jdbcHelper.executeUpdateReturnKeyGenerate(
				"insert into  `t_partnerbankinfo`(f_companyName,f_bankName,f_bankNo,f_partnerId,f_usedCount,f_realName,f_hehuoName,f_address,f_organCode,f_signAccountId,f_sealData,f_idCard) value(?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[] { partnerBankInfo.getCompanyName(), partnerBankInfo.getBankName(),
						partnerBankInfo.getBankNo(), partnerBankInfo.getPartnerId(), partnerBankInfo.getUsedCount(),
						partnerBankInfo.getRealName(), partnerBankInfo.getHehuoName(), partnerBankInfo.getAddress(),
						partnerBankInfo.getOrganCode(), partnerBankInfo.getSignAccountId(),
						partnerBankInfo.getSealData(), partnerBankInfo.getIdCard() });
	}

	public int updatePartnerBankInfoById(PartnerBankInfo partnerBankInfo) {
		return jdbcHelper.update(
				"UPDATE `t_partnerbankinfo` SET f_companyName=?,f_bankName=?,f_bankNo=?,f_realName=?,f_hehuoName=?,f_address=?,f_organCode=?,f_signAccountId=?,f_sealData=?,f_idCard=? where f_id=?",
				new Object[] { partnerBankInfo.getCompanyName(), partnerBankInfo.getBankName(),
						partnerBankInfo.getBankNo(), partnerBankInfo.getRealName(), partnerBankInfo.getHehuoName(),
						partnerBankInfo.getAddress(), partnerBankInfo.getOrganCode(),
						partnerBankInfo.getSignAccountId(), partnerBankInfo.getSealData(), partnerBankInfo.getIdCard(),
						partnerBankInfo.getId() });
	}

	public int updatePartnerBankInfoColById(final String col, final Object value, final int id) {
		StringBuilder sb = new StringBuilder("UPDATE `t_partnerbankinfo` SET ");
		sb.append(col).append("=? WHERE f_id=?");

		int ref = jdbcHelper.update(sb.toString(), value, id);
		return ref;
	}
}

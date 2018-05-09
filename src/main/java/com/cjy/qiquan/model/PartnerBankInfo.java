package com.cjy.qiquan.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 代理商
 * 
 * @author chenjiyin
 *
 */
public class PartnerBankInfo {

	private int id;

	private int partnerId;

	private String companyName;// 公司名称

	private String realName;// 法人名称

	private String hehuoName;// 普通合伙人名称

	private String address;// 注册地址

	private String bankName;

	private String bankNo;
	
	private String idCard;

	private String organCode;

	private String signAccountId;

	private String sealData;

	private int usedCount;

	public PartnerBankInfo() {

	}

	public PartnerBankInfo(ResultSet res) throws SQLException {
		id = res.getInt("f_id");
		partnerId = res.getInt("f_partnerId");
		companyName = res.getString("f_companyName");
		bankName = res.getString("f_bankName");
		bankNo = res.getString("f_bankNo");
		realName = res.getString("f_realName");
		hehuoName = res.getString("f_hehuoName");
		address = res.getString("f_address");
		organCode = res.getString("f_organCode");
		signAccountId = res.getString("f_signAccountId");
		sealData = res.getString("f_sealData");
		idCard = res.getString("f_idCard");
		usedCount = res.getInt("f_usedCount");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public int getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(int usedCount) {
		this.usedCount = usedCount;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getHehuoName() {
		return hehuoName;
	}
	
	
	

	public String getOrganCode() {
		return organCode;
	}

	public void setOrganCode(String organCode) {
		this.organCode = organCode;
	}

	public String getSignAccountId() {
		return signAccountId;
	}

	public void setSignAccountId(String signAccountId) {
		this.signAccountId = signAccountId;
	}

	public String getSealData() {
		return sealData;
	}

	public void setSealData(String sealData) {
		this.sealData = sealData;
	}

	public void setHehuoName(String hehuoName) {
		this.hehuoName = hehuoName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	

}

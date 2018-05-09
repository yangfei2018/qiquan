package com.cjy.qiquan.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.DateFormater;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class User extends UserSession {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7260030929437062229L;

	@JsonIgnore
	private String password_hash;

	private String realName;

	private String idCard;

	private int region;

	private String mobile;

	private String address;
	@JsonIgnore
	private String bankOfDeposit;// 开户银行

	@JsonIgnore
	private String bankCardNo;// 银行卡号

	@JsonIgnore
	private String bankAddress;
	@JsonIgnore
	private String bankRegion;
	@JsonIgnore
	private String idCardFrontPic;
	@JsonIgnore
	private String idCardBackgroundPic;
	@JsonIgnore
	private String bankCardPic;
	@JsonIgnore
	private double amount;// 余额

	private String partnerCode;// 代理商邀请码

	private int partnerbankinfoId;

	private String regTime;

	private String agreementTime;
	
	private int partnerposition;

	private String signAccountId;
	
	private String sealData;
	
	private int ban;
	
	public User() {
		super();
	}

	public User(ResultSet res) throws SQLException {
		super(res);
		password_hash = res.getString("f_password_hash");
		realName = res.getString("f_realName");
		idCard = res.getString("f_idCard");
		region = res.getInt("f_region");
		mobile = res.getString("f_mobile");
		address = res.getString("f_address");
		bankOfDeposit = res.getString("f_bankOfDeposit");
		bankAddress = res.getString("f_bankAddress");
		bankRegion = res.getString("f_bankRegion");
		bankCardNo = res.getString("f_bankCardNo");
		idCardFrontPic = res.getString("f_idCardFrontPic");
		idCardBackgroundPic = res.getString("f_idCardBackgroundPic");
		bankCardPic = res.getString("f_bankCardPic");
		amount = res.getDouble("f_amount");
		partnerCode = res.getString("f_partnerCode");
		partnerbankinfoId = res.getInt("f_partnerbankinfoId");
		partnerposition = res.getInt("f_partnerposition");
		signAccountId = res.getString("f_signAccountId");
		sealData = res.getString("f_sealData");
		ban = res.getInt("f_ban");
		regTime = DateFormater.simpleDateFormat(new Date(res.getTimestamp("f_regTime").getTime()),
				DateFormater.datetimeFormat2);
		{
			Timestamp agrt = res.getTimestamp("f_agreementTime");
			if (agrt != null) {
				agreementTime = DateFormater.simpleDateFormat(new Date(agrt.getTime()), DateFormater.datetimeFormat2);
			}
		}
	}

	public int getPartnerbankinfoId() {
		return partnerbankinfoId;
	}

	public void setPartnerbankinfoId(int partnerbankinfoId) {
		this.partnerbankinfoId = partnerbankinfoId;
	}

	public String getPassword_hash() {
		return CommonUtils.defaultNullString(password_hash);
	}

	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}
	
	

	public int getBan() {
		return ban;
	}

	public void setBan(int ban) {
		this.ban = ban;
	}

	public String getRealName() {
		return CommonUtils.defaultNullString(realName);
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return CommonUtils.defaultNullString(idCard);
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	
	
	public int getPartnerposition() {
		return partnerposition;
	}

	public void setPartnerposition(int partnerposition) {
		this.partnerposition = partnerposition;
	}

	public String getAddress() {
		return CommonUtils.defaultNullString(address);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBankOfDeposit() {
		return CommonUtils.defaultNullString(bankOfDeposit);
	}

	public String getMobile() {
		return CommonUtils.defaultNullString(mobile);
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setBankOfDeposit(String bankOfDeposit) {
		this.bankOfDeposit = bankOfDeposit;
	}

	public String getBankAddress() {
		return CommonUtils.defaultNullString(bankAddress);
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getBankRegion() {
		return CommonUtils.defaultNullString(bankRegion);
	}

	public void setBankRegion(String bankRegion) {
		this.bankRegion = bankRegion;
	}

	public String getIdCardFrontPic() {
		return CommonUtils.defaultNullString(idCardFrontPic);
	}

	public void setIdCardFrontPic(String idCardFrontPic) {
		this.idCardFrontPic = idCardFrontPic;
	}

	
	public String getSealData() {
		return sealData;
	}

	public void setSealData(String sealData) {
		this.sealData = sealData;
	}

	public String getIdCardBackgroundPic() {
		return idCardBackgroundPic;
	}

	public void setIdCardBackgroundPic(String idCardBackgroundPic) {
		this.idCardBackgroundPic = idCardBackgroundPic;
	}

	public String getBankCardPic() {
		return CommonUtils.defaultNullString(bankCardPic);
	}

	public void setBankCardPic(String bankCardPic) {
		this.bankCardPic = bankCardPic;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPartnerCode() {
		return CommonUtils.defaultNullString(partnerCode);
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getBankCardNo() {
		return CommonUtils.defaultNullString(bankCardNo);
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getIdCardFormat() {
		if (CommonUtils.hasText(getIdCard())) {
			return CommonUtils.hideIdCard(getIdCard());
		}
		return "";
	}

	public String getAgreementTime() {
		return CommonUtils.defaultNullString(agreementTime);
	}

	public void setAgreementTime(String agreementTime) {
		this.agreementTime = agreementTime;
	}

	public String getBankCardNoFormat() {
		if (CommonUtils.hasText(getBankCardNo())) {
			return CommonUtils.hideBankCardNo(getBankCardNo());
		}
		return "";
	}

	public String getAmountFormat() {
		if (getAmount() == 0) {
			return "0.00";
		}
		NumberFormat nf = new DecimalFormat("#,###.##");
		String str = nf.format(getAmount());
		return str;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public String getSignAccountId() {
		return CommonUtils.defaultNullString(signAccountId);
	}

	public void setSignAccountId(String signAccountId) {
		this.signAccountId = signAccountId;
	}

	
	
	
}

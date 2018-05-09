package com.cjy.qiquan.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cjy.qiquan.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 代理商账号
 * 
 * @author chenjiyin
 *
 */
public class Partner extends UserSession {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5853128782633719174L;

	@JsonIgnore
	private String password_hash;

	private String realName;// 法人姓名

	private String partnerNo;// 代理商编号

	private String companyName;// 代理商名称
	
	private String idCard;//身份证

	private String mobile;

	
	private String bankhm; //银行卡户名
	
	@JsonIgnore
	private String bankOfDeposit;// 开户银行

	@JsonIgnore
	private String bankCardNo;// 银行卡号

	@JsonIgnore
	private String businessLicencePic;// 营业执照图片
	@JsonIgnore
	private String idCardFrontPic;
	@JsonIgnore
	private String idCardBackgroundPic;
	@JsonIgnore
	private String bankCardPic;
	private String code;// 代理商邀请码

	private int belongtogmsuer;  //负责gm管理员
	
	
	private String organCode;
	
	private String signAccountId;
	
	private String sealData;
	
	public Partner() {
		super();
	}

	public Partner(ResultSet res) throws SQLException {
		super(res);
		password_hash = res.getString("f_password_hash");
		realName = res.getString("f_realName");
		partnerNo = res.getString("f_partnerNo");
		mobile = res.getString("f_mobile");
		companyName = res.getString("f_companyName");
		bankOfDeposit = res.getString("f_bankOfDeposit");
		bankCardNo = res.getString("f_bankCardNo");
		idCardFrontPic = res.getString("f_idCardFrontPic");
		idCardBackgroundPic = res.getString("f_idCardBackgroundPic");
		businessLicencePic = res.getString("f_businessLicencePic");
		code = res.getString("f_code");
		belongtogmsuer = res.getInt("f_belongtogmsuer");
		organCode = res.getString("f_organCode");
		signAccountId = res.getString("f_signAccountId");
		sealData = res.getString("f_sealData");
		idCard = res.getString("f_idCard");
		bankhm = res.getString("f_bankhm");
	}
	
	
	

	public String getBankhm() {
		return CommonUtils.defaultNullString(bankhm);
	}

	public void setBankhm(String bankhm) {
		this.bankhm = bankhm;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getSealData() {
		return sealData;
	}

	public void setSealData(String sealData) {
		this.sealData = sealData;
	}

	public int getBelongtogmsuer() {
		return belongtogmsuer;
	}

	public void setBelongtogmsuer(int belongtogmsuer) {
		this.belongtogmsuer = belongtogmsuer;
	}

	public String getPassword_hash() {
		return password_hash;
	}

	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPartnerNo() {
		return partnerNo;
	}

	public void setPartnerNo(String partnerNo) {
		this.partnerNo = partnerNo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBankOfDeposit() {
		return bankOfDeposit;
	}

	public void setBankOfDeposit(String bankOfDeposit) {
		this.bankOfDeposit = bankOfDeposit;
	}

	public String getBankCardNo() {
		return CommonUtils.defaultNullString(bankCardNo);
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBusinessLicencePic() {
		return businessLicencePic;
	}

	public void setBusinessLicencePic(String businessLicencePic) {
		this.businessLicencePic = businessLicencePic;
	}

	public String getIdCardFrontPic() {
		return idCardFrontPic;
	}

	public void setIdCardFrontPic(String idCardFrontPic) {
		this.idCardFrontPic = idCardFrontPic;
	}

	public String getIdCardBackgroundPic() {
		return idCardBackgroundPic;
	}

	public void setIdCardBackgroundPic(String idCardBackgroundPic) {
		this.idCardBackgroundPic = idCardBackgroundPic;
	}

	public String getBankCardPic() {
		return bankCardPic;
	}

	public void setBankCardPic(String bankCardPic) {
		this.bankCardPic = bankCardPic;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOrganCode() {
		return CommonUtils.defaultNullString(organCode);
	}

	public void setOrganCode(String organCode) {
		this.organCode = organCode;
	}

	public String getSignAccountId() {
		return CommonUtils.defaultNullString(signAccountId);
	}

	public void setSignAccountId(String signAccountId) {
		this.signAccountId = signAccountId;
	}
	
	
	

}

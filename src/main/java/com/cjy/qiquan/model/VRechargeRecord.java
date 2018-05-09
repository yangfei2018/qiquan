package com.cjy.qiquan.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cjy.qiquan.utils.CommonUtils;

public class VRechargeRecord extends RechargeRecord {

	private String buyerName;

	private String buyerMobile;
	
	private String companyName;

	public VRechargeRecord(ResultSet res) throws SQLException {
		super(res);
		buyerName = res.getString("f_buyerName");
		buyerMobile = res.getString("f_buyerMobile");
		companyName = res.getString("f_partnerCompanyName");
	}

	public VRechargeRecord() {
		super();
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerMobile() {
		return buyerMobile;
	}

	public void setBuyerMobile(String buyerMobile) {
		this.buyerMobile = buyerMobile;
	}

	public String getCompanyName() {
		return CommonUtils.defaultNullString(companyName);
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
	
}

package com.cjy.qiquan.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cjy.qiquan.utils.CommonUtils;

public class VUser extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4227221365682528217L;
	
	
	private String partnerCompanyName;
	private String pcompanyName;
	

	public VUser() {
		super();
	}

	public VUser(ResultSet res) throws SQLException {
		super(res);
		this.partnerCompanyName = res.getString("f_partnerCompanyName");
		this.pcompanyName = res.getString("f_pcompanyname");
	}

	public String getPartnerCompanyName() {
		return CommonUtils.defaultNullString(partnerCompanyName);
	}

	public void setPartnerCompanyName(String partnerCompanyName) {
		this.partnerCompanyName = partnerCompanyName;
	}


	public String getPcompanyName() {
		return pcompanyName;
	}

	public void setPcompanyName(String pcompanyName) {
		this.pcompanyName = pcompanyName;
	}
}

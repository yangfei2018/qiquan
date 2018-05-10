package com.cjy.qiquan.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.cjy.qiquan.utils.DateFormater;

public class CashRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6134219679666810830L;

	private int id;

	private int userId;

	private double amount;

	private Date createTime;

	private int status;

	private double beforeAmount;

	private double afterAmount;
	
	private String bankOfDeposit;

	private String bankCardNo;
	
	private String realName;

	private String name;		//会员帐号

	private String partnerCompanyName;	//代理商

	private String pcompanyName;		//有限合伙公司

	private Date clearingTime;			//结算时间
	
	public CashRecord() {

	}

	public CashRecord(ResultSet res) throws SQLException {
		id = res.getInt("f_id");
		userId = res.getInt("f_userId");
		amount = res.getDouble("f_amount");
		createTime = new Date(res.getTimestamp("f_createTime").getTime());
		status = res.getInt("f_status");
		beforeAmount = res.getDouble("f_beforeAmount");
		afterAmount = res.getDouble("f_afterAmount");
		bankOfDeposit = res.getString("f_bankOfDeposit");
		bankCardNo = res.getString("f_bankCardNo");
		realName = res.getString("f_realName");
		name = res.getString("f_name");
		partnerCompanyName = res.getString("f_partnerCompanyName");
		pcompanyName = res.getString("f_pcompanyname");
		Object temp = res.getTimestamp("f_clearingTime");
		if(null != temp){
			clearingTime = new Date(res.getTimestamp("f_clearingTime").getTime());
		}
	}
	
	
	
	

	public String getBankOfDeposit() {
		return bankOfDeposit;
	}

	public void setBankOfDeposit(String bankOfDeposit) {
		this.bankOfDeposit = bankOfDeposit;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	public String getCreateTimeFormat() {
		return DateFormater.simpleDateFormat(getCreateTime(), DateFormater.datetimeFormat2);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getBeforeAmount() {
		return beforeAmount;
	}

	public void setBeforeAmount(double beforeAmount) {
		this.beforeAmount = beforeAmount;
	}

	public double getAfterAmount() {
		return afterAmount;
	}

	public void setAfterAmount(double afterAmount) {
		this.afterAmount = afterAmount;
	}
	
	public String getAmountFormat() {
		if (getAmount() == 0) {
			return "0.00";
		}
		NumberFormat nf = new DecimalFormat("#,###.##");
		String str = nf.format(getAmount());
		return str;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPartnerCompanyName() {
		return partnerCompanyName;
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

	public Date getClearingTime() {
		return clearingTime;
	}

	public void setClearingTime(Date clearingTime) {
		this.clearingTime = clearingTime;
	}

	public String getClearingTimeFormat() {
		if(getClearingTime() != null){
			return DateFormater.simpleDateFormat(getClearingTime(), DateFormater.datetimeFormat2);
		}else{
			return "";
		}

	}
}

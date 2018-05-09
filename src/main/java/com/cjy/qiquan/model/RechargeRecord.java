package com.cjy.qiquan.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.DateFormater;

/**
 * 用户充值记录
 * 
 * @author chenjiyin
 *
 */
public class RechargeRecord {

	private int id;

	private String rechargeNo;

	private String bankcardname;
	
	private int userId;// 充值用户id

	private double amount;// 充值金额

	private Date createTime;// 订单创建时间

	private int status;// 订单状态 0-下单，1-已汇款

	private Date updateTime;// 审核人更新时间

	private double userBalanceBefore;// 下单人下单前余额

	private double userBalanceAfter;// 审核后余额

	private int auditorId;// 审核人

	private int partnerId;// 合作方id

	private String companyName;

	private String bankName;

	private String bankNo;

	private String name;//会员帐号

	private String pcompanyName;//有限合伙公司

	public RechargeRecord() {

	}

	public RechargeRecord(ResultSet res) throws SQLException {
		id = res.getInt("f_id");
		rechargeNo = res.getString("f_rechargeNo");
		userId = res.getInt("f_userId");
		amount = res.getInt("f_amount");
		createTime = new Date(res.getTimestamp("f_createTime").getTime());
		status = res.getInt("f_status");
		userBalanceBefore = res.getDouble("f_userBalanceBefore");
		userBalanceAfter = res.getDouble("f_userBalanceAfter");
		auditorId = res.getInt("f_auditorId");
		partnerId = res.getInt("f_partnerId");
		companyName = res.getString("f_companyName");
		bankName = res.getString("f_bankName");
		bankNo = res.getString("f_bankNo");
		bankcardname = res.getString("f_bankcardname");
		name = res.getString("f_name");
		pcompanyName = res.getString("f_pcompanyname");
	}
	
	
	

	public String getBankcardname() {
		return CommonUtils.defaultNullString(bankcardname);
	}

	public void setBankcardname(String bankcardname) {
		this.bankcardname = bankcardname;
	}

	public int getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}

	public String getRechargeNo() {
		return rechargeNo;
	}

	public void setRechargeNo(String rechargeNo) {
		this.rechargeNo = rechargeNo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public double getUserBalanceBefore() {
		return userBalanceBefore;
	}

	public void setUserBalanceBefore(double userBalanceBefore) {
		this.userBalanceBefore = userBalanceBefore;
	}

	public double getUserBalanceAfter() {
		return userBalanceAfter;
	}

	public void setUserBalanceAfter(double userBalanceAfter) {
		this.userBalanceAfter = userBalanceAfter;
	}

	public int getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(int auditorId) {
		this.auditorId = auditorId;
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

	public String getAmountFormat() {
		NumberFormat nf = new DecimalFormat("#,###.##");
		String str = nf.format(getAmount());
		return str;
	}

	public String getCreateTimeFormat() {
		return DateFormater.simpleDateFormat(getCreateTime(), DateFormater.datetimeFormat2);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPcompanyName() {
		return pcompanyName;
	}

	public void setPcompanyName(String pcompanyName) {
		this.pcompanyName = pcompanyName;
	}
}

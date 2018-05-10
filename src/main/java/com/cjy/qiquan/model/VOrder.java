package com.cjy.qiquan.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.cjy.qiquan.utils.CommonUtils;

public class VOrder extends Order {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6571610085134957589L;

	private String buyerName;

	private String buyerMobile;

	private String buyerPartnerCompanyName;
	private String buyerHehuoCompanyName;

	private double difAmount;

	public VOrder() {
		super();
	}

	public VOrder(ResultSet res) throws SQLException {
		super(res);
		buyerName = res.getString("f_buyerName");
		buyerMobile = res.getString("f_buyerMobile");
		buyerPartnerCompanyName = res.getString("f_buyerPartnerCompanyName");
		buyerHehuoCompanyName = res.getString("f_buyer_hehuo_companyname");
	}

	public String getBuyerName() {
		return CommonUtils.defaultNullString(buyerName);
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerMobile() {
		return CommonUtils.defaultNullString(buyerMobile);
	}

	public void setBuyerMobile(String buyerMobile) {
		this.buyerMobile = buyerMobile;
	}

	public String getBuyerPartnerCompanyName() {
		return CommonUtils.defaultNullString(buyerPartnerCompanyName);
	}

	public void setBuyerPartnerCompanyName(String buyerPartnerCompanyName) {
		this.buyerPartnerCompanyName = buyerPartnerCompanyName;
	}

	/**
	 * 应返有限合伙
	 * @return
	 */
	public double getHehuoAmount() {
		if (getStatus() == 3) {
			return Math.min(getBalanceAmount(), getAmount());
		} else {
			return 0;
		}
	}

	/**
	 * 应返客户
	 * @return
	 */
	public double getDiscount() {
		if (getStatus() == 3) {
			return Math.max(getBalanceAmount() - getAmount(), 0);
		} else {
			return 0;
		}
	}

	public String getHehuoAmountFormat() {

		if (getHehuoAmount() == 0) {
			return "";
		}
		NumberFormat nf = new DecimalFormat("#,###.##");
		String str = nf.format(getHehuoAmount());
		return str;

	}

	public String getDiscountFormat() {

		if (getDiscount() == 0) {
			return "";
		}
		NumberFormat nf = new DecimalFormat("#,###.##");
		String str = nf.format(getDiscount());
		return str;

	}

	public double getDifAmount() {
		return difAmount;
	}

	public void setDifAmount(double difAmount) {
		this.difAmount = difAmount;
	}


	public String getBuyerHehuoCompanyName() {
		return buyerHehuoCompanyName;
	}

	public void setBuyerHehuoCompanyName(String buyerHehuoCompanyName) {
		this.buyerHehuoCompanyName = buyerHehuoCompanyName;
	}
}

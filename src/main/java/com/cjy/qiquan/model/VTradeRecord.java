package com.cjy.qiquan.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VTradeRecord extends TradeRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8269543607451048053L;

	private String buyerName;

	private String buyerMobile;

	public VTradeRecord(ResultSet res) throws SQLException {
		super(res);
		buyerName = res.getString("f_realName");
		buyerMobile = res.getString("f_mobile");
	}

	public VTradeRecord() {
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

}

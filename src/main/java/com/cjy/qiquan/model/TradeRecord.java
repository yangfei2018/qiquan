package com.cjy.qiquan.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.cjy.qiquan.utils.DateFormater;

/**
 * 交易记录
 * 
 * @author chenjiyin
 *
 */
public class TradeRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8175079837424323764L;

	private int id;

	private String tradeNo;// 询价编号

	private Date createTime;

	private int userId;

	private int categoryId;// 产品类别

	private int buyAndFall;// 买涨买跌

	private String productName;// 产品名称

	private String productCode;// 产品代码

	private double amount;// 权利金(单位万)

	private int status; // 0-等待报价,1-已报价,-1-报价错误

	private double notionalPrincipal;// 名义本金

	private int type;// 类型(1-美式,2-欧式)

	private int period;// 弃权期限(day) 15,30

	private Date updateTime;// 报价时间

	private int buyType;//下单方式（1-市价下单，2-收盘价下单）
	
	private int unit;
	
	private int shou;
	
	public TradeRecord(ResultSet res) throws SQLException {
		id = res.getInt("f_id");
		tradeNo = res.getString("f_tradeNo");
		createTime = new Date(res.getTimestamp("f_createTime").getTime());
		userId = res.getInt("f_userId");
		categoryId = res.getInt("f_categoryId");
		buyAndFall = res.getInt("f_buyAndFall");
		productName = res.getString("f_productName");
		productCode = res.getString("f_productCode");
		amount = res.getDouble("f_amount");
		status = res.getInt("f_status");
		notionalPrincipal = res.getDouble("f_notionalPrincipal");
		unit = res.getInt("f_unit");
		type = res.getInt("f_type");
		period = res.getInt("f_period");
		buyType = res.getInt("f_buyType");
		Timestamp updateTimeSt = res.getTimestamp("f_updateTime");
		shou = res.getInt("f_shou");
		if (updateTimeSt != null) {
			updateTime = new Date(updateTimeSt.getTime());
		}
	}

	public TradeRecord() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getBuyAndFall() {
		return buyAndFall;
	}

	public void setBuyAndFall(int buyAndFall) {
		this.buyAndFall = buyAndFall;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getAmount() {
		return amount;
	}
	

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getNotionalPrincipal() {
		return notionalPrincipal;
	}

	public void setNotionalPrincipal(double notionalPrincipal) {
		this.notionalPrincipal = notionalPrincipal;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	
	public String getCreateTimeFormat() {
		return DateFormater.simpleDateFormat(getCreateTime(), DateFormater.datetimeFormat2);
	}
	
	
	public String getUpdateTimeFormat() {
		if (getUpdateTime()!=null) {
			return DateFormater.simpleDateFormat(getUpdateTime(), DateFormater.datetimeFormat2);
		}else {
			return "";
		}
	}
	
	
	public String getAmountFormat() {
		NumberFormat nf = new DecimalFormat("#,###.##");
		String str = nf.format(getAmount());
		return str;
	}
	
	
	
	public int getBuyType() {
		return buyType;
	}

	public void setBuyType(int buyType) {
		this.buyType = buyType;
	}

	public String getNotionalPrincipalFormat() {
		if (getNotionalPrincipal()==0) {
			return "";
		}
		NumberFormat nf = new DecimalFormat("#,###.##");
		String str = nf.format(getNotionalPrincipal());
		return str;
	}

	public int getShou() {
		return shou;
	}

	public void setShou(int shou) {
		this.shou = shou;
	}
	
	
	

}

package com.cjy.qiquan.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.cjy.qiquan.service.ClService;
import com.cjy.qiquan.service.GoodsService;
import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.DateFormater;

public class Order extends TradeRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7631588133548480732L;

	private String orderNo;

	private String orderStartTime; // 合同开始时间

	private String orderEndTime; // 合同结束时间

	private double tradeAmount; // 成交价
	private double balanceAmount; // 结算金额
	private Date balanceTime; // 结算时间

	private int pingcangType; // pingcang方式（1-市价下单，2-收盘价下单,3-行权报价）

	private double zhishu; // 期望指数（行权报价时用）

	private String closeReason;
	
	private double notionalPrincipalbefore;
	
	private double executivePrice;

	private Date dealTime;			//成交时间

	private Date optTime;			//操作时间（关闭交易）

	public Order() {
		super();
	}

	public Order(ResultSet res) throws SQLException {
		super(res);
		this.orderNo = res.getString("f_orderNo");
		this.orderStartTime = res.getString("f_orderStartTime");
		this.orderEndTime = res.getString("f_orderEndTime");
		this.balanceAmount = res.getDouble("f_balanceAmount");
		this.tradeAmount = res.getDouble("f_tradeAmount");
		this.pingcangType = res.getInt("f_pingcangType");
		this.closeReason = res.getString("f_closeReason");
		this.zhishu = res.getDouble("f_zhishu");
		this.notionalPrincipalbefore = res.getDouble("f_notionalPrincipalbefore");
		this.executivePrice = res.getDouble("f_executivePrice");
		{
			Timestamp t = res.getTimestamp("f_balanceTime");
			if (t != null) {
				balanceTime = new Date(t.getTime());
			}
		}
		{
			Timestamp temp = res.getTimestamp("f_dealTime");
			if (temp != null) {
				this.dealTime = new Date(temp.getTime());
			}
		}
		{
			Timestamp temp = res.getTimestamp("f_optTime");
			if (temp != null) {
				this.optTime = new Date(temp.getTime());
			}
		}

	}

	public String getCloseReason() {
		return CommonUtils.defaultNullString(closeReason);
	}

	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderStartTime() {
		return CommonUtils.defaultNullString(orderStartTime);
	}

	public void setOrderStartTime(String orderStartTime) {
		this.orderStartTime = orderStartTime;
	}

	public String getOrderEndTime() {
		return CommonUtils.defaultNullString(orderEndTime);
	}

	public void setOrderEndTime(String orderEndTime) {
		this.orderEndTime = orderEndTime;
	}

	public String getOrderStatusName() {
		return Constant.ORDER_STATUS.get(getStatus()) != null ? Constant.ORDER_STATUS.get(getStatus()).getName() : "";
	}

	public int getPingcangType() {
		return pingcangType;
	}

	public void setPingcangType(int pingcangType) {
		this.pingcangType = pingcangType;
	}
	
	

	public double getExecutivePrice() {
		return executivePrice;
	}

	public void setExecutivePrice(double executivePrice) {
		this.executivePrice = executivePrice;
	}

	public double getZhishu() {
		return zhishu;
	}

	public void setZhishu(double zhishu) {
		this.zhishu = zhishu;
	}

	public double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public Date getBalanceTime() {
		return balanceTime;
	}

	public void setBalanceTime(Date balanceTime) {
		this.balanceTime = balanceTime;
	}

	public String getBalanceAmountFormat() {
		if (getBalanceAmount() == 0) {
			return "";
		}
		NumberFormat nf = new DecimalFormat("#,###.##");
		String str = nf.format(getBalanceAmount());
		return str;
	}

	public double getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(double tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	
	
	public double getNotionalPrincipalbefore() {
		return notionalPrincipalbefore;
	}

	public void setNotionalPrincipalbefore(double notionalPrincipalbefore) {
		this.notionalPrincipalbefore = notionalPrincipalbefore;
	}

	public String getTradeAmountFormat() {
		if (getTradeAmount() == 0) {
			return "";
		}
		NumberFormat nf = new DecimalFormat("#,###.##");
		String str = nf.format(getTradeAmount());
		return str;
	}
	
	
	public String getNotionalPrincipalbeforeFormat() {
		if (getNotionalPrincipalbefore() == 0) {
			return "";
		}
		NumberFormat nf = new DecimalFormat("#,###.##");
		String str = nf.format(getNotionalPrincipalbefore());
		return str;
	}

	public double getYj() {
		try {
			if (this.getStatus() == 1) {
				Cl cl = ClService.instance().getRecentPriceByszlable(this.getProductCode());
				Goods goods = GoodsService.instance().getGoodsByCode(getProductCode());

				if (cl != null && goods != null) {
					double f = 0;
					if (this.getBuyAndFall() == 1) {
						// 看涨
						// 第三方数据*购买手数*手数单位-名义本金
						f =  CommonUtils.floatFormat(cl.getFnewprice() * this.getShou() * (double) goods.getUnit() - this.getNotionalPrincipal());
					} else {
						// 看跌

						// 名义本金-第三方数据*手数*手数单位
						f =  CommonUtils.floatFormat(this.getNotionalPrincipal() - cl.getFnewprice() * this.getShou() * (double) goods.getUnit());
					}
					
					f =  CommonUtils.floatFormat(f -this.getAmount());

					if (f > 0) {
						return CommonUtils.floatFormat(f);
					} else {
						// 数值为负数并超过权利金时为权利金，（比如权利金是10万，预计盈亏为-11万时，也为-10万，就是亏损金额不超过权利金）
						if (Math.abs(f) > this.getAmount()) {
							return -1 * CommonUtils.floatFormat(this.getAmount());
						} else {
							return CommonUtils.floatFormat(f);
						}
					}
				}
			}

		} catch (Exception e) {
		}

		return 0.00;

	}

	public String getBalanceTimeFormat() {
		if (getBalanceTime() != null) {
			return DateFormater.simpleDateFormat(getBalanceTime(), DateFormater.datetimeFormat2);
		} else {
			return "";
		}
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	public Date getOptTime() {
		return optTime;
	}

	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}

	public String getDealTimeFormat() {
		if (getDealTime() != null) {
			return DateFormater.simpleDateFormat(getDealTime(), DateFormater.datetimeFormat2);
		} else {
			return "";
		}
	}

	public String getOptTimeFormat() {
		if (getOptTime() != null) {
			return DateFormater.simpleDateFormat(getOptTime(), DateFormater.datetimeFormat2);
		} else {
			return "";
		}
	}
}

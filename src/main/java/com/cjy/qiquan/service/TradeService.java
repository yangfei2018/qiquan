package com.cjy.qiquan.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjy.qiquan.dao.TradeDao;
import com.cjy.qiquan.dao.UserDao;
import com.cjy.qiquan.model.CashRecord;
import com.cjy.qiquan.model.GmUser;
import com.cjy.qiquan.model.Goods;
import com.cjy.qiquan.model.Order;
import com.cjy.qiquan.model.RechargeRecord;
import com.cjy.qiquan.model.TradeRecord;
import com.cjy.qiquan.model.User;
import com.cjy.qiquan.model.VOrder;
import com.cjy.qiquan.model.VRechargeRecord;
import com.cjy.qiquan.model.VTradeRecord;
import com.cjy.qiquan.po.Page;
import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.DateFormater;
import com.cjy.qiquan.utils.SpringApplicationContext;

@Service
public class TradeService {

	@Autowired
	private TradeDao tradeDao;

	@Autowired
	private UserDao userDao;

	public static TradeService instance() {
		return SpringApplicationContext.getBean("tradeService");
	}

	public int createTrade(TradeRecord tradeRecord) {
		int ref = tradeDao.createTrade(tradeRecord);
		tradeRecord.setId(ref);

		return ref;
	}

	public int delTrade(final int id) {
		return tradeDao.delTrade(id);
	}

	public int baojia(TradeRecord tradeRecord) {
		int ref = tradeDao.baojia(tradeRecord);
		return ref;
	}

	public int createRechargeRecord(RechargeRecord rechargeRecord) {
		int ref = tradeDao.createRechargeRecord(rechargeRecord);
		rechargeRecord.setId(ref);
		return ref;
	}

	public TradeRecord getTradeRecordByTradeNo(final String tradeNo) {
		return tradeDao.getTradeRecordByTradeNo(tradeNo);
	}

	public RechargeRecord getRechargeRecordById(final int id) {
		return tradeDao.getRechargeRecordById(id);
	}

	public int bohuiRecharge(final int id, GmUser audor) throws Exception {
		RechargeRecord tRechargeRecord = getRechargeRecordById(id);
		if (tRechargeRecord == null || tRechargeRecord.getStatus() == 1) {
			return 0;
		}

		int ref = tradeDao.updateRechargeRecord(id, -1, audor.getUserId(), 0, 0);

		return ref;

	}

	/**
	 * 根据充值订单充值
	 * 
	 * @param id
	 * @param audor
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public int chongzhi(final int id, GmUser audor) throws Exception {
		RechargeRecord tRechargeRecord = getRechargeRecordById(id);
		if (tRechargeRecord == null || tRechargeRecord.getStatus() != 0) {
			return 0;
		}

		int ref = 0;
		User user = UserService.instance().getUserById(tRechargeRecord.getUserId());
		if (user != null) {
			double amount = user.getAmount();
			amount += tRechargeRecord.getAmount();

			ref = tradeDao.updateRechargeRecord(id, 1, audor.getUserId(), user.getAmount(), amount);
			if (ref < 0) {
				throw new Exception();
			}

			ref = userDao.updateColById("f_amount", amount, user.getUserId());
			if (ref < 0) {
				throw new Exception();
			}

		}
		return ref;

	}

	public VTradeRecord getVTradeRecordByTradeNo(final String tradeNo) {
		return tradeDao.getVTradeRecordByTradeNo(tradeNo);
	}
	
	
	public VTradeRecord getVTradeRecordById(final int id) {
		return tradeDao.getVTradeRecordById(id);
	}

	public Page<TradeRecord> listTradeByUser(final int userid, final int status, int index, int size) {
		if (index <= 0) {
			index = 1;
		}
		if (size <= 0) {
			size = 20;
		}

		return tradeDao.listTradeByUser(userid, status, index, size);
	}

	public Page<VTradeRecord> listVTradeByStatus(final int status, List<String> userid, int index, int size) {
		if (index <= 0) {
			index = 1;
		}
		if (size <= 0) {
			size = 20;
		}

		return tradeDao.listVTradeByStatus(status, userid, index, size);
	}

	public int closeOrder(Order order, String closeReason) throws Exception {
		User user = UserService.instance().getUserById(order.getUserId());
		double orderAmount = order.getAmount();
		double amount = user.getAmount() + orderAmount;
		int ref = tradeDao.closeOrder(closeReason, order.getId());
		if (ref > 0) {
			ref = userDao.updateColById("f_amount", amount, user.getUserId());
			if (ref < 0) {
				throw new Exception();
			}
		}
		return ref;
	}

	@Transactional
	public int trade(TradeRecord tradeRecord, User user) throws Exception {

		double amount = user.getAmount() - tradeRecord.getAmount();
		if (amount < 0) {
			throw new Exception();
		}
		// 扣钱
		int ref = userDao.updateColById("f_amount", amount, user.getUserId());
		if (ref < 0) {
			throw new Exception();
		}
		String tradeNoFirst = user.getPartnerCode()
				+ DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()), DateFormater.dateFormatNoSplit1);

		ref = tradeDao.updateTradeRecordColById("f_buyType", tradeRecord.getBuyType(), tradeRecord.getId());
		if (ref < 0) {
			throw new Exception();
		}
		// 生成订单
		Order order = new Order();
		order.setTradeNo(tradeRecord.getTradeNo());
		order.setAmount(tradeRecord.getAmount());
		order.setBuyAndFall(tradeRecord.getBuyAndFall());
		order.setCategoryId(tradeRecord.getCategoryId());
		order.setPeriod(tradeRecord.getPeriod());
		order.setProductCode(tradeRecord.getProductCode());
		order.setProductName(tradeRecord.getProductName());
		order.setType(tradeRecord.getType());
		order.setUserId(tradeRecord.getUserId());
		order.setNotionalPrincipal(tradeRecord.getNotionalPrincipal());
		order.setNotionalPrincipalbefore(tradeRecord.getNotionalPrincipal());
		order.setOrderNo(CommonUtils.getNewOrderSN(tradeNoFirst));
		order.setStatus(Constant.ORDER_STATUS.CREATE.getId());
		order.setShou(tradeRecord.getShou());
		order.setBuyType(tradeRecord.getBuyType());
		order.setUnit(tradeRecord.getUnit());
		ref = tradeDao.createOrder(order);
		if (ref <= 0) {
			throw new Exception();
		}
		// 更新询价表相关状态
		ref = tradeDao.updateTradeRecordColById("f_status", Constant.TRADE_STATUS.DEAL.getId(), tradeRecord.getId());
		if (ref < 0) {
			throw new Exception();
		}
		return ref;
	}

	public Page<Order> listOrderByUser(final int userid, final int status, int index, int size) {
		if (index <= 0) {
			index = 1;
		}
		if (size <= 0) {
			size = 20;
		}
		return tradeDao.listOrderByUser(userid, status, index, size);
	}

	public Page<VOrder> listVOrderByStatus(final int status, List<String> userid, int index, int size, String ids, Map map) {
		if (index <= 0) {
			index = 1;
		}
		if (size <= 0) {
			size = 20;
		}
		return tradeDao.listVOrderByStatus(status, userid, index, size, ids,map);
	}

	public Order getOrderByOrderNo(final String orderNo) {
		return tradeDao.getOrderByOrderNo(orderNo);
	}

	public VOrder getVOrderByOrderNo(final String orderNo) {
		return tradeDao.getVOrderByOrderNo(orderNo);
	}

	public int pingcang(final int id, final int pingcangType, final double zhishu) {
		return tradeDao.pingcang(id, pingcangType, zhishu);
	}

	public Page<RechargeRecord> listRechargeRecordByUser(final int userid, final int status, int index, int size) {
		if (index <= 0) {
			index = 1;
		}
		if (size <= 0) {
			size = 20;
		}
		return tradeDao.listRechargeRecordByUser(userid, status, index, size);
	}

	public Page<VRechargeRecord> listRechargeRecordByPartnerId(final int partnerId, final int status, int index,
			int size,String searchValue) {
		if (index <= 0) {
			index = 1;
		}
		if (size <= 0) {
			size = 20;
		}
		return tradeDao.listRechargeRecordByPartnerId(partnerId, status, index, size,searchValue);
	}

	@Transactional
	public int dealOrder(VOrder order) throws Exception {
		double difAmount = order.getDifAmount();
		
		if (difAmount<0) {
			return -1;
		}
		
		Order oldOrder = this.getOrderByOrderNo(order.getOrderNo());
		double amount = oldOrder.getAmount();
		double newAmount = order.getAmount();
		if (amount<newAmount) {
			return -1;
		}
		
		User user = UserService.instance().getUserById(oldOrder.getUserId());
		if (user==null) {
			return -1;
		}
		
		//名义本金=下单手数*成交价格*每手单位
		Goods goods = GoodsService.instance().getGoodsByCode(order.getProductCode());
		if (goods==null) {
			return -1;
		}
		
		
		double notionalPrincipal = CommonUtils.floatFormat(order.getTradeAmount() * (double)oldOrder.getShou() * (double)goods.getUnit());
		if (notionalPrincipal<=0) {
			return -1;
		}
		order.setNotionalPrincipal(notionalPrincipal);
		
		int ref =  tradeDao.dealOrder(order);
		if (ref>0) {
			double accountAmount = user.getAmount() + difAmount;
			ref = userDao.updateColById("f_amount", accountAmount, user.getUserId());
			if (ref < 0) {
				throw new Exception();
			}
		}
		
		return ref;
	}

	@Transactional
	public int balanceOrder(Order order, final int userId) throws Exception {
		int ref = tradeDao.balanceOrder(order);
		if (ref > 0) {
			User user = UserService.instance().getUserById(userId);
			if (user == null) {
				throw new Exception();
			}
			double amount = user.getAmount() + order.getBalanceAmount();
			if (amount < 0) {
				throw new Exception();
			}
			// 加钱
			ref = userDao.updateColById("f_amount", amount, user.getUserId());
			if (ref < 0) {
				throw new Exception();
			}
		}
		return ref;
	}

	@Transactional
	public int withdrawCash(User user, double amount) throws Exception {
		if (user.getAmount() < amount) {
			throw new Exception();
		}
		if (amount <= 0) {
			throw new Exception();
		}

		CashRecord cashRecord = new CashRecord();
		cashRecord.setAfterAmount(0);
		cashRecord.setAmount(amount);
		cashRecord.setStatus(0);
		cashRecord.setUserId(user.getUserId());
		int ref = tradeDao.createCashRecord(cashRecord);
		if (ref <= 0) {
			throw new Exception();
		}

		ref = userDao.updateColById("f_amount", 0, user.getUserId());
		if (ref <= 0) {
			throw new Exception();
		}

		return ref;
	}

	public int updateCashRecordColById(final String col, final Object value, final List<Integer> id) {
		if (id.isEmpty()) {
			return 0;
		}
		return tradeDao.updateCashRecordColById(col, value, id);
	}

	public Page<CashRecord> listCashRecordByStatus(final int status, List<String> userid, final int index,
			final int size,String searchValue) {
		return tradeDao.listCashRecordByStatus(status, userid, index, size,searchValue);
	}

	public int updateWithdrawStatus(Integer id){
		return tradeDao.updateWithdrawStatus(id);
	}
}

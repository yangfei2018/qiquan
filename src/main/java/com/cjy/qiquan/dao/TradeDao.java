package com.cjy.qiquan.dao;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cjy.qiquan.dbhelper.JdbcHelper;
import com.cjy.qiquan.model.CashRecord;
import com.cjy.qiquan.model.Order;
import com.cjy.qiquan.model.RechargeRecord;
import com.cjy.qiquan.model.TradeRecord;
import com.cjy.qiquan.model.VOrder;
import com.cjy.qiquan.model.VRechargeRecord;
import com.cjy.qiquan.model.VTradeRecord;
import com.cjy.qiquan.po.Page;
import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;

@Repository
public class TradeDao {

	@Autowired
	private JdbcHelper jdbcHelper;

	private RowMapper<TradeRecord> TRADE_RECORD = new RowMapper<TradeRecord>() {
		@Override
		public TradeRecord mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			TradeRecord tradeRecord = new TradeRecord(resultSet);
			return tradeRecord;
		}
	};

	private RowMapper<VTradeRecord> VTRADE_RECORD = new RowMapper<VTradeRecord>() {
		@Override
		public VTradeRecord mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			VTradeRecord tradeRecord = new VTradeRecord(resultSet);
			return tradeRecord;
		}
	};

	private RowMapper<Order> ORDER = new RowMapper<Order>() {
		@Override
		public Order mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			Order tradeRecord = new Order(resultSet);
			return tradeRecord;
		}
	};

	private RowMapper<VOrder> VORDER = new RowMapper<VOrder>() {
		@Override
		public VOrder mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			VOrder tradeRecord = new VOrder(resultSet);
			return tradeRecord;
		}
	};

	public int createTrade(TradeRecord tradeRecord) {
		return jdbcHelper.executeUpdateReturnKeyGenerate(
				"INSERT INTO `t_traderecord`(f_userId,f_categoryId,f_buyAndFall,f_productName,f_productCode,f_amount,f_type,f_period,f_tradeNo,f_unit,f_shou) value(?,?,?,?,?,?,?,?,?,?,?)",
				new Object[] { tradeRecord.getUserId(), tradeRecord.getCategoryId(), tradeRecord.getBuyAndFall(),
						tradeRecord.getProductName(), tradeRecord.getProductCode(), tradeRecord.getAmount(),
						tradeRecord.getType(), tradeRecord.getPeriod(), tradeRecord.getTradeNo(),tradeRecord.getUnit(),tradeRecord.getShou() });
	}

	
	public VTradeRecord getVTradeRecordById(final int id) {
		return jdbcHelper.queryForObject("SELECT * FROM `v_traderecord` WHERE f_id=?", new Object[] { id },
				VTRADE_RECORD);
	} 
	
	public int delTrade(final int id) {
		return jdbcHelper.update("DELETE FROM `t_traderecord` WHERE f_id=?",new Object[] {id});
	}
	
	public int baojia(TradeRecord tradeRecord) {
		return jdbcHelper.update(
				"update `t_traderecord` SET f_notionalPrincipal=?,f_updateTime=NOW(),f_status=? where f_tradeNo=?",
				new Object[] { tradeRecord.getNotionalPrincipal(), tradeRecord.getStatus(), tradeRecord.getTradeNo() });
	}

	public TradeRecord getTradeRecordByTradeNo(final String tradeNo) {
		return jdbcHelper.queryForObject("SELECT * FROM `t_traderecord` WHERE f_tradeNo=?", new Object[] { tradeNo },
				TRADE_RECORD);
	}

	public VTradeRecord getVTradeRecordByTradeNo(final String tradeNo) {
		return jdbcHelper.queryForObject("SELECT * FROM `v_traderecord` WHERE f_tradeNo=?", new Object[] { tradeNo },
				VTRADE_RECORD);
	}

	public Page<TradeRecord> listTradeByUser(final int userid, final int status, int index, int size) {
		Page<TradeRecord> page = new Page<>();
		page.setIndex(index);
		page.setSize(size);
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM `t_traderecord` WHERE f_status=" + status).append(" and f_userId=").append(userid);

		int total = jdbcHelper.queryForObject("SELECT count(*) " + sql.toString(), Integer.class);
		page.setTotal(total);
		sql.append(" ORDER BY f_createTime DESC");
		sql.append(" LIMIT ").append((page.getIndex() - 1) * page.getSize()).append(" , ").append(page.getSize());

		System.out.println(sql.toString());
		List<TradeRecord> list = jdbcHelper.query("SELECT * " + sql.toString(), new RowMapper<TradeRecord>() {
			@Override
			public TradeRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new TradeRecord(rs);
			}
		});

		page.setList(list);
		return page;

	}

	public Page<VTradeRecord> listVTradeByStatus(final int status, List<String> userid, int index, int size) {
		Page<VTradeRecord> page = new Page<>();
		page.setIndex(index);
		page.setSize(size);
		
		if (userid != null && userid.isEmpty()) { 
			return page;
		}		

		StringBuilder sql = new StringBuilder();
		sql.append(" FROM `v_traderecord` WHERE 1=1");
		if (status >= 0) {
			sql.append(" AND f_status=" + status);
		}

		if (userid != null && (!userid.isEmpty())) {
			sql.append(" AND f_userId in (");
			for (int i = 0; i < userid.size(); i++) {
				if (i > 0) {
					sql.append(",");
				}

				sql.append(userid.get(i));
			}

			sql.append(") ");
		}

		int total = jdbcHelper.queryForObject("SELECT count(*) " + sql.toString(), Integer.class);
		page.setTotal(total);
		sql.append(" ORDER BY f_createTime DESC");
		sql.append(" LIMIT ").append((page.getIndex() - 1) * page.getSize()).append(" , ").append(page.getSize());

		System.out.println(sql.toString());
		List<VTradeRecord> list = jdbcHelper.query("SELECT * " + sql.toString(), new RowMapper<VTradeRecord>() {
			@Override
			public VTradeRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new VTradeRecord(rs);
			}
		});

		page.setList(list);
		return page;

	}

	public Page<Order> listOrderByUser(final int userid, final int status, int index, int size) {
		Page<Order> page = new Page<>();
		page.setIndex(index);
		page.setSize(size);
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM `t_order` WHERE f_status=" + status).append(" and f_userId=").append(userid);

		int total = jdbcHelper.queryForObject("SELECT count(*) " + sql.toString(), Integer.class);
		page.setTotal(total);
		sql.append(" ORDER BY f_createTime DESC");
		sql.append(" LIMIT ").append((page.getIndex() - 1) * page.getSize()).append(" , ").append(page.getSize());

		System.out.println(sql.toString());
		List<Order> list = jdbcHelper.query("SELECT * " + sql.toString(), new RowMapper<Order>() {
			@Override
			public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Order(rs);
			}
		});

		page.setList(list);
		return page;

	}

	public int createOrder(Order tradeRecord) {
		return jdbcHelper.executeUpdateReturnKeyGenerate(
				"INSERT INTO `t_order`(f_userId,f_categoryId,f_buyAndFall,f_productName,f_productCode,f_amount,f_type,f_period,f_notionalPrincipal,f_tradeNo,f_orderNo,f_buyType,f_unit,f_shou,f_notionalPrincipalbefore) value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[] { tradeRecord.getUserId(), tradeRecord.getCategoryId(), tradeRecord.getBuyAndFall(),
						tradeRecord.getProductName(), tradeRecord.getProductCode(), tradeRecord.getAmount(),
						tradeRecord.getType(), tradeRecord.getPeriod(), tradeRecord.getNotionalPrincipal(),
						tradeRecord.getTradeNo(), tradeRecord.getOrderNo(),tradeRecord.getBuyType(),tradeRecord.getUnit(),tradeRecord.getShou(),tradeRecord.getNotionalPrincipalbefore() });
	}

	
	public int closeOrder(String reason,final int id) {
		return jdbcHelper.update("UPDATE `t_order` set f_status=?,f_closeReason=?,f_optTime=NOW(),f_updateTime=NOW() where f_id=?", new Object[] {
				-1,reason,id
		});
	}
	
	public int updateTradeRecordColById(final String col, final Object value, final int id) {
		StringBuilder sb = new StringBuilder("UPDATE `t_traderecord` SET ");
		sb.append(col).append("=? WHERE f_id=?");

		int ref = jdbcHelper.update(sb.toString(), value, id);
		return ref;
	}

	public Order getOrderByOrderNo(final String orderNo) {
		return jdbcHelper.queryForObject("SELECT * FROM `t_order` WHERE f_orderNo=?", new Object[] { orderNo }, ORDER);
	}

	public VOrder getVOrderByOrderNo(final String orderNo) {
		return jdbcHelper.queryForObject("SELECT * FROM `v_order` WHERE f_orderNo=?", new Object[] { orderNo }, VORDER);
	}

	public int pingcang(final int id,final int pingcangType,final double zhishu) {
		StringBuilder sb = new StringBuilder("UPDATE `t_order` SET f_status=?,f_updateTime=NOW(),f_pingcangType=?,f_zhishu=? ");
		sb.append(" WHERE f_id=?");

		int ref = jdbcHelper.update(sb.toString(), Constant.ORDER_STATUS.EVENING_UP.getId(),pingcangType,zhishu, id);
		return ref;
	}

	public int createRechargeRecord(RechargeRecord rechargeRecord) {
		try {
			return jdbcHelper.executeUpdateReturnKeyGenerate(
					"INSERT INTO `t_rechargerecord` (f_rechargeNo,f_userId,f_amount,f_partnerId,f_companyName,f_bankName,f_bankNo,f_bankcardname) value(?,?,?,?,?,?,?,?)",
					new Object[] { rechargeRecord.getRechargeNo(), rechargeRecord.getUserId(),
							rechargeRecord.getAmount(), rechargeRecord.getPartnerId(), rechargeRecord.getCompanyName(),
							rechargeRecord.getBankName(), rechargeRecord.getBankNo(),
							rechargeRecord.getBankcardname() });
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	public Page<RechargeRecord> listRechargeRecordByUser(final int userid, final int status, int index, int size) {
		Page<RechargeRecord> page = new Page<>();
		page.setIndex(index);
		page.setSize(size);
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM `t_rechargerecord` WHERE f_status=" + status).append(" and f_userId=").append(userid);

		int total = jdbcHelper.queryForObject("SELECT count(*) " + sql.toString(), Integer.class);
		page.setTotal(total);
		sql.append(" ORDER BY f_createTime DESC");
		sql.append(" LIMIT ").append((page.getIndex() - 1) * page.getSize()).append(" , ").append(page.getSize());

		System.out.println(sql.toString());
		List<RechargeRecord> list = jdbcHelper.query("SELECT * " + sql.toString(), new RowMapper<RechargeRecord>() {
			@Override
			public RechargeRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new RechargeRecord(rs);
			}
		});

		page.setList(list);
		return page;

	}

	public RechargeRecord getRechargeRecordById(final int id) {
		return jdbcHelper.queryForObject("SELECT * FROM `t_rechargerecord` WHERE f_id=?", new Object[] { id },
				new RowMapper<RechargeRecord>() {
					@Override
					public RechargeRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new RechargeRecord(rs);
					}
				});
	}

	public int updateRechargeRecord(final int id, final int status, final int auditId, double beforeAmount,
			double afterAmount) {
		return jdbcHelper.update(
				"UPDATE t_rechargerecord SET f_status=?,f_updateTime=NOW(),f_auditorId=?,f_userBalanceBefore=?,f_userBalanceAfter=? WHERE f_id=?",
				new Object[] { status, auditId, beforeAmount, afterAmount, id });
	}

	public Page<VRechargeRecord> listRechargeRecordByPartnerId(final int partnerId, final int status, int index,
			int size) {
		Page<VRechargeRecord> page = new Page<>();
		page.setIndex(index);
		page.setSize(size);
		StringBuilder sql = new StringBuilder();
		if (partnerId == 0) {
			sql.append(" FROM `v_rechargerecord` WHERE f_status=" + status);

		} else {
			sql.append(" FROM `v_rechargerecord` WHERE f_status=" + status).append(" and f_partnerId=")
					.append(partnerId);

		}

		int total = jdbcHelper.queryForObject("SELECT count(*) " + sql.toString(), Integer.class);
		page.setTotal(total);
		sql.append(" ORDER BY f_createTime DESC");
		sql.append(" LIMIT ").append((page.getIndex() - 1) * page.getSize()).append(" , ").append(page.getSize());

		System.out.println(sql.toString());
		List<VRechargeRecord> list = jdbcHelper.query("SELECT * " + sql.toString(), new RowMapper<VRechargeRecord>() {
			@Override
			public VRechargeRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new VRechargeRecord(rs);
			}
		});

		page.setList(list);
		return page;

	}

	public Page<VOrder> listVOrderByStatus(final int status, List<String> userid, int index, int size, String ids, Map map) {
		Page<VOrder> page = new Page<>();
		page.setIndex(index);
		page.setSize(size);
		
		if (userid != null && userid.isEmpty()) { 
			return page;
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM `v_order` WHERE 1=1");
		if (status >= -1) {
			sql.append(" and f_status=").append(status);
		}

		
		
		if (userid != null && (!userid.isEmpty())) {
			sql.append(" AND f_userId in (");
			for (int i = 0; i < userid.size(); i++) {
				if (i > 0) {
					sql.append(",");
				}

				sql.append(userid.get(i));
			}

			sql.append(") ");
		}
		
		if (CommonUtils.hasText(ids)) {
			sql.append(" AND f_id in (").append(ids).append(")");
		}
		if(null != map){
			String startTime = (String) map.get("startTime");
			String endTime = (String) map.get("endTime");
			String searchText = (String) map.get("searchText");
			if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
				switch (status){
					case 0:
						sql.append(" and f_createTime >= '").append(startTime).append("'")
								.append(" and f_createTime <= '").append(endTime).append("'");
						break;
					case 3:
						sql.append(" and f_balanceTime >= '").append(startTime).append("'")
								.append(" and f_balanceTime <= '").append(endTime).append("'");
						break;
					default:
						sql.append(" and f_updateTime >= '").append(startTime).append("'")
								.append(" and f_updateTime <= '").append(endTime).append("'");
						break;
				}
			}
			if(StringUtils.isNotBlank(searchText)){
				try {
					searchText= new String(searchText.getBytes("iso-8859-1"), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				sql.append(" and (f_buyerMobile like concat('%','").append(searchText).append("','%')")
						.append(" or f_buyerName like concat('%','").append(searchText).append("','%')")
						.append(" or f_buyerPartnerCompanyName like concat('%','").append(searchText).append("','%'))");
			}
		}
		int total = jdbcHelper.queryForObject("SELECT count(*) " + sql.toString(), Integer.class);
		page.setTotal(total);
		if (status==2) {
			sql.append(" ORDER BY f_updateTime DESC");
		}else if(status == -1) {
			sql.append(" ORDER BY f_optTime DESC");
		}else if(status == 1) {
			sql.append(" ORDER BY f_dealTime DESC");
		}else if(status == 3) {
			sql.append(" ORDER BY f_balanceTime DESC");
		}else{
			sql.append(" ORDER BY f_createTime DESC");
		}
		
		sql.append(" LIMIT ").append((page.getIndex() - 1) * page.getSize()).append(" , ").append(page.getSize());

		List<VOrder> list = jdbcHelper.query("SELECT * " + sql.toString(), new RowMapper<VOrder>() {
			@Override
			public VOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new VOrder(rs);
			}
		});

		page.setList(list);
		return page;

	}

	public int dealOrder(Order order) {
		return jdbcHelper.update(
				"update `t_order` SET f_orderStartTime=?,f_orderEndTime=?,f_status=?,f_updateTime=NOW(),f_dealTime=NOW(),f_tradeAmount=?,f_amount=?,f_notionalPrincipal=? where f_orderNo=?",
				new Object[] { order.getOrderStartTime(), order.getOrderEndTime(), 1, order.getTradeAmount(),order.getAmount(),order.getNotionalPrincipal(),
						order.getOrderNo() });
	}

	public int balanceOrder(Order order) {
		return jdbcHelper.update(
				"update `t_order` SET f_executivePrice=?,f_balanceAmount=?,f_status=?,f_balanceTime=NOW() where f_orderNo=?",
				new Object[] { order.getExecutivePrice(), order.getBalanceAmount(), 3, order.getOrderNo() });
	}

	public int createCashRecord(CashRecord cashRecord) {
		return jdbcHelper.executeUpdateReturnKeyGenerate(
				"INSERT INTO `t_cashRecord`(f_userId,f_amount,f_beforeAmount,f_afterAmount,f_status) value(?,?,?,?,?)",
				new Object[] { cashRecord.getUserId(), cashRecord.getAmount(), cashRecord.getBeforeAmount(),
						cashRecord.getAfterAmount(), cashRecord.getStatus() });
	}
	
	
	public int updateCashRecordColById(final String col, final Object value, final List<Integer> id) {
		StringBuilder sb = new StringBuilder("UPDATE `t_cashrecord` SET ");
		sb.append(col).append("=? WHERE f_id in (");
		
		for (int i = 0; i < id.size(); i++) {
			if (i > 0) {
				sb.append(",");
			}

			sb.append(id.get(i));
		}

		sb.append(") ");

		int ref = jdbcHelper.update(sb.toString(), value);
		return ref;
	}

	public Page<CashRecord> listCashRecordByStatus(final int status,List<String> userid, final int index, final int size) {
		Page<CashRecord> page = new Page<>();
		page.setIndex(index);
		page.setSize(size);
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM `v_cashrecord` WHERE 1=1");
		if (status >= 0) {
			sql.append(" and f_status=").append(status);
		}
		
		if (userid != null && (!userid.isEmpty())) {
			sql.append(" AND f_userId in (");
			for (int i = 0; i < userid.size(); i++) {
				if (i > 0) {
					sql.append(",");
				}

				sql.append(userid.get(i));
			}

			sql.append(") ");
		}

		int total = jdbcHelper.queryForObject("SELECT count(*) " + sql.toString(), Integer.class);
		page.setTotal(total);
		sql.append(" ORDER BY f_createTime DESC");
		sql.append(" LIMIT ").append((page.getIndex() - 1) * page.getSize()).append(" , ").append(page.getSize());

		System.out.println(sql.toString());
		List<CashRecord> list = jdbcHelper.query("SELECT * " + sql.toString(), new RowMapper<CashRecord>() {
			@Override
			public CashRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new CashRecord(rs);
			}
		});

		page.setList(list);
		return page;

	}

	/**
	 * 更新结算状态
	 * @param id
	 * @return
	 */
	public int updateWithdrawStatus(Integer id){
		Integer result = jdbcHelper.update("update t_cashrecord set f_status = 1,f_clearingTime = now() where f_id = ?",id);
		return result;
	}
}

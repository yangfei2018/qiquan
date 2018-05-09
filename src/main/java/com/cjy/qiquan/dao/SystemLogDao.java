package com.cjy.qiquan.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cjy.qiquan.dbhelper.JdbcHelper;
import com.cjy.qiquan.model.SystemLog;
import com.cjy.qiquan.po.Page;
import com.cjy.qiquan.utils.DateFormater;

@Repository
public class SystemLogDao {

	@Autowired
	private JdbcHelper jdbcHelper;

	public int create(SystemLog systemLog) {
		return jdbcHelper.executeUpdateReturnKeyGenerate(
				"INSERT INTO `t_systemLog`(f_body,f_mainId,f_belongUserId) value(?,?,?)",
				new Object[] { systemLog.getBody(), systemLog.getMainId(), systemLog.getBelongUserId() });
	}

	public int updateColById(final String col, final Object value, final int id) {
		StringBuilder sb = new StringBuilder("UPDATE `t_systemLog` SET ");
		sb.append(col).append("=? WHERE f_id=?");

		int ref = jdbcHelper.update(sb.toString(), value, id);
		return ref;
	}

	public Page<SystemLog> listPaged(final int userid, int index, int size) {
		Page<SystemLog> page = new Page<>();
		page.setIndex(index);
		page.setSize(size);
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM `t_systemLog` WHERE 1=1");
		if (userid > 0) {
			sql.append(" AND f_belongUserId=").append(userid);
		}

		int total = jdbcHelper.queryForObject("SELECT count(*) " + sql.toString(), Integer.class);
		page.setTotal(total);
		sql.append(" ORDER BY f_createTime DESC");
		sql.append(" LIMIT ").append((page.getIndex() - 1) * page.getSize()).append(" , ").append(page.getSize());

		System.out.println(sql.toString());
		List<SystemLog> list = jdbcHelper.query("SELECT * " + sql.toString(), new RowMapper<SystemLog>() {
			@Override
			public SystemLog mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new SystemLog(rs);
			}
		});

		page.setList(list);
		return page;
	}

	public List<SystemLog> listUnreadMessage(final int userid) {
		return jdbcHelper.query("SELECT * from `t_systemLog` WHERE f_belongUserId=? and isnull(f_readTime) ORDER BY f_createTime DESC",
				new Object[] { userid }, new RowMapper<SystemLog>() {
					@Override
					public SystemLog mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new SystemLog(rs);
					}
				});
	}
	
	
	public int updateUnReadMessage(final int userid,final int mainId) {
		return jdbcHelper.update("UPDATE `t_systemLog` SET f_readTime=? where f_belongUserId=? and f_mainId=?",DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()), DateFormater.datetimeFormat2), userid,mainId);
	}
}

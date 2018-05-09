package com.cjy.qiquan.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cjy.qiquan.dbhelper.JdbcHelper;
import com.cjy.qiquan.model.GmUser;

@Repository
public class GmDao {

	final static Logger Out = Logger.getLogger(GmDao.class);
	@Autowired
	private JdbcHelper jdbcHelper;

	private RowMapper<GmUser> USER = new RowMapper<GmUser>() {
		@Override
		public GmUser mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			GmUser user = new GmUser(resultSet);
			return user;
		}
	};

	public int create(GmUser user) {
		return jdbcHelper.executeUpdateReturnKeyGenerate(
				"INSERT INTO `t_gmuser`(f_name,f_password_hash,f_role,f_isAdmin,f_positionId,f_realName,f_status) value(?,?,?,?,?,?,?)",
				new Object[] { user.getName(), user.getPassword_hash(), user.getRole(), user.getIsAdmin(),
						user.getPositionId(), user.getRealName(),user.getStatus() });
	}

	public int update(GmUser user) {
		return jdbcHelper.update(
				"update `t_gmuser` set f_realName=?,f_status=?,f_isAdmin=?,f_positionId=? WHERE f_id=?",
				new Object[] { user.getRealName(), user.getStatus(), user.getIsAdmin(), user.getPositionId(),
						user.getUserId() });
	}
	
	public List<GmUser> listAllGmUser(){
		return jdbcHelper.query("SELECT * FROM `t_gmuser` ", USER);
	}

	public GmUser getUserById(final int id) {
		return jdbcHelper.queryForObject("SELECT * FROM t_gmuser where f_id=?", new Object[] { id }, USER);
	}

	public int updateColById(final String col, final Object value, final int id) {
		StringBuilder sb = new StringBuilder("UPDATE `t_gmuser` SET ");
		sb.append(col).append("=? WHERE f_id=?");

		int ref = jdbcHelper.update(sb.toString(), value, id);
		return ref;
	}

	public GmUser getUserByName(final String name) {
		return jdbcHelper.queryForObject("SELECT * FROM `t_gmuser` where f_name=?", new Object[] { name }, USER);
	}

}

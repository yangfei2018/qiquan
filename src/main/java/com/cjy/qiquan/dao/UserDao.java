package com.cjy.qiquan.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cjy.qiquan.dbhelper.JdbcHelper;
import com.cjy.qiquan.model.User;
import com.cjy.qiquan.model.VUser;
import com.cjy.qiquan.po.Page;

@Repository
public class UserDao {

	final static Logger Out = Logger.getLogger(UserDao.class);
	@Autowired
	private JdbcHelper jdbcHelper;

	private RowMapper<User> USER = new RowMapper<User>() {
		@Override
		public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			User user = new User(resultSet);
			return user;
		}
	};

	private RowMapper<VUser> VUSER = new RowMapper<VUser>() {
		@Override
		public VUser mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			VUser user = new VUser(resultSet);
			return user;
		}
	};

	public int create(User user) {
		return jdbcHelper.executeUpdateReturnKeyGenerate(
				"INSERT INTO `t_user`(f_name,f_password_hash,f_role,f_mobile,f_partnerCode) value(?,?,?,?,?)",
				new Object[] { user.getName(), user.getPassword_hash(), user.getRole(), user.getMobile(),
						user.getPartnerCode() });
	}

	public int update(User user) {
		return jdbcHelper.update(
				"update `t_user` set f_realName=?,f_idCard=?,f_address=?,f_bankOfDeposit=?,f_bankCardNo=?,f_bankAddress=?,f_idCardFrontPic=?,f_idCardBackgroundPic=?,f_status=?,f_statusChangeTime=NOW() WHERE f_id=?",
				new Object[] { user.getRealName(), user.getIdCard(), user.getAddress(), user.getBankOfDeposit(),
						user.getBankCardNo(), user.getBankAddress(), user.getIdCardFrontPic(),
						user.getIdCardBackgroundPic(), user.getStatus(), user.getUserId() });
	}

	public User getUserById(final int id) {
		return jdbcHelper.queryForObject("SELECT * FROM t_user where f_id=?", new Object[] { id }, USER);
	}

	public VUser getVUserById(final int id) {
		return jdbcHelper.queryForObject("SELECT * FROM v_user where f_id=?", new Object[] { id }, VUSER);
	}

	public int updateColById(final String col, final Object value, final int id) {
		StringBuilder sb = new StringBuilder("UPDATE `t_user` SET ");
		sb.append(col).append("=? WHERE f_id=?");

		int ref = jdbcHelper.update(sb.toString(), value, id);
		return ref;
	}

	public User getUserByName(final String name) {
		return jdbcHelper.queryForObject("SELECT * FROM t_user where f_name=?", new Object[] { name }, USER);
	}

	public List<VUser> listUserByHehuoId(final int hehuoId) {
		List<VUser> list = jdbcHelper.query("SELECT * from `v_user` WHERE f_partnerbankinfoId=?",
				new Object[] { hehuoId }, new RowMapper<VUser>() {
					@Override
					public VUser mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new VUser(rs);
					}
				});
		return list;
	}

	public List<Integer> listHehuoPos(final int hehuoId) {
		return jdbcHelper.query(
				"SELECT f_partnerposition FROM `t_user` WHERE f_partnerbankinfoId=? ORDER BY f_partnerposition asc",
				new Object[] { hehuoId }, new RowMapper<Integer>() {
					@Override
					public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getInt("f_partnerposition");
					}
				});
	}

	public List<String> listUserIdsByCodes(final List<String> codes) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT f_id FROM `t_user` ");
		if (codes != null && (!codes.isEmpty())) {
			sql.append(" WHERE f_partnerCode in (");
			for (int i = 0; i < codes.size(); i++) {
				if (i > 0) {
					sql.append(",");
				}

				sql.append("'").append(codes.get(i)).append("'");
			}

			sql.append(") ");
		}

		return jdbcHelper.query(sql.toString(), new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("f_id") + "";
			}
		});
	}

	public Page<VUser> listUserPagedByStatusId(final int status, final List<String> codes, final int index,
			final int size) {
		Page<VUser> page = new Page<>();
		page.setIndex(index);
		page.setSize(size);
		StringBuilder sql = new StringBuilder();
		sql.append(" FROM `v_user` WHERE 1=1");
		if (status > 0) {
			sql.append(" AND f_status=").append(status);
		}

		if (codes != null && (!codes.isEmpty())) {
			sql.append(" AND f_partnerCode in (");
			for (int i = 0; i < codes.size(); i++) {
				if (i > 0) {
					sql.append(",");
				}

				sql.append("'").append(codes.get(i)).append("'");
			}

			sql.append(") ");
		}

		int total = jdbcHelper.queryForObject("SELECT count(*) " + sql.toString(), Integer.class);
		page.setTotal(total);
		sql.append(" ORDER BY f_regTime DESC");
		sql.append(" LIMIT ").append((page.getIndex() - 1) * page.getSize()).append(" , ").append(page.getSize());

		System.out.println(sql.toString());
		List<VUser> list = jdbcHelper.query("SELECT * " + sql.toString(), new RowMapper<VUser>() {
			@Override
			public VUser mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new VUser(rs);
			}
		});

		page.setList(list);
		return page;
	}

}

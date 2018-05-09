package com.cjy.qiquan.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cjy.qiquan.dbhelper.JdbcHelper;
import com.cjy.qiquan.model.Cl;

@Repository
public class ClDao {

	
	@Autowired
	private JdbcHelper jdbcHelper;
	
	
	private RowMapper<Cl> CL = new RowMapper<Cl>() {
		@Override
		public Cl mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			Cl cl = new Cl(resultSet);
			return cl;
		}
	};
	
	
	public Cl getRecentPriceByszlable(final String szlable) {
		return jdbcHelper.queryForObject("SELECT szlable,fnewprice from cl WHERE szlable=? order by id desc limit 1", new Object[] {szlable},CL);
	}
	
}

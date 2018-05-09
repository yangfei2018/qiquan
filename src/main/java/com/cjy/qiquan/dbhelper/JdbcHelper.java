package com.cjy.qiquan.dbhelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class JdbcHelper extends JdbcTemplate {

	@Override
	public <T> T queryForObject(String sql, Object[] args,
			RowMapper<T> rowMapper) throws DataAccessException {
		List<T> results = query(sql, args, new RowMapperResultSetExtractor<T>(
				rowMapper, 1));
		if (results == null || results.isEmpty()) {
			return null;
		}
		return DataAccessUtils.requiredSingleResult(results);
	}

	public int executeUpdateReturnKeyGenerate(final String sql,final Object[] args) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int autoIncId = 0;
		this.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql,
						PreparedStatement.RETURN_GENERATED_KEYS);
				PreparedStatementSetter.setValues(ps, args);
				return ps;
			}
		}, keyHolder);

		autoIncId = keyHolder.getKey().intValue();
		return autoIncId;
	}
	
}

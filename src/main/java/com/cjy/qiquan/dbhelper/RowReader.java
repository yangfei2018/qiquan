package com.cjy.qiquan.dbhelper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowReader {
	
	/**
	 * 对ResultSet返回的每一行数据进行封装的方法,用作行数据与BEAN的映射
	 */
	public void mapRow(ResultSet rs) throws SQLException;

}

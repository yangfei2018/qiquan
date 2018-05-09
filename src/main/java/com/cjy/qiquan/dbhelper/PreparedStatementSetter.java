package com.cjy.qiquan.dbhelper;

import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;

public class PreparedStatementSetter {

	/**
	 * 
	 */
	private PreparedStatementSetter() {
	}

	public static void setValues(PreparedStatement ps, Object[] args) throws SQLException {
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				Object arg = args[i];
				if (arg == null) {
					boolean useSetObject = false;
					try {
						useSetObject = (ps.getConnection().getMetaData().getDatabaseProductName().indexOf("Informix") != -1);
					} catch (Throwable ex) {
						//
					}
					if (useSetObject) {
						ps.setObject(i + 1, null);
					} else {
						ps.setNull(i + 1, Types.NULL);
					}
				} else {
					if (isStringValue(arg)) {
						ps.setString(i + 1, arg.toString());
					} else if (isDateValue(arg)) {
						ps.setTimestamp(i + 1, new java.sql.Timestamp(
								((java.util.Date) arg).getTime()));
					} else if (arg instanceof Calendar) {
						Calendar cal = (Calendar) arg;
						ps.setTimestamp(i + 1, new java.sql.Timestamp(cal
								.getTime().getTime()));
					} else {
						ps.setObject(i + 1, arg);
					}
				}
			}
		}
	}

	/**
	 * Check whether the given value can be treated as a String value.
	 */
	private static boolean isStringValue(Object inValue) {
		return (inValue instanceof String || inValue instanceof StringBuffer || inValue instanceof StringWriter);
	}

	/**
	 * Check whether the given value is a <code>java.util.Date</code> (but not
	 * one of the JDBC-specific subclasses).
	 */
	private static boolean isDateValue(Object inValue) {
		return (inValue instanceof java.util.Date && !(inValue instanceof java.sql.Date
				|| inValue instanceof java.sql.Time || inValue instanceof java.sql.Timestamp));
	}

}

package com.cjy.qiquan.client.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.cjy.qiquan.client.utils.Response;
import com.cjy.qiquan.exception.RestApiException;
import com.cjy.qiquan.org.json.JSONException;
import com.cjy.qiquan.org.json.JSONObject;

public class RestApiResponse implements Serializable {
	private static Map<String, SimpleDateFormat> formatMap = new HashMap<String, SimpleDateFormat>();
	private static final long serialVersionUID = 3519962197957449562L;
	private transient int rateLimitLimit = -1;
	private transient int rateLimitRemaining = -1;
	private transient long rateLimitReset = -1;
//	private static final boolean IS_DALVIK = Configuration.dalvik;

	public RestApiResponse() {
	}

	public RestApiResponse(Response res) {
		String limit = res.getResponseHeader("X-RateLimit-Limit");
		if (null != limit) {
			rateLimitLimit = Integer.parseInt(limit);
		}
		String remaining = res.getResponseHeader("X-RateLimit-Remaining");
		if (null != remaining) {
			rateLimitRemaining = Integer.parseInt(remaining);
		}
		String reset = res.getResponseHeader("X-RateLimit-Reset");
		if (null != reset) {
			rateLimitReset = Long.parseLong(reset);
		}
	}

	protected static Date parseDate(String str, String format)
			throws RestApiException {
		if (str == null || "".equals(str)) {
			return null;
		}
		SimpleDateFormat sdf = formatMap.get(format);
		if (null == sdf) {
			sdf = new SimpleDateFormat(format, Locale.ENGLISH);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			formatMap.put(format, sdf);
		}
		try {
			synchronized (sdf) {
				// SimpleDateFormat is not thread safe
				return sdf.parse(str);
			}
		} catch (ParseException pe) {
			throw new RestApiException("Unexpected format(" + str
					+ ") returned from sina.com.cn");
		}
	}

	protected static String getString(String name, JSONObject json,
			boolean decode) {
		String returnValue = null;
		try {
			returnValue = json.getString(name);
			if (decode) {
				try {
					returnValue = URLDecoder.decode(returnValue, "UTF-8");
				} catch (UnsupportedEncodingException ignore) {
				}
			}
		} catch (JSONException ignore) {
			// refresh_url could be missing
		}
		return returnValue;
	}

	protected static int getInt(String key, JSONObject json)
			throws JSONException {
		String str = json.getString(key);
		if (null == str || "".equals(str) || "null".equals(str)) {
			return -1;
		}
		return Integer.parseInt(str);
	}

	protected static long getLong(String key, JSONObject json)
			throws JSONException {
		String str = json.getString(key);
		if (null == str || "".equals(str) || "null".equals(str)) {
			return -1;
		}
		return Long.parseLong(str);
	}

	protected static boolean getBoolean(String key, JSONObject json)
			throws JSONException {
		String str = json.getString(key);
		if (null == str || "".equals(str) || "null".equals(str)) {
			return false;
		}
		return Boolean.valueOf(str);
	}

	public int getRateLimitLimit() {
		return rateLimitLimit;
	}

	public int getRateLimitRemaining() {
		return rateLimitRemaining;
	}

	public long getRateLimitReset() {
		return rateLimitReset;
	}
}

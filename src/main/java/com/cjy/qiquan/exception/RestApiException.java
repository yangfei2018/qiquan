package com.cjy.qiquan.exception;

import com.cjy.qiquan.org.json.JSONException;
import com.cjy.qiquan.org.json.JSONObject;


public class RestApiException extends Exception {

	private int statusCode = -1;
	private int errorCode = -1;
	private String request;
	private String error;
	private static final long serialVersionUID = -2623309261327598087L;

	public RestApiException(String msg) {
		super(msg);
	}

	public RestApiException(Exception cause) {
		super(cause);
	}

	public RestApiException(String msg, int statusCode) throws JSONException {
		super(msg);
		this.statusCode = statusCode;
	}

	public RestApiException(String msg, JSONObject json, int statusCode)
			throws JSONException {
		super(msg + "\n error:" + json.getString("error") + " error_code:"
				+ json.getInt("error_code") + json.getString("request"));
		this.statusCode = statusCode;
		this.errorCode = json.getInt("error_code");
		this.error = json.getString("error");
		this.request = json.getString("request");

	}

	public RestApiException(String msg, Exception cause) {
		super(msg, cause);
	}

	public RestApiException(String msg, Exception cause, int statusCode) {
		super(msg, cause);
		this.statusCode = statusCode;

	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getRequest() {
		return request;
	}

	public String getError() {
		return error;
	}

}

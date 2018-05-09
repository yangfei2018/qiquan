package com.cjy.qiquan.exception;

import org.springframework.http.HttpStatus;

import com.cjy.qiquan.org.json.JSONException;
import com.cjy.qiquan.org.json.JSONObject;

public class AppServiceException extends RestApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int status = HttpStatus.OK.value();

	private String code;

	public AppServiceException(String code,String message){
		super(message);
		this.code = code;
		this.status = HttpStatus.OK.value();
	}
	
	
	/**
	 * Constructor.
	 *
	 * @param message
	 *            Exception message.
	 * @param cause
	 *            An exception.
	 * @param status
	 *            It's usually a HTTP Status Code (404, 500, etc.)
	 */
	public AppServiceException(String code,String message, int status) {
		super(message);
		this.status = status;
		this.code = code;
	}

	/**
	 * 
	 * {"state":"0199","desc":"用户密码不正确"}
	 * 
	 * @param json
	 * @param statusCode
	 * @throws JSONException
	 */
	public AppServiceException(JSONObject json)
			throws JSONException {
		super(json.getString("desc"));
		this.code = json.getString("state");
	}
	
	
	
	/**
	 * Constructor.
	 *
	 * @param message
	 *            Exception message.
	 * @param status
	 *            It's usually a HTTP Status Code (404, 500, etc.)
	 */
	public AppServiceException(int status, String message) {
		super(message);
		this.status = status;
	}
	

	/**
	 * Get the exception's status. It's usually a HTTP Status Code (404, 500,
	 * etc.)
	 */
	public int getStatus() {
		return this.status;
	}

	public String getCode() {
		return code;
	}
	
}

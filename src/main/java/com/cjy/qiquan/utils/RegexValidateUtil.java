package com.cjy.qiquan.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cjy.qiquan.exception.AppServiceException;

/**
 * 正则验证
 * 
 * @author pc
 *
 */
public class RegexValidateUtil {

	public static final String REG_MAIL = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	public static final String REG_MOBILE = "^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";

	/*
	 * 验证邮箱
	 * 
	 * @param email
	 * 
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			Pattern regex = Pattern.compile(REG_MAIL);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean checkMobileNumber(String mobileNumber) {
		boolean flag = false;
		try {
			Pattern regex = Pattern.compile(REG_MOBILE);
			Matcher matcher = regex.matcher(mobileNumber);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 密码验证
	 * 
	 * @param password
	 * @throws AppServiceException
	 */
	public static void check_password(final String password) throws AppServiceException {
		if (CommonUtils.isBlankOrNull(password)) {
			throw new AppServiceException(StatesUtils.States.not_validate, "密码不能为空");
		}

		if (password.length() < 6 || password.length() > 128) {
			throw new AppServiceException(StatesUtils.States.params_error, "密码长度必须在6~128个字符之间");
		}
	}

}

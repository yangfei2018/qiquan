package com.cjy.qiquan.web.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;


/**
 * Cookie 辅助类
 * 
 * @author liufang
 * 
 */
public class CookieUtils {
	/**
	 * 好友列表页数cookie名称
	 */
	public static final String COOKIE_FPS = "_cookie_friend_count";
	/**
	 * 默认每页条数
	 */
	public static final int DEFAULT_SIZE = 10;
	/**
	 * 最大每页条数
	 */
	public static final int MAX_SIZE = 20;

	/**
	 * 获得cookie的好友总页数
	 * 
	 * 使用_cookie_friend_page_count作为cookie name
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return
	 */
	public static int getFreindCount(HttpServletRequest request) {
		Cookie cookie = getCookie(request, COOKIE_FPS);
		int count = 1;
		if (cookie != null) {
			try {
				count = Integer.parseInt(cookie.getValue());
			} catch (Exception e) {
			}
		}
		return count;
	}

	/**
	 * 获得cookie
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param name
	 *            cookie name
	 * @return if exist return cookie, else return null.
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					return c;
				}
			}
		}
		return null;
	}

	/**
	 * 根据部署路径，将cookie保存在根目录。
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 * @return
	 */
	public static Cookie addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value) {
		return addCookie(request,response,name,value,24*60*60*7);
	}
	
	public static Cookie addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,int expiry) {
		Cookie cookie = new Cookie(name, value);
		String ctx = request.getContextPath();
		if (!StringUtils.hasText(ctx)) {
			cookie.setPath("/");
		} else {
			cookie.setPath(ctx);
		}
		cookie.setMaxAge(expiry);
		response.addCookie(cookie);
		return cookie;
	}

	/**
	 * 取消cookie
	 * 
	 * @param response
	 * @param name
	 * @param domain
	 */
	public static void delCookie(HttpServletRequest request,
			HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, null);
		String ctx = request.getContextPath();
		if (!StringUtils.hasText(ctx)) {
			cookie.setPath("/");
		} else {
			cookie.setPath(ctx);
		}
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
}

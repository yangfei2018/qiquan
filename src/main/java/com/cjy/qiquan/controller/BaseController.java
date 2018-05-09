package com.cjy.qiquan.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cjy.qiquan.cache.CacheManager;
import com.cjy.qiquan.cache.redis.RedisManager;
import com.cjy.qiquan.exception.AuthorizationException;
import com.cjy.qiquan.exception.NotMasterValidateException;
import com.cjy.qiquan.exception.NotPartnerValidateException;
import com.cjy.qiquan.exception.NotValidateException;
import com.cjy.qiquan.exception.UserBanException;
import com.cjy.qiquan.model.GmUser;
import com.cjy.qiquan.model.Partner;
import com.cjy.qiquan.model.User;
import com.cjy.qiquan.model.UserSession;
import com.cjy.qiquan.service.GmService;
import com.cjy.qiquan.service.PartnerService;
import com.cjy.qiquan.service.UserService;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.RegistAuthority;
import com.cjy.qiquan.web.WebContext;

/**
 * Created by chenjiyin on 15/6/3.
 */
public class BaseController extends HandlerInterceptorAdapter {

	static final Logger Out = Logger.getLogger(BaseController.class);

	protected CacheManager cacheManager = new RedisManager();

	public static class PageIds {
		public final static String INDEX = "index";
		public final static String MASTER_LOGIN = "gm/login";
		public final static String PARTNER_LOGIN = "partner/login";
	}

	public BaseController() {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getRequestURI();
		if (url.indexOf("/images") >= 0 || url.indexOf("/js/") >= 0 || url.indexOf("/css") >= 0
				|| url.indexOf("/font") >= 0) {
			return true;
		}
		WebContext.setClient(request, response);
		WebContext.getSession().setAttribute("tmpuri", url);
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
			// 根据annotation 拦截判断各种情况
			HandlerMethod handler2 = (HandlerMethod) handler;
			RegistAuthority registAuthority = handler2.getMethodAnnotation(RegistAuthority.class);
			if (null != registAuthority) {
				if (registAuthority.value() == Constant.AuthorityType.CUSTOMER) {
					// 普通用户登录
					UserSession userSession = (UserSession) WebContext.getSession()
							.getAttribute(Constant.SESSION_KEY.currentUser);
					if (userSession == null) {
						if (WebContext.getCookie(Constant.SESSION_KEY.currentUser) != null) {
							String token = WebContext.getCookie(Constant.SESSION_KEY.currentUser);
							User user = UserService.instance().getUserByToken(token);
							if (user != null) {

								if (user.getStatus() == -1) {
									throw new NotValidateException();
								}
								
								if (user.getBan()==1) {
									throw new UserBanException();
								}
								
								userSession = new UserSession(token, user.getUserId(), user.getName(), "customer",
										user.getStatus());
								WebContext.getSession().setAttribute(Constant.SESSION_KEY.currentUser, userSession);
							} else {
								WebContext.getSession().removeAttribute(Constant.SESSION_KEY.currentUser);
								WebContext.delCookie(Constant.SESSION_KEY.currentUser);
								throw new AuthorizationException();
							}
						} else {
							throw new AuthorizationException();
						}
					} else {
						// 验证token是否过期
						String token = userSession.getToken();
						User user = UserService.instance().getUserByToken(token);
						if (user == null) {
							WebContext.getSession().removeAttribute(Constant.SESSION_KEY.currentUser);
							WebContext.delCookie(Constant.SESSION_KEY.currentUser);
							throw new AuthorizationException();
						} else {
							if (user.getStatus() == -1) {
								throw new NotValidateException();
							}
							if (user.getBan()==1) {
								throw new UserBanException();
							}
						}
					}
				} else if (registAuthority.value() == Constant.AuthorityType.MASTER) {
					// GM用户
					UserSession userSession = (UserSession) WebContext.getSession()
							.getAttribute(Constant.SESSION_KEY.currentGmUser);
					if (userSession == null) {
						if (WebContext.getCookie(Constant.SESSION_KEY.currentGmUser) != null) {
							String token = WebContext.getCookie(Constant.SESSION_KEY.currentGmUser);
							GmUser user = GmService.instance().getUserByToken(token);
							if (user != null) {
								if (user.getStatus() == -1) {
									throw new NotMasterValidateException();
								}

								userSession = new UserSession(token, user.getUserId(), user.getName(), user.getPositionName(),
										user.getStatus());
								WebContext.getSession().setAttribute(Constant.SESSION_KEY.currentGmUser, userSession);
							} else {
								WebContext.getSession().removeAttribute(Constant.SESSION_KEY.currentGmUser);
								WebContext.delCookie(Constant.SESSION_KEY.currentGmUser);
								throw new NotMasterValidateException();
							}
						} else {
							throw new NotMasterValidateException();
						}
					} else {
						// 验证token是否过期
						String token = userSession.getToken();
						GmUser user = GmService.instance().getUserByToken(token);
						if (user == null) {
							WebContext.getSession().removeAttribute(Constant.SESSION_KEY.currentGmUser);
							WebContext.delCookie(Constant.SESSION_KEY.currentGmUser);
							throw new NotMasterValidateException();
						} else {
							if (user.getStatus() == -1) {
								throw new NotMasterValidateException();
							}
						}
					}
				} else if (registAuthority.value() == Constant.AuthorityType.PARTNER) {
					// 合作方用户
					UserSession userSession = (UserSession) WebContext.getSession()
							.getAttribute(Constant.SESSION_KEY.currentPartnerUser);
					if (userSession == null) {
						if (WebContext.getCookie(Constant.SESSION_KEY.currentPartnerUser) != null) {
							String token = WebContext.getCookie(Constant.SESSION_KEY.currentPartnerUser);
							Partner user = PartnerService.instance().getUserByToken(token);
							if (user != null) {
								if (user.getStatus() == -1) {
									throw new NotPartnerValidateException();
								}

								userSession = new UserSession(token, user.getUserId(), user.getName(), "partner",
										user.getStatus());
								WebContext.getSession().setAttribute(Constant.SESSION_KEY.currentPartnerUser, userSession);
							} else {
								WebContext.getSession().removeAttribute(Constant.SESSION_KEY.currentPartnerUser);
								WebContext.delCookie(Constant.SESSION_KEY.currentPartnerUser);
								throw new NotPartnerValidateException();
							}
						} else {
							throw new NotPartnerValidateException();
						}
					} else {
						// 验证token是否过期
						String token = userSession.getToken();
						Partner user = PartnerService.instance().getUserByToken(token);
						if (user == null) {
							WebContext.getSession().removeAttribute(Constant.SESSION_KEY.currentPartnerUser);
							WebContext.delCookie(Constant.SESSION_KEY.currentPartnerUser);
							throw new NotPartnerValidateException();
						} else {
							if (user.getStatus() == -1) {
								throw new NotPartnerValidateException();
							}
						}
					}
				}
			}
		}
		return true;
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		WebContext.releaseClient();
	}
}

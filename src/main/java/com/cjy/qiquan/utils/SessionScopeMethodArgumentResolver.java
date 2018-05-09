package com.cjy.qiquan.utils;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.cjy.qiquan.model.GmUser;
import com.cjy.qiquan.model.Partner;
import com.cjy.qiquan.model.User;
import com.cjy.qiquan.model.UserSession;
import com.cjy.qiquan.service.GmService;
import com.cjy.qiquan.service.PartnerService;
import com.cjy.qiquan.service.UserService;
import com.cjy.qiquan.web.WebContext;

public class SessionScopeMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// 让方法和参数，两种target通过
		if (parameter.hasParameterAnnotation(SessionScope.class))
			return true;
		else if (parameter.getMethodAnnotation(SessionScope.class) != null)
			return true;
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		Class<?> klass = parameter.getParameterType();

		if (klass.isAssignableFrom(User.class)) {
			if (WebContext.getSession().getAttribute(Constant.SESSION_KEY.currentUser) != null) {
				UserSession userSession = (UserSession) WebContext.getSession()
						.getAttribute(Constant.SESSION_KEY.currentUser);
				return UserService.instance().getUserById(userSession.getUserId());
			}
		}

		if (klass.isAssignableFrom(GmUser.class)) {
			if (WebContext.getSession().getAttribute(Constant.SESSION_KEY.currentGmUser) != null) {
				UserSession userSession = (UserSession) WebContext.getSession()
						.getAttribute(Constant.SESSION_KEY.currentGmUser);
				return GmService.instance().getUserById(userSession.getUserId());
			}
		}

		if (klass.isAssignableFrom(Partner.class)) {
			if (WebContext.getSession().getAttribute(Constant.SESSION_KEY.currentPartnerUser) != null) {
				UserSession userSession = (UserSession) WebContext.getSession()
						.getAttribute(Constant.SESSION_KEY.currentPartnerUser);
				return PartnerService.instance().getPartnerById(userSession.getUserId());
			}
		}

		return null;
	}
}

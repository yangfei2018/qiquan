package com.cjy.qiquan.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.cjy.qiquan.po.ErrorVo;
import com.cjy.qiquan.utils.StatesUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

	private String defaultErrorView;

	public String getDefaultErrorView() {
		return defaultErrorView;
	}

	public void setDefaultErrorView(String defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handdler,
			Exception ex) {
		ErrorVo err = new ErrorVo();
		if (ex instanceof AppServiceException) {
			response.setStatus(((AppServiceException) ex).getStatus());
			err.setState(((AppServiceException) ex).getCode());
			err.setDesc(ex.getMessage());
		} else if (ex instanceof AuthorizationException) {
			err.setState(StatesUtils.States.not_login);
			err.setDesc("您尚未登录");
		} else if (ex instanceof UserBanException) {
			err.setState(StatesUtils.States.not_validate);
			err.setDesc("账号已被封停，请联系管理员");
		} else {
			// response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			err.setState(StatesUtils.States.server_error);
			err.setDesc(ex.getMessage());
		}

		if (!(request.getHeader("accept").indexOf("application/json") > -1
				|| (request.getHeader("X-Requested-With") != null
						&& request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
			ModelAndView model;
			if (err.getState().equals(StatesUtils.States.not_login)) {
				model = new ModelAndView("index");
			} else {
				model = new ModelAndView(defaultErrorView);
			}
			model.addObject(err);
			return model;
		} else {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");

			PrintWriter writer;
			try {
				writer = response.getWriter();
				writer.write(new ObjectMapper().writeValueAsString(err));
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		//
		// String viewName = super.determineViewName(ex, request);
		// if (viewName!=null){
		//
		//
		//
		// ModelAndView model = new ModelAndView(defaultErrorView);
		// model.addObject(err);
		// return model;
		// }else{
		// response.setCharacterEncoding("UTF-8");
		// response.setContentType("application/json");
		//
		// PrintWriter writer;
		// try {
		// writer = response.getWriter();
		// writer.write(new ObjectMapper().writeValueAsString(err));
		// writer.flush();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// return null;
		// }
	}
}

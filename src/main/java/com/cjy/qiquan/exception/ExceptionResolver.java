package com.cjy.qiquan.exception;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.cjy.qiquan.controller.BaseController;
import com.cjy.qiquan.po.ErrorVo;
import com.cjy.qiquan.po.ResultVo;
import com.cjy.qiquan.utils.StatesUtils;

/**
 * 不必在Controller中对异常进行处理，抛出即可，由此异常解析器统一控制。<br>
 * ajax请求（有@ResponseBody的Controller）发生错误，输出JSON。<br>
 * 页面请求（无@ResponseBody的Controller）发生错误，输出错误页面。<br>
 * 需要与AnnotationMethodHandlerAdapter使用同一个messageConverters<br>
 * Controller中需要有专门处理异常的方法。
 *
 *
 */
public class ExceptionResolver extends ExceptionHandlerExceptionResolver {

	private String defaultErrorView;

	public String getDefaultErrorView() {
		return defaultErrorView;
	}

	public void setDefaultErrorView(String defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}

	private String notLoginErrorView;

	public String getNotLoginErrorView() {
		return notLoginErrorView;
	}

	public void setNotLoginErrorView(String notLoginErrorView) {
		this.notLoginErrorView = notLoginErrorView;
	}

	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
			HandlerMethod handlerMethod, Exception exception) {

		if (handlerMethod == null) {
			return null;
		}

		Method method = handlerMethod.getMethod();

		if (method == null) {
			return null;
		}
		// 如果定义了ExceptionHandler则返回相应的Map中的数据
		ModelAndView returnValue = super.doResolveHandlerMethodException(request, response, handlerMethod, exception);
		ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
		if (responseBodyAnn != null) {
			try {
				ResponseStatus responseStatusAnn = AnnotationUtils.findAnnotation(method, ResponseStatus.class);
				if (responseStatusAnn != null) {
					HttpStatus responseStatus = responseStatusAnn.value();
					String reason = responseStatusAnn.reason();
					if (!StringUtils.hasText(reason)) {
						response.setStatus(responseStatus.value());
					} else {
						try {
							response.sendError(responseStatus.value(), reason);
						} catch (IOException e) {
						}
					}
				}
				// 如果没有ExceptionHandler注解那么returnValue就为空
				if (returnValue == null) {
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json");
					// response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
					// ErrorVo err = new ErrorVo();
					ResultVo.Builder<String> err = new ResultVo.Builder<String>(false);
					if (exception instanceof AppServiceException) {
						response.setStatus(((AppServiceException) exception).getStatus());
						// err.setState(((AppServiceException) exception).getCode());
						err.setData(exception.getMessage());
					} else if (exception instanceof AuthorizationException) {
						// err.setState(StatesUtils.States.not_login);
						err.setData("您尚未登录");
					} else if (exception instanceof NotValidateException) {
						// err.setState(StatesUtils.States.not_validate);
						err.setData("您的账号尚未激活，无法执行此操作");
					} else if (exception instanceof UserBanException) {
						// err.setState(StatesUtils.States.not_validate);
						err.setData("您的账号已被封停，无法执行此操作");
					}
					else if (exception instanceof NotMasterValidateException) {
						// err.setState(StatesUtils.States.not_validate);
						err.setData("您的账号不是管理员，无法执行此操作");
					} else if (exception instanceof NotPartnerValidateException) {
						// err.setState(StatesUtils.States.not_validate);
						err.setData("您的账号不是代理商管理员，无法执行此操作");
					} else if (exception instanceof RestApiException) {
						err.setData(((RestApiException) exception).getError());
					} else {
						// response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
						// err.setState(StatesUtils.States.server_error);
						err.setData("系统忙，请稍后再试");
					}
					handleResponseError(err.build(), request, response);
					return new ModelAndView();
				}
				return handleResponseBody(returnValue, request, response);
			} catch (Exception e) {
				return null;
			}
		}

		if (null == returnValue) {
			ErrorVo err = new ErrorVo();
			if (exception instanceof AppServiceException) {
				response.setStatus(((AppServiceException) exception).getStatus());
				err.setState(((AppServiceException) exception).getCode());
				err.setDesc(exception.getMessage());
			} else if (exception instanceof AuthorizationException) {
				err.setState(StatesUtils.States.not_login);
				err.setDesc("您尚未登录");
			} else if (exception instanceof NotValidateException) {
				err.setState(StatesUtils.States.not_validate);
				err.setDesc("您的账号尚未激活，无法执行此操作");
			} else if (exception instanceof NotPartnerValidateException) {
				err.setState(StatesUtils.States.not_partner);
				err.setDesc("您的账号不是代理商管理员，无法执行此操作");
			} else if (exception instanceof UserBanException) {
				err.setState(StatesUtils.States.not_login);
				err.setDesc("您的账号已被封停，无法执行此操作");
			} 
			else if (exception instanceof NotMasterValidateException) {
				err.setState(StatesUtils.States.not_master);
				err.setDesc("您的账号不是管理员，无法执行此操作");
			} else if (exception instanceof RestApiException) {
				err.setState(String.valueOf(((RestApiException) exception).getErrorCode()));
				err.setDesc(((RestApiException) exception).getError());
			} else {
				err.setDesc("系统忙，请稍后再试 ");
			}
			returnValue = new ModelAndView();
			returnValue.addObject(err);
			if (null == returnValue.getViewName()) {
				if (err.getState() == StatesUtils.States.not_login) {
					return new ModelAndView("redirect:/" + BaseController.PageIds.INDEX);
				} else if (err.getState() == StatesUtils.States.not_master) {
					return new ModelAndView("redirect:/" + BaseController.PageIds.MASTER_LOGIN);
				} else if (err.getState() == StatesUtils.States.not_partner) {
					return new ModelAndView("redirect:/" + BaseController.PageIds.PARTNER_LOGIN);
				} else {
					returnValue.setViewName(defaultErrorView);
				}
			}
		}
		return returnValue;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "resource" })
	private ModelAndView handleResponseBody(ModelAndView returnValue, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Map value = returnValue.getModelMap();
		HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
		if (acceptedMediaTypes.isEmpty()) {
			acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
		}
		MediaType.sortByQualityValue(acceptedMediaTypes);
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
		Class<?> returnValueType = value.getClass();
		List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
		if (messageConverters != null) {
			for (MediaType acceptedMediaType : acceptedMediaTypes) {
				for (HttpMessageConverter messageConverter : messageConverters) {
					if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
						messageConverter.write(value, acceptedMediaType, outputMessage);
						return new ModelAndView();
					}
				}
			}
		}
		if (logger.isWarnEnabled()) {
			logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and "
					+ acceptedMediaTypes);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "resource" })
	private ModelAndView handleResponseError(ResultVo<String> returnValue, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
		if (acceptedMediaTypes.isEmpty()) {
			acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
		}
		MediaType.sortByQualityValue(acceptedMediaTypes);
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
		Class<?> returnValueType = returnValue.getClass();
		List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
		if (messageConverters != null) {
			for (MediaType acceptedMediaType : acceptedMediaTypes) {
				for (HttpMessageConverter messageConverter : messageConverters) {
					if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
						messageConverter.write(returnValue, acceptedMediaType, outputMessage);
						return new ModelAndView();
					}
				}
			}
		}
		if (logger.isWarnEnabled()) {
			logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and "
					+ acceptedMediaTypes);
		}
		return null;
	}

}

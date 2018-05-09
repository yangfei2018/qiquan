package com.cjy.qiquan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cjy.qiquan.exception.AppServiceException;
import com.cjy.qiquan.model.LoginVo;
import com.cjy.qiquan.po.ResultVo;
import com.cjy.qiquan.service.UserService;
import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.web.WebContext;

@Controller
@RequestMapping("/passport")
public class PassportController {

	@Autowired
	private UserService userService;

	@RequestMapping("/login")
	public String login() {
		return "/passport/login";
	}

	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> doLogin(@RequestParam(required = false, defaultValue = "") String name,
			@RequestParam(required = false, defaultValue = "") String password) {
		ResultVo.Builder<String> res = new ResultVo.Builder<>();

		if (CommonUtils.isBlankOrNull(name) || CommonUtils.isBlankOrNull(password)) {
			return res.setResult(false).setData("用户名或密码不能为空").build();
		}
		try {
			LoginVo user = userService.login(name, password);
			WebContext.getSession().setAttribute(Constant.SESSION_KEY.currentUser, user);
			res.setResult(true).setData("登录成功");
		} catch (AppServiceException e) {
			return res.setResult(false).setData(e.getMessage()).build();
		}

		return res.build();
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout() {
		WebContext.getSession().removeAttribute(Constant.SESSION_KEY.currentUser);
		WebContext.delCookie(Constant.SESSION_KEY.currentUser);
		return "redirect:/passport/login";
	}

}

package com.cjy.qiquan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cjy.qiquan.service.WsSocketService;

@Controller
@RequestMapping("/json")
public class JsonController {

	@RequestMapping("/startService")
	public @ResponseBody String startService() {
		WsSocketService.instance().startServer();
		return "SUCCESS";
	}

}

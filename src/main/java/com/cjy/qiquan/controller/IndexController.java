package com.cjy.qiquan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {



	@RequestMapping("/index")
	public String index(Model model) {

		return "index";
	}

	
	@RequestMapping("/reg")
	public String reg(Model model) {

		return "reg";
	}
	

	@RequestMapping("/page_404")
	public String page_404() {
		return "page_404";
	}
}

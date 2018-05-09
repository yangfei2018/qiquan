package com.cjy.qiquan.controller;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cjy.qiquan.model.PartnerBankInfo;
import com.cjy.qiquan.model.User;
import com.cjy.qiquan.service.PartnerService;
import com.cjy.qiquan.service.UserService;
import com.cjy.qiquan.utils.DateFormater;

@Controller
@RequestMapping("/agreement")
public class AgreementController {

	@Autowired
	private UserService userService;

	@Autowired
	private PartnerService partnerService;

	@RequestMapping(value = "/yxhh", method = RequestMethod.GET)
	public String yxhh(@RequestParam int userid, Model model) {
		User user = userService.getUserById(userid);
		if (user != null) {
			PartnerBankInfo partner = partnerService.getPartnerBankInfoOnice(user);
			model.addAttribute("partner", partner);
			Calendar cal = Calendar.getInstance();
			int year = cal.get(cal.YEAR);
			model.addAttribute("year", year);
			model.addAttribute("month", (cal.get(cal.MONTH)+1));
			model.addAttribute("day", cal.get(cal.DATE));

		}

		return "agreement/youxianhehuo";
	}

	@RequestMapping(value = "/hhxybc", method = RequestMethod.GET)
	public String hhxybc(@RequestParam int userid, Model model) {
		User user = userService.getUserById(userid);
		if (user != null) {
			PartnerBankInfo partner = partnerService.getPartnerBankInfoOnice(user);
			model.addAttribute("partner", partner);
			Calendar cal = Calendar.getInstance();
			int year = cal.get(cal.YEAR);
			model.addAttribute("year", year);
			model.addAttribute("month", (cal.get(cal.MONTH)+1));
			model.addAttribute("day", cal.get(cal.DATE));
			model.addAttribute("user", user);
			model.addAttribute("lei", user.getPartnerposition());
			model.addAttribute("riqi", DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()), DateFormater.dateFormat));
		}
		
		return "agreement/hhxybc";
	}

}

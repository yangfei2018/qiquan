package com.cjy.qiquan.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cjy.qiquan.utils.SpringApplicationContext;

@Service
public class AppEnv {

	public static List<String> BANKLIST = new ArrayList<>();

	static {

	}


	public static AppEnv instance() {
		return SpringApplicationContext.getBean("appEnv");
	}

	public void loadByDB() {
		BANKLIST.add("工商银行");
		BANKLIST.add("建设银行");
		BANKLIST.add("农业银行");
		BANKLIST.add("招商银行");
		BANKLIST.add("中信银行");
		BANKLIST.add("中国银行");
		BANKLIST.add("交通银行");
		BANKLIST.add("北京银行");
		BANKLIST.add("浦发银行");
		BANKLIST.add("华夏银行");
		BANKLIST.add("其他");
	}

}

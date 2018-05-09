package com.cjy.qiquan.job.impl;

import com.cjy.qiquan.job.Job;
import com.cjy.qiquan.job.JobDispatcher;
import com.cjy.qiquan.model.TradeRecord;
import com.cjy.qiquan.service.TradeService;

public class AutoDelXunjia implements JobDispatcher {

	@Override
	public void execute(Job job) {
		String tradeNo = (String) job.getExtParams().get("tradeNo");
		TradeRecord tradeRecord = TradeService.instance().getTradeRecordByTradeNo(tradeNo);
		if (tradeRecord != null && tradeRecord.getStatus() == 1) {
			TradeService.instance().delTrade(tradeRecord.getId());
		}

	}
}

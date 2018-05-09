package com.cjy.qiquan.job.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cjy.qiquan.job.Job;
import com.cjy.qiquan.job.JobDispatcher;
import com.cjy.qiquan.job.JobTaskIds;
import com.cjy.qiquan.job.JobTracker;
import com.cjy.qiquan.model.Goods;
import com.cjy.qiquan.model.TradeRecord;
import com.cjy.qiquan.service.GoodsService;
import com.cjy.qiquan.service.TradeService;

public class AutoUpdateXunjia implements JobDispatcher {

	@Override
	public void execute(Job job) {
		String tradeNo = (String) job.getExtParams().get("tradeNo");
		TradeRecord tradeRecord = TradeService.instance().getTradeRecordByTradeNo(tradeNo);
		if (tradeRecord != null && tradeRecord.getStatus() == 0) {
			
			Goods goods = GoodsService.instance().getGoodsByName(tradeRecord.getProductName());
			if (goods != null) {
				
				double feilv = tradeRecord.getPeriod()==15?goods.getFeilv_15():goods.getFeilv_30();
				
				double notionalPrincipalFormat = tradeRecord.getAmount() / feilv * 100.00; 
				
				tradeRecord.setNotionalPrincipal(notionalPrincipalFormat);
				tradeRecord.setUpdateTime(new Date(System.currentTimeMillis()));
				tradeRecord.setStatus(1);
				TradeService.instance().baojia(tradeRecord);
				
				
				Job newjob = new Job();
				newjob.setJobId("AUTO_DEL_XUNJIA_" + tradeRecord.getTradeNo());
				newjob.setDelayTime((long) (60*60*2));
				newjob.setTaskId(JobTaskIds.AUTO_DEL_BAOJIA);
				Map<String, String> params1 = new HashMap<>();
				params1.put("tradeNo", tradeRecord.getTradeNo());
				newjob.setExtParams(params1);
				JobTracker.instance().put(newjob);
			}

		}

	}
}

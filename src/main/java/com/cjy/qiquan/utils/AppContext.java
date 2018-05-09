package com.cjy.qiquan.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cjy.qiquan.job.JobTracker;
import com.cjy.qiquan.service.AppEnv;
import com.cjy.qiquan.service.GoodsService;
import com.cjy.qiquan.service.UserService;

import cn.tsign.ching.eSign.SignHelper;

@Component
public class AppContext implements ServletContextListener {

	final static Logger Out = Logger.getLogger(AppContext.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		Out.debug("=======contextDestroyed=========");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		AppEnv.instance().loadByDB();
		GoodsService.instance().loadOnStart();
		UserService.instance().loadByDB();
		JobTracker.instance().load_from_cache();
		SignHelper.initProject();
		
//		WsSocketService.instance().startServer();
	}

}

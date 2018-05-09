package com.cjy.qiquan.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class AppStart implements InitializingBean,ApplicationContextAware {

	final static Logger Out = Logger.getLogger(AppStart.class);
	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		Out.debug("setApplicationContext");
		SpringApplicationContext.setApplicationContext(arg0);
	}


	@Override
	public void afterPropertiesSet() throws Exception {

	}

}

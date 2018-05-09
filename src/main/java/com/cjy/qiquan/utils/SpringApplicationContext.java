package com.cjy.qiquan.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 程序内获得applicationContext
 * @author chenjy
 *
 */
public class SpringApplicationContext{
	
	protected static ApplicationContext applicationContext;

	public static void initApplicationContext(){
		if (applicationContext  == null){
			applicationContext = new ClassPathXmlApplicationContext("springmvc-servlet.xml","proxool.xml");
		}
	}
	
	
	public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringApplicationContext.applicationContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanid){
		if (applicationContext  == null){
			initApplicationContext();
		}
		return (T) applicationContext.getBean(beanid);
	}
	
	// 注释会影响任务:任务使用了反射机制,需要提前加载配置
//	static{
//		
//		System.out.println("SpringApplicationContext加载");
//	}
	

}

package com.cjy.qiquan.web.utils;

import org.apache.log4j.Logger;

import com.cjy.qiquan.utils.Constant;

/**
 * 
 * @author chenjiyin
 *
 */
public class Monitor {
	
	private static final Logger Out = Logger.getLogger(Monitor.class);
//	private static final int __TOTAL__ = 100000;

	private int errorCount;

	private long lastTime;
	private int tmpCount;
	
//	private int totalCount;

	/**
	 * 记录次数
	 */
	public void count() {
		long tmpTime = System.currentTimeMillis();
		if (tmpTime - lastTime > Constant.__TIME__) {
			lastTime = tmpTime;
			tmpCount = 0;
		} else {
			tmpCount++;
		}
		
		Out.debug("tmpCount:"+tmpCount);
//		totalCount++;
	}

	/**
	 * 检查时间
	 */
	public int checkError() {
//		if(__TOTAL__ < totalCount) {
//			return ++errorCount;
//		}
		if (tmpCount < Constant.__COUNT__) {
			return 0;
		}
		tmpCount = 0;
		return ++errorCount;
	}

}
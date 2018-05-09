package com.cjy.qiquan.job;

import java.util.HashMap;
import java.util.Map;

import com.cjy.qiquan.job.impl.AutoDelXunjia;
import com.cjy.qiquan.job.impl.AutoUpdateXunjia;

public class JobDispatcherFactory {

	private Map<Integer, JobDispatcher> handlersMap = new HashMap<Integer, JobDispatcher>();

	private static JobDispatcherFactory instance = new JobDispatcherFactory();

	private JobDispatcherFactory() {
		load();
	}

	public synchronized static JobDispatcherFactory instance() {
		if (instance == null) {
			instance = new JobDispatcherFactory();
		}
		return instance;
	}

	private void load() {
		handlersMap.put(JobTaskIds.AUTO_BAOJIA, new AutoUpdateXunjia());
		handlersMap.put(JobTaskIds.AUTO_DEL_BAOJIA, new AutoDelXunjia());
	}

	public JobDispatcher get(Integer command) {
		return handlersMap.get(command);
	}

}

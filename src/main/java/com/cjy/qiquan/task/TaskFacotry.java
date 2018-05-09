package com.cjy.qiquan.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




public class TaskFacotry {

	private static TaskFacotry instance = new TaskFacotry();
	
	/**任务，日常任务单独线程*/
	private ExecutorService taskExecutor = Executors.newSingleThreadExecutor();
	
	public void put(TaskEvent event) {
		taskExecutor.execute(event);
		
	}
	
	public void put(Runnable runnable){
		taskExecutor.execute(runnable);
	}
	
	public static TaskFacotry instance() {
		if (instance == null) {
			synchronized (TaskFacotry.class) {
				instance = new TaskFacotry();
			}
		}
		return instance;
	}
	
}

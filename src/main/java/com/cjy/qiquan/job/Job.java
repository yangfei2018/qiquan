package com.cjy.qiquan.job;

import java.io.Serializable;
import java.util.Map;

public class Job implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5683364644452147002L;

	
	private String jobId;
	
	private int taskId;

	private Map<String, String> extParams;
	
	public Job(){
		
	}
	
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	/**
	 * 延时多久执行
	 */
	private Long delayTime;
	
	/**
	 * 任务的最触发发时间
	 */
	private Long triggerTime;

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public Map<String, String> getExtParams() {
		return extParams;
	}

	public void setExtParams(Map<String, String> extParams) {
		this.extParams = extParams;
	}

	public Long getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Long delayTime) {
		this.delayTime = delayTime;
	}

	public Long getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(Long triggerTime) {
		this.triggerTime = triggerTime;
	}
	
	public void execute(){
		JobDispatcher dispatcher = JobDispatcherFactory.instance().get(this.taskId);
		if (dispatcher!=null){
			try {
				dispatcher.execute(this);
			} catch (Exception e) {
			}
		}
	}
	
}

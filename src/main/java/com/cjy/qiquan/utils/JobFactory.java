package com.cjy.qiquan.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JobFactory {
	private ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(2);
	private ExecutorService executor = Executors.newFixedThreadPool(3);

	private static final JobFactory instance = new JobFactory();

	public static JobFactory getInstance() {
		return instance;
	}

	private JobFactory() {
	}

	public void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		scheduled.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	public void schedule(Runnable command, long delay, TimeUnit unit) {
		scheduled.schedule(command, delay, unit);
	}

	public void exec(Runnable runnable) {
		executor.execute(runnable);
	}

	/** 延迟执行，执行一次 */
	public synchronized ScheduledFuture<?> addDelayJob(Runnable runnable, long delay, TimeUnit unit) {
		return scheduled.schedule(runnable, delay, unit);
	}

}

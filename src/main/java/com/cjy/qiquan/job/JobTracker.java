package com.cjy.qiquan.job;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.cjy.qiquan.cache.CacheManager;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.JobFactory;

public class JobTracker {

	private Map<String, ScheduledFuture<?>> futureMaps = new ConcurrentHashMap<String, ScheduledFuture<?>>();

	private static JobTracker instance = new JobTracker();

	public static JobTracker instance() {
		if (instance == null) {
			synchronized (JobTracker.class) {
				instance = new JobTracker();
			}
		}
		return instance;
	}

	public void load_from_cache() {
		Set<String> list = CacheManager.instance.setMember(Constant.CacheGroup.JOB_LIST);
		for (String jobId : list) {
			Job job = CacheManager.instance.getCache(Constant.CacheGroup.JOB_OBJ, jobId);
			if (job != null) {
				System.out.println("从缓存中重启job：" + jobId);
				restart(job);
			}
		}
	}

	public void restart(final Job job) {
		long curr = System.currentTimeMillis();
		long res = (curr - job.getTriggerTime()) / 1000;
		long delayTime = (job.getDelayTime() - res) < 0 ? 0 : (job.getDelayTime() - res);
		job.setTriggerTime(curr);
		job.setDelayTime(delayTime);
		dispatcher(job);

		// futureMaps.put(job.getJobId(), JobFactory.getInstance().addDelayJob(new
		// Runnable() {
		// @Override
		// public void run() {
		// job.execute();
		// remove(job.getJobId());
		// }
		// }, dalayTime, TimeUnit.SECONDS));
	}

	public void put(final Job job) {
		job.setTriggerTime(System.currentTimeMillis());
		dispatcher(job);
		saveCache(job);
	}

	private void dispatcher(final Job job) {
		futureMaps.put(job.getJobId(), JobFactory.getInstance().addDelayJob(new Runnable() {
			@Override
			public void run() {
				job.execute();
				remove(job.getJobId());
			}
		}, job.getDelayTime(), TimeUnit.SECONDS));
	}

	public void saveCache(Job job) {
		CacheManager.instance.setAdd(Constant.CacheGroup.JOB_LIST, job.getJobId());
		CacheManager.instance.addCache(Constant.CacheGroup.JOB_OBJ, job.getJobId(), job);
	}

	public void removeCache(String jobId) {
		CacheManager.instance.setRemove(Constant.CacheGroup.JOB_LIST, jobId);
		CacheManager.instance.removeCache(Constant.CacheGroup.JOB_OBJ, jobId);
	}

	public void remove(String jobId) {
		futureMaps.remove(jobId);
		removeCache(jobId);
	}

	public void stopJob(String jobId) {
		try {
			ScheduledFuture<?> schedule = futureMaps.remove(jobId);
			if (schedule != null) {
				schedule.cancel(true);
			}
			removeCache(jobId);
		} catch (Exception e) {
		}
	}

	public boolean containJobById(final String jobId) {
		return futureMaps.containsKey(jobId);
	}

}

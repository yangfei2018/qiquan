package com.cjy.qiquan.web.utils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.cjy.qiquan.utils.Constant;

/**
 * IP监控
 * @author chenjiyin
 *
 */
public class IpMonitorUtils {
	
	private final static Logger Out = Logger.getLogger(IpMonitorUtils.class);
	
	private static IpMonitorUtils instance = null;

	public static Map<String,Monitor> monitors;
	/** 冷却半个小时 */
	// 黑名单IP
	private Set<String> blacks = null;

	private IpMonitorUtils() {
		monitors = new ConcurrentHashMap<String, Monitor>();
		blacks = new CopyOnWriteArraySet<>();
	}

	public static IpMonitorUtils instance() {
		if (instance == null) {
			synchronized (IpMonitorUtils.class) {
				instance = new IpMonitorUtils();
			}
		}
		return instance;
	}
	
	
	/**
	 * 获取指令
	 */
	private Monitor geMonitor(String ip) {
		Monitor monitor = monitors.get(ip);
		if (monitor == null) {
			monitor = new Monitor();
			monitors.put(ip, monitor);
		}
		return monitor;
	}
	
	
	public boolean checkIpBlack(String ip){
		if (blacks.contains(ip)){
			return true;
		}
		return false;
	}
	
	/**
	 * 检查指令时间
	 */
	public boolean checkIp(String ip) {
		Monitor monitor = geMonitor(ip);
		monitor.count();
		int errorCount = monitor.checkError();
		if (errorCount == 0) {
			return true;
		} else {
			if (errorCount >= 20) {
				if (ip != null && !blacks.contains(ip)) {
					blacks.add(ip);
					Out.warn("黑名单 <- " + ip );
					class Cool implements Runnable {
						private String ip;
						private Cool(String ip) {
							this.ip = ip;
						}
						@Override
						public void run() {
							blacks.remove(ip);
							Out.warn("从黑名单中移除 -> "+ ip);
						}
					}
					addDelayJob(new Cool(ip), Constant.BLACKCOOL, TimeUnit.MILLISECONDS);
				}
			}
			return false;
		}
	}
	
	
	private static ScheduledExecutorService __EXECUTOR__  = null;
	/** 延迟执行，执行一次 */
	public synchronized static ScheduledFuture<?> addDelayJob(Runnable runnable, long delay, TimeUnit unit){
		if (__EXECUTOR__ == null) {
			__EXECUTOR__ = Executors.newSingleThreadScheduledExecutor();
		}
		return __EXECUTOR__.schedule(runnable, delay, unit);
	}
}

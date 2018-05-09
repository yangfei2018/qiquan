package com.cjy.qiquan.task;

import com.cjy.qiquan.model.SystemLog;
import com.cjy.qiquan.service.DBHandler;
import com.cjy.qiquan.service.SystemLogService;

public class SystemLogHandler extends DBHandler {

	private SystemLog systemlog;

	public SystemLogHandler(final String body, final int mainId, final int belongUserId) {
		systemlog = new SystemLog();
		systemlog.setBody(body);
		systemlog.setMainId(mainId);
		systemlog.setBelongUserId(belongUserId);
//		systemlog.setCreateTime(DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()), DateFormater.datetimeFormat2));

	}

	@Override
	protected void impl() {
		SystemLogService.instance().create(systemlog);
	}

	@Override
	protected int id() {
		return 0;
	}

}

package com.cjy.qiquan.task;

import com.cjy.qiquan.service.DBHandler;
import com.cjy.qiquan.service.SystemLogService;

public class UpdateUnReadMessage extends DBHandler {

	private int userId;

	private int mainId;

	public UpdateUnReadMessage(final int userId, final int mainId) {
		this.userId = userId;
		this.mainId = mainId;
	}

	@Override
	protected void impl() {
		SystemLogService.instance().updateUnReadMessage(userId, mainId);
	}

	@Override
	protected int id() {
		return 1;
	}

}

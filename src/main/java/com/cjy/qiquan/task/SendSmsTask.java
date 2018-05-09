package com.cjy.qiquan.task;

import com.cloopen.rest.sdk.SDKSendTemplateSMS;

public class SendSmsTask extends TaskEvent {

	private String mobile;
	private String templateId;
	private String[] params;

	public SendSmsTask(String mobile, String templateId, String... params) {
		this.mobile = mobile;
		this.templateId = templateId;
		this.params = params;
	}

	@Override
	protected void impl() {
		SDKSendTemplateSMS.restAPI.sendTemplateSMS(mobile, templateId, params);
	}

}

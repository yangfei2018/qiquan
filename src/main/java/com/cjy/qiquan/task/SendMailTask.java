package com.cjy.qiquan.task;

import com.cjy.qiquan.mail.po.EmailMessage;
import com.cjy.qiquan.mail.transport.EmailTransportConfiguration;

public class SendMailTask extends TaskEvent {

	private String toMail;
	private String title;
	private String body;
	
	public SendMailTask(final String toMail,final String title,final String body){
		this.toMail = toMail;
		this.title = title;
		this.body = body;
	}
	
	
	@Override
	protected void impl() {
		try {
			new EmailMessage().from(EmailTransportConfiguration.from).to(toMail)
			.withSubject(title)
			.withBody("<html><body>" + body + "</body></html>").send();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

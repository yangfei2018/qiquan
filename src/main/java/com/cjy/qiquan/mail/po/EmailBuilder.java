package com.cjy.qiquan.mail.po;

import java.util.List;

public interface EmailBuilder {

	EmailBuilder from(String address);

	EmailBuilder to(String... addresses);

	EmailBuilder cc(String... addresses);

	EmailBuilder bcc(String... addresses);

	EmailBuilder withSubject(String subject);

	EmailBuilder withBody(String body);
	
	EmailBuilder withAttachment(String... attachments);
	
	EmailBuilder withAttachment(List<String> attachments);

	void send();
}
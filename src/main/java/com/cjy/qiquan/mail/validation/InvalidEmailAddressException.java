package com.cjy.qiquan.mail.validation;

public class InvalidEmailAddressException extends RuntimeException {

	private static final long serialVersionUID = 7521502257697884074L;

	public InvalidEmailAddressException(String message) {
		super(message);
	}
}

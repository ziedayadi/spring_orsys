package com.acme.ex3.exception;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 8699644235400219134L;

	private boolean messageI18nKey;
	
	public boolean isMessageI18nKey() {
		return messageI18nKey;
	}

	public void setMessageI18nKey(boolean messageI18nKey) {
		this.messageI18nKey = messageI18nKey;
	}

	public ApplicationException(String message, boolean messageI18nKey) {
		super(message);
		setMessageI18nKey(messageI18nKey);
	}
}

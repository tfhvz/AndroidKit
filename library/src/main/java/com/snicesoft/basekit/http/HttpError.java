package com.snicesoft.basekit.http;

public class HttpError {
	private String message;

	public HttpError(String msg) {
		this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

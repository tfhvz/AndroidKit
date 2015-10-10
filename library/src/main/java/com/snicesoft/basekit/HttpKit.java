package com.snicesoft.basekit;

public abstract class HttpKit implements IHttpKit {
	protected static HttpKit instance;

	public synchronized static HttpKit getInstance() {
		return instance;
	}
}

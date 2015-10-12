package com.snicesoft.basekit.bitmap;

public class BitmapConfig {
	private int loadingRes;
	private int failRes;

	public BitmapConfig() {
		super();
	}

	public BitmapConfig(int loadingRes, int failRes) {
		super();
		this.loadingRes = loadingRes;
		this.failRes = failRes;
	}

	public int getLoadingRes() {
		return loadingRes;
	}

	public void setLoadingRes(int loadingRes) {
		this.loadingRes = loadingRes;
	}

	public int getFailRes() {
		return failRes;
	}

	public void setFailRes(int failRes) {
		this.failRes = failRes;
	}

}

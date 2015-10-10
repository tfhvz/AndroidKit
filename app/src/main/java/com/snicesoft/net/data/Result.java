package com.snicesoft.net.data;

public class Result<T> {
	private String msg;
	private int status;
	private T data;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setData(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	@Override
	public String toString() {
		return "Result [msg=" + msg + ", status=" + status + ", data=" + data + "]";
	}

}

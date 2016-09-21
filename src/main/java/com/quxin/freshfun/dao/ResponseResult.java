package com.quxin.freshfun.dao;

public class ResponseResult {
	private String success;
	private String data;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResponseResult [success=" + success + ", data=" + data + "]";
	}
}

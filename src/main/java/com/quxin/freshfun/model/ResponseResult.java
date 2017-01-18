package com.quxin.freshfun.model;

public class ResponseResult {
	/**
	 * 支付成功响应码
	 */
	private String success;
	/**
	 * 支付成功数据
	 */
	private String data;
	/**
	 * 订单编号
	 */
	private Long orderId;
	/**
	 * 是否绑定手机
	 */
	private Integer isPhone;

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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getIsPhone() {
		return isPhone;
	}

	public void setIsPhone(Integer isPhone) {
		this.isPhone = isPhone;
	}

	@Override
	public String toString() {
		return "ResponseResult [success=" + success + ", data=" + data + "]";
	}
}

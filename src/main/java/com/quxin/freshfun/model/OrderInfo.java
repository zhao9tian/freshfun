package com.quxin.freshfun.model;

import java.util.List;

public class OrderInfo {
	private String user_id;
	private Integer address_id;
	private Integer payment_method;
	private String user_red_id;
	private Integer pay_sign;
	private String code;
	private String openid;
	private List<GoodsInfo> goodsInfo;
	
	public List<GoodsInfo> getGoodsInfo() {
		return goodsInfo;
	}
	public void setGoodsInfo(List<GoodsInfo> goodsInfo) {
		this.goodsInfo = goodsInfo;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public Integer getPay_sign() {
		return pay_sign;
	}
	public void setPay_sign(Integer pay_sign) {
		this.pay_sign = pay_sign;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Integer getAddress_id() {
		return address_id;
	}
	public void setAddress_id(Integer address_id) {
		this.address_id = address_id;
	}
	public Integer getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(Integer payment_method) {
		this.payment_method = payment_method;
	}
	public String getUser_red_id() {
		return user_red_id;
	}
	public void setUser_red_id(String user_red_id) {
		this.user_red_id = user_red_id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "OrderInfo [user_id=" + user_id + ", address_id=" + address_id
				+ ", payment_method=" + payment_method + ", user_red_id="
				+ user_red_id + ", code=" + code + ", goodsInfo=" + goodsInfo
				+ "]";
	}
	
	
}

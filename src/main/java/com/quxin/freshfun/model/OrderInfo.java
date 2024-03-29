package com.quxin.freshfun.model;

import java.util.List;

public class OrderInfo {
	private Long userId;
	private Integer addressId;
	private Integer paymentMethod;
	private String userRedId;
	private Integer paySign;
	private String code;
	private String openid;
	private List<GoodsInfo> goodsInfo;
	private GoodsInfo goods;
	/**
	 * 平台标识
	 */
	private String appId;
	/**
	 * 通过分享过来的标记
	 */
	private String fetcherId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getFetcherId() {
		return fetcherId;
	}

	public void setFetcherId(String fetcherId) {
		this.fetcherId = fetcherId;
	}

	public GoodsInfo getGoods() {
		return goods;
	}

	public void setGoods(GoodsInfo goods) {
		this.goods = goods;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getUserRedId() {
		return userRedId;
	}

	public void setUserRedId(String userRedId) {
		this.userRedId = userRedId;
	}

	public Integer getPaySign() {
		return paySign;
	}

	public void setPaySign(Integer paySign) {
		this.paySign = paySign;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public List<GoodsInfo> getGoodsInfo() {
		return goodsInfo;
	}

	public void setGoodsInfo(List<GoodsInfo> goodsInfo) {
		this.goodsInfo = goodsInfo;
	}
}

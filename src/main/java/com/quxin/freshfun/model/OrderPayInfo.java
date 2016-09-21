package com.quxin.freshfun.model;

public class OrderPayInfo {
	private String goodsName;
	private Integer goodsPrice;
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Integer getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(Integer goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public OrderPayInfo(String goodsName, Integer goodsPrice) {
		super();
		this.goodsName = goodsName;
		this.goodsPrice = goodsPrice;
	}
	public OrderPayInfo() {
		super();
	}
}

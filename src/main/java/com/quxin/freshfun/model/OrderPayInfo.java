package com.quxin.freshfun.model;

public class OrderPayInfo {
	private String goodsName;
	private Integer goodsPrice;
	/**
	 * 数量
	 */
	private Integer total;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
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

	public OrderPayInfo() {
		super();
	}

	public OrderPayInfo(String goodsName, Integer goodsPrice, Integer total) {
		this.goodsName = goodsName;
		this.goodsPrice = goodsPrice;
		this.total = total;
	}
}

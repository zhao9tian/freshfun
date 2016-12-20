package com.quxin.freshfun.model;

public class OrderPayInfo {
	private Long goodsId;
	private String goodsName;
	private Integer goodsPrice;
	/**
	 * 数量
	 */
	private Integer total;
	/**
	 * 是否优惠
	 */
	private int isDiscount;
	/**
	 * 优惠表中主键编号:如果isDiscount为1时有效
	 */
	private Integer promotionId;

	public Integer getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}

	public Integer getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(int discount) {
		isDiscount = discount;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

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

package com.quxin.freshfun.model;

public class GoodsInfo {
	private Integer scId;
	private Integer goodsId;
	private Integer count;

	public Integer getScId() {
		return scId;
	}

	public void setScId(Integer scId) {
		this.scId = scId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public GoodsInfo(){}

	public GoodsInfo(Integer goodsId) {
		this.goodsId = goodsId;
	}
}
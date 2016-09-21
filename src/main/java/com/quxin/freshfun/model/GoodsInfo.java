package com.quxin.freshfun.model;

public class GoodsInfo {
	private Integer sc_id;
	private Integer goodsId;
	private Integer count;
	
	public Integer getSc_id() {
		return sc_id;
	}
	public void setSc_id(Integer sc_id) {
		this.sc_id = sc_id;
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
	@Override
	public String toString() {
		return "OrderInfo [sc_id=" + sc_id + ", goodsId=" + goodsId
				+ ", count=" + count + "]";
	}
}
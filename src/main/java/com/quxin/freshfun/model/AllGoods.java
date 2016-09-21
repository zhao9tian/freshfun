package com.quxin.freshfun.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AllGoods {
	
	private GoodsPOJO goodsPOJO;
	
	private GoodsLimit goodsLimit;
	
	public GoodsLimit getGoodsLimit() {
		return goodsLimit;
	}

	public void setGoodsLimit(GoodsLimit goodsLimit) {
		this.goodsLimit = goodsLimit;
	}

	private List<GoodsMongo> goodsMongo;

	public GoodsPOJO getGoodsPOJO() {
		return goodsPOJO;
	}

	public void setGoodsPOJO(GoodsPOJO goodsPOJO) {
		this.goodsPOJO = goodsPOJO;
	}

	public List<GoodsMongo> getGoodsMongo() {
		return goodsMongo;
	}

	public void setGoodsMongo(List<GoodsMongo> goodsMongo) {
		this.goodsMongo = goodsMongo;
	}

}

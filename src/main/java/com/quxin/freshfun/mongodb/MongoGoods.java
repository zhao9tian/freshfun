package com.quxin.freshfun.mongodb;

import com.quxin.freshfun.model.GoodsMongo;

import java.util.List;
public interface MongoGoods {
	
	public List<GoodsMongo> findGoodsMongo(Integer goodsId);
	
}

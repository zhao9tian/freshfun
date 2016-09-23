package com.quxin.freshfun.mongodb.impl;

import java.util.List;

import com.quxin.freshfun.dao.GoodsDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.GoodsMongo;
import com.quxin.freshfun.mongodb.MongoGoods;
@Repository
public class MongoGoodsImpl implements MongoGoods {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private GoodsDetailsMapper goodsDetailsMapper;

	@Override
	public List<GoodsMongo> findGoodsMongo(Integer goodsId) {
		return goodsDetailsMapper.selectGoodsDetailByGoodsId(goodsId);
	}

	@Override
	public List<GoodsMongo> findLimitGoodsMongo(Query query) {
		return mongoTemplate.find(query, GoodsMongo.class, "goodsLimitDetail");
	}

}

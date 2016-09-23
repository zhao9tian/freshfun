package com.quxin.freshfun.mongodb;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.GoodsMongo;
@Repository
public interface MongoGoods {
	
	public List<GoodsMongo> findGoodsMongo(Integer goodsId);
	
	public List<GoodsMongo> findLimitGoodsMongo(Query query);

}

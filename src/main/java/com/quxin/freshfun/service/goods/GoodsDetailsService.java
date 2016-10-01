package com.quxin.freshfun.service.goods;

import com.quxin.freshfun.model.GoodsMongo;

import java.util.List;
public interface GoodsDetailsService {
	
	public List<GoodsMongo> findGoodsMongo(Integer goodsId);
	
}

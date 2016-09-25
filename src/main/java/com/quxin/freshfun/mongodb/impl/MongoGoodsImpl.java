package com.quxin.freshfun.mongodb.impl;

import com.quxin.freshfun.dao.GoodsDetailsMapper;
import com.quxin.freshfun.model.GoodsMongo;
import com.quxin.freshfun.mongodb.MongoGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mongoGoods")
public class MongoGoodsImpl implements MongoGoods {

    @Autowired
    private GoodsDetailsMapper goodsDetailsMapper;

    @Override
    public List<GoodsMongo> findGoodsMongo(Integer goodsId) {
        return goodsDetailsMapper.selectGoodsDetailByGoodsId(goodsId);
    }

}

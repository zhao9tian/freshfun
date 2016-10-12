package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.GoodsMongo;

import java.util.List;

/**
 * Created by TuZl on 2016/9/23.
 */
public interface GoodsDetailsMapper {

    /**
     * 根据商品Id查询商品详情
     * @param goodsId 商品Id
     * @return 返回商品列表
     */
    List<GoodsMongo> selectGoodsDetailByGoodsId(Integer goodsId);

    /**
     * 根据商品Id查询商品详情
     * @param goodsId 商品Id
     * @return 返回单个商品详情
     */
    GoodsMongo selectGoodsDetailPOJOByGoodsId(Integer goodsId);
}

package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.goods.GoodsGroupPurPOJO;

/**
 * Created by qucheng on 17/1/18.
 */
public interface GoodsGroupPurMapper {

    /**
     * 根据商品id查询商品团购信息
     * @param goodsId 商品id
     * @return 商品团购信息
     */
    GoodsGroupPurPOJO selectGoodsGPByGoodsId(Long goodsId);
}

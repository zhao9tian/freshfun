package com.quxin.freshfun.service.goods;

import com.quxin.freshfun.model.goods.GoodsGroupPurPOJO;

/**
 * 团购商品service
 * Created by qucheng on 17/1/18.
 */
public interface GoodsGroupPurService {

    /**
     * 根据商品id查询商品团购信息
     * @param goodsId 商品id
     * @return 返回团购商品信息
     */
    GoodsGroupPurPOJO queryGoodsGroupPurByGoodsId(Long goodsId);
}

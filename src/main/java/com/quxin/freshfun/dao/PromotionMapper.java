package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.goods.PromotionGoodsPOJO;
import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.model.param.PromotionParam;
import com.quxin.freshfun.model.pojo.FlowPOJO;
import com.quxin.freshfun.model.pojo.PromotionPOJO;

import java.util.List;
import java.util.Map;

public interface PromotionMapper {


    /**
     * 添加优惠商品
     * @param promotionParam
     * @return
     */
    Integer insert(PromotionParam promotionParam);


    /**
     * 获取商品的优惠内容
     * @return
     */
    PromotionPOJO selectPromotionInfo(Map paramMap);

}
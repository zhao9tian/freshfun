package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.goods.PromotionGoodsPOJO;
import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.model.param.PromotionParam;
import com.quxin.freshfun.model.pojo.FlowPOJO;
import com.quxin.freshfun.model.pojo.PromotionPOJO;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 查询未结束的限时购商品
     * @return
     */
    List<PromotionPOJO> selectLimitGoodsInfo(Long currentDate);

    /**
     * 查询单个限时购商品
     * @param goodsId
     * @param currentDate
     * @return
     */
    PromotionPOJO selectLimitByGoodsId(@Param("goodsId") Long goodsId,@Param("currentDate") Long currentDate);

    /**
     * 查询限时购商品
     * @return
     */
    List<Long> selectLimitGoodsId();

}
package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.model.ShoppingCartPOJO;
import com.quxin.freshfun.model.goods.LimitedNumGoodsPOJO;
import com.quxin.freshfun.model.OrderPayInfo;
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
     *
     * @return
     */
    List<PromotionPOJO> selectLimitedGoodsInfo(List<OrderPayInfo> goodsBaseList);

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

    /**
     * 根据商品id判断是否是限量购商品
     * @param goodsId 商品id
     * @return 是否限量购商品
     */
    LimitedNumGoodsPOJO selectLimitedNumGoodsById(@Param("goodsId") Long goodsId);

    /**
     * 根据商品编号查询限量库存
     * @param goodsBaseList
     * @return
     */
    List<PromotionPOJO> selectStockByGoodsList(List<OrderPayInfo> goodsBaseList);

    /**
     * 查询限量购超时订单
     * @param orderDetails
     * @return
     */
    List<PromotionPOJO> selectLimitedGoods(List<OrderDetailsPOJO> orderDetails);

    /**
     * 批量修改限量购库存
     * @param orderIdList 限量购商品
     * @return
     */
    int updateLimitedStock(OrderDetailsPOJO orderIdList);

    /**
     * 修改限量商品库存
     * @param map 修改信息
     * @return
     */
    int updateStockById(Map<String,Object> map);

    /**
     * 查询是否属于限量购商品
     * @return
     */
    List<PromotionPOJO> selectCartLimitedGoods(List<ShoppingCartPOJO> carts);
}
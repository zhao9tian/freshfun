package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.goods.LimitedNumGoodsPOJO;
import com.quxin.freshfun.model.GoodsInfo;
import com.quxin.freshfun.model.OrderPayInfo;
import com.quxin.freshfun.model.param.GoodsParam;
import com.quxin.freshfun.model.pojo.PromotionPOJO;
import com.quxin.freshfun.model.pojo.goods.GoodsBasePOJO;
import com.quxin.freshfun.model.pojo.goods.GoodsImage;
import com.quxin.freshfun.model.pojo.goods.GoodsStandard;
import com.quxin.freshfun.model.pojo.goods.SelectionPOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by gsix on 2016/10/26.
 */
public interface GoodsBaseMapper {
    /**
     * 根据商品编号查询商品信息
     * @param goodsId
     * @return
     */
    GoodsBasePOJO findGoodsById(Long goodsId);
    /**
     * 根据商品编号查询商品信息
     * @param goodsId
     * @return
     */
    List<GoodsBasePOJO> findGoodsInId(Long [] goodsId);
    /**
     * 根据商品编号查询商品信息
     * @param goodsList
     * @return
     */
    List<GoodsBasePOJO> findGoodsInList(List<SelectionPOJO> goodsList);

    /**
     * 根据优惠商品编号查询商品信息
     * @param promotionList
     * @return
     */
    List<GoodsBasePOJO> findPromotionGoodsList(List<PromotionPOJO> promotionList);

    /**
     * 查询二级类目商品
     * @param category
     * @return
     */
    List<GoodsBasePOJO> selectCatagory2Goods(@Param("category") Integer category,@Param("page") Integer page,@Param("pageSize") Integer pageSize);

    /**
     * 查询商品列表
     * @param page
     * @param pageSize
     * @return
     */
    List<GoodsBasePOJO> selectGoodsList(@Param("page") Integer page,@Param("pageSize") Integer pageSize);

    /**
     * 查询推荐商品
     * @return
     */
    List<GoodsBasePOJO> findRecommendGoods();

    /**
     * 根据商品编号查询商品图
     * @return
     */
    GoodsImage selectGoodsImgByGoodsId(Long goodsId);

    /**
     * 根据商品查询商品属性
     * @return
     */
    GoodsStandard selectGoodsStandard(Long goodsId);

    /**
     * 订单支付根据商品编号查询商品信息
     * @return
     */
    GoodsBasePOJO selectOrderPayInfo(Long goodsId);

    /**
     * 根据商品集合查询商品信息
     * @return
     */
    List<OrderPayInfo> selectGoodsInfoByGoodsList(List<GoodsInfo> goodsInfo);

    /**
     * 修改库存数量
     * @return
     */
    Integer updateGoodsStock(Map<String,Object> map);

    /**
     * 修改商品销量
     * @param goodsId 商品编号
     * @return
     */
    Integer updateGoodsSaleNum(Long goodsId);

    /**
     * 根据商品编号查询商品信息
     * @param goodsId
     * @return
     */
    GoodsParam selectGoodsByGoodsId(Long goodsId);

    /**
     * 根据商品编号查询库存
     * @return
     */
    GoodsBasePOJO selectStockByGoodsId(Long goodsId);

    /**
     * 批量修改库存
     * @param goodsBaseList 商品信息
     * @return
     */
    int batchUpdateStock(List<OrderPayInfo> goodsBaseList);

    /**
     * 批量查询库存
     * @return
     */
    List<GoodsBasePOJO> selectBatchStock(List<OrderPayInfo> goodsBaseList);

    /**
     * 根据排序Id查询排序限量购商品
     * @param limitedNumIds 限量购商品id
     * @return 限量购商品
     */
    List<LimitedNumGoodsPOJO> selectAllLimitedNumInfo(@Param("limitedNumIds") List<Long> limitedNumIds);

    /**
     * 根据排序Id查询排序限量购剩余库存不为0的商品
     * @param limitedNumIds 限量购商品id
     * @return 库存不为0限量购信息
     */
    List<LimitedNumGoodsPOJO> selectIndexLimitedNumInfo(@Param("limitedNumIds") List<Long> limitedNumIds);

    /**
     * 根据排序Id查询限量购商品
     * @param limitedNumIds 限量购排序id
     * @return 限量购商品信息
     */
    List<GoodsBasePOJO> selectGoodsByIds(@Param("limitedNumIds") List<Long> limitedNumIds);

    /**
     * 根据商品id查询限量购商品详情
     * @param goodsId 商品id
     * @return 限量购商品详情
     */
    LimitedNumGoodsPOJO selectLimitedGoodsById(@Param("goodsId") Long goodsId);

}

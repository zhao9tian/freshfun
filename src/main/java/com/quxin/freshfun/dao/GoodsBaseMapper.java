package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.param.GoodsParam;
import com.quxin.freshfun.model.pojo.goods.GoodsBasePOJO;
import com.quxin.freshfun.model.pojo.goods.GoodsImage;
import com.quxin.freshfun.model.pojo.goods.GoodsStandard;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * 修改库存数量
     * @return
     */
    Integer updateGoodsStock(Long goodsId);

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
}

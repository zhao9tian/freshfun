package com.quxin.freshfun.service.goods;

import com.quxin.freshfun.model.outparam.goods.BannerOut;
import com.quxin.freshfun.model.outparam.goods.GoodsOut;
import com.quxin.freshfun.model.outparam.goods.SpecialOut;

import java.util.List;
import java.util.Map;

/**
 * Created by qingtian on 2016/10/26.
 */
public interface GoodsBaseService {
    /**
     * 查询专题模块
     * @param page
     * @param pageSize
     * @return
     */
    List<SpecialOut> getSpecialTheme(Integer page, Integer pageSize);

    /**
     * 获取banner数据
     * @return
     */
    List<BannerOut> getBannerList();

    /**
     * 获取精选列表
     * @return
     */
    List<GoodsOut> getSelectionList();

    /**
     * 获取首页专题列表
     * @return
     */
    List<SpecialOut> getSpecialList();

    /**
     * 获取商品排序列表
     * @return
     */
    List<GoodsOut> getGoodsSortList();

    /**
     * 查询更多商品
     * @return
     */
    List<GoodsOut> getGoodsList(Integer page,Integer pageSize);

    /**
     * 根据类目编号查询商品
     * @param categoryKey 类目编号
     * @return
     */
    List<GoodsOut> getCategory(Integer categoryKey);

    /**
     * 根据类目编号查询类目图
     * @param categoryKey
     * @return
     */
    String getCategoryImg(Integer categoryKey);

    /**
     * 根据专题编号查询专题详情
     * @param specialId
     * @return
     */
    SpecialOut getSpecialDetails(Long specialId);

    /**
     * 根据商品编号查询商品详情
     * @return
     */
    Map<String,Object> getGoodsDetails(Long goodsId);

    /**
     * 查询商品详情
     * @param goodsId 商品编号
     * @return
     */
    Map<String,Object> findGoodsDetails(Long goodsId);

}

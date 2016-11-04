package com.quxin.freshfun.service.goods;

import com.quxin.freshfun.model.outparam.goods.BannerOut;
import com.quxin.freshfun.model.outparam.goods.GoodsOut;
import com.quxin.freshfun.model.outparam.goods.SpecialOut;
import com.quxin.freshfun.utils.BusinessException;

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
    List<SpecialOut> getSpecialTheme(Integer page, Integer pageSize) throws BusinessException;

    /**
     * 获取banner数据
     * @return
     */
    List<BannerOut> getBannerList();

    /**
     * 获取精选列表
     * @return
     */
    List<GoodsOut> getSelectionList() throws BusinessException;

    /**
     * 获取首页专题列表
     * @return
     */
    List<SpecialOut> getSpecialList() throws BusinessException;

    /**
     * 获取商品排序列表
     * @return
     */
    List<GoodsOut> getGoodsSortList() throws BusinessException;

    /**
     * 查询更多商品
     * @return
     */
    List<GoodsOut> getGoodsList(Integer page,Integer pageSize) throws BusinessException;

    /**
     * 查询限时购商品
     * @return
     */
    List<GoodsOut> getGoodsLimitList() throws BusinessException;

    /**
     * 根据类目编号查询商品
     * @param categoryKey 类目编号
     * @return
     */
    List<GoodsOut> getCategory(Integer categoryKey,Integer page,Integer pageSize) throws BusinessException;

    /**
     * 根据类目编号查询类目图
     * @param categoryKey
     * @return
     */
    String getCategoryImg(Integer categoryKey) throws BusinessException;

    /**
     * 根据专题编号查询专题详情
     * @param specialId
     * @return
     */
    SpecialOut getSpecialDetails(Long specialId) throws BusinessException;

    /**
     * 查询商品详情
     * @param goodsId 商品编号
     * @return
     */
    Map<String,Object> findGoodsDetails(Long goodsId) throws BusinessException;

}

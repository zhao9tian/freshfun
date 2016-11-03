package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.goods.BannerPOJO;
import com.quxin.freshfun.model.pojo.goods.ThemePOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsPOJO record);

    int insertSelective(GoodsPOJO record);

    GoodsPOJO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsPOJO record);

    int updateByPrimaryKey(GoodsPOJO record);

    List<GoodsPOJO> findByLimit(Map<String, Integer> goodsMap);

    List<GoodsPOJO> findByLimitOfB(Map<String, Integer> goodsMap);

    GoodsPOJO findByGoodsId(Integer goodsId);
    /**
     * 根据商品编号查询代理商户信息
     * @param goodsId 商品Id
     * @return 商品信息
     */
    GoodsPOJO selectProxyMerchantByGoodsId(Integer goodsId);
    /**
     * 查询推荐商品
     * @return 商品列表
     */
    List<GoodsPOJO> selectRecommendGoods();
    /**
     * 查询单个商品信息
     * @param goodsId 商品Id
     * @return 商品列表
     */
    GoodsPOJO selectShoppingInfo(Integer goodsId);

    /**
     * 根据代理商户ID查询代理的商品信息
     *
     * @param map id
     * @return 商品列表
     */
    List<GoodsPOJO> findProxyGoods(Map<String,Object> map);

    /**
     * 修改商品代理状态
     * @param userId 用户ID
     * @param goodsId 商品Id
     * @return 修改条数
     */
    int updateGoodsAgent(@Param("userId") Long userId,@Param("goodsId") Integer goodsId);

    /**
     * 查询商品图墙内容
     * @param pictureWall 图墙
     * @return 返回排序的json字符串
     */
    String selectPictureWall(String pictureWall);

    /**
     * 根据查询出的排序Id查询排序的商品
     * @param sortList 排序
     * @return 20个排序的商品列表
     */
    List<GoodsPOJO> selectSortGoods(List<Integer> sortList);

    /**
     * 根据bannerId 查询banner信息
     * @param bannerId id
     * @return banner信息
     */
    BannerPOJO selectBannerById(Integer bannerId);

    /**
     * 根据专题Id查询专题信息
     * @param themeId 专题Id
     * @return 返回专题信息
     */
    ThemePOJO selectThemeById(Integer themeId);
}
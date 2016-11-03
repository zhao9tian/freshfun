package com.quxin.freshfun.service.goods;

import com.quxin.freshfun.model.GoodsMongo;
import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.SmidVsGid;
import com.quxin.freshfun.model.StidVsGid;
import com.quxin.freshfun.model.goods.BannerPOJO;
import com.quxin.freshfun.model.pojo.goods.ThemePOJO;

import java.util.List;
import java.util.Map;

public interface GoodsService {
	
	/**
	 * 查询mongoDB中商品信息
	 */
	List<GoodsMongo> findGoodsMongo(Integer goodsID);

	/**
	 * 根据商品Id查询商品详情
	 * @param goodsId 商品id
	 * @return 返回商品详情
	 */
	GoodsMongo queryGoodsDetailById(Integer goodsId);
	
	/**
	 * 查询mysql中商品信息
	 */
	GoodsPOJO findGoodsMysql(Integer goodsID);
	

	/**
	 * 查询专题商品
	 */
	List<StidVsGid> findThemeGoods(Map<String, Integer> themeMap);
	
	/**
	 * 查询banner商品
	 */
	List<SmidVsGid> findMallGoods(Map<String, Integer> mallMap);
	
	/**
	 * 根据代理商户ID查询代理的商品信息
	 * @param map 传入Id
	 * @return 商品信息
	 */
	List<GoodsPOJO> findProxyGoods (Map<String,Object> map);

	/**
	 * 查询图墙排序的商品
	 * @return 商品列表
	 */
    List<GoodsPOJO> querySortGoods();

	/**
	 * 根据bannerId 查询banner信息
	 * @param bannerId 轮播Id
	 * @return 轮播信息
	 */
    BannerPOJO queryBannerById(Integer bannerId);

	/**
	 * 根据themeId查询专题信息
	 * @param themeId 专题Id
	 * @return 专题信息
	 */
	ThemePOJO queryThemeById(Integer themeId);
}

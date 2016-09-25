package com.quxin.freshfun.service.goods;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.quxin.freshfun.model.Comment;
import com.quxin.freshfun.model.GoodsLimit;
import com.quxin.freshfun.model.GoodsMongo;
import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.SmidVsGid;
import com.quxin.freshfun.model.StidVsGid;


@Service
public interface GoodsService {
	
	/**
	 * 查询mongoDB中商品信息
	 */
	public List<GoodsMongo> findGoodsMongo(Integer goodsID);
	
	/**
	 * 查询mysql中商品信息
	 */
	public GoodsPOJO findGoodsMysql(Integer goodsID);
	
	

	/**
	 * 查询mysql中限时商品信息
	 */
	public GoodsLimit findLimitGoodsMysql(Integer goodsID);
	
	/**
	 * 查询专题商品
	 */
	public List<StidVsGid> findThemeGoods(Map<String, Integer> themeMap);
	
	/**
	 * 查询banner商品
	 */
	public List<SmidVsGid> findMallGoods(Map<String, Integer> mallMap);
	
	/**
	 * 添加评论
	 */
	public void addComment(Comment comment);

	/**
	 * 根据代理商户ID查询代理的商品信息
	 * @param map
	 * @return
	 */
	List<GoodsPOJO> findProxyGoods (Map<String,Object> map);
}

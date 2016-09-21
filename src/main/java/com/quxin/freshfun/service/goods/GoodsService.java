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
	 * 查询mongoDB中限时商品信息
	 */
	public List<GoodsMongo> findLimitGoodsMongo(Integer goodsID);
	
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
	 * 查询评论
	 */
	public List<Comment> findComment(String userID, String goodsID);
	
	/**
	 * 添加评论
	 */
	public void addComment(Comment comment);

}

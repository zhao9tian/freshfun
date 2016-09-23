package com.quxin.freshfun.service.goods;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.GoodsTypePOJO;
import com.quxin.freshfun.model.SpecialMall;
import com.quxin.freshfun.model.SpecialTheme;
import com.quxin.freshfun.model.UsersPOJO;

@Service
public interface HomePageService {
	
	/**
	 * 查询banner数据
	 */
	public List<SpecialMall> homeBanner();
	
	
	/**
	 * 查询分类数据
	 */
	public List<GoodsTypePOJO> homeGoodsType();

	
	/**
	 * 查询精选
	 */
	public List<GoodsTypePOJO> homeGoodsTypeByType(String str);

	
	/**
	 * 查询专题商品
	 */
	public List<SpecialTheme> homeGoodsTheme();
	
	/**
	 * 查询商品
	 */
	public List<GoodsPOJO> findGoods(Map<String, Integer> goodsMap);

	/**
	 * 查询用户活动权限
	 */
	public UsersPOJO findEnterByID(Integer id);
	
	/**
	 * 查询分类商品
	 */
	public List<GoodsTypePOJO> findTypeGoods();
	
}

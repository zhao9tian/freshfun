package com.quxin.freshfun.controller.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quxin.freshfun.model.AllGoods;
import com.quxin.freshfun.model.GoodsLimit;
import com.quxin.freshfun.model.GoodsMongo;
import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.GoodsThemeInfo;
import com.quxin.freshfun.model.SmidVsGid;
import com.quxin.freshfun.model.StidVsGid;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.utils.MoneyFormat;

@Controller
@RequestMapping("/goods/")
public class GoodsDetails {
	@Autowired
	private GoodsService goods;
	
	
	/**
	 * 商品详情页
	 * @return Goods
	 */
	
//	@RequestMapping("/goods1")
//	@ResponseBody
//	public GoodsPOJO findGoods(Integer goodsID,HttpServletResponse response){
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		GoodsPOJO goodsMysql = goods.findGoodsMysql(goodsID);
//		return goodsMysql;
//	}
	
	/**
	 * 商品详情页
	 * @return List<GoodsMongo>
	 */
	@RequestMapping("/goods")
	@ResponseBody
	public AllGoods findMongoGoods(Integer goodsID){
		AllGoods allGoods = new AllGoods();
		List<GoodsMongo> goodsMongo = goods.findGoodsMongo(goodsID);
		allGoods.setGoodsMongo(goodsMongo);
		GoodsPOJO goodsMysql = goods.findGoodsMysql(goodsID);
		goodsMysql.setGoodsMoney(MoneyFormat.priceFormatString(goodsMysql.getShop_price()));
		goodsMysql.setMarketMoney(MoneyFormat.priceFormatString(goodsMysql.getMarket_price()));
		goodsMysql.setShop_price(null);
		goodsMysql.setMarket_price(null);
		allGoods.setGoodsPOJO(goodsMysql);
		return allGoods;
	}
	
	/**
	 * 限时商品详情页
	 * @return List<GoodsMongo>
	 */
	@RequestMapping("/goodslimit")
	@ResponseBody
	public AllGoods findLimitGoods(Integer goodsID){
		AllGoods allGoods = new AllGoods();
		List<GoodsMongo> goodsMongo = goods.findLimitGoodsMongo(goodsID);
		allGoods.setGoodsMongo(goodsMongo);
		GoodsLimit goodsMysql = goods.findLimitGoodsMysql(goodsID);
		allGoods.setGoodsLimit(goodsMysql);
		return allGoods;
	}
	
	/**
	 * 专题商品
	 * @return List<GoodsMongo>
	 */
	@RequestMapping(value="/goodstheme",method={RequestMethod.POST})
	@ResponseBody
	public List<StidVsGid> findThemeGoods(@RequestBody GoodsThemeInfo goodsThemeIngo){
		Integer pagetime = goodsThemeIngo.getPagetime();
		Integer theme_id = goodsThemeIngo.getThemeID();
		System.out.println(pagetime);
		System.out.println(theme_id);
		Map<String, Integer> themeMap = new HashMap<String, Integer>(2);
		Integer page = (pagetime - 1) * 20;
		themeMap.put("theme_id", theme_id);
		themeMap.put("page", page);
		List<StidVsGid> specialTheme = goods.findThemeGoods(themeMap);
		return specialTheme;
	}
	
	/**
	 * 首页
	 * @return List<GoodsMongo>
	 */
	@RequestMapping(value="/goodsmall",method={RequestMethod.POST})
	@ResponseBody
	public List<SmidVsGid> findMallGoods(@RequestBody GoodsThemeInfo goodsThemeIngo){
		Integer pagetime = goodsThemeIngo.getPagetime();
		Integer theme_id = goodsThemeIngo.getThemeID();
		System.out.println(pagetime);
		System.out.println(theme_id);
		Map<String, Integer> mallMap = new HashMap<String, Integer>(2);
		Integer page = (pagetime - 1) * 20;
		mallMap.put("mall_id", theme_id);
		mallMap.put("page", page);
		List<SmidVsGid> mallTheme = goods.findMallGoods(mallMap);
		return mallTheme;
	}

}

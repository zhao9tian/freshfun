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
	private GoodsService goodsService;
	
	
	/**
	 * 商品详情页
	 * @return Goods
	 */
	
//	@RequestMapping("/goods1")
//	@ResponseBody
//	public GoodsPOJO findGoods(Integer goodsID,HttpServletResponse response){
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		GoodsPOJO goodsMysql = goodsService.findGoodsMysql(goodsID);
//		return goodsMysql;
//	}
	
	/**
	 * 商品详情页
	 * @return List<GoodsMongo>
	 */
	@RequestMapping("/goods")
	@ResponseBody
	public AllGoods findMongoGoods(Integer goodsId){
		AllGoods allGoods = new AllGoods();
		List<GoodsMongo> goodsMongo = goodsService.findGoodsMongo(goodsId);
		allGoods.setGoodsMongo(goodsMongo);
		GoodsPOJO goodsMysql = goodsService.findGoodsMysql(goodsId);
		goodsMysql.setGoodsMoney(MoneyFormat.priceFormatString(goodsMysql.getShopPrice()));
		goodsMysql.setMarketMoney(MoneyFormat.priceFormatString(goodsMysql.getMarketPrice()));
		goodsMysql.setShopPrice(null);
		goodsMysql.setMarketPrice(null);
		allGoods.setGoodsPOJO(goodsMysql);
		return allGoods;
	}
	
	/**
	 * 限时商品详情页
	 * @return List<GoodsMongo>
	 */
	@RequestMapping("/goodslimit")
	@ResponseBody
	public AllGoods findLimitGoods(Integer goodsId){
		AllGoods allGoods = new AllGoods();
		List<GoodsMongo> goodsMongo = goodsService.findLimitGoodsMongo(goodsId);
		allGoods.setGoodsMongo(goodsMongo);
		GoodsLimit goodsMysql = goodsService.findLimitGoodsMysql(goodsId);
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
		Integer themeId = goodsThemeIngo.getThemeID();
		Map<String, Integer> themeMap = new HashMap<String, Integer>(2);
		Integer page = (pagetime - 1) * 20;
		themeMap.put("themeId", themeId);
		themeMap.put("page", page);
		List<StidVsGid> specialTheme = goodsService.findThemeGoods(themeMap);
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
		Integer themeId = goodsThemeIngo.getThemeID();
		System.out.println(pagetime);
		System.out.println(themeId);
		Map<String, Integer> mallMap = new HashMap<String, Integer>(2);
		Integer page = (pagetime - 1) * 20;
		mallMap.put("mallId", themeId);
		mallMap.put("page", page);
		List<SmidVsGid> mallTheme = goodsService.findMallGoods(mallMap);
		return mallTheme;
	}

}

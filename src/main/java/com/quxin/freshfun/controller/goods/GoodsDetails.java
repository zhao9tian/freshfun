package com.quxin.freshfun.controller.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.quxin.freshfun.common.FreshFunEncoder;
import com.quxin.freshfun.utils.CookieUtil;
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

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/goods/")
public class GoodsDetails {
	@Autowired
	private GoodsService goodsService;
	
	/**
	 * 商品详情页
	 * @return List<GoodsMongo>
	 */
	@RequestMapping("/goods")
	@ResponseBody
	public AllGoods findMongoGoods(Integer goodsId, HttpServletRequest request){
		AllGoods allGoods = new AllGoods();
		List<GoodsMongo> goodsMongo = goodsService.findGoodsMongo(goodsId);
		allGoods.setGoodsMongo(goodsMongo);
		GoodsPOJO goodsMysql = goodsService.findGoodsMysql(goodsId);
		goodsMysql.setGoodsMoney(MoneyFormat.priceFormatString(goodsMysql.getShopPrice()));
		goodsMysql.setMarketMoney(MoneyFormat.priceFormatString(goodsMysql.getMarketPrice()));
		goodsMysql.setShopPrice(null);
		goodsMysql.setMarketPrice(null);
		allGoods.setGoodsPOJO(goodsMysql);
		Long userId = CookieUtil.getUserIdFromCookie(request);
		allGoods.setSign(FreshFunEncoder.idToUrl(userId));
		return allGoods;
	}
	

	/**
	 * 专题商品
	 * @return List<GoodsMongo>
	 */
	@RequestMapping(value="/goodstheme",method={RequestMethod.POST})
	@ResponseBody
	public Map<String , Object> findThemeGoods(@RequestBody GoodsThemeInfo goodsThemeIngo,HttpServletRequest request){
		Integer pagetime = goodsThemeIngo.getPagetime();
		Integer themeId = goodsThemeIngo.getThemeId();
		Map<String, Integer> themeMap = new HashMap<String, Integer>(2);
		Integer page = (pagetime - 1) * 20;
		themeMap.put("themeId", themeId);
		themeMap.put("page", page);
		List<StidVsGid> specialTheme = goodsService.findThemeGoods(themeMap);
		Long userId = CookieUtil.getUserIdFromCookie(request);
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("specialTheme",specialTheme);
		map.put("sign", FreshFunEncoder.idToUrl(userId));
		return map;
	}
	
	/**
	 * 首页
	 * @return List<GoodsMongo>
	 */
	@RequestMapping(value="/goodsmall",method={RequestMethod.POST})
	@ResponseBody
	public Map<String , Object> findMallGoods(@RequestBody GoodsThemeInfo goodsThemeIngo,HttpServletRequest request){
		Integer pagetime = goodsThemeIngo.getPagetime();
		Integer themeId = goodsThemeIngo.getThemeId();
		Map<String, Integer> mallMap = new HashMap<String, Integer>(2);
		Integer page = (pagetime - 1) * 20;
		mallMap.put("mallId", themeId);
		mallMap.put("page", page);
		List<SmidVsGid> mallTheme = goodsService.findMallGoods(mallMap);
		Long userId = CookieUtil.getUserIdFromCookie(request);
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("mallTheme",mallTheme);
		map.put("sign",FreshFunEncoder.idToUrl(userId));
		return map;
	}

}

package com.quxin.freshfun.controller.goods;

import com.quxin.freshfun.common.FreshFunEncoder;
import com.quxin.freshfun.model.*;
import com.quxin.freshfun.model.goods.GoodsInfoPOJO;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.service.user.UserService;
import com.quxin.freshfun.utils.CookieUtil;
import com.quxin.freshfun.utils.MoneyFormat;
import com.quxin.freshfun.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/goods/")
public class GoodsDetails {
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private UserService userService;
	
	/**
	 * 商品详情页
	 * @return List<GoodsMongo>
	 */
	@RequestMapping("/goods")
	@ResponseBody
	public AllGoods findMongoGoods(Integer goodsId, HttpServletRequest request){
		AllGoods allGoods = new AllGoods();
		if (goodsId == null) {
			return null;
		}
		List<GoodsMongo> goodsMongo = goodsService.findGoodsMongo(goodsId);
		allGoods.setGoodsMongo(goodsMongo);
		GoodsPOJO goodsMysql = goodsService.findGoodsMysql(goodsId);
		goodsMysql.setGoodsMoney(MoneyFormat.priceFormatString(goodsMysql.getShopPrice()));
		goodsMysql.setMarketMoney(MoneyFormat.priceFormatString(goodsMysql.getMarketPrice()));
		goodsMysql.setShopPrice(null);
		goodsMysql.setMarketPrice(null);
		allGoods.setGoodsPOJO(goodsMysql);
		Long userId = CookieUtil.getUserIdFromCookie(request);
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("allGoods",allGoods);
		allGoods.setSign(FreshFunEncoder.idToUrl(userId));
		return allGoods;
	}

	/**
	 * 查询商品详情
	 *
	 * @param goodsId 商品Id
	 * @param request 页面请求
	 * @return 返回查询结果
	 */
	@RequestMapping(value = "/queryGoodsDetail")
	@ResponseBody
	public Map<String , Object> queryGoodsInfo(Integer goodsId , HttpServletRequest request){
		GoodsInfoPOJO goodsInfoPOJO = new GoodsInfoPOJO();
		Map<String , Object> result = new HashMap<String , Object>();
		//商品基本信息
		GoodsPOJO goodsMysql = goodsService.findGoodsMysql(goodsId);
		//赋值
		if(goodsMysql != null){
			//查询商品详情
			GoodsMongo goodsMongo = goodsService.queryGoodsDetailById(goodsId);
			String des = goodsMongo.getDes();
			if(des != null && !"".equals(des)){
				String[] desArr = des.split("@`");
				goodsInfoPOJO.setGoodsName(desArr[0]);
				goodsInfoPOJO.setGoodsDes(desArr[1]);
				goodsInfoPOJO.setFfunerSaid(desArr[2]);
			}
			goodsInfoPOJO.setGoodsId(goodsId);
			goodsInfoPOJO.setActualMoney(MoneyFormat.priceFormatString(goodsMysql.getShopPrice()));
			goodsInfoPOJO.setOriginMoney(MoneyFormat.priceFormatString(goodsMysql.getMarketPrice()));
			goodsInfoPOJO.setStandardImg(goodsMongo.getStandardImgPath());
			goodsInfoPOJO.setBannerImg(goodsMongo.getCarouselImgPath());
			goodsInfoPOJO.setDetailImg(goodsMongo.getDetailImgPath());
			Map<String , Object> map = new HashMap<String , Object>();
			map.put("goodsInfo" , goodsInfoPOJO);
			//Long userId = CookieUtil.getUserIdFromCookie(request);
			//if(userService.findIsMobile(userId))
				//map.put("fetcherId",FreshFunEncoder.idToUrl(userId));
			result = ResultUtil.success(map);
		}else{
			result = ResultUtil.fail(1004 ,"该商品已经下架或者没有该商品" );
		}
		return result;
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
		//Long userId = CookieUtil.getUserIdFromCookie(request);
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("specialTheme",specialTheme);
		//if(userService.findIsMobile(userId))
			//map.put("fetcherId",FreshFunEncoder.idToUrl(userId));
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
		//Long userId = CookieUtil.getUserIdFromCookie(request);
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("mallTheme",mallTheme);
		//if(userService.findIsMobile(userId))
			//map.put("fetcherId",FreshFunEncoder.idToUrl(userId));
		return map;
	}

}

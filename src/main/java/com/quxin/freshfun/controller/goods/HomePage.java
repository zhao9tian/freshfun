package com.quxin.freshfun.controller.goods;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																															

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.GoodsTypePOJO;
import com.quxin.freshfun.model.HomePagePOJO;
import com.quxin.freshfun.model.SpecialMall;
import com.quxin.freshfun.model.SpecialTheme;
import com.quxin.freshfun.service.goods.HomePageService;
import com.quxin.freshfun.utils.MoneyFormat;

@Controller
@RequestMapping("/")
public class HomePage {
	@Autowired
	private HomePageService homePage;
	/**
	 * 首页
	 * @return List<HomePage>
	 */
	
	@RequestMapping("/homepage")
	@ResponseBody
	public HomePagePOJO ShowHomePage(){
		/**
		 * 首页banner
		 * @return List<SpecialMall>
		 */
		List<SpecialMall> homeBanner = homePage.homeBanner();
		HomePagePOJO homePagePOJO = new HomePagePOJO();
		homePagePOJO.setSpecialMall(homeBanner);
		
		/**
		 * 首页分类
		 * @return List<SpecialMall>
		 */
		List<GoodsTypePOJO> homeGoodsType = homePage.homeGoodsType();
		homePagePOJO.setGoodsType(homeGoodsType);

		
		/**
		 * 分类or精选商品
		 * @return List<GoodsTypePOJO>
		 */
		
		List<GoodsTypePOJO> homeSelection = homePage.homeGoodsTypeByType("精选");

		homePagePOJO.setSelection(homeSelection);

		
		/**
		 * 专题商品
		 * @return List<SpecialTheme>
		 */
		
		List<SpecialTheme> homeSpecialTheme = homePage.homeGoodsTheme();
		homePagePOJO.setSpecialTheme(homeSpecialTheme);
		/**
		 * 查询商品
		 * @return List<Goods>
		 */
		Map<String, Integer> goodsMap = new HashMap<String, Integer>(2);
		Integer page = 0;
		goodsMap.put("page", page);
		goodsMap.put("pagesize", 20);
		List<GoodsPOJO> goods = homePage.findGoods(goodsMap);
        for (GoodsPOJO goodsPOJO : goods) {
			
			goodsPOJO.setGoodsMoney(MoneyFormat.priceFormatString(goodsPOJO.getShopPrice()));
			goodsPOJO.setMarketMoney(MoneyFormat.priceFormatString(goodsPOJO.getMarketPrice()));
			goodsPOJO.setShopPrice(null);
			goodsPOJO.setMarketPrice(null);
			
		}
		
		homePagePOJO.setGoodsByLimit(goods);
		
		
		
		return homePagePOJO;
		
	}
	


	
	/**
	 * 查询商品
	 * @return List<Goods>
	 */
	@RequestMapping("/homepage1")
	@ResponseBody
	public List<GoodsPOJO> findGoodsByLimit(Integer pagetime){
		Map<String, Integer> goodsMap = new HashMap<String, Integer>(2);
		Integer page = (pagetime) * 20;
		goodsMap.put("page", page);
		goodsMap.put("pagesize", 20);
		System.out.println(goodsMap);
		List<GoodsPOJO> goods = homePage.findGoods(goodsMap);
		for (GoodsPOJO goodsPOJO : goods) {
			
			goodsPOJO.setGoodsMoney(MoneyFormat.priceFormatString(goodsPOJO.getShopPrice()));
			goodsPOJO.setMarketMoney(MoneyFormat.priceFormatString(goodsPOJO.getMarketPrice()));
			goodsPOJO.setShopPrice(null);
			goodsPOJO.setMarketPrice(null);
			
		}
		return goods;
	}
	
	/**
	 * 分类or精选商品
	 * @return List<GoodsTypePOJO>
	 */
	@RequestMapping("/goodstype")
	@ResponseBody
	public List<GoodsTypePOJO> findSelection(){
		List<GoodsTypePOJO> goodsSelection = homePage.findTypeGoods();
		return goodsSelection;
	}
}

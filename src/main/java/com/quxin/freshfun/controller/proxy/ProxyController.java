package com.quxin.freshfun.controller.proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quxin.freshfun.model.AllGoods;
import com.quxin.freshfun.model.GoodsMongo;
import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.HomePagePOJO;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.service.goods.HomePageService;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.utils.MoneyFormat;

@Controller
@RequestMapping("/proxy")
public class ProxyController {
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private HomePageService homePage;
	@Autowired
	private GoodsService goods;
	@Autowired
	private OrderService orders;

	/**
	 * 查询商品
	 * 
	 * @return List<Goods>
	 */

	@RequestMapping("/homepage")
	@ResponseBody
	public HomePagePOJO ShowHomePage() {
		HomePagePOJO homePagePOJO = new HomePagePOJO();
		Map<String, Integer> goodsMap = new HashMap<>(2);
		Integer page = 0;
		goodsMap.put("page", page);
		goodsMap.put("pagesize", 20);
		List<GoodsPOJO> goods = homePage.findGoods(goodsMap);
		for (GoodsPOJO goodsPOJO : goods) {
			goodsPOJO.setGoodsMoney(MoneyFormat.priceFormatString(goodsPOJO.getShop_price()));
			goodsPOJO.setMarketMoney(MoneyFormat.priceFormatString(goodsPOJO.getMarket_price()));
			goodsPOJO.setSales((int) (Math.random() * 10000));
			goodsPOJO.setAgencyFees(30000);
		}
		homePagePOJO.setGoodsByLimit(goods);
		return homePagePOJO;
	}

	/**
	 * 查询代理商品
	 * 
	 * @return List<Goods>
	 */
	@RequestMapping("/homepage1")
	@ResponseBody
	public List<GoodsPOJO> findGoodsByLimit(Integer pagetime) {
		Map<String, Integer> goodsMap = new HashMap<>(2);
		Integer page = (pagetime) * 20;
		goodsMap.put("page", page);
		goodsMap.put("pagesize", 20);
		List<GoodsPOJO> goods = homePage.findGoods(goodsMap);
		for (GoodsPOJO goodsPOJO : goods) {
			goodsPOJO.setGoodsMoney(MoneyFormat.priceFormatString(goodsPOJO.getShop_price()));
			goodsPOJO.setMarketMoney(MoneyFormat.priceFormatString(goodsPOJO.getMarket_price()));
			goodsPOJO.setSales((int) (Math.random() * 10000));
			goodsPOJO.setAgencyFees(30000);
		}
		return goods;
	}

	/**
	 * 商品详情页
	 * 
	 * @return List<GoodsMongo>
	 */
	@RequestMapping("/goods")
	@ResponseBody
	public AllGoods findMongoGoods(Integer goodsID) {
		AllGoods allGoods = new AllGoods();
		List<GoodsMongo> goodsMongo = goods.findGoodsMongo(goodsID);
		allGoods.setGoodsMongo(goodsMongo);
		GoodsPOJO goodsMysql = goods.findGoodsMysql(goodsID);
		goodsMysql.setAgencyFees(30000);
		goodsMysql.setGoodsMoney(MoneyFormat.priceFormatString(goodsMysql.getShop_price()));
		goodsMysql.setMarketMoney(MoneyFormat.priceFormatString(goodsMysql.getMarket_price()));
		allGoods.setGoodsPOJO(goodsMysql);
		return allGoods;
	}

	/**
	 * 我的品牌
	 * @param merchant_proxy_id
	 * @return
	 */
	@RequestMapping("/toBrands")
	@ResponseBody
	public List<GoodsPOJO> toMyBrand(Integer merchant_proxy_id) {
		Map<String, Object> doos = new HashMap<>();
		doos.put("mpId", merchant_proxy_id);
		List<GoodsPOJO> goodss = goodsService.findProxyGoods(doos);
		for (GoodsPOJO goodsPOJO : goodss) {
			goodsPOJO.setGoodsMoney(MoneyFormat.priceFormatString(goodsPOJO.getShop_price()));
			int goodsID = goodsPOJO.getId();
			List<GoodsMongo> goodsMongo = goods.findGoodsMongo(goodsID);
			String img = goodsMongo.get(0).getDetailImgPath();
			goodsPOJO.setGoodsDetailImg(img);
		}
		return goodss;
	}


	@RequestMapping("/getIncome")
	@ResponseBody
	public  Map<String, Object> getIncome(String userId) {
		Map<String, Object> map = new HashMap<>();
        //总收益
		int totalRevenue = orders.queryAllIncome(Long.parseLong(userId));
		//已入账收益
		int earnedRevenue =orders.queryEarnedIncome(Long.parseLong(userId));
		//未入账收益
		int unbilledRevenue = totalRevenue-earnedRevenue;
		map.put("totalRevenue", (double)totalRevenue/5);
		map.put("unbilledRevenue", (double)unbilledRevenue/5);
		return map;
	}
}
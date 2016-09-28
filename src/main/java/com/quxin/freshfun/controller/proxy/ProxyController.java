package com.quxin.freshfun.controller.proxy;

import com.quxin.freshfun.model.AllGoods;
import com.quxin.freshfun.model.GoodsMongo;
import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.HomePagePOJO;
import com.quxin.freshfun.service.bill.BillService;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.service.goods.HomePageService;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.utils.MoneyFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

@Controller
@RequestMapping("/proxy")
public class ProxyController {
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private HomePageService homePageService;
	@Autowired
	private GoodsService goods;
	@Autowired
	private OrderService orders;
	@Autowired
	private BillService billService;

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
		List<GoodsPOJO> goods = homePageService.findGoods(goodsMap);
		for (GoodsPOJO goodsPOJO : goods) {
			goodsPOJO.setGoodsMoney(MoneyFormat.priceFormatString(goodsPOJO.getShopPrice()));
			goodsPOJO.setMarketMoney(MoneyFormat.priceFormatString(goodsPOJO.getMarketPrice()));
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
		List<GoodsPOJO> goods = homePageService.findGoods(goodsMap);
		for (GoodsPOJO goodsPOJO : goods) {
			goodsPOJO.setGoodsMoney(MoneyFormat.priceFormatString(goodsPOJO.getShopPrice()));
			goodsPOJO.setMarketMoney(MoneyFormat.priceFormatString(goodsPOJO.getMarketPrice()));
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
		goodsMysql.setGoodsMoney(MoneyFormat.priceFormatString(goodsMysql.getShopPrice()));
		goodsMysql.setMarketMoney(MoneyFormat.priceFormatString(goodsMysql.getMarketPrice()));
		allGoods.setGoodsPOJO(goodsMysql);
		return allGoods;
	}

	/**
	 * 我的品牌
	 * @param merchantProxyId
	 * @return
	 */
	@RequestMapping("/toBrands")
	@ResponseBody
	public List<GoodsPOJO> toMyBrand(Integer merchantProxyId) {
		Map<String, Object> doos = new HashMap<>();
		doos.put("mpId", merchantProxyId);
		List<GoodsPOJO> goodss = goodsService.findProxyGoods(doos);
		for (GoodsPOJO goodsPOJO : goodss) {
			goodsPOJO.setGoodsMoney(MoneyFormat.priceFormatString(goodsPOJO.getShopPrice()));
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
//		Integer totalRevenue = orders.queryAllIncome(Long.parseLong(userId));
		String totalRevenue = billService.selectAgentsIncome(Long.parseLong(userId));
		if(totalRevenue == null){
			totalRevenue = "0";
		}
		//未入账收益
		String unbilledRevenue = billService.selectAgentsRreezeMoney(Long.parseLong(userId));
		if (unbilledRevenue == null){
			unbilledRevenue = "0";
		}
		map.put("totalRevenue", Double.valueOf(totalRevenue));
		map.put("unbilledRevenue",Double.valueOf(unbilledRevenue));
		return map;
	}
}
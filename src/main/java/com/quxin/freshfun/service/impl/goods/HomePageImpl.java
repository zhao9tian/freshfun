package com.quxin.freshfun.service.impl.goods;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.dao.ActivityMapper;
import com.quxin.freshfun.dao.GoodsLimitMapper;
import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.dao.GoodsTypeMapper;
import com.quxin.freshfun.dao.GtypeVsGidMapper;
import com.quxin.freshfun.dao.SpecialMallMapper;
import com.quxin.freshfun.dao.SpecialThemeMapper;
import com.quxin.freshfun.dao.StidVsGidMapper;
import com.quxin.freshfun.dao.UsersMapper;
import com.quxin.freshfun.model.Activity;
import com.quxin.freshfun.model.GoodsLimit;
import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.GoodsTypePOJO;
import com.quxin.freshfun.model.GtypeVsGid;
import com.quxin.freshfun.model.SpecialMall;
import com.quxin.freshfun.model.SpecialTheme;
import com.quxin.freshfun.model.StidVsGid;
import com.quxin.freshfun.model.UsersPOJO;
import com.quxin.freshfun.service.goods.HomePageService;
import com.quxin.freshfun.utils.MoneyFormat;

@Service
public class HomePageImpl implements HomePageService {
	@Autowired
	private SpecialMallMapper specialMall;
	@Autowired
	private GoodsTypeMapper goodsType;
	@Autowired
	private ActivityMapper activity;
	@Autowired
	private GtypeVsGidMapper gtypeVsGid;
	@Autowired
	private GoodsLimitMapper goodsLimit;
	@Autowired
	private StidVsGidMapper stidVsGid;
	@Autowired
	private SpecialThemeMapper specialTheme;
	@Autowired
	private GoodsMapper goods;
	@Autowired
	private UsersMapper users;
	

	/**
	 * 查询banner数据
	 */
	@Override
	public List<SpecialMall> homeBanner() {
		return specialMall.findAll();
	}
	
	/**
	 * 查询分类数据
	 */
	@Override
	public List<GoodsTypePOJO> homeGoodsType() {
		return goodsType.findAll();
	}
	
	/**
	 * 查询活动详情
	 */
	@Override
	public List<Activity> homeActivity(Byte id) {
		return activity.findAll(id);
	}
	
	/**
	 * 查看编辑精选
	 */
	@Override
	public List<GoodsTypePOJO> homeGoodsTypeByType(String type) {
		List<GoodsTypePOJO> getAllGoods = goodsType.findByType(type);
		for (GoodsTypePOJO goodstype : getAllGoods) {
			List<GtypeVsGid> gtypeVsGid = goodstype.getTypeGids();
			for (GtypeVsGid goodsType : gtypeVsGid){
				GoodsPOJO goods = goodsType.getGoods();
				if(goods != null){
					goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShop_price()));
					goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarket_price()));
				}
//				goods.setShop_price(null);
//				goods.setMarket_price(null);
			}
		}
		return getAllGoods;
	}
	
	/**
	 * 查看限时商品
	 */
	@Override
	public GoodsLimit homeGoodsNowLimit(long now_time) {
		GoodsLimit goods = goodsLimit.findIsLimit(now_time);
		if (goods != null){
			goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShop_price()));
			goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarket_price()));
//			goods.setShop_price(null);
//			goods.setMarket_price(null);
		}
		
		return goods;
	}

	@Override
	public GoodsLimit homeGoodsGoingLimit(long now_time) {
		
		GoodsLimit goods = goodsLimit.findIsLimit(now_time);
		if (goods != null){
			goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShop_price()));
			goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarket_price()));
//			goods.setShop_price(null);
//			goods.setMarket_price(null);
		}
		return goods;
	}

	@Override
	public List<SpecialTheme> homeGoodsTheme() {
		List<SpecialTheme> getSpecialGoods = specialTheme.findAll();
		for (SpecialTheme goodstheme : getSpecialGoods) {
			List<StidVsGid> stidVsGid = goodstheme.getStidVsGid();
			for (StidVsGid goodsType : stidVsGid){
				GoodsPOJO goods = goodsType.getGoods();
				if(goods != null){
					goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShop_price()));
					goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarket_price()));
				}
				
//				goods.setShop_price(null);
//				goods.setMarket_price(null);
			}
		}
		return getSpecialGoods;
	}
	

	@Override
	public List<GoodsPOJO> findGoods(Map<String, Integer> goodsMap) {
		return goods.findByLimit(goodsMap);
	}
	
	@Override
	public UsersPOJO findEnterByID(Integer id) {
		return users.findEnterByID(id);
	}

	@Override
	public List<GoodsTypePOJO> findTypeGoods() {
		// TODO Auto-generated method stub
		List<GoodsTypePOJO> getAllGoods = goodsType.findAllGoods();
		for (GoodsTypePOJO goodstype : getAllGoods) {
			List<GtypeVsGid> gtypeVsGid = goodstype.getTypeGids();
			for (GtypeVsGid goodsType : gtypeVsGid){
				GoodsPOJO goods = goodsType.getGoods();
				if(goods != null){
					goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShop_price()));
					goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarket_price()));
				}
//				goods.setShop_price(null);
//				goods.setMarket_price(null);
			}
			
			
		}
		return getAllGoods;
	}

}

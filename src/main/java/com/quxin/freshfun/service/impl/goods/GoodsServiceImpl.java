package com.quxin.freshfun.service.impl.goods;

import com.quxin.freshfun.dao.*;
import com.quxin.freshfun.model.*;
import com.quxin.freshfun.model.pojo.CommentPOJO;
import com.quxin.freshfun.service.comment.CommentService;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.utils.MoneyFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {
	
	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private GoodsDetailsMapper goodsDetailsMapper;

	@Autowired
	private GoodsLimitMapper goodsLimitMapper;
	
	@Autowired
	private StidVsGidMapper stidVsGidMapper;
	
	@Autowired
	private SmidVsGidMapper smidVsGidMapper;
	
	@Autowired
	private CommentService mongeComment;

	@Override
	public List<GoodsMongo> findGoodsMongo(Integer goodsID) {
		return goodsDetailsMapper.selectGoodsDetailByGoodsId(goodsID);
	}

	@Override
	public GoodsPOJO findGoodsMysql(Integer goodsID) {
		
		GoodsPOJO oneGoods = goodsMapper.findByGoodsId(goodsID);
		if (oneGoods != null){
			oneGoods.setGoodsMoney(MoneyFormat.priceFormatString(oneGoods.getShopPrice()));
			oneGoods.setMarketMoney(MoneyFormat.priceFormatString(oneGoods.getMarketPrice()));
		}
		
		return oneGoods;
	}



	@Override
	public GoodsLimit findLimitGoodsMysql(Integer goodsID) {
		GoodsLimit goods = goodsLimitMapper.findById(goodsID);
		if (goods != null){
			goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
			goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarketPrice()));
		}
		
		return goods;
	}



	@Override
	public List<StidVsGid> findThemeGoods(Map<String, Integer> themeMap) {
		// TODO Auto-generated method stub
		List<StidVsGid> stidVsGid = stidVsGidMapper.selectGoodsLimit(themeMap);
		for (StidVsGid goodsTheme : stidVsGid){
			GoodsPOJO goods = goodsTheme.getGoods();
			if(goods != null){
				goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
				goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarketPrice()));
			}

		}
		return stidVsGid;
	}



	@Override
	public List<SmidVsGid> findMallGoods(Map<String, Integer> mallMap) {
		// TODO Auto-generated method stub
		
		List<SmidVsGid> smidVsGid = smidVsGidMapper.selectGoodsLimit(mallMap);
		for (SmidVsGid goodsMall : smidVsGid){
			GoodsPOJO goods = goodsMall.getGoods();
			if(goods != null){
				goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
				goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarketPrice()));
			}

		}
		return smidVsGid;
	}

	@Override
	public List<GoodsPOJO> findProxyGoods(Map<String, Object> map) {
		return goodsMapper.findProxyGoods(map);
	}
}
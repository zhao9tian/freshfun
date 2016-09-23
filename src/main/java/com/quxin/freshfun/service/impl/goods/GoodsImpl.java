package com.quxin.freshfun.service.impl.goods;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.dao.GoodsLimitMapper;
import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.dao.SmidVsGidMapper;
import com.quxin.freshfun.dao.StidVsGidMapper;
import com.quxin.freshfun.model.Comment;
import com.quxin.freshfun.model.GoodsLimit;
import com.quxin.freshfun.model.GoodsMongo;
import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.SmidVsGid;
import com.quxin.freshfun.model.StidVsGid;
import com.quxin.freshfun.mongodb.MongoComment;
import com.quxin.freshfun.mongodb.MongoGoods;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.utils.MoneyFormat;
@Service("goodsService")
public class GoodsImpl implements GoodsService {
	
	@Autowired
	private MongoGoods mongoGoods;
	
	@Autowired
	private GoodsMapper goods;
	
	@Autowired
	private GoodsLimitMapper goodsLimit;
	
	@Autowired
	private StidVsGidMapper specialTheme;
	
	@Autowired
	private SmidVsGidMapper specialMall;
	
	@Autowired
	private MongoComment mongeComment;
	
	

	@Override
	public List<GoodsMongo> findGoodsMongo(Integer goodsID) {
		Query query = Query.query(Criteria.where("goodsId").is(goodsID));
		System.out.println(query);
		return mongoGoods.findGoodsMongo(query);
	}



	@Override
	public GoodsPOJO findGoodsMysql(Integer goodsID) {
		
		GoodsPOJO oneGoods = goods.findByGoodsId(goodsID);
		if (oneGoods != null){
			oneGoods.setGoodsMoney(MoneyFormat.priceFormatString(oneGoods.getShopPrice()));
			oneGoods.setMarketMoney(MoneyFormat.priceFormatString(oneGoods.getMarketPrice()));
		}
		
		return oneGoods;
	}



	@Override
	public List<GoodsMongo> findLimitGoodsMongo(Integer goodsID) {
		Query query = Query.query(Criteria.where("goodsId").is(goodsID));
		System.out.println(query);
		return mongoGoods.findLimitGoodsMongo(query);
	}



	@Override
	public GoodsLimit findLimitGoodsMysql(Integer goodsID) {
		GoodsLimit goods = goodsLimit.findById(goodsID);
		if (goods != null){
			goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
			goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarketPrice()));
		}
		
		return goods;
	}



	@Override
	public List<StidVsGid> findThemeGoods(Map<String, Integer> themeMap) {
		// TODO Auto-generated method stub
		List<StidVsGid> stidVsGid = specialTheme.selectGoodsLimit(themeMap);
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
		
		List<SmidVsGid> smidVsGid = specialMall.selectGoodsLimit(mallMap);
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
	public List<Comment> findComment(String userID, String goodsID) {
		// TODO Auto-generated method stub
		Query query = Query.query(Criteria.where("goodsId").is(goodsID).and("user_id").is(userID));
		System.out.println(query);
		return mongeComment.findComment(query);
	}



	@Override
	public void addComment(Comment comment) {
		mongeComment.addComment(comment);
		
	}

	@Override
	public List<GoodsPOJO> findProxyGoods(Map<String, Object> map) {
		return goods.findProxyGoods(map);
	}
}
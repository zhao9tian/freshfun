package com.quxin.freshfun.service.impl.goods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.quxin.freshfun.dao.GoodsDetailsMapper;
import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.dao.SmidVsGidMapper;
import com.quxin.freshfun.dao.StidVsGidMapper;
import com.quxin.freshfun.model.GoodsMongo;
import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.SmidVsGid;
import com.quxin.freshfun.model.StidVsGid;
import com.quxin.freshfun.model.goods.BannerPOJO;
import com.quxin.freshfun.model.goods.ThemePOJO;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.utils.MoneyFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private GoodsDetailsMapper goodsDetailsMapper;

	@Autowired
	private StidVsGidMapper stidVsGidMapper;
	
	@Autowired
	private SmidVsGidMapper smidVsGidMapper;
	

	@Override
	public List<GoodsMongo> findGoodsMongo(Integer goodsID) {
		return goodsDetailsMapper.selectGoodsDetailByGoodsId(goodsID);
	}

	@Override
	public GoodsMongo queryGoodsDetailById(Integer goodsId) {
		return goodsDetailsMapper.selectGoodsDetailPOJOByGoodsId(goodsId);
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
	public List<StidVsGid> findThemeGoods(Map<String, Integer> themeMap) {
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

	@Override
	public List<GoodsPOJO> querySortGoods() {
		String sortValue = goodsMapper.selectPictureWall("pictureWall");
		if(sortValue == null || "".equals(sortValue)){
			return null;
		}
		JSONArray sortArr = JSON.parseArray(sortValue);
		List<Integer> sortList = new ArrayList<>();
		if(sortArr != null && sortArr.size() > 0){
			for(Object goodsId : sortArr){
				sortList.add((Integer) goodsId);
			}
			return goodsMapper.selectSortGoods(sortList);
		}else{
			return null;
		}
	}

	@Override
	public BannerPOJO queryBannerById(Integer bannerId) {
		if(bannerId != null){
			BannerPOJO bannerPOJO = goodsMapper.selectBannerById(bannerId);
			if(bannerPOJO == null){
				logger.error("没有该banner的信息，或者已禁用");
			}
			return bannerPOJO;
		}else{
			logger.error("bannerId 不能为空");
			return null;
		}
	}

	@Override
	public ThemePOJO queryThemeById(Integer themeId) {
		if(themeId != null){
			ThemePOJO themePOJO = goodsMapper.selectThemeById(themeId);
			if(themePOJO == null){
				logger.error("没有该专题的信息,或者已禁用");
			}else{
				return themePOJO;
			}
		}else{
			logger.error("bannerId 不能为空");
			return null;
		}
		return null;
	}
}
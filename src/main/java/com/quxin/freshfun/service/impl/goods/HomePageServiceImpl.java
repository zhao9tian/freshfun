package com.quxin.freshfun.service.impl.goods;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.dao.GoodsTypeMapper;

import com.quxin.freshfun.dao.SpecialMallMapper;
import com.quxin.freshfun.dao.SpecialThemeMapper;

import com.quxin.freshfun.dao.UsersMapper;
import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.GoodsTypePOJO;
import com.quxin.freshfun.model.GtypeVsGid;
import com.quxin.freshfun.model.SpecialMall;
import com.quxin.freshfun.model.SpecialTheme;
import com.quxin.freshfun.model.StidVsGid;
import com.quxin.freshfun.model.UsersPOJO;
import com.quxin.freshfun.service.goods.HomePageService;
import com.quxin.freshfun.utils.MoneyFormat;

@Service("homePageService")
public class HomePageServiceImpl implements HomePageService {
	@Autowired
	private SpecialMallMapper specialMallMapper;
	@Autowired
	private GoodsTypeMapper goodsTypeMapper;
	@Autowired
	private SpecialThemeMapper specialThemeMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private UsersMapper usersMapper;
	

	/**
	 * 查询banner数据
	 */
	@Override
	public List<SpecialMall> homeBanner() {
		return specialMallMapper.findAll();
	}
	
	/**
	 * 查询分类数据
	 */
	@Override
	public List<GoodsTypePOJO> homeGoodsType() {
		return goodsTypeMapper.findAll();
	}
	

	/**
	 * 查看编辑精选
	 */
	@Override
	public List<GoodsTypePOJO> homeGoodsTypeByType(String type) {
		List<GoodsTypePOJO> getAllGoods = goodsTypeMapper.findByType(type);
		for (GoodsTypePOJO goodstype : getAllGoods) {
			List<GtypeVsGid> gtypeVsGid = goodstype.getTypeGids();
			for (GtypeVsGid goodsType : gtypeVsGid){
				GoodsPOJO goods = goodsType.getGoods();
				if(goods != null){
					goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
					goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarketPrice()));
				}
			}
		}
		return getAllGoods;
	}


	@Override
	public List<SpecialTheme> homeGoodsTheme() {
		List<SpecialTheme> getSpecialGoods = specialThemeMapper.findAll();
		for (SpecialTheme goodstheme : getSpecialGoods) {
			List<StidVsGid> stidVsGid = goodstheme.getStidVsGid();
			for (StidVsGid goodsType : stidVsGid){
				GoodsPOJO goods = goodsType.getGoods();
 				if(goods != null){
					goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
					goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarketPrice()));
				}

			}
		}
		return getSpecialGoods;
	}
	

	@Override
	public List<GoodsPOJO> findGoods(Map<String, Integer> goodsMap) {
		return goodsMapper.findByLimit(goodsMap);
	}
	
	@Override
	public UsersPOJO findEnterByID(Integer id) {
		return usersMapper.findEnterByID(id);
	}

	@Override
	public List<GoodsTypePOJO> findTypeGoods() {
		List<GoodsTypePOJO> getAllGoods = goodsTypeMapper.findAllGoods();
		for (GoodsTypePOJO goodstype : getAllGoods) {
			List<GtypeVsGid> gtypeVsGid = goodstype.getTypeGids();
			for (GtypeVsGid goodsType : gtypeVsGid){
				GoodsPOJO goods = goodsType.getGoods();
				if(goods != null){
					goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
					goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarketPrice()));
				}
			}
			
			
		}
		return getAllGoods;
	}

}

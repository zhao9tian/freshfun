package com.quxin.freshfun.controller.goods;

import com.quxin.freshfun.model.*;
import com.quxin.freshfun.model.goods.BannerPOJO;
import com.quxin.freshfun.model.goods.GoodsInfoPOJO;
import com.quxin.freshfun.model.pojo.goods.ThemePOJO;
import com.quxin.freshfun.model.outparam.BannerOutParam;
import com.quxin.freshfun.model.outparam.ThemeOutParam;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.utils.MoneyFormat;
import com.quxin.freshfun.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GoodsService goodsService;

	/**
	 * 查询商品详情
	 *
	 * @param goodsId 商品Id
	 * @return 返回查询结果
	 */
	@RequestMapping(value = "/queryGoodsDetail")
	@ResponseBody
	public Map<String , Object> queryGoodsDetailByGoodsId(Integer goodsId){
		GoodsInfoPOJO goodsInfoPOJO = new GoodsInfoPOJO();
		Map<String , Object> result;
		//商品基本信息
        if(goodsId != null){
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
                Map<String , Object> map = new HashMap<>();
                map.put("goodsInfo" , goodsInfoPOJO);
                result = ResultUtil.success(map);
            }else{
                result = ResultUtil.fail(1004 ,"该商品已经下架或者没有该商品" );
            }
        }else{
            logger.error("商品Id不能为空");
            result = ResultUtil.fail(1004,"商品Id不能为空");
        }
		return result;
	}


	/**
	 * 专题商品
	 * @return List<GoodsMongo>
	 */
	@RequestMapping(value="/goodstheme",method={RequestMethod.POST})
	@ResponseBody
	public Map<String , Object> findThemeGoods(@RequestBody GoodsThemeInfo goodsThemeIngo){
		Integer pagetime = goodsThemeIngo.getPagetime();
		Integer themeId = goodsThemeIngo.getThemeId();
		Map<String, Integer> themeMap = new HashMap<>();
		Integer page = (pagetime - 1) * 20;
		themeMap.put("themeId", themeId);
		themeMap.put("page", page);
		List<StidVsGid> specialTheme = goodsService.findThemeGoods(themeMap);
		Map<String , Object> map = new HashMap<>();
		map.put("specialTheme",specialTheme);
		return map;
	}
	
	/**
	 * 首页
	 * @return List<GoodsMongo>
	 */
	@RequestMapping(value="/goodsmall",method={RequestMethod.POST})
	@ResponseBody
	public Map<String , Object> findMallGoods(@RequestBody GoodsThemeInfo goodsThemeIngo){
		Integer pagetime = goodsThemeIngo.getPagetime();
		Integer themeId = goodsThemeIngo.getThemeId();
		Map<String, Integer> mallMap = new HashMap<>();
		Integer page = (pagetime - 1) * 20;
		mallMap.put("mallId", themeId);
		mallMap.put("page", page);
		List<SmidVsGid> mallTheme = goodsService.findMallGoods(mallMap);
		Map<String , Object> map = new HashMap<>();
		map.put("mallTheme",mallTheme);
		return map;
	}

	@RequestMapping(value = "queryBannerById" , method = RequestMethod.GET)
	@ResponseBody
	public Map<String , Object> queryBannerById(Integer bannerId){
		Map<String , Object> result ;
		if(bannerId !=null){
			BannerPOJO bannerPOJO = goodsService.queryBannerById(bannerId);
			if(bannerPOJO != null){
				BannerOutParam bannerOutParam = new BannerOutParam();
				bannerOutParam.setBannerId(bannerPOJO.getBannerId());
				bannerOutParam.setMallDes(bannerPOJO.getBannerDes());
				bannerOutParam.setMallInfoImg(bannerPOJO.getBannerImg());
				bannerOutParam.setMallInfoContent(bannerPOJO.getMallInfoContent());
				result = ResultUtil.success(bannerOutParam);
			}else{
				result = ResultUtil.fail(1004 , "没有该banner信息或者已经禁用");
			}
		}else{
			logger.error("bannerId 不能为空");
			result = ResultUtil.fail(1004 , "bannerId不能为空");
		}
		return result;
	}

	@RequestMapping(value = "queryThemeById" , method = RequestMethod.GET)
	@ResponseBody
	public Map<String , Object> queryThemeById(Integer themeId){
		Map<String , Object> result ;
		if(themeId != null){
			ThemePOJO themePOJO = goodsService.queryThemeById(themeId);
			if(themePOJO != null){
				ThemeOutParam themeOutParam = new ThemeOutParam();
				themeOutParam.setThemeId(themeId);
				themeOutParam.setThemeDes(themePOJO.getThemeDes());
				themeOutParam.setThemeInfoContent(themePOJO.getThemeInfoContent());
				themeOutParam.setThemeInfoImg(themePOJO.getThemeImg());
				result = ResultUtil.success(themeOutParam);
			}else{
				result = ResultUtil.fail(1004 , "没有该banner信息或者已经禁用");
			}
		}else{
			logger.error("themeId不能为空");
			result = ResultUtil.fail(1004,"themeId不能为空");
		}
		return result;
	}

}

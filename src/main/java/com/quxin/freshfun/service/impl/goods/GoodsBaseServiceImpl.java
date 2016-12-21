package com.quxin.freshfun.service.impl.goods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quxin.freshfun.common.Constant;
import com.quxin.freshfun.dao.GoodsBaseMapper;
import com.quxin.freshfun.dao.GoodsPropertyMapper;
import com.quxin.freshfun.dao.GoodsThemeMapper;
import com.quxin.freshfun.dao.PromotionMapper;
import com.quxin.freshfun.model.goods.LimitedNumGoodsPOJO;
import com.quxin.freshfun.model.goods.PromotionGoodsPOJO;
import com.quxin.freshfun.model.outparam.goods.BannerOut;
import com.quxin.freshfun.model.outparam.goods.GoodsOut;
import com.quxin.freshfun.model.outparam.goods.SpecialOut;
import com.quxin.freshfun.model.pojo.PromotionPOJO;
import com.quxin.freshfun.model.pojo.goods.*;
import com.quxin.freshfun.service.goods.GoodsBaseService;
import com.quxin.freshfun.service.promotion.PromotionService;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.DateUtils;
import com.quxin.freshfun.utils.MoneyFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qingtian on 2016/10/26.
 */
@Service("goodsBaseService")
public class GoodsBaseServiceImpl implements GoodsBaseService {
    @Autowired
    private GoodsThemeMapper goodsThemeMapper;
    @Autowired
    private GoodsBaseMapper goodsBaseMapper;
    @Autowired
    private GoodsPropertyMapper goodsPropertyMapper;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private PromotionMapper promotionMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<SpecialOut> getSpecialTheme(Integer page, Integer pageSize) throws BusinessException {
        List<ThemePOJO> themeList = goodsThemeMapper.selectAll(page, pageSize);
        if (themeList != null) {
            for (ThemePOJO theme : themeList) {
                String content = theme.getThemeInfoContent();
                //分割商品编号
                Long[] goodsIds = splitGoodsContent(content);
                if (goodsIds != null && goodsIds.length > 0) {
                    List<GoodsOut> goodsList = getGoodsList(goodsIds);
                    theme.setGoodsList(goodsList);
                }
            }
            return getSpecialList(themeList);
        }
        return null;
    }

    /**
     * banner列表
     *
     * @return
     */
    @Override
    public List<BannerOut> getBannerList() {
        GoodsPropertyPOJO banner = goodsPropertyMapper.selectValueByKey(Constant.BANNER_KEY);
        if (banner != null) {
            List<BannerOut> bannerOuts = strJsonToList(banner.getValue());
            for (BannerOut bannerOut : bannerOuts) {
                if (bannerOut.getThemeId() != null && !"".equals(bannerOut.getThemeId())) {
                    bannerOut.setIsCampaign(0);
                } else {
                    bannerOut.setThemeId(0L);
                    bannerOut.setIsCampaign(10);
                }
            }
            return bannerOuts;
        }
        return null;
    }

    /**
     * 精选列表
     *
     * @return
     */
    @Override
    public List<GoodsOut> getSelectionList() throws BusinessException {
        GoodsPropertyPOJO selection = goodsPropertyMapper.selectValueByKey(Constant.SELECTION_KEY);
        if (selection != null) {
            //分割商品编号
            List<GoodsOut> goodsOuts = new ArrayList<>();
            List<SelectionPOJO> selectionList = strToList(selection.getValue());
            if(selectionList != null) {
                    List<GoodsBasePOJO> goodsList = goodsBaseMapper.findGoodsInList(selectionList);
                    if(goodsList != null) {
                        for (GoodsBasePOJO goods : goodsList) {
                            for (SelectionPOJO sl: selectionList) {
                                if(goods.getId().equals(sl.getGoodsId())){
                                    goods.setGoodsImg(sl.getImg());
                                }
                            }
                            GoodsOut goodsOut = getGoods(goods);
                            goodsOuts.add(goodsOut);
                        }
                    }
                return goodsOuts;
            }
        }
        return null;
    }

    /**
     * 专题列表
     *
     * @return
     */
    @Override
    public List<SpecialOut> getSpecialList() throws BusinessException {
        GoodsPropertyPOJO banner = goodsPropertyMapper.selectValueByKey(Constant.THEME_KEY);
        if (banner != null) {
            //分割商品编号
            Long[] themeIds = splitGoodsContent(banner.getValue());
            List<SpecialOut> specialList = new ArrayList<>();
            if(themeIds != null && themeIds.length > 0) {
                    List<ThemePOJO> themeList = goodsThemeMapper.selectThemeInId(themeIds);
                    if(themeList != null) {
                        //获取专题下的商品
                        for (ThemePOJO theme : themeList) {
                            Long[] goodsIds = splitGoodsContent(theme.getThemeInfoContent());
                            if (goodsIds != null && goodsIds.length > 0) {
                                List<GoodsOut> goodsList = getGoodsList(goodsIds);
                                //生成出参对象
                                SpecialOut special = new SpecialOut();
                                special.setSpecialId(theme.getThemeId());
                                special.setSpecialDesc(theme.getThemeDes());
                                special.setSpecialInfoImg(theme.getThemeImg());
                                special.setGoodsList(goodsList);
                                specialList.add(special);
                            }
                        }
                    }
                return specialList;
            }
        }
        return null;
    }

    /**
     * 获取商品排序列表
     * @return
     */
    @Override
    public List<GoodsOut> getGoodsSortList() throws BusinessException {
        GoodsPropertyPOJO goodsProperty = goodsPropertyMapper.selectValueByKey(Constant.GOODS_KEY);
        if (goodsProperty != null) {
            Long[] goodsIds = splitGoodsContent(goodsProperty.getValue());
            if (goodsIds != null && goodsIds.length > 0) {
                return getGoodsList(goodsIds);
            }
        }
        return null;
    }

    /**
     * 查询更多推荐商品
     *
     * @return
     */
    @Override
    public List<GoodsOut> getGoodsList(Integer page, Integer pageSize) throws BusinessException {
        if (page == null || pageSize == 0 || pageSize == null || pageSize == 0)
            return null;
        List<GoodsBasePOJO> goodsList = goodsBaseMapper.selectGoodsList(page, pageSize);
        if (goodsList != null)
            return goodsListConverted(goodsList);
        return null;
    }

    /**
     * 查询限时购商品
     * @return
     */
    @Override
    public List<GoodsOut> getGoodsLimitList() throws BusinessException {
        Long currentDate = DateUtils.getCurrentDate();
        List<GoodsOut> goodsList = new ArrayList<>();
        List<PromotionPOJO> promotionList = promotionMapper.selectLimitGoodsInfo(currentDate);
        if (promotionList != null) {
            for (PromotionPOJO promotion : promotionList) {
                //根据商品编号查询商品
                GoodsBasePOJO goods = goodsBaseMapper.findGoodsById(promotion.getObjectId());
                if (goods != null) {
                    GoodsOut goodsOut = getGoods(goods);
                    if (currentDate >= promotion.getStartTime()) {
                        //限时购进行中
                        goodsOut.setIsDiscount(10);
                        goodsOut.setEndTime(promotion.getEndTime() - currentDate);
                        goodsOut.setStartTime(0l);
                    } else {
                        goodsOut.setIsDiscount(5);
                        goodsOut.setStartTime(promotion.getStartTime() - currentDate);
                        goodsOut.setEndTime(promotion.getEndTime() - currentDate);
                    }
                    goodsList.add(goodsOut);
                }
            }
        }
        return goodsList;
    }

    /**
     * 根据类目编号查询商品信息
     * @param categoryKey 类目编号
     * @return
     */
    @Override
    public List<GoodsOut> getCategory(Integer categoryKey, Integer page, Integer pageSize) throws BusinessException {
        if (categoryKey == null)
            throw new BusinessException("查询分类商品类目编号不能为空");
        List<GoodsBasePOJO> goodsList = goodsBaseMapper.selectCatagory2Goods(categoryKey, page, pageSize);
        if (goodsList != null) {
            return goodsListConverted(goodsList);
        }
        return null;
    }

    /**
     * 根据类目编号查询类目图
     *
     * @param categoryKey
     * @return
     */
    @Override
    public String getCategoryImg(Integer categoryKey) throws BusinessException {
        if (categoryKey == null)
            throw new BusinessException("查询分类商品类目编号不能为空");
        GoodsPropertyPOJO goodsProperty = goodsPropertyMapper.selectValueByKey(Constant.TYPE_IMG);
        if (goodsProperty != null) {
            List<TypeImagePOJO> typeImgList = typeImgToList(goodsProperty.getValue());
            for (TypeImagePOJO typeImg : typeImgList) {
                if (categoryKey.equals(typeImg.getTypeNo())) {
                    return typeImg.getImg();
                }
            }
        }
        return null;
    }

    /**
     * 根据专题编号查询专题详情
     *
     * @param specialId
     * @return
     */
    @Override
    public SpecialOut getSpecialDetails(Long specialId) throws BusinessException {
        if (specialId == null) {
            throw new BusinessException("查询专题专题编号不能为空");
        }
        ThemePOJO theme = goodsThemeMapper.selectThemeById(specialId);
        //查询推荐商品
        Long[] goodsIds = splitGoodsContent(theme.getThemeInfoContent());
        if (goodsIds != null) {
            List<GoodsOut> goodsList = getGoodsList(goodsIds);
            theme.setGoodsList(goodsList);
        } else {
            logger.error("查询专题详情时,获取推荐商品编号为空");
        }
        return getSpecial(theme);
    }

    @Override
    public Map<String, Object> findGoodsDetails(Long goodsId) throws BusinessException {
        if (goodsId == null) {
            throw new BusinessException("查询商品详情商品编号不能为空");
        }
        Map<String, Object> map = Maps.newHashMap();
        GoodsBasePOJO goodsBase = goodsBaseMapper.findGoodsById(goodsId);
        if (goodsBase == null) {
            return null;
        }
        GoodsOut goodsOut = generateGoodsDetails(goodsBase);
        //是否限时
        isLimitGoods(goodsOut);
        isLimitedNumGoods(goodsOut);//给限量购商品赋值做区分
        GoodsStandard goodsStandard = goodsBaseMapper.selectGoodsStandard(goodsId);
        List<PropertyValue> goodsStandardList = getGoodsStandardList(goodsStandard);
        map.put("goods", goodsOut);
        map.put("specification", goodsStandardList);
        return map;
    }

    /**
     * 是否限量购,是就赋值属性 , 不是就不赋值
     *
     * @param goodsOut 商品id
     * @return 是否是限量购
     */
    private Boolean isLimitedNumGoods(GoodsOut goodsOut) {
        if (goodsOut != null && goodsOut.getGoodsId() != null) {
            LimitedNumGoodsPOJO limitedNumGoods = promotionMapper.selectLimitedNumGoodsById(goodsOut.getGoodsId());
            if (limitedNumGoods != null && limitedNumGoods.getLimitedRealStock() > 0) {//在打折表里面且限量购库存大于0
                //处理金额
                String limitedPriceDB = limitedNumGoods.getLimitedGoodsPrice();
                String limitedPrice = "0.00";
                if (limitedPriceDB != null && !"".equals(limitedPriceDB)) {
                    Map<String, Object> map = JSON.parseObject(limitedPriceDB);
                    limitedPrice = MoneyFormat.priceFormatString(Integer.parseInt((String) map.get("discountPrice")));
                }
                goodsOut.setShopMoney(limitedPrice);
                goodsOut.setLimitedNumStock(limitedNumGoods.getLimitedGoodsStock());
                goodsOut.setLimitedNumLeaveStock(limitedNumGoods.getLimitedRealStock());
                goodsOut.setIsLimitedNum(1);//是限量购
            }else{
                goodsOut.setIsLimitedNum(0);
                return true;
            }
        } else {
            logger.error("限量购商品id不能为空");
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> queryLimitGoods(Integer isIndex) {
        List<Map<String, Object>> unLimitedSortGoods = new ArrayList<>();
        List<Map<String, Object>> limitedSortGoods = new ArrayList<>();
        //1.查询排序
        GoodsPropertyPOJO goodsProperty = goodsPropertyMapper.selectValueByKey(Constant.LIMITED_GOODS);
        if (goodsProperty != null && goodsProperty.getValue() != null && !"".equals(goodsProperty.getValue()) && !"[]".equals(goodsProperty.getValue())) {
            List<Long> limitedNumIds = JSON.parseArray(goodsProperty.getValue(), Long.class);//排序规则
            //2.查询限量购商品信息
            List<LimitedNumGoodsPOJO> limitedNumGoodsPOJOs;
            if (isIndex == null || isIndex.equals(1)) {//null(默认)或者1都查询所有排序
                limitedNumGoodsPOJOs = goodsBaseMapper.selectAllLimitedNumInfo(limitedNumIds);
            } else {//查询首页--剩余库存为0的不展示
                limitedNumGoodsPOJOs = goodsBaseMapper.selectIndexLimitedNumInfo(limitedNumIds);
            }
            //3.查询商品信息
            List<GoodsBasePOJO> goodsBasePOJOs = goodsBaseMapper.selectGoodsByIds(limitedNumIds);
            //4.组合
            if (limitedNumGoodsPOJOs != null) {
                for (LimitedNumGoodsPOJO limitedNumGoodsPOJO : limitedNumGoodsPOJOs) {
                    for (GoodsBasePOJO goodsBasePOJO : goodsBasePOJOs) {
                        if (limitedNumGoodsPOJO.getLimitedGoodsId().equals(goodsBasePOJO.getId())) {
                            Map<String, Object> limitedGoods = Maps.newHashMap();
                            limitedGoods.put("goodsId", limitedNumGoodsPOJO.getLimitedGoodsId());
                            limitedGoods.put("limitStock", limitedNumGoodsPOJO.getLimitedGoodsStock());
                            limitedGoods.put("limitLeave", limitedNumGoodsPOJO.getLimitedRealStock());
                            //价格处理
                            String limitedGoodsPriceDB = limitedNumGoodsPOJO.getLimitedGoodsPrice();
                            if (limitedGoodsPriceDB != null && !"".equals(limitedGoodsPriceDB)) {
                                Map<String, Object> money = JSON.parseObject(limitedGoodsPriceDB);
                                Integer limitPrice = Integer.parseInt((String) money.get("discountPrice"));
                                limitedGoods.put("limitPrice", MoneyFormat.priceFormatString(limitPrice));
                            }
                            limitedGoods.put("title", goodsBasePOJO.getTitle());
                            limitedGoods.put("originPrice", MoneyFormat.priceFormatString(goodsBasePOJO.getOriginPrice()));
                            limitedGoods.put("goodsImg", goodsBasePOJO.getGoodsImg());
                            if (isIndex == null || isIndex.equals(1)) {//列表页
                                limitedGoods.put("subTitle", goodsBasePOJO.getSubTitle());
                                if(limitedNumGoodsPOJO.getLimitedRealStock() <= 0){
                                    limitedGoods.put("limitPrice", MoneyFormat.priceFormatString(goodsBasePOJO.getShopPrice()));
                                }
                                unLimitedSortGoods.add(limitedGoods);
                            } else {
                                if (isIndex.equals(2) && limitedNumGoodsPOJO.getLimitedRealStock() > 0) {//首页
                                    unLimitedSortGoods.add(limitedGoods);
                                }
                            }
                        }
                    }
                }
                //5.排序
                for (Long id : limitedNumIds) {
                    for (Map<String, Object> map : unLimitedSortGoods) {
                        if (map.get("goodsId").equals(id)) {
                            limitedSortGoods.add(map);
                        }
                    }
                }
                //6.首页最多只显示4条
                if (isIndex != null && isIndex == 2 && limitedSortGoods.size() >= 4) {
                    limitedSortGoods = limitedSortGoods.subList(0, 4);
                }
            } else {
                logger.warn("限量购商品已经售完");
                limitedSortGoods = new ArrayList<>();
            }
        } else {
            logger.warn("数据库里面没有限量购商品排序");
        }
        return limitedSortGoods;
    }

    /**
     * 设置商品价格
     *
     * @param goodsOut 商品实体
     */
    private void getShopPrice(GoodsOut goodsOut) throws BusinessException {
        PromotionGoodsPOJO promotionGoods = promotionService.queryDiscountPriceByGoodsId(goodsOut.getGoodsId());
        //是否打折商品
        if (promotionGoods != null) {
            if (promotionGoods.getDiscount()) {
                goodsOut.setShopMoney(MoneyFormat.priceFormatString(promotionGoods.getDiscountPrice().intValue()));
            }
        }
    }

    /**
     * 获取商品
     *
     * @param goodsStandard 商品属性表
     * @return
     */
    private List<PropertyValue> getGoodsStandardList(GoodsStandard goodsStandard) {
        if (goodsStandard == null)
            return new ArrayList<>();
        Field[] fields = goodsStandard.getClass().getDeclaredFields();
        List<PropertyValue> propertyList = new ArrayList<>();
        //查询商品属性列表
        GoodsPropertyPOJO goodsProperty = goodsPropertyMapper.selectValueByKey(Constant.GOODS_STANDARD);
        List<PropertyValue> propertyAllList = goodsStandardStrToList(goodsProperty.getValue());
        for (int i = 0; i < propertyAllList.size(); i++) {
            for (int j = 0; j < fields.length; j++) {
                if (propertyAllList.get(i).getKey().equals(fields[j].getName())) {
                    Object value = getFieldValueByName(fields[j].getName(), goodsStandard);
                    if (value != null && !"".equals(value)) {
                        PropertyValue propertyValue = new PropertyValue();
                        propertyValue.setKey(propertyAllList.get(i).getName().toString());
                        propertyValue.setName(value);
                        propertyList.add(propertyValue);
                    }
                }
            }
        }
        return propertyList;
    }

    /**
     * 根据属性名获取属性值
     */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 生成商品详情出参
     *
     * @param goodsBase
     */
    private GoodsOut generateGoodsDetails(GoodsBasePOJO goodsBase) throws BusinessException {
        if (goodsBase == null) return null;
        //查询商品图片
        GoodsImage goodsImage = goodsBaseMapper.selectGoodsImgByGoodsId(goodsBase.getId());
        //设置商品的价格
        if (goodsImage != null) {
            GoodsOut goods = getGoods(goodsBase);
            if (goods != null) {
                //是否上架
                goods.setIsOnSale(goodsBase.getIsOnSale());
                goods.setDescriptionStr(goodsBase.getGoodsDes());
                goods.setBannerImgList(ImgStrToList(goodsImage.getCarouselImg()));
                goods.setDetailImgList(ImgStrToList(goodsImage.getDetailImg()));
                return goods;
            }
        }
        return null;
    }

    /**
     * goodsList转出参
     *
     * @param goodsList
     * @return
     */
    private List<GoodsOut> goodsListConverted(List<GoodsBasePOJO> goodsList) throws BusinessException {
        if (goodsList != null) {
            List<GoodsOut> goodsOuts = new ArrayList<>();
            for (GoodsBasePOJO goods : goodsList) {
                GoodsOut goodsOut = getGoods(goods);
                goodsOuts.add(goodsOut);
            }
            return goodsOuts;
        }
        return null;
    }


    /**
     * 专题POJO转出参
     *
     * @param themeList
     * @return
     */
    private List<SpecialOut> getSpecialList(List<ThemePOJO> themeList) {
        if (themeList == null)
            return null;
        List<SpecialOut> specialList = new ArrayList<>();
        for (ThemePOJO theme : themeList) {
            SpecialOut special = getSpecial(theme);
            specialList.add(special);
        }
        return specialList;
    }

    /**
     * 专题转出参
     *
     * @param theme
     * @return
     */
    private SpecialOut getSpecial(ThemePOJO theme) {
        SpecialOut special = new SpecialOut();
        special.setSpecialId(theme.getThemeId());
        special.setSpecialName(theme.getThemeName());
        special.setSpecialDesc(theme.getThemeDes());
        special.setSpecialInfoImg(theme.getThemeImg());
        special.setGoodsList(theme.getGoodsList());
        return special;
    }

    /**
     * 商品POJO转出参
     *
     * @param goods
     * @return
     */
    private GoodsOut getGoods(GoodsBasePOJO goods) throws BusinessException {
        GoodsOut goodsOut = new GoodsOut();
        goodsOut.setGoodsId(goods.getId());
        goodsOut.setGoodsName(goods.getTitle());
        goodsOut.setGoodsDesc(goods.getSubTitle());
        goodsOut.setGoodsImg(goods.getGoodsImg());
        goodsOut.setShopMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
        goodsOut.setMarketMoney(MoneyFormat.priceFormatString(goods.getOriginPrice()));
        //是否打折
        getShopPrice(goodsOut);
        return goodsOut;
    }

    /**
     * 判断是否是限时商品
     *
     * @param goodsOut 商品类
     */
    private void isLimitGoods(GoodsOut goodsOut) {
        if (goodsOut == null)
            return;
        PromotionPOJO promotionPOJO = promotionMapper.selectLimitByGoodsId(goodsOut.getGoodsId(), DateUtils.getCurrentDate());
        goodsOut.setIsDiscount(0);
        if (promotionPOJO != null) {
            Long currentDate = DateUtils.getCurrentDate();
            if (currentDate >= promotionPOJO.getStartTime()) {
                goodsOut.setIsDiscount(10);
                goodsOut.setStartTime(0l);
                goodsOut.setEndTime(promotionPOJO.getEndTime() - currentDate);
            } else {
                goodsOut.setIsDiscount(5);
                goodsOut.setStartTime(promotionPOJO.getStartTime() - currentDate);
                goodsOut.setEndTime(promotionPOJO.getEndTime() - currentDate);
            }
        }
    }

    /**
     * 根据商品列表
     * @param ids
     * @return
     */
    private List<GoodsOut> getGoodsList(Long[] ids) throws BusinessException {
        if (ids == null)
            return null;
        List<GoodsOut> goodsList = new ArrayList<>();
        //for (Long id: ids) {
            //根据商品编号查询商品
            List<GoodsBasePOJO> goodsBaseList = goodsBaseMapper.findGoodsInId(ids);
            if(goodsBaseList != null) {
                for (GoodsBasePOJO goods : goodsBaseList) {
                    GoodsOut goodsOut = getGoods(goods);
                    goodsList.add(goodsOut);
                }
            }
       // }
        return goodsList;
    }

    /**
     * 分割商品编号
     *
     * @param content
     * @return
     */
    private Long[] splitGoodsContent(String content) {
        if (content == null || "".equals(content))
            return null;
        Gson gson = new Gson();
        return gson.fromJson(content, Long[].class);
    }

    /**
     * 商品分类图
     *
     * @param str
     * @return
     */
    private List<TypeImagePOJO> typeImgToList(String str) {
        if (str == null || "".equals(str))
            return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<TypeImagePOJO>>() {
        }.getType();
        return gson.fromJson(str, type);
    }

    /**
     * 图片字符串转集合
     *
     * @return
     */
    private List<String> ImgStrToList(String str) {
        if (str == null || "".equals(str))
            return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(str, type);
    }

    /**
     * 商品属性Str转List
     *
     * @param value
     * @return
     */
    private List<PropertyValue> goodsStandardStrToList(String value) {
        if (value == null || "".equals(value))
            return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<PropertyValue>>() {
        }.getType();
        return gson.fromJson(value, type);
    }

    /**
     * Json字符串转List
     *
     * @param str json字符串
     * @return-
     */
    private List<BannerOut> strJsonToList(String str) {
        if (str == null || "".equals(str))
            return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<BannerOut>>() {
        }.getType();
        return gson.fromJson(str, type);
    }

    /**
     * 专题Json字符串转集合
     *
     * @param str
     * @return
     */
    public List<SelectionPOJO> strToList(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<SelectionPOJO>>() {
        }.getType();
        return gson.fromJson(str, type);
    }


    public static void main(String[] args) {
        System.out.println(DateUtils.getCurrentDate());
    }
}

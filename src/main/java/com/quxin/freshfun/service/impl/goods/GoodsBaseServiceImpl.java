package com.quxin.freshfun.service.impl.goods;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quxin.freshfun.common.Constant;
import com.quxin.freshfun.dao.GoodsBaseMapper;
import com.quxin.freshfun.dao.GoodsPropertyMapper;
import com.quxin.freshfun.dao.GoodsThemeMapper;
import com.quxin.freshfun.model.outparam.goods.BannerOut;
import com.quxin.freshfun.model.outparam.goods.GoodsOut;
import com.quxin.freshfun.model.outparam.goods.SpecialOut;
import com.quxin.freshfun.model.pojo.goods.*;
import com.quxin.freshfun.service.goods.GoodsBaseService;
import com.quxin.freshfun.utils.MoneyFormat;
import com.quxin.freshfun.utils.weixinPayUtils.WXUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
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

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<SpecialOut> getSpecialTheme(Integer page,Integer pageSize) {
        List<ThemePOJO> themeList = goodsThemeMapper.selectAll(page, pageSize);
        if(themeList != null) {
            for (ThemePOJO theme : themeList) {
                String content = theme.getThemeInfoContent();
                //分割商品编号
                Long[] goodsIds = splitGoodsContent(content);
                if(goodsIds != null && goodsIds.length > 0) {
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
     * @return
     */
    @Override
    public List<BannerOut> getBannerList() {
        GoodsPropertyPOJO banner = goodsPropertyMapper.selectValueByKey(Constant.BANNER_KEY);
        if(banner != null) {
            List<BannerOut> bannerOuts = strJsonToList(banner.getValue());
            for (BannerOut bannerOut: bannerOuts) {
                if(bannerOut.getThemeId()!= null && !"".equals(bannerOut.getThemeId())){
                    bannerOut.setIsCampaign(0);
                }else{
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
     * @return
     */
    @Override
    public List<GoodsOut> getSelectionList() {
        GoodsPropertyPOJO selection = goodsPropertyMapper.selectValueByKey(Constant.SELECTION_KEY);
        if(selection != null){
            //分割商品编号
            List<SelectionPOJO> selectionList = new ArrayList<>();
            List<GoodsOut> goodsOuts = new ArrayList<>();
            selectionList = strToList(selection.getValue());
            if(selectionList != null) {
                for (SelectionPOJO sp : selectionList) {
                    GoodsBasePOJO goods = goodsBaseMapper.findGoodsById(sp.getGoodsId());
                    if(goods != null) {
                        goods.setGoodsImg(sp.getImg());
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
     * @return
     */
    @Override
    public List<SpecialOut> getSpecialList() {
        GoodsPropertyPOJO banner = goodsPropertyMapper.selectValueByKey(Constant.THEME_KEY);
        if(banner != null){
            //分割商品编号
            Long [] themeIds = splitGoodsContent(banner.getValue());
            List<SpecialOut> specialList = new ArrayList<>();
            if(themeIds != null && themeIds.length > 0) {
                for (int i = 0; i < themeIds.length; i++) {
                    ThemePOJO theme = goodsThemeMapper.selectThemeById(themeIds[i]);
                    if(theme != null) {
                        //获取专题下的商品
                        Long[] goodsIds = splitGoodsContent(theme.getThemeInfoContent());
                        if(goodsIds != null && goodsIds.length > 0) {
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
    public List<GoodsOut> getGoodsSortList() {
        GoodsPropertyPOJO goodsProperty = goodsPropertyMapper.selectValueByKey(Constant.GOODS_KEY);
        if(goodsProperty != null){
            Long[] goodsIds = splitGoodsContent(goodsProperty.getValue());
            if(goodsIds != null && goodsIds.length > 0){
               return getGoodsList(goodsIds);
            }
        }
        return null;
    }

    /**
     * 查询更多推荐商品
     * @return
     */
    @Override
    public List<GoodsOut> getGoodsList(Integer page,Integer pageSize) {
        if(page == null || pageSize == 0 || pageSize == null || pageSize == 0)
            return null;
        List<GoodsBasePOJO> goodsList = goodsBaseMapper.selectGoodsList(page, pageSize);
        if(goodsList != null)
            return goodsListConverted(goodsList);
        return null;
    }

    /**
     * 根据类目编号查询商品信息
     * @param categoryKey 类目编号
     * @return
     */
    @Override
    public List<GoodsOut> getCategory(Integer categoryKey) {
        List<GoodsBasePOJO> goodsList = goodsBaseMapper.selectCatagory2Goods(categoryKey);
        if(goodsList != null) {
            return goodsListConverted(goodsList);
        }
        return null;
    }

    /**
     * 根据类目编号查询类目图
     * @param categoryKey
     * @return
     */
    @Override
    public String getCategoryImg(Integer categoryKey) {
        GoodsPropertyPOJO goodsProperty = goodsPropertyMapper.selectValueByKey(Constant.TYPE_IMG);
        if(goodsProperty != null){
            TypeImagePOJO typeImg = new TypeImagePOJO();
            typeImg = WXUtil.strToJson(goodsProperty.getValue(),typeImg);
            return typeImg.getImg();
        }
        return null;
    }

    /**
     * 根据专题编号查询专题详情
     * @param specialId
     * @return
     */
    @Override
    public SpecialOut getSpecialDetails(Long specialId) {
        ThemePOJO theme = goodsThemeMapper.selectThemeById(specialId);
        //查询推荐商品
        List<GoodsBasePOJO> recommendGoods = goodsBaseMapper.findRecommendGoods();
        List<GoodsOut> goodsOuts = goodsListConverted(recommendGoods);
        theme.setGoodsList(goodsOuts);
        return  getSpecial(theme);
    }

    /**
     * 根据商品编号查询商品详情
     * @param goodsId 商品编号
     * @return
     */
    @Override
    public Map<String,Object> getGoodsDetails(Long goodsId) {
        Map<String,Object> map = Maps.newHashMap();
        GoodsBasePOJO goodsBase = goodsBaseMapper.findGoodsById(goodsId);
        GoodsOut goodsOut = generateGoodsDetails(goodsBase);
        GoodsStandard goodsStandard = goodsBaseMapper.selectGoodsStandard(goodsId);
        List<PropertyValue> goodsStandardFields = getGoodsStandardFields(goodsStandard);
        if(goodsOut == null || goodsStandardFields == null) {
            return null;
        }else {
            map.put("goods", goodsOut);
            map.put("specification", goodsStandardFields);
            return map;
        }
    }

    @Override
    public Map<String, Object> findGoodsDetails(Long goodsId) {
        Map<String,Object> map = Maps.newHashMap();
        GoodsBasePOJO goodsBase = goodsBaseMapper.findGoodsById(goodsId);
        GoodsOut goodsOut = generateGoodsDetails(goodsBase);
        GoodsStandard goodsStandard = goodsBaseMapper.selectGoodsStandard(goodsId);
        List<PropertyValue> goodsStandardList = getGoodsStandardList(goodsStandard);
        if(goodsOut == null || goodsStandardList == null) {
            return null;
        }else {
            map.put("goods", goodsOut);
            map.put("specification", goodsStandardList);
        }
        return map;
    }

    /**
     * 获取商品
     * @param goodsStandard 商品属性表
     * @return
     */
    private List<PropertyValue> getGoodsStandardList(GoodsStandard goodsStandard) {
        if(goodsStandard == null)
            return null;
        Field[] fields = goodsStandard.getClass().getDeclaredFields();
        List<PropertyValue> propertyList = new ArrayList<>();
        //查询商品属性列表
        GoodsPropertyPOJO goodsProperty = goodsPropertyMapper.selectValueByKey(Constant.GOODS_STANDARD);
        List<PropertyValue> propertyAllList =  goodsStandardStrToList(goodsProperty.getValue());
        for(int i = 0;i < propertyAllList.size();i++){
            for(int j = 0; j < fields.length;j++) {
                if (propertyAllList.get(i).getKey().equals(fields[j].getName())) {
                    Object value = getFieldValueByName(fields[j].getName(), goodsStandard);
                    if (value != null&& !"".equals(value)) {
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
     * 获取goodsStandard对象属性信息并生成map
     * @param goodsStandard 商品属性对象
     */
    private List<PropertyValue> getGoodsStandardFields(GoodsStandard goodsStandard) {
        if(goodsStandard == null)
            return null;
        Field[] fields = goodsStandard.getClass().getDeclaredFields();
        List<PropertyValue> propertyList = new ArrayList<>();
        for(int i = 0;i < fields.length;i++){
            Object value = getFieldValueByName(fields[i].getName(),goodsStandard);
            if(value != null) {
                PropertyValue property = new PropertyValue();
                property.setKey(fields[i].getName());
                property.setName(value);
                propertyList.add(property);
            }
        }
        return propertyList;
    }

    /**
     * 根据属性名获取属性值
     * */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    /**
     * 生成商品详情出参
     * @param goodsBase
     */
    private GoodsOut generateGoodsDetails(GoodsBasePOJO goodsBase) {
        if(goodsBase == null) return null;
        //查询商品图片
        GoodsImage goodsImage = goodsBaseMapper.selectGoodsImgByGoodsId(goodsBase.getId());
        if(goodsImage != null) {
            GoodsOut goods = getGoods(goodsBase);
            if(goods != null) {
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
     * @param goodsList
     * @return
     */
    private List<GoodsOut> goodsListConverted(List<GoodsBasePOJO> goodsList) {
        if(goodsList != null){
            List<GoodsOut> goodsOuts = new ArrayList<>();
            for (GoodsBasePOJO goods: goodsList) {
                GoodsOut goodsOut = getGoods(goods);
                goodsOuts.add(goodsOut);
            }
            return goodsOuts;
        }
        return null;
    }


    /**
     * 专题POJO转出参
     * @param themeList
     * @return
     */
    private List<SpecialOut> getSpecialList(List<ThemePOJO> themeList) {
        if(themeList == null)
            return null;
        List<SpecialOut> specialList = new ArrayList<>();
        for (ThemePOJO theme: themeList) {
            SpecialOut special = getSpecial(theme);
            specialList.add(special);
        }
        return specialList;
    }

    /**
     * 专题转出参
     * @param theme
     * @return
     */
    private SpecialOut getSpecial(ThemePOJO theme) {
        SpecialOut special = new SpecialOut();
        special.setSpecialId(theme.getThemeId());
        special.setSpecialDesc(theme.getThemeDes());
        special.setSpecialInfoImg(theme.getThemeImg());
        special.setGoodsList(theme.getGoodsList());
        return special;
    }

    /**
     * 商品POJO转出参
     * @param goods
     * @return
     */
    private GoodsOut getGoods(GoodsBasePOJO goods) {
        GoodsOut goodsOut = new GoodsOut();
        goodsOut.setGoodsId(goods.getId());
        goodsOut.setGoodsName(goods.getTitle());
        goodsOut.setGoodsDesc(goods.getSubTitle());
        goodsOut.setGoodsImg(goods.getGoodsImg());
        goodsOut.setShopMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
        goodsOut.setMarketMoney(MoneyFormat.priceFormatString(goods.getOriginPrice()));
        return goodsOut;
    }

    /**
     * 根据商品列表
     * @param ids
     * @return
     */
    private List<GoodsOut> getGoodsList(Long [] ids){
        if(ids == null)
            return null;
        List<GoodsOut> goodsList = new ArrayList<>();
        for (Long id: ids) {
            //根据商品编号查询商品
            GoodsBasePOJO goods = goodsBaseMapper.findGoodsById(id);
            if(goods != null) {
                GoodsOut goodsOut = getGoods(goods);
                goodsList.add(goodsOut);
            }
        }
        return goodsList;
    }

    /**
     * 分割商品编号
     * @param content
     * @return
     */
    private Long[] splitGoodsContent(String content) {
        if(content == null || "".equals(content))
            return null;
        Gson gson = new Gson();
        return gson.fromJson(content,Long [].class);
    }

    /**
     * 图片字符串转集合
     * @return
     */
    private List<String> ImgStrToList(String str){
        if(str == null || "".equals(str))
            return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(str,type);
    }

    /**
     * 商品属性Str转List
     * @param value
     * @return
     */
    private List<PropertyValue> goodsStandardStrToList(String value) {
        if(value == null || "".equals(value))
            return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<PropertyValue>>() {}.getType();
        return gson.fromJson(value, type);
    }

    /**
     * Json字符串转List
     * @param str json字符串
     * @return-
     */
    private List<BannerOut> strJsonToList(String str){
        if(str == null || "".equals(str))
            return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<BannerOut>>() {}.getType();
        return gson.fromJson(str, type);
    }

    /**
     * 专题Json字符串转集合
     * @param str
     * @return
     */
    public List<SelectionPOJO> strToList(String str){
        if(str == null || "".equals(str)){
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<SelectionPOJO>>() {}.getType();
        return gson.fromJson(str,type);
    }

}

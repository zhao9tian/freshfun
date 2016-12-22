package com.quxin.freshfun.common;

/**
 * Created by tianmingzhao on 16/9/27.
 */
public class Constant {
    /**
     * 商户订单分成
     */
    public static final Double AGENT_COMPONENT = 0.2;

    public static final Double FECTHER_COMPONENT = 0.1;


    /**
     * banner对应商品属性中的key
     */
    public static final String BANNER_KEY = "bannerSort";
    /**
     * 精选对应商品属性中key
     */
    public static final String SELECTION_KEY = "selectionSort";
    /**
     * 首页20个商品的排序
     */
    public static final String GOODS_KEY = "goodsSort";
    /**
     * 专题对应商品属性表中的key
     */
    public static final String THEME_KEY = "themeSort";
    /**
     * 分类顶图对应goodsProperty中的key
     */
    public static final String TYPE_IMG = "typeImg";
    /**
     * 商品属性对应goodsProperty中的key
     */
    public static final String GOODS_STANDARD = "goodsStandard";

    /**
     * 限量购排序
     */
    public static final String LIMITED_GOODS = "limitedGoodsSort";

    /** 限时购 */
    public static final Integer OBJECT_TYPE_TIME_LIMIT = 1;
    /** 优惠券 */
    public static final Integer OBJECT_TYPE_COUPON = 2;

    /**
     * 普通订单
     */
    public static final Integer ORDER_COMMON = 1;
    /**
     * 限量购订单
     */
    public static final Integer ORDER_LIMITED = 2;
}

package com.quxin.freshfun.model;

/**
 * Created by admin on 2016-09-22.
 */
public class QuanMingPayInfo {
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 商品编号
     */
    private Integer goodsId;
    /**
     * 微信公众号唯一编号
     */
    private String openId;
    /**
     * 微信编码
     */
    private String code;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

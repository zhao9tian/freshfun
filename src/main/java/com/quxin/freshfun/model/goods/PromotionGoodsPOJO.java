package com.quxin.freshfun.model.goods;

import org.apache.xpath.operations.Bool;

/**
 * Created by tianmingzhao on 16/11/3.
 */
public class PromotionGoodsPOJO {

    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 折扣价
     */
    private Long discountPrice;
    /**
     * 优惠开始时间
     */
    private Long startTime;
    /**
     * 优惠结束时间
     */
    private Long endTime;
    /**
     * 是否打折中
     */
    private Boolean isDiscount;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Boolean getDiscount() {
        return isDiscount;
    }

    public void setDiscount(Boolean discount) {
        isDiscount = discount;
    }
}

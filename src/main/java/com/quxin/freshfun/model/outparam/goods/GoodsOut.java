package com.quxin.freshfun.model.outparam.goods;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by qingtian on 2016/10/26.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoodsOut {
    /**
     * 商品编号
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品描述
     */
    private String goodsDesc;
    /**
     * 商品图
     */
    private String goodsImg;
    /**
     * 实际销售价格
     */
    private String shopMoney;
    /**
     * 参考价格
     */
    private String marketMoney;
    /**
     * 悦选小编说
     */
    private String descriptionStr;
    /**
     * banner图
     */
    private List<String> bannerImgList;
    /**
     * 商品详情图
     */
    private List<String> detailImgList;
    /**
     * 距离开始时间
     */
    private Long startTime;
    /**
     * 距离结束时间
     */
    private Long endTime;
    /**
     * 是否打折中
     */
    private Integer isDiscount;

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

    public Integer getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(Integer isDiscount) {
        this.isDiscount = isDiscount;
    }

    public String getDescriptionStr() {
        return descriptionStr;
    }

    public void setDescriptionStr(String descriptionStr) {
        this.descriptionStr = descriptionStr;
    }

    public List<String> getBannerImgList() {
        return bannerImgList;
    }

    public void setBannerImgList(List<String> bannerImgList) {
        this.bannerImgList = bannerImgList;
    }

    public List<String> getDetailImgList() {
        return detailImgList;
    }

    public void setDetailImgList(List<String> detailImgList) {
        this.detailImgList = detailImgList;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getShopMoney() {
        return shopMoney;
    }

    public void setShopMoney(String shopMoney) {
        this.shopMoney = shopMoney;
    }

    public String getMarketMoney() {
        return marketMoney;
    }

    public void setMarketMoney(String marketMoney) {
        this.marketMoney = marketMoney;
    }
}

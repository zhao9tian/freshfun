package com.quxin.freshfun.model.goods;

/**
 * Created by qucheng on 2016/10/12.
 */
public class GoodsInfoPOJO {

    /**商品Id*/
    private Integer goodsId;
    /**商品名称*/
    private String goodsName;
    /**商品描述*/
    private String goodsDes;
    /**悦选小编说*/
    private String ffunerSaid;
    /**商品实际售价*/
    private String actualMoney;
    /**商品标价*/
    private String originMoney;
    /**规格图*/
    private String standardImgPath;
    /**详情图*/
    private String detailImgPath;
    /**轮播图*/
    private String carouselImgPath;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDes() {
        return goodsDes;
    }

    public void setGoodsDes(String goodsDes) {
        this.goodsDes = goodsDes;
    }

    public String getFfunerSaid() {
        return ffunerSaid;
    }

    public void setFfunerSaid(String ffunerSaid) {
        this.ffunerSaid = ffunerSaid;
    }

    public String getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(String actualMoney) {
        this.actualMoney = actualMoney;
    }

    public String getOriginMoney() {
        return originMoney;
    }

    public void setOriginMoney(String originMoney) {
        this.originMoney = originMoney;
    }

    public String getStandardImgPath() {
        return standardImgPath;
    }

    public void setStandardImgPath(String standardImgPath) {
        this.standardImgPath = standardImgPath;
    }

    public String getDetailImgPath() {
        return detailImgPath;
    }

    public void setDetailImgPath(String detailImgPath) {
        this.detailImgPath = detailImgPath;
    }

    public String getCarouselImgPath() {
        return carouselImgPath;
    }

    public void setCarouselImgPath(String carouselImgPath) {
        this.carouselImgPath = carouselImgPath;
    }

    @Override
    public String toString() {
        return "GoodsInfoPOJO{" +
                "goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsDes='" + goodsDes + '\'' +
                ", ffunerSaid='" + ffunerSaid + '\'' +
                ", actualMoney='" + actualMoney + '\'' +
                ", originMoney='" + originMoney + '\'' +
                ", standardImgPath='" + standardImgPath + '\'' +
                ", detailImgPath='" + detailImgPath + '\'' +
                ", carouselImgPath='" + carouselImgPath + '\'' +
                '}';
    }
}

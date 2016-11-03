package com.quxin.freshfun.model.pojo.goods;

/**
 * Created by gsix on 2016/10/26.
 */
public class GoodsImage {
    private Long id;
    /**
     * 商品编号
     */
    private Long goodsId;
    /**
     * 商品详情图
     */
    private String detailImg;
    /**
     * 轮播图
     */
    private String carouselImg;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getDetailImg() {
        return detailImg;
    }

    public void setDetailImg(String detailImg) {
        this.detailImg = detailImg;
    }

    public String getCarouselImg() {
        return carouselImg;
    }

    public void setCarouselImg(String carouselImg) {
        this.carouselImg = carouselImg;
    }
}

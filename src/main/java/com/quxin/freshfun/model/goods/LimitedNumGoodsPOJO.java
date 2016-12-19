package com.quxin.freshfun.model.goods;

/**
 * 限量购商品实体
 * Created by qucheng on 16/12/16.
 */
public class LimitedNumGoodsPOJO {

    private Long limitedGoodsId ;

    private String limitedGoodsPrice ;

    private Integer limitedGoodsStock ;

    private Integer limitedRealStock;

    public Long getLimitedGoodsId() {
        return limitedGoodsId;
    }

    public void setLimitedGoodsId(Long limitedGoodsId) {
        this.limitedGoodsId = limitedGoodsId;
    }

    public String getLimitedGoodsPrice() {
        return limitedGoodsPrice;
    }

    public void setLimitedGoodsPrice(String limitedGoodsPrice) {
        this.limitedGoodsPrice = limitedGoodsPrice;
    }

    public Integer getLimitedGoodsStock() {
        return limitedGoodsStock;
    }

    public void setLimitedGoodsStock(Integer limitedGoodsStock) {
        this.limitedGoodsStock = limitedGoodsStock;
    }

    public Integer getLimitedRealStock() {
        return limitedRealStock;
    }

    public void setLimitedRealStock(Integer limitedRealStock) {
        this.limitedRealStock = limitedRealStock;
    }
}

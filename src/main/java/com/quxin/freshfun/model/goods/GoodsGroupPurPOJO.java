package com.quxin.freshfun.model.goods;

/**
 * 拼团商品实体类
 * Created by qucheng on 17/1/18.
 */
public class GoodsGroupPurPOJO {

    /**
     * 商品id
     */
    private Long goodsId ;

    /**
     * 第一次购买价格
     */
    private Long firPrice ;

    /**
     * 第二次购买价格
     */
    private Long secPrice ;

    /**
     * 第三次购买价格
     */
    private Long thiPrice ;


    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getFirPrice() {
        return firPrice;
    }

    public void setFirPrice(Long firPrice) {
        this.firPrice = firPrice;
    }

    public Long getSecPrice() {
        return secPrice;
    }

    public void setSecPrice(Long secPrice) {
        this.secPrice = secPrice;
    }

    public Long getThiPrice() {
        return thiPrice;
    }

    public void setThiPrice(Long thiPrice) {
        this.thiPrice = thiPrice;
    }

    @Override
    public String toString() {
        return "GoodsGroupPurPOJO{" +
                "goodsId=" + goodsId +
                ", firPrice=" + firPrice +
                ", secPrice=" + secPrice +
                ", thiPrice=" + thiPrice +
                '}';
    }
}

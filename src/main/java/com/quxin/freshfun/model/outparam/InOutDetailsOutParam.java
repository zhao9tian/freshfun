package com.quxin.freshfun.model.outparam;

/**
 * 明细出参
 * Created by qucheng on 2016/10/1.
 */
public class InOutDetailsOutParam {
    private String goodsName;
    private Long time;
    private String price ;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "InOutDetailsOutParam{" +
                "goodsName='" + goodsName + '\'' +
                ", time=" + time +
                ", price='" + price + '\'' +
                '}';
    }
}

package com.quxin.freshfun.model.pojo;

import java.util.List;

/**
 * 收入支出记录实体
 * Created by qucheng on 2016/9/30.
 */
public class InOutDetailsPOJO {
    private String goodsName;
    private Long time;
    private Integer price ;

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "InOutDetailsPOJO{" +
                "goodsName='" + goodsName + '\'' +
                ", time=" + time +
                ", price=" + price +
                '}';
    }
}

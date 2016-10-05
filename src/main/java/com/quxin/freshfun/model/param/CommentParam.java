package com.quxin.freshfun.model.param;

import com.quxin.freshfun.model.entity.BaseEntity;

/**
 * 评论入参
 * Created by qucheng on 2016/10/4.
 */
public class CommentParam extends BaseEntity{
    /**订单Id*/
    private String orderId;
    /**用户Id*/
    private String userId;
    /**商品Id*/
    private String goodsId;
    /**评论内容*/
    private String content;
    /**综合评价*/
    private Integer generalLevel;
    /**口感评价*/
    private Integer tasteLevel;
    /**包装评价*/
    private Integer packLevel;
    /**物流评价*/
    private Integer logisticsLevel;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getGeneralLevel() {
        return generalLevel;
    }

    public void setGeneralLevel(Integer generalLevel) {
        this.generalLevel = generalLevel;
    }

    public Integer getTasteLevel() {
        return tasteLevel;
    }

    public void setTasteLevel(Integer tasteLevel) {
        this.tasteLevel = tasteLevel;
    }

    public Integer getPackLevel() {
        return packLevel;
    }

    public void setPackLevel(Integer packLevel) {
        this.packLevel = packLevel;
    }

    public Integer getLogisticsLevel() {
        return logisticsLevel;
    }

    public void setLogisticsLevel(Integer logisticsLevel) {
        this.logisticsLevel = logisticsLevel;
    }
}

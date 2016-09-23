package com.quxin.freshfun.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GoodsDeliveryTimesPOJO {
    private Integer id;

    private Long gmtCreate;

    private Long gmtModified;

    private Integer goodsId;

    private Integer typeId;

    private Integer deliveryTimes;

    private String resevedField;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getDeliveryTimes() {
        return deliveryTimes;
    }

    public void setDeliveryTimes(Integer deliveryTimes) {
        this.deliveryTimes = deliveryTimes;
    }

    public String getResevedField() {
        return resevedField;
    }

    public void setResevedField(String resevedField) {
        this.resevedField = resevedField;
    }
}
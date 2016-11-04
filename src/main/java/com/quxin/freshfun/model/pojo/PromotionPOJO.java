package com.quxin.freshfun.model.pojo;

import com.quxin.freshfun.model.entity.BaseEntity;

/**
 * Created by tianmingzhao on 16/9/29.
 */
public class PromotionPOJO extends BaseEntity {
    private Integer id;
    /** 'type=1 为商品id' */
    private Long objectId;
    /** '1:限时促销 2:优惠券' */
    private Integer objectType;
    /** '优惠计算方式 限时促销为{"discountPrice":100}' */
    private String content;
    /** 开始时间 */
    private Long startTime;
    /** 结束时间 */
    private Long endTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Integer getObjectType() {
        return objectType;
    }

    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}

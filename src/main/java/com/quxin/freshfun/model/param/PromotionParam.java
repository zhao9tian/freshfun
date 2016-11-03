package com.quxin.freshfun.model.param;

import com.quxin.freshfun.model.entity.BaseEntity;

/**
 * Created by tianmingzhao on 16/9/29.
 */
public class PromotionParam extends BaseEntity {

    private Long objectId;
    private Integer objectType;
    private String content;
    private Long startTime;
    private Long endTime;

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

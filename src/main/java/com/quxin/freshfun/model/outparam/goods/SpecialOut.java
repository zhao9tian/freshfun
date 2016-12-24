package com.quxin.freshfun.model.outparam.goods;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by qingtian on 2016/10/26.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpecialOut {
    /**
     * 专题编号
     */
    private Long specialId;
    /**
     * 主题名
     */
    private String specialName;
    /**
     * 专题描述
     */
    private String specialDesc;
    /**
     * 专题图片
     */
    private String specialInfoImg;
    /**
     * 商品集合
     */
    private List<GoodsOut> goodsList;

    public String getSpecialName() {
        return specialName;
    }

    public void setSpecialName(String specialName) {
        this.specialName = specialName;
    }

    public String getSpecialDesc() {
        return specialDesc;
    }

    public void setSpecialDesc(String specialDesc) {
        this.specialDesc = specialDesc;
    }

    public List<GoodsOut> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsOut> goodsList) {
        this.goodsList = goodsList;
    }

    public Long getSpecialId() {
        return specialId;
    }

    public void setSpecialId(Long specialId) {
        this.specialId = specialId;
    }

    public String getSpecialInfoImg() {
        return specialInfoImg;
    }

    public void setSpecialInfoImg(String specialInfoImg) {
        this.specialInfoImg = specialInfoImg;
    }
}

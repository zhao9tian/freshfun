package com.quxin.freshfun.model.pojo.goods;

import com.quxin.freshfun.model.outparam.goods.GoodsOut;

import java.util.List;

/**
 * 专题实体
 * Created by qucheng on 2016/10/19.
 */
public class ThemePOJO {

    /**
     * 专题Id
     */
    private Integer themeId ;
    /**
     * 专题描述
     */
    private String themeDes ;
    /**
     * 专题图片
     */
    private String themeImg ;
    /**
     * 专题内容
     */
    private String themeInfoContent;
    /**
     * 生成时间
     */
    private Long created;
    /**
     * 修改时间
     */
    private Long updated;

    /**
     * 商品集合
     */
    private List<GoodsOut> goodsList;

    public List<GoodsOut> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsOut> goodsList) {
        this.goodsList = goodsList;
    }

    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    public String getThemeDes() {
        return themeDes;
    }

    public void setThemeDes(String themeDes) {
        this.themeDes = themeDes;
    }

    public String getThemeImg() {
        return themeImg;
    }

    public void setThemeImg(String themeImg) {
        this.themeImg = themeImg;
    }

    public String getThemeInfoContent() {
        return themeInfoContent;
    }

    public void setThemeInfoContent(String themeInfoContent) {
        this.themeInfoContent = themeInfoContent;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

}

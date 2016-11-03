package com.quxin.freshfun.model.outparam.goods;

/**
 * Created by qingtian on 2016/10/27.
 */
public class BannerOut {

    private Long themeId;
    /**
     * banner图
     */
    private String img;
    /**
     * 客户端跳转地址
     */
    private String url;
    /**
     * 是否为活动页banner
     */
    private Integer isCampaign;

    public Integer getIsCampaign() {
        return isCampaign;
    }

    public void setIsCampaign(Integer isCampaign) {
        this.isCampaign = isCampaign;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

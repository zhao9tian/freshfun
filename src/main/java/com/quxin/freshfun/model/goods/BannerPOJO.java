package com.quxin.freshfun.model.goods;

/**
 * banner实体
 * Created by qucheng on 2016/10/19.
 */
public class BannerPOJO {
    /**
     * 轮播Id
     */
    private Integer bannerId ;
    /**
     * 轮播描述
     */
    private String bannerDes;
    /**
     * 轮播图片
     */
    private String bannerImg;
    /**
     * 悦选小编说
     */
    private String mallInfoContent;
    /**
     * 生成时间
     */
    private Long created;
    /**
     * 最后的编辑时间
     */
    private Long updated;

    public Integer getBannerId() {
        return bannerId;
    }

    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerDes() {
        return bannerDes;
    }

    public void setBannerDes(String bannerDes) {
        this.bannerDes = bannerDes;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getMallInfoContent() {
        return mallInfoContent;
    }

    public void setMallInfoContent(String mallInfoContent) {
        this.mallInfoContent = mallInfoContent;
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

    @Override
    public String toString() {
        return "BannerPOJO{" +
                "bannerId=" + bannerId +
                ", bannerDes='" + bannerDes + '\'' +
                ", bannerImg='" + bannerImg + '\'' +
                ", mallInfoContent='" + mallInfoContent + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}

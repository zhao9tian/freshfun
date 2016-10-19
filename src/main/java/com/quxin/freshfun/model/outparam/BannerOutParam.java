package com.quxin.freshfun.model.outparam;

/**
 * bannerId 出参
 * Created by qucheng on 2016/10/19.
 */
public class BannerOutParam {

    private Integer bannerId;
    private String mallDes;
    private String mallInfoContent;
    private String mallInfoImg;

    public Integer getBannerId() {
        return bannerId;
    }

    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    public String getMallDes() {
        return mallDes;
    }

    public void setMallDes(String mallDes) {
        this.mallDes = mallDes;
    }

    public String getMallInfoContent() {
        return mallInfoContent;
    }

    public void setMallInfoContent(String mallInfoContent) {
        this.mallInfoContent = mallInfoContent;
    }

    public String getMallInfoImg() {
        return mallInfoImg;
    }

    public void setMallInfoImg(String mallInfoImg) {
        this.mallInfoImg = mallInfoImg;
    }
}

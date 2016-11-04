package com.quxin.freshfun.model.pojo.goods;

/**
 * Created by qingtian on 2016/10/26.
 */
public class GoodsBasePOJO {
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 副标题
     */
    private String subTitle;
    /**
     * 商品描述
     */
    private String goodsDes;
    /**
     * 商品售价
     */
    private Integer shopPrice;
    /**
     * 商品原价
     */
    private Integer originPrice;
    /**
     * 商品图片
     */
    private String goodsImg;
    /**
     * 销量
     */
    private Integer saleNum;
    /**
     * 库存
     */
    private Integer stockNum;
    /**
     * 类目1
     */
    private Integer category1;

    private Integer category2;

    private Integer category3;

    private Integer category4;
    /**
     * 商户编号
     */
    private Long shopId;
    /**
     * 微信公众号Id
     */
    private Long appId;

    private Long created;

    private Long updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getGoodsDes() {
        return goodsDes;
    }

    public void setGoodsDes(String goodsDes) {
        this.goodsDes = goodsDes;
    }

    public Integer getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(Integer shopPrice) {
        this.shopPrice = shopPrice;
    }

    public Integer getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Integer originPrice) {
        this.originPrice = originPrice;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    public Integer getCategory1() {
        return category1;
    }

    public void setCategory1(Integer category1) {
        this.category1 = category1;
    }

    public Integer getCategory2() {
        return category2;
    }

    public void setCategory2(Integer category2) {
        this.category2 = category2;
    }

    public Integer getCategory3() {
        return category3;
    }

    public void setCategory3(Integer category3) {
        this.category3 = category3;
    }

    public Integer getCategory4() {
        return category4;
    }

    public void setCategory4(Integer category4) {
        this.category4 = category4;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
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

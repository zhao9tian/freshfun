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
    private Integer catagory1;

    private Integer catagory2;

    private Integer catagory3;

    private Integer catagory4;
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

    public Integer getCatagory1() {
        return catagory1;
    }

    public void setCatagory1(Integer catagory1) {
        this.catagory1 = catagory1;
    }

    public Integer getCatagory2() {
        return catagory2;
    }

    public void setCatagory2(Integer catagory2) {
        this.catagory2 = catagory2;
    }

    public Integer getCatagory3() {
        return catagory3;
    }

    public void setCatagory3(Integer catagory3) {
        this.catagory3 = catagory3;
    }

    public Integer getCatagory4() {
        return catagory4;
    }

    public void setCatagory4(Integer catagory4) {
        this.catagory4 = catagory4;
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

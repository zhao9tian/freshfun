package com.quxin.freshfun.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GoodsPOJO {
    private Integer id;

    private Long gmtCreate;

    private Long gmtModified;

    private String goodsName;

    private String goodsDes;

    private String goodsImg;

    private String goodsType;

    private Integer shopPrice;

    private Integer marketPrice;

    private Long storeId;

    private Integer goodsWeight;

    private Integer goodsStorage;

    private Byte warnning;

    private Byte isOnSale;

    private Byte isNew;

    private Byte isHot;

    private Byte isPromote;

    private Long merchantProxyId;

    private String goodsMoney;

    private String marketMoney;

    private String shareUrl;

    private GoodsSelectionPOJO goodsSelection;

    private Integer agencyFees;

    private Integer sales;

    private Double SalesMoney;

    private String goodsDetailImg;

    private String reservedField;

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDes() {
        return goodsDes;
    }

    public void setGoodsDes(String goodsDes) {
        this.goodsDes = goodsDes;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(Integer shopPrice) {
        this.shopPrice = shopPrice;
    }

    public Integer getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Integer marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Integer getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(Integer goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public Integer getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(Integer goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public Byte getWarnning() {
        return warnning;
    }

    public void setWarnning(Byte warnning) {
        this.warnning = warnning;
    }

    public Byte getIsOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(Byte isOnSale) {
        this.isOnSale = isOnSale;
    }

    public Byte getIsNew() {
        return isNew;
    }

    public void setIsNew(Byte isNew) {
        this.isNew = isNew;
    }

    public Byte getIsHot() {
        return isHot;
    }

    public void setIsHot(Byte isHot) {
        this.isHot = isHot;
    }

    public Byte getIsPromote() {
        return isPromote;
    }

    public void setIsPromote(Byte isPromote) {
        this.isPromote = isPromote;
    }

    public Long getMerchantProxyId() {
        return merchantProxyId;
    }

    public void setMerchantProxyId(Long merchantProxyId) {
        this.merchantProxyId = merchantProxyId;
    }

    public String getGoodsMoney() {
        return goodsMoney;
    }

    public void setGoodsMoney(String goodsMoney) {
        this.goodsMoney = goodsMoney;
    }

    public String getMarketMoney() {
        return marketMoney;
    }

    public void setMarketMoney(String marketMoney) {
        this.marketMoney = marketMoney;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public GoodsSelectionPOJO getGoodsSelection() {
        return goodsSelection;
    }

    public void setGoodsSelection(GoodsSelectionPOJO goodsSelection) {
        this.goodsSelection = goodsSelection;
    }

    public Integer getAgencyFees() {
        return agencyFees;
    }

    public void setAgencyFees(Integer agencyFees) {
        this.agencyFees = agencyFees;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Double getSalesMoney() {
        return SalesMoney;
    }

    public void setSalesMoney(Double salesMoney) {
        SalesMoney = salesMoney;
    }

    public String getGoodsDetailImg() {
        return goodsDetailImg;
    }

    public void setGoodsDetailImg(String goodsDetailImg) {
        this.goodsDetailImg = goodsDetailImg;
    }

    public String getReservedField() {
        return reservedField;
    }

    public void setReservedField(String reservedField) {
        this.reservedField = reservedField;
    }

    @Override
    public String toString() {
        return "GoodsPOJO{" +
                "id=" + id +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", goodsName='" + goodsName + '\'' +
                ", goodsDes='" + goodsDes + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                ", goodsType='" + goodsType + '\'' +
                ", shopPrice=" + shopPrice +
                ", marketPrice=" + marketPrice +
                ", storeId=" + storeId +
                ", goodsWeight=" + goodsWeight +
                ", goodsStorage=" + goodsStorage +
                ", warnning=" + warnning +
                ", isOnSale=" + isOnSale +
                ", isNew=" + isNew +
                ", isHot=" + isHot +
                ", isPromote=" + isPromote +
                ", merchantProxyId=" + merchantProxyId +
                ", goodsMoney='" + goodsMoney + '\'' +
                ", marketMoney='" + marketMoney + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", goodsSelection=" + goodsSelection +
                ", agencyFees=" + agencyFees +
                ", sales=" + sales +
                ", SalesMoney=" + SalesMoney +
                ", goodsDetailImg='" + goodsDetailImg + '\'' +
                ", reservedField='" + reservedField + '\'' +
                '}';
    }
}
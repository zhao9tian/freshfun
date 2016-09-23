package com.quxin.freshfun.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GoodsTypePOJO {
    private Integer id;

    private Long gmtCreate;

    private Long gmtModified;

    private String goodsType;

    private String goodsTypeImg;

    private Byte isDeleted;

    private String reservedField;
    
    private List<GtypeVsGid> typeGids;
    
    private String goodsInfoDes;

	private String goodsInfoImg;

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

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsTypeImg() {
        return goodsTypeImg;
    }

    public void setGoodsTypeImg(String goodsTypeImg) {
        this.goodsTypeImg = goodsTypeImg;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getReservedField() {
        return reservedField;
    }

    public void setReservedField(String reservedField) {
        this.reservedField = reservedField;
    }

    public List<GtypeVsGid> getTypeGids() {
        return typeGids;
    }

    public void setTypeGids(List<GtypeVsGid> typeGids) {
        this.typeGids = typeGids;
    }

    public String getGoodsInfoDes() {
        return goodsInfoDes;
    }

    public void setGoodsInfoDes(String goodsInfoDes) {
        this.goodsInfoDes = goodsInfoDes;
    }

    public String getGoodsInfoImg() {
        return goodsInfoImg;
    }

    public void setGoodsInfoImg(String goodsInfoImg) {
        this.goodsInfoImg = goodsInfoImg;
    }
}
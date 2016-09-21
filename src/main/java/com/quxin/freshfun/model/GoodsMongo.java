package com.quxin.freshfun.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GoodsMongo {
	
	private String id;
	private Integer goodsId;
	private String des;
	
	private Integer goodsUnit;
	private String goodsBrand;
	private String goodsSize;
	private String goodsPlace;
	private String storageMethod;
	private Long goodsBirthdate ;
	private Long goodsOutdate ;
	private String goodsDelivery;
	private String goodsService;
	
	private String standardImgPath;
	private String detailImgPath;
	private String carouselImgPath;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public Integer getGoodsUnit() {
		return goodsUnit;
	}
	public void setGoodsUnit(Integer goodsUnit) {
		this.goodsUnit = goodsUnit;
	}
	public String getGoodsBrand() {
		return goodsBrand;
	}
	public void setGoodsBrand(String goodsBrand) {
		this.goodsBrand = goodsBrand;
	}
	public String getGoodsSize() {
		return goodsSize;
	}
	public void setGoodsSize(String goodsSize) {
		this.goodsSize = goodsSize;
	}
	public String getGoodsPlace() {
		return goodsPlace;
	}
	public void setGoodsPlace(String goodsPlace) {
		this.goodsPlace = goodsPlace;
	}
	public String getStorageMethod() {
		return storageMethod;
	}
	public void setStorageMethod(String storageMethod) {
		this.storageMethod = storageMethod;
	}
	public Long getGoodsBirthdate() {
		return goodsBirthdate;
	}
	public void setGoodsBirthdate(Long goodsBirthdate) {
		this.goodsBirthdate = goodsBirthdate;
	}
	public Long getGoodsOutdate() {
		return goodsOutdate;
	}
	public void setGoodsOutdate(Long goodsOutdate) {
		this.goodsOutdate = goodsOutdate;
	}
	public String getGoodsDelivery() {
		return goodsDelivery;
	}
	public void setGoodsDelivery(String goodsDelivery) {
		this.goodsDelivery = goodsDelivery;
	}
	public String getGoodsService() {
		return goodsService;
	}
	public void setGoodsService(String goodsService) {
		this.goodsService = goodsService;
	}
	public String getStandardImgPath() {
		return standardImgPath;
	}
	public void setStandardImgPath(String standardImgPath) {
		this.standardImgPath = standardImgPath;
	}
	public String getDetailImgPath() {
		return detailImgPath;
	}
	public void setDetailImgPath(String detailImgPath) {
		this.detailImgPath = detailImgPath;
	}
	public String getCarouselImgPath() {
		return carouselImgPath;
	}
	public void setCarouselImgPath(String carouselImgPath) {
		this.carouselImgPath = carouselImgPath;
	}
	

}

package com.quxin.freshfun.model;

public class GoodsDetail {
	private String id;
	private Integer goodsId;
	private String des;
	private String actualImg;
	private Integer goodsUnit;
	private String goodsBrand;
	private String goodsSize;
	private String goodsPlace;
	private String storageMethod;
	private Long goodsBirthdate ;
	private Long goodsOutdate ;
	private String goodsDelivery;
	private String goodsService;
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
	public String getActualImg() {
		return actualImg;
	}
	public void setActualImg(String actualImg) {
		this.actualImg = actualImg;
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
	@Override
	public String toString() {
		return "goodsMongo [id=" + id + ", goodsId=" + goodsId + ", des=" + des
				+ ", actualImg=" + actualImg + ", goodsUnit=" + goodsUnit
				+ ", goodsBrand=" + goodsBrand + ", goodsSize=" + goodsSize
				+ ", goodsPlace=" + goodsPlace + ", storageMethod="
				+ storageMethod + ", goodsBirthdate=" + goodsBirthdate
				+ ", goodsOutdate=" + goodsOutdate + ", goodsDelivery="
				+ goodsDelivery + ", goodsService=" + goodsService + "]";
	}
}

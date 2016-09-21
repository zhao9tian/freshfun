package com.quxin.freshfun.model;

public class OrderAddress {
	private String id;
	private Integer orderId;
	private Integer userId;
	private Integer isDefault;
	private String receiveName;
	private String receiveMobile;
	private String country;
	private String province;
	private String city;
	private String area;
	private String street;
	private String detail;
	private Long gmtCreate;
	private Long gmtModified;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getReceiveMobile() {
		return receiveMobile;
	}
	public void setReceiveMobile(String receiveMobile) {
		this.receiveMobile = receiveMobile;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
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
	@Override
	public String toString() {
		return "OrderAddress [id=" + id + ", orderId=" + orderId + ", userId="
				+ userId + ", isDefault=" + isDefault + ", receiveName="
				+ receiveName + ", receiveMobile=" + receiveMobile
				+ ", country=" + country + ", province=" + province + ", city="
				+ city + ", area=" + area + ", street=" + street + ", detail="
				+ detail + ", gmtCreate=" + gmtCreate + ", gmtModified="
				+ gmtModified + "]";
	}
	
}
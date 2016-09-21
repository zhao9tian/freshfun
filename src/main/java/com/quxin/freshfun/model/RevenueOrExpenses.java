package com.quxin.freshfun.model;

public class RevenueOrExpenses {
	private String orderId;
	private String price;
	private String name;
	private Long date;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "RevenueOrExpenses [orderId=" + orderId + ", price=" + price
				+ ", name=" + name + ", date=" + date + "]";
	}
	
	
}

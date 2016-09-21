package com.quxin.freshfun.model;

public class ShoppingCart {
private String user_id;
	
	private String goods_id;
	
	private String goods_totals;
	
	private String goods_totals_price;
	
	private Integer pay_status;
	
	private Integer is_delete;
	

	public Integer getPay_status() {
		return pay_status;
	}

	public void setPay_status(Integer pay_status) {
		this.pay_status = pay_status;
	}

	public Integer getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(Integer is_delete) {
		this.is_delete = is_delete;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getGoods_totals() {
		return goods_totals;
	}

	public void setGoods_totals(String goods_totals) {
		this.goods_totals = goods_totals;
	}

	public String getGoods_totals_price() {
		return goods_totals_price;
	}

	public void setGoods_totals_price(String goods_totals_price) {
		this.goods_totals_price = goods_totals_price;
	}

}
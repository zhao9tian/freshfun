package com.quxin.freshfun.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Share {
	private String id;
	private String user_id;
	private String goods_id;
	private String code;
	private Long gmt_create;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return user_id;
	}
	public void setUserId(String userId) {
		this.user_id = userId;
	}
	public String getGoodsId() {
		return goods_id;
	}
	public void setGoodsId(String goodsId) {
		this.goods_id = goodsId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getGmtCreate() {
		return gmt_create;
	}
	public void setGmtCreate(Long gmtCreate) {
		this.gmt_create = gmtCreate;
	}
	
	public Share() {
		super();
	}
	public Share(String userId,String code) {
		super();
		this.user_id = userId;
		this.code = code;
	}
	@Override
	public String toString() {
		return "SharePOJO [id=" + id + ", userId=" + user_id + ", goodsId="
				+ goods_id + ", code=" + code + ", gmtCreate=" + gmt_create + "]";
	}
	
}

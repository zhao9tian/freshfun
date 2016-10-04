package com.quxin.freshfun.model.pojo;

import com.quxin.freshfun.model.entity.BaseEntity;

public class CommentPOJO  extends BaseEntity {

	private Integer id;

	private Long orderId;

	private Long userId;
	
	private Integer goodsId;
	
	private String content;

	private Integer generalLevel;

	private Integer tasteLevel;

	private Integer packLevel;

	private Integer logisticsLevel;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getGeneralLevel() {
		return generalLevel;
	}

	public void setGeneralLevel(Integer generalLevel) {
		this.generalLevel = generalLevel;
	}

	public Integer getTasteLevel() {
		return tasteLevel;
	}

	public void setTasteLevel(Integer tasteLevel) {
		this.tasteLevel = tasteLevel;
	}

	public Integer getPackLevel() {
		return packLevel;
	}

	public void setPackLevel(Integer packLevel) {
		this.packLevel = packLevel;
	}

	public Integer getLogisticsLevel() {
		return logisticsLevel;
	}

	public void setLogisticsLevel(Integer logisticsLevel) {
		this.logisticsLevel = logisticsLevel;
	}

}

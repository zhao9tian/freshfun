package com.quxin.freshfun.model;

public class Comment {
	private String orderId;

	private Long userId;
	
	private Integer goodsId;
	
	private String content;
	
	private String imgStorage;
	
	private String parentId;
	
	private Long gmtCreate;
	
	private Long gmtModified;

	private Byte isDeleted;

	private String commentLevel;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
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

	public String getImgStorage() {
		return imgStorage;
	}

	public void setImgStorage(String imgStorage) {
		this.imgStorage = imgStorage;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public Byte getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCommentLevel() {
		return commentLevel;
	}

	public void setCommentLevel(String commentLevel) {
		this.commentLevel = commentLevel;
	}
}

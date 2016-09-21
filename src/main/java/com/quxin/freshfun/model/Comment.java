package com.quxin.freshfun.model;

public class Comment {
	private String order_id;
	
	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	private String user_id;
	
	private String goods_id;
	
	private String content;
	
	private String img_storage;
	
	private String parent_id;
	
	private String gmt_create;
	
	private String gmt_modified;
	
	public String getGmt_create() {
		return gmt_create;
	}

	public void setGmt_create(String gmt_create) {
		this.gmt_create = gmt_create;
	}

	public String getGmt_modified() {
		return gmt_modified;
	}

	public void setGmt_modified(String gmt_modified) {
		this.gmt_modified = gmt_modified;
	}

	public String getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(String is_deleted) {
		this.is_deleted = is_deleted;
	}

	private String is_deleted;
	
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImg_storage() {
		return img_storage;
	}

	public void setImg_storage(String img_storage) {
		this.img_storage = img_storage;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getComment_level() {
		return comment_level;
	}

	public void setComment_level(String comment_level) {
		this.comment_level = comment_level;
	}

	private String comment_level;

}

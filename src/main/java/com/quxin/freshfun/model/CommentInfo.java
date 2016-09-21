package com.quxin.freshfun.model;

public class CommentInfo {
	
    private String user_id;
	
	private String goods_id;
	
	private String content;
	
	private String order_id;
	
	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String getComment_level() {
		return comment_level;
	}

	public void setComment_level(String comment_level) {
		this.comment_level = comment_level;
	}

	
	private String comment_level;

}

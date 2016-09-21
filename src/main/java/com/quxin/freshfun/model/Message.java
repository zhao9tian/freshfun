package com.quxin.freshfun.model;


/**
 * 短信实体类
 * @author TuZl
 * @time 2016年9月6日下午9:55:56
 */
public class Message {
	private Integer id ;
	private String userId ;
	private String code;
	private String phoneNum;
	private Long date;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "Message [id=" + id + ", userId=" + userId + ", code=" + code
				+ ", phoneNum=" + phoneNum + ", date=" + date + "]";
	}
	
	
	
	
}

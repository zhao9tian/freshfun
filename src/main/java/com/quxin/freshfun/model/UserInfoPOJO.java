package com.quxin.freshfun.model;

public class UserInfoPOJO {
	
	private Long userId ;
	private WxInfo wxInfo;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public WxInfo getWxInfo() {
		return wxInfo;
	}
	public void setWxInfo(WxInfo wxInfo) {
		this.wxInfo = wxInfo;
	}
	@Override
	public String toString() {
		return "UserInfoPOJO [userId=" + userId + ", wxInfo=" + wxInfo + "]";
	}
	
	
}

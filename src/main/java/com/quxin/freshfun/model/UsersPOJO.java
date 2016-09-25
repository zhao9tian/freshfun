package com.quxin.freshfun.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UsersPOJO {
    private Long id;

    private Long gmtCreate;

    private Long gmtModified;

    private String userName;

    private String userHeadUrl;

    private String mobilePhone;

    private String deviceId;

    private String wxId;

    private String wzId;

    private String loginMethod;

    private Byte isReceived;

    private Long parentId;

    private Long regTime;

    private Byte userCredit;

    private Integer userMoney;

    private Integer frozenMoney;

    private Byte userIdentify;

    private Byte incomeIdentify;

    private Byte userEnter;

    private Integer userInfoId;

    private Byte isDeleted;

    private Integer userAddress;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getWzId() {
        return wzId;
    }

    public void setWzId(String wzId) {
        this.wzId = wzId;
    }

    public String getLoginMethod() {
        return loginMethod;
    }

    public void setLoginMethod(String loginMethod) {
        this.loginMethod = loginMethod;
    }

    public Byte getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(Byte isReceived) {
        this.isReceived = isReceived;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getRegTime() {
        return regTime;
    }

    public void setRegTime(Long regTime) {
        this.regTime = regTime;
    }

    public Byte getUserCredit() {
        return userCredit;
    }

    public void setUserCredit(Byte userCredit) {
        this.userCredit = userCredit;
    }

    public Integer getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(Integer userMoney) {
        this.userMoney = userMoney;
    }

    public Integer getFrozenMoney() {
        return frozenMoney;
    }

    public void setFrozenMoney(Integer frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    public Byte getUserIdentify() {
        return userIdentify;
    }

    public void setUserIdentify(Byte userIdentify) {
        this.userIdentify = userIdentify;
    }

    public Byte getIncomeIdentify() {
        return incomeIdentify;
    }

    public void setIncomeIdentify(Byte incomeIdentify) {
        this.incomeIdentify = incomeIdentify;
    }

    public Byte getUserEnter() {
        return userEnter;
    }

    public void setUserEnter(Byte userEnter) {
        this.userEnter = userEnter;
    }

    public Integer getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Integer userInfoId) {
        this.userInfoId = userInfoId;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(Integer userAddress) {
        this.userAddress = userAddress;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
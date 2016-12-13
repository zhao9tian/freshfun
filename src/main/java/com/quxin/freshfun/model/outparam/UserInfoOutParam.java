package com.quxin.freshfun.model.outparam;

import com.quxin.freshfun.model.entity.BaseEntity;

/**
 * Created by Ziming on 2016/10/20.
 */
public class UserInfoOutParam extends BaseEntity{
    private Long userId;                //用户id，唯一标识
    private String userName;            //用户昵称
    private String userHeadImg;         //用户头像url
    private String phoneNumber;         //用户绑定手机号
    private String deviceId;            //设备号，app端登录使用
    private String openId;              //微站id，对服务号或公众号唯一
    private String unionId;             //微信id，对开放平台唯一
    private Byte loginType;             //登录方式，1：APP登录，2：微信登录，3：微站登录
    private Byte source;                //用户信息来源，默认为0：悦选用户
    private Byte identity;              //用户身份标识，默认为0：普通用户
    private Long fetcherId;             //分享者id，即捕手id
    private Byte isFetcher;             //是否是捕手标识，默认为0：不是捕手，1：是捕手
    private String city;                //用户所在城市
    private String province;            //用户所在省份
    private String country;             //用户所在国家
    private String appId;               //用户所属公众号

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Byte getLoginType() {
        return loginType;
    }

    public void setLoginType(Byte loginType) {
        this.loginType = loginType;
    }

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }

    public Byte getIdentity() {
        return identity;
    }

    public void setIdentity(Byte identity) {
        this.identity = identity;
    }

    public Long getFetcherId() {
        return fetcherId;
    }

    public void setFetcherId(Long fetcherId) {
        this.fetcherId = fetcherId;
    }

    public Byte getIsFetcher() {
        return isFetcher;
    }

    public void setIsFetcher(Byte isFetcher) {
        this.isFetcher = isFetcher;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}

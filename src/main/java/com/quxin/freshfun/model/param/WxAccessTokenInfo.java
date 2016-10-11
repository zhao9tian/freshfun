package com.quxin.freshfun.model.param;

/**
 * Created by gsix on 2016/10/8.
 */
public class WxAccessTokenInfo {
    /**
     * 接口凭证
     */
    private String access_token;
    /**
     * 凭证超时时间
     */
    private String expires_in;
    /**
     * 用户刷新access_token
     */
    private String refresh_token;
    /**
     * 授权用户唯一标识
     */
    private String openid;
    /**
     * 用户授权作用域
     */
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}

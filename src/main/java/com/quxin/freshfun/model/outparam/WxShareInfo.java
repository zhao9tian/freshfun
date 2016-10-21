package com.quxin.freshfun.model.outparam;

/**
 * Created by gsix on 2016/10/19.
 */
public class WxShareInfo {
    /**
     * 公众号的唯一标识
     */
    private String appId;
    /**
     * 生成签名时间戳
     */
    private String timestamp;
    /**
     * 生成签名随机串
     */
    private String nonceStr;
    /**
     * 签名
     */
    private String signature;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

package com.quxin.freshfun.utils.weixinPayUtils;

/**
 * Created by gsix on 2016/10/10.
 */
public class WxConstantUtil {
    /**
     * 商家可以考虑读取配置文件
     */

    //初始化
    public static String APP_ID = "wx9820b9bacf3ba98a";//微信开发平台应用id
    public static String APP_SECRET = "7ec923f117ebcd7c58737426a23eafee";//应用对应的凭证
    //应用对应的密钥
    public static String APP_KEY = "L8LrMqqeGRxST5reouB0K66CaYAWpqhAVsq7ggKkxHCOastWksvuX1uvmvQclxaHoYd3ElNBrNO2DHnnzgfVG9Qs473M3DTOZug5er46FhuGofumV8H2FVR9qkjSlC5K";
    public static String PARTNER = "1335619501";//财付通商户号
    public static String PARTNER_KEY = "560e2d12b29efa5e5605575b1ec5daa0";//商户号对应的密钥
    public static String TOKENURL = "https://api.weixin.qq.com/cgi-bin/token";//获取access_token对应的url
    public static String GRANT_TYPE = "client_credential";//常量固定值
    public static String EXPIRE_ERRCODE = "42001";//access_token失效后请求返回的errcode
    public static String FAIL_ERRCODE = "40001";//重复获取导致上一次获取的access_token失效,返回错误码
    public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//获取预支付id的接口url
    //"https://api.weixin.qq.com/pay/genprepay?access_token="
    public static String ACCESS_TOKEN = "access_token";//access_token常量值
    public static String ERRORCODE = "errcode";//用来判断access_token是否失效的值
    public static String SIGN_METHOD = "sha1";//签名算法常量值
}
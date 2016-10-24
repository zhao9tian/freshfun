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
    public static String PARTNER = "1390031102";//财付通商户号
    public static String PARTNER_KEY = "lvfjkbdwyulnbcd441RYUIU563lkhfss";//商户号对应的密钥
    public static String TOKENURL = "https://api.weixin.qq.com/cgi-bin/token";//获取access_token对应的url
    public static String GRANT_TYPE = "client_credential";//常量固定值
    public static String EXPIRE_ERRCODE = "42001";//access_token失效后请求返回的errcode
    public static String FAIL_ERRCODE = "40001";//重复获取导致上一次获取的access_token失效,返回错误码
    public static String GATEURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//获取预支付id的接口url
    /**
     * 微信oAuth认证获取accessToken地址
     */
    public static String OAUTH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=";

    public static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    /**
     * 获取微信用户信息地址
     */
    public static String WX_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=";
    /**
     * 获取jsapi_ticket地址
     */
    public static String WX_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=";
    //接收财付通通知的URL
    public static String NOTIFY_URL = "https://www.freshfun365.com/FreshFun/payCallback.do";

    public static String ACCESS_TOKEN = "access_token";//access_token常量值
    public static String ERRORCODE = "errcode";//用来判断access_token是否失效的值
    public static String SIGN_METHOD = "sha1";//签名算法常量值
}

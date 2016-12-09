package com.quxin.freshfun.utils.weixinPayUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quxin.freshfun.model.WxInfo;
import com.quxin.freshfun.model.param.WxAccessTokenInfo;
import com.quxin.freshfun.model.param.WxTicketInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class WXUtil {

	private static final Logger logger = LoggerFactory.getLogger(WXUtil.class);

	private static int tokenCount = 0;

	public static String getNonceStr() {
		Random random = new Random();
		return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), "GBK");
	}

	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	private synchronized static void setCount(){
		tokenCount = tokenCount + 1;
	}

	/**
	 * 微信登录获取accessToken
	 * @param appId 微信公众平台对应的标识
	 * @param appSecert 密钥
	 * @param code 获取用户微信信息标识
	 */
	public static WxAccessTokenInfo getOauthAccessToken(String appId,String appSecert,String code){
		StringBuilder sb = new StringBuilder();
		sb.append(WxConstantUtil.OAUTH_ACCESS_TOKEN_URL);
		sb.append(appId);
		sb.append("&secret=");
		sb.append(appSecert);
		sb.append("&code=");
		sb.append(code);
		sb.append("&grant_type=authorization_code");
		WxAccessTokenInfo wxToken = new WxAccessTokenInfo();
		wxToken = sendWxRequest(sb,wxToken);
		setCount();
		logger.warn("调用AccessToken次数：",tokenCount);
		return wxToken;
	}

	/**
	 * 获取微信accessToken
	 * @param appid
	 * @param appSecert
	 */
	public static WxAccessTokenInfo getAccessToken(String appid,String appSecert){
		StringBuilder sb = new StringBuilder();
		sb.append(WxConstantUtil.ACCESS_TOKEN_URL);
		sb.append("&appid=");
		sb.append(appid);
		sb.append("&secret=");
		sb.append(appSecert);
		WxAccessTokenInfo tokenInfo = new WxAccessTokenInfo();
		tokenInfo = sendWxRequest(sb, tokenInfo);
		setCount();
		logger.warn("调用AccessToken次数：",tokenCount);
		return tokenInfo;
	}

	/**
	 * 获取微信用户信息
	 * @param accessToken 根据用户凭证获取微信用户详细信息
	 * @param openId
	 */
	public static WxInfo getWxUserInfo(String accessToken,String openId){
		StringBuilder sb = new StringBuilder();
		sb.append(WxConstantUtil.WX_USERINFO_URL);
		sb.append(accessToken);
		sb.append("&openid=");
		sb.append(openId);
		WxInfo wxInfo = new WxInfo();
		wxInfo = sendWxRequest(sb,wxInfo);
		return wxInfo;
	}

	/**
	 * 微信分享获取分享凭证
	 */
	public static WxTicketInfo getTicket(String accessToken){
		StringBuilder sb = new StringBuilder();
		sb.append(WxConstantUtil.WX_TICKET_URL);
		sb.append(accessToken);
		sb.append("&type=jsapi");
		WxTicketInfo info = new WxTicketInfo();
		info = sendWxRequest(sb, info);
		return info;
	}

	/**
	 * 发送请求
	 * @param sb
	 * @param t
	 * @param <T>
	 * @return
	 */
	private static <T> T sendWxRequest(StringBuilder sb,T t) {
		Object o = new Object();
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String StrJson = gson.toJson(o);
		String str = com.quxin.freshfun.utils.HttpClientUtil.jsonToPost(sb.toString(), StrJson);
		return strToJson(str,t);
	}

	/**
	 * String转对象
	 * @param str
	 * @return
	 */
	public static <T> T strToJson(String str,T t){
		Gson gson = new Gson();
		T wxInfo = (T) gson.fromJson(str,t.getClass());
		return wxInfo;
	}
}

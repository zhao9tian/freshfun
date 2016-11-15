package com.quxin.freshfun.service.impl.wechat;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.quxin.freshfun.service.impl.wechat.client.*;
import com.quxin.freshfun.model.param.WxPayInfo;
import com.quxin.freshfun.utils.FieldUtil;
import com.quxin.freshfun.utils.weixinPayUtils.ConstantUtil;
import com.quxin.freshfun.utils.weixinPayUtils.MD5Util;
import com.quxin.freshfun.utils.weixinPayUtils.Sha1Util;
import com.quxin.freshfun.utils.weixinPayUtils.WxConstantUtil;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PrepayIdRequestHandler extends RequestHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public PrepayIdRequestHandler() {
		super();
	}

	/**
	 * 创建签名SHA1
	 *
	 * @return
	 * @throws Exception
	 */
	public String createSHA1Sign() {
		Set es = super.getAllParameters().entrySet();
		Iterator it = es.iterator();
		StringBuilder sb = getSignStr(it);
		String params = sb.substring(0, sb.lastIndexOf("&"));
		String appsign = Sha1Util.getSha1(params);
		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "sha1 sb:" + params);
		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "app sign:" + appsign);
		return appsign;
	}

	/**
	 * MD5签名
	 * @param partnerKey 商户对应密钥
	 * @return
	 */
	public String createMD5Sign(String partnerKey){
		Set es = super.getAllParameters().entrySet();
		Iterator it = es.iterator();
		StringBuilder sb = getSignStr(it);
		String stringA = sb.substring(0, sb.lastIndexOf("&"));
        String params = stringA+"&key="+ partnerKey;
		String payStr = MD5Util.MD5Encode(params,"UTF-8").toUpperCase();
        return payStr;
	}

	private StringBuilder getSignStr(Iterator it) {
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			sb.append(k + "=" + v + "&");
		}
		return sb;
	}

	// 提交预支付
	public String sendPrepay() throws JSONException {
		String prepayid = "";
		StringBuilder sb = new StringBuilder("{");
		StringBuilder paramsStr = new StringBuilder();
		Set es = super.getAllParameters().entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v)) {
				sb.append("\"" + k + "\":\"" + v + "\",");
			}
		}
		paramsStr.append(sb.substring(0, sb.lastIndexOf(",")));
		paramsStr.append("}");
        String payStr = FieldUtil.jsonToXml(paramsStr.toString());
        String requestUrl = super.getGateUrl();
		TenpayHttpClient httpClient = new TenpayHttpClient();
		httpClient.setReqContent(requestUrl);

		if (httpClient.callHttpPost(requestUrl, payStr)) {
			String resultXml = httpClient.getResContent();
			String resultJson = FieldUtil.xmlToJson(resultXml);
			WxPayInfo info = strToJsonObject(resultJson);
			if("SUCCESS".equals(info.getResult_code())){
				prepayid = info.getPrepay_id();
			}else{
				logger.error("微信支付获取prepayid失败");
			}
		}
		return prepayid;
	}

	private WxPayInfo strToJsonObject(String jsonStr){
		Gson gson = new Gson();
		return gson.fromJson(jsonStr,WxPayInfo.class);
	}

	// 判断access_token是否失效
	public String sendAccessToken() {
		String accesstoken = "";
		StringBuffer sb = new StringBuffer("{");
		Set es = super.getAllParameters().entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"appkey".equals(k)) {
				sb.append("\"" + k + "\":\"" + v + "\",");
			}
		}
		String params = sb.substring(0, sb.lastIndexOf(","));
		params += "}";

		String requestUrl = super.getGateUrl();
//		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "requestUrl:"
//				+ requestUrl);
		TenpayHttpClient httpClient = new TenpayHttpClient();
		httpClient.setReqContent(requestUrl);
		String resContent = "";
//		this.setDebugInfo(this.getDebugInfo() + "\r\n" + "post data:" + params);
		if (httpClient.callHttpPost(requestUrl, params)) {
			resContent = httpClient.getResContent();
			if (2 == resContent.indexOf(ConstantUtil.ERRORCODE)) {
				accesstoken = resContent.substring(11, 16);//获取对应的errcode的值
			}
		}
		return accesstoken;
	}
}

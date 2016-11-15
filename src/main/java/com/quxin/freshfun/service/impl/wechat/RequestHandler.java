package com.quxin.freshfun.service.impl.wechat;


import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 请求处理类
 * 请求处理类继承此类，重写createSign方法即可。
 * @author miklchen
 *
 */
public class RequestHandler {

	/** 网关url地址 */
	private String gateUrl;

	/** 密钥 */
	private String key;

	/** 请求的参数 */
	private SortedMap<String,Object> parameters;

	/** debug信息 */
	private String debugInfo;

	/**
	 * 构造函数
	 */
	public RequestHandler() {
		this.gateUrl = "https://gw.tenpay.com/gateway/pay.htm";
		this.key = "";
		this.parameters = new TreeMap<>();
		this.debugInfo = "";
	}

	/**
	 *初始化函数。
	 */
	public void init() {
		//nothing to do
	}

	/**
	 *获取入口地址,不包含参数值
	 */
	public String getGateUrl() {
		return gateUrl;
	}

	/**
	 *设置入口地址,不包含参数值
	 */
	public void setGateUrl(String gateUrl) {
		this.gateUrl = gateUrl;
	}

	/**
	 *获取密钥
	 */
	public String getKey() {
		return key;
	}

	/**
	 *设置密钥
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 获取参数值
	 * @param parameter 参数名称
	 * @return String
	 */
	public String getParameter(String parameter) {
		String s = (String)this.parameters.get(parameter);
		return (null == s) ? "" : s;
	}

	/**
	 * 设置参数值
	 * @param parameter 参数名称
	 * @param parameterValue 参数值
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if(null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}

	/**
	 * 返回所有的参数
	 * @return SortedMap
	 */
	public SortedMap getAllParameters() {
		return this.parameters;
	}

	/**
	 *获取debug信息
	 */
	public String getDebugInfo() {
		return debugInfo;
	}

	/**
	 *设置debug信息
	 */
	protected void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}

}

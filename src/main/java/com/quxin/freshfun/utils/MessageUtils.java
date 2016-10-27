package com.quxin.freshfun.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 短信验证码
 * @author qucheng
 * @time 2016年9月6日下午9:37:20
 */
public class MessageUtils {
	/**
	 * 发送短信
	 */
	private static void sendMessage(String code , String phoneNum) throws IOException {
		String content = "【悦选美食】"+"您的短信验证码为:"+code+"，请勿将验证码给他人使用。";
		String sign="";
		
		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb;
		sb = new StringBuffer("http://sms.1xinxi.cn/asmx/smsservice.aspx?");

		// 向StringBuffer追加用户名
		sb.append("name=yuexuan2016");//之前的是quxin2016

		// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
		sb.append("&pwd=140DC7BD9C90A23757B40A854800");//C877EC8499ACDA41BC44548C64AB

		// 向StringBuffer追加手机号码
		sb.append("&mobile=").append(phoneNum);

		// 向StringBuffer追加消息内容转URL标准码
		sb.append("&content=").append(URLEncoder.encode(content, "UTF-8"));
		//追加发送时间，可为空，为空为及时发送
		sb.append("&stime=");

		//加签名
		sb.append("&sign=").append(URLEncoder.encode(sign, "UTF-8"));
		//type为固定值pt  extno为扩展码，必须为数字 可为空
		sb.append("&type=pt&extno=");
		// 创建url对象
		URL url = new URL(sb.toString());

		// 打开url连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 设置url请求方式 ‘get’ 或者 ‘post’
		connection.setRequestMethod("POST");

		// 发送
		url.openStream();

	}
	
	/**
	 * 给手机号发送短信验证码
	 * @param phoneNum 手机号
	 * @return 发送并返回验证码
	 */
	public static String createMessage(String phoneNum){
		String code;
		Integer i = (int) (Math.random()*9000+1000);
		code = i.toString();
		//发送短信
		try {
			sendMessage(code , phoneNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}

	public static void main(String[] args) {
		createMessage("13166078059");
	}
}

package com.quxin.freshfun.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

public class GtexMd5B32Util {
	/**
	 * md5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getMd5(String str) {
		StringBuffer sb = new StringBuffer(32);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(str.getBytes("UTF-8"));
			for (int i = 0; i < array.length; i++) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.toLowerCase().substring(1, 3));
			}
//			BASE64Encoder b64 = new BASE64Encoder();
//			String result = b64.encode(array);
//			System.out.println("base64:  " + result);
		} catch (Exception e) {
			System.out.println("Can not encode the string '" + str
					+ "' to MD5!");
			e.printStackTrace();
			return null;
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 判断md5加密是否正确
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMd5B32Result(String sign, String str) {
		if (sign.equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 返回一个json对象
	 * @param waybill 订单编号
	 * @param company 快递公司
	 * @return
	 */
	public static String insertPostHttp(String waybill , String company){
		String responseMsg = "";
		//1.构造HttpClient的实例
        HttpClient httpClient=new HttpClient();
        httpClient.getParams().setContentCharset("utf-8");
        String url="http://open.gtexpress.cn/gt_open/query/order/query.action";
        //2.构造PostMethod的实例
        PostMethod postMethod=new PostMethod(url);
        //3.把参数值放入到PostMethod对象中
        //数据传输格式
		String gtex_type="json";
		 //用户账号
		String gtex_user_login="jiuaiziji_fu@163.com";
		 //appKey
		String gtex_key="fsh_409622";
		 //接口方法名
		String gtex_method="getOrderByCarrier";
		 //当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp=sdf.format(System.currentTimeMillis()/1000);
		 //数字签名
		String gtex_sign = gtex_key + gtex_user_login + timestamp;
		gtex_sign = getMd5(gtex_sign);
		 //请求参数
		String gtex_params="{'gtex_carrier_code':'"+company+"','gtex_bill_code':'"+waybill+"'}";
        postMethod.addParameter("gtex_type", gtex_type);
        postMethod.addParameter("gtex_sign", gtex_sign);
        postMethod.addParameter("gtex_key", gtex_key);
        postMethod.addParameter("gtex_user_login", gtex_user_login);
        postMethod.addParameter("gtex_method", gtex_method);
        postMethod.addParameter("timestamp", timestamp);
        postMethod.addParameter("gtex_params", gtex_params);
        try {
            //4.执行postMethod,调用http接口
            httpClient.executeMethod(postMethod);//200
            //5.读取内容
            responseMsg = postMethod.getResponseBodyAsString().trim();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //7.释放连接
            postMethod.releaseConnection();
        }
        return responseMsg;
	}

//	public static void main(String[] args) {
//		System.out.println(insertPostHttp("410212319098","zhongtong"));
//	}
}

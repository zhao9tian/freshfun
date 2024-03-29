package com.quxin.freshfun.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpClientUtil {

	private static HttpClient httpClient = HttpClients.createDefault();

	/**
	 * 发送Get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String get(String url, List<NameValuePair> params) {
		String body = null;
		try {
			// Get请求
			HttpGet httpget = new HttpGet(url);
			// 设置参数
			if(params!=null){
				String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
				httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
			}
			// 发送请求
			HttpResponse httpresponse = httpClient.execute(httpget);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return body;
	}
	
	
	 public static String executeGet(String url) throws Exception {  
	        BufferedReader in = null;  
	  
	        String content = null;  
	        try {  
	            // 定义HttpClient  
	            HttpClient client = HttpClients.createDefault();
	            // 实例化HTTP方法  
	            HttpGet request = new HttpGet();  
	            request.setURI(new URI(url));  
	            HttpResponse response = client.execute(request);  
	  
	            InputStream input = response.getEntity().getContent();
	            
	            in = new BufferedReader(new InputStreamReader(input,"gbk"));  
	            StringBuffer sb = new StringBuffer("");  
	            String line = "";  
	            String NL = System.getProperty("line.separator");  
	            int count=0;
	            while ((line = in.readLine()) != null) {  
	            	if(count==5){
	                sb.append(line + NL);  
	                	break;
	                }
	            	count++;
	            }  
	            in.close();  
	            content = sb.toString();  
	        } finally {  
	            if (in != null) {  
	                try {  
	                    in.close();// 最后要关闭BufferedReader  
	                } catch (Exception e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
	        return content;  
	    } 

	/**
	 * 发送 Post请求
	 * 
	 * @param url
	 * @param reqXml
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String post(String url, String reqXml) {
		String body = null;
		try {
			// 设置客户端编码
			if (httpClient == null) {
				// Create HttpClient Object
				httpClient = HttpClients.createDefault();
			}
			httpClient.getParams().setParameter(
					"http.protocol.content-charset", HTTP.UTF_8);
			httpClient.getParams().setParameter(HTTP.CONTENT_ENCODING,
					HTTP.UTF_8);
			httpClient.getParams().setParameter(HTTP.CHARSET_PARAM, HTTP.UTF_8);
			httpClient.getParams().setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET,
					HTTP.UTF_8);
			// Post请求
			HttpPost httppost = new HttpPost(url);
			// 设置post编码
			httppost.getParams().setParameter("http.protocol.content-charset",
					HTTP.UTF_8);
			httppost.getParams()
					.setParameter(HTTP.CONTENT_ENCODING, HTTP.UTF_8);
			httppost.getParams().setParameter(HTTP.CHARSET_PARAM, HTTP.UTF_8);
			httppost.getParams().setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET,
					HTTP.UTF_8);

			// 设置参数
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("data", reqXml));
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// StringEntity entity1 = new StringEntity(getUTF8XMLString(reqXml),
			// "UTF-8");
			// entity1.setContentType("text/xml;charset=UTF-8");
			// entity1.setContentEncoding("UTF-8");
			// httppost.setEntity(entity1);
			// 设置报文头
			httppost.setHeader("Content-Type", "text/xml;charset=UTF-8");
			// 发送请求
			HttpResponse httpresponse = httpClient.execute(httppost);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}
	
	/**
	 * 发送 Post请求
	 * 
	 * @param url
	 * @param url
	 * @return
	 */
	public static String defaultPost(String url, String data) {
		String body = null;
		try {
			// 设置客户端编码
			if (httpClient == null) {
				httpClient = HttpClients.createDefault();
			}
			// Post请求
			HttpPost httppost = new HttpPost(url);
			// 设置参数
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("data", data)); 
			httppost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
			// 设置报文头
			httppost.setHeader("Content-Type", "text/xml;charset=UTF-8");
			// 发送请求
			HttpResponse httpresponse = httpClient.execute(httppost);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}
	
	/**
	 * 发送 Post请求<br/> 直接发送JSON
	 * 
	 * @param url
	 * @param
	 * @return
	 */
	public static String jsonToPost(String url, String data) {
		String body = null;
		try {
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			configureHttpClient2(httpClientBuilder);
			httpClient = httpClientBuilder.build();
			HttpPost httppost=new HttpPost(url);
			StringEntity params =new StringEntity(data,"UTF-8");
			httppost.addHeader("content-type", "application/json");
			httppost.setEntity(params);
			// 发送请求
			HttpResponse httpresponse = httpClient.execute(httppost);
			// 获取返回数据
			HttpEntity entity = httpresponse.getEntity();
			body = EntityUtils.toString(entity,"UTF-8");
//			String result = new String("11", "utf-8");
			EntityUtils.consume(entity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}


public static void configureHttpClient2(HttpClientBuilder clientBuilder)
	{
		try
		{
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager()
			{
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
				{

				}
				public void checkServerTrusted(X509Certificate[] chain,  String authType) throws CertificateException
				{

				}
				public X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}
			};
			ctx.init(null, new TrustManager[]{tm}, null);
			clientBuilder.setSslcontext(ctx);

		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	/**
	 * 根据样式格式化时间 "yyyyMMdd" "yyyyMMddHHmmss" "yyyyMMddHHmmssssssss"
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static String getnowDate(String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(new Date());
	}

}

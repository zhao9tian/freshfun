package com.quxin.freshfun.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.quxin.freshfun.model.Share;
import com.quxin.freshfun.model.param.WxPushMessageResult;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

public class FieldUtil {
//	public static <T> T copyObj(T source) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
//		//获取泛型对象类型
//		Class<? extends Object> clazz = source.getClass();
//		//获取构造
//		Constructor<? extends Object> constructor = clazz.getConstructor();
//		//实例化对象
//		@SuppressWarnings("unchecked")
//		T obj = (T) constructor.newInstance();
//		//获取所有对象属性
//		Field[] fields = clazz.getDeclaredFields();
//		for(int i = 0;i < fields.length;i++){
//			Field field = fields[i];
//			field.setAccessible(true);
//			//属性名
//			String filedName = field.getName();
//			Object object = field.get(source);
//			if(object != null){
//				//获取属性首字母
//				String firstLetter = filedName.substring(0, 1).toUpperCase();
//				//拼接get方法名
//				String getMethodName = "get"+firstLetter + filedName.substring(1);
//				//得到get方法对象
//				Method method = clazz.getMethod(getMethodName);
//				//对源对象调用get方法获取属性值
//				Object value = method.invoke(source);
//
//				// 拼接set方法名
//				String setMethodName = "set" + firstLetter + filedName.substring(1);
//				// 获取set方法对象
//				Method setMethod = clazz.getMethod(setMethodName, new Class[] { field.getType() });
//				// 对目标对象调用set方法装入属性值
//				setMethod.invoke(obj, new Object[] { value });
//			}
//		}
//
//		return obj;
//	}

    /**
     * 微信表情过滤
     * @return
     */
    public static String EmojiFilter(String nickname){
        if(nickname.trim().isEmpty()){
            return nickname;
        }
        String pattern = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
        String reStr = "";
        Pattern emoji = Pattern.compile(pattern);
        Matcher emojiMatcher = emoji.matcher(nickname);
        nickname = emojiMatcher.replaceAll(reStr);
        return nickname;
    }

    /**
     * json字符串转Xml字符串
     * @param jsonStr
     * @return
     */
    public static String jsonToXml(String jsonStr){
        XMLSerializer xmlSerializer = new XMLSerializer();
        xmlSerializer.setRootName("xml");
        xmlSerializer.clearNamespaces("xml");
        String write = xmlSerializer.write(JSONSerializer.toJSON(jsonStr));
        return write;
    }

    /**
     * xml格式转json
     * @return
     */
    public static String xmlToJson(String xmlStr){
        return new XMLSerializer().read(xmlStr).toString();
    }

    public static void main(String [] args) throws UnsupportedEncodingException {
        /*String str = "{\"appid\":\"wx667c9cd468801536\",\"body\":\"sss\",\"fee_type\":\"1\",\"mch_id\":\"1335619501\",\"nonce_str\":\"5bcf8dd060e5ea0bff484b4a4127cb47\",\"notify_url\":\"https://freshfun.meiguoyouxian.com/FreshFun/payCallback.do\",\"out_trade_no\":\"Z13566057\",\"sign\":\"706EF245CFEFCC3FCF09BB8B108380DA\",\"spbill_create_ip\":\"192.168.3.21\",\"total_fee\":\"1000\",\"trade_type\":\"APP\"}";
        String ss = jsonToXml(str);
        System.out.println(ss);*/

        //String str = new String("绛惧悕閿欒\uE1E4".getBytes("",""));
//        String s = "会发光的m记\uD83C\uDF5F";
//        String str = new String(s.getBytes("utf-8"),"unicode");
//        System.out.println(str);
        String str = "{\n" +
                "           \"errcode\":0,\n" +
                "           \"errmsg\":\"ok\",\n" +
                "           \"msgid\":200228332\n" +
                "       }";
        Gson gson = new Gson();
        WxPushMessageResult wxPushMessageResult = gson.fromJson(str, WxPushMessageResult.class);
        System.out.println(wxPushMessageResult.getErrmsg());

    }
}
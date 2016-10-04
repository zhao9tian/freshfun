package com.quxin.freshfun.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.model.Share;

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

}
package com.quxin.freshfun.utils;

import com.quxin.freshfun.model.GoodsDetail;
import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: baoluo
 * Date: 15/11/30
 * Time: 下午8:04
 * To change this template use File | Settings | File Templates.
 */
public class ResultUtil {

    private static final Logger logger = LoggerFactory.getLogger(ResultUtil.class);

    // Map --> Bean 2: 利用org.apache.commons.beanutils 工具类实现 Map --> Bean
    public static void map2bean2(Map<String, Object> map, Object obj) {
        if (map == null || obj == null) {
            return;
        }
        try {
            BeanUtils.populate(obj, map);
        } catch (Exception e) {
            System.out.println("transMap2Bean2 Error " + e);
        }
    }

    // Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean
    public static void map2bean(Map<String, Object> map, Object obj) {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }

            }

        } catch (Exception e) {
            System.out.println("transMap2Bean Error " + e);
        }

        return;

    }

    // Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
    public static Map<String, Object> bean2map(Object obj) {

        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }

        return map;

    }

    public static String map2json(Map<String,Object> map){
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(map);
        } catch (IOException e) {
            logger.error("com.mogujie.service.wormhole.util.BeanUtil#map2json exception ",e);

        }
        return json;
    }


    public static Map<String, Object> json2map(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> productMap = mapper.readValue(json,Map.class);//转成map
        return productMap;
    }

    /**
     * 成功参数封装
     * @param obj
     * @return
     */
    public static Map<String, Object> success(Object obj){

        Map<String, Object>  map = new HashMap<String, Object>();
        map.put("code",1001);
        map.put("msg","请求成功");
        Map<String, Object>  resultMap = new HashMap<String, Object>();
        resultMap.put("status",map);
        resultMap.put("data",obj);
        return resultMap;
    }

    /**
     * 失败参数封装
     * @param code
     * @param msg
     * @param obj
     * @return
     */
    public static Map<String, Object> fail(int code, String msg,Object obj){

        Map<String, Object>  map = new HashMap<String, Object>();
        map.put("code",code);
        map.put("msg",msg);
        Map<String, Object>  resultMap = new HashMap<String, Object>();
        resultMap.put("status",map);
        resultMap.put("data",obj);
        return resultMap;
    }

    /**
     * 失败参数封装
     * @param code
     * @param msg
     * @return
     */
    public static Map<String, Object> fail(int code, String msg){

        Map<String, Object>  map = new HashMap<String, Object>();
        map.put("code",code);
        map.put("msg",msg);
        Map<String, Object>  resultMap = new HashMap<String, Object>();
        resultMap.put("status",map);
        return resultMap;
    }



    public static void main(String[] args) throws IOException {

        GoodsDetail goods = new GoodsDetail();
        goods.setGoodsBirthdate(123111111111111123l);
        goods.setGoodsId(123);
        goods.setDes("aa");

//        System.out.println(BeanUtil.success(goods));
//        System.out.println(BeanUtil.fail(4004,"呵呵哒"));
    }
}

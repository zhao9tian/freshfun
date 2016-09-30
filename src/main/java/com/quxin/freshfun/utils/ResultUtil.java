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

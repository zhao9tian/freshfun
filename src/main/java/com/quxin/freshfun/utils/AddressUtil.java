package com.quxin.freshfun.utils;

import com.quxin.freshfun.model.pojo.address.AddressUtilPOJO;
import com.quxin.freshfun.service.address.AddressUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ziming on 2016/11/16.
 */
@Component
public class AddressUtil implements ApplicationContextAware {
    @Autowired
    private AddressUtilService addressUtilService;
    public static List<Map<String, Object>> addressDateIOS = new ArrayList<Map<String, Object>>();
    public static List<Map<String, Object>> addressDateH5 = new ArrayList<Map<String, Object>>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("*************************************开始初始化全国地址*************************************");
        Long begin = System.currentTimeMillis();
        List<AddressUtilPOJO> addressProvList = addressUtilService.queryAllAddress(1);
        List<AddressUtilPOJO> addressCityList = addressUtilService.queryAllAddress(2);
        List<AddressUtilPOJO> addressDistList = addressUtilService.queryAllAddress(3);
        for(AddressUtilPOJO prov : addressProvList){
            Map<String, Object> mapProvIOS = new HashMap<String, Object>();     //ios省级
            List<Map<String, Object>> cityResultIOS = new ArrayList<Map<String, Object>>();//ios市级
            Map<String,Object> map1 = new HashMap<String,Object>();
            map1.put("id",prov.getAreaNo());
            map1.put("value",prov.getAreaName());
            List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
            for(AddressUtilPOJO city : addressCityList) {
                if (city.getParentNo().equals(prov.getAreaNo())) {
                    List<String> distsResultIOS = new ArrayList<String>();     //ios县区级，名称
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("id", city.getAreaNo());
                    map2.put("value", city.getAreaName());
                    List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
                    for (AddressUtilPOJO dist : addressDistList) {
                        if (dist.getParentNo().equals(city.getAreaNo())) {
                            Map<String, Object> map3 = new HashMap<String, Object>();
                            map3.put("id", dist.getAreaNo());
                            map3.put("value", dist.getAreaName());
                            list2.add(map3);
                            distsResultIOS.add(dist.getAreaName()+","+dist.getAreaNo());
                        }
                    }
                    Map<String, Object> mapCityIOS = new HashMap<String, Object>();   //ios市级
                    mapCityIOS.put("areas", distsResultIOS);     //放入县区
                    mapCityIOS.put("city", city.getAreaName() + "," + city.getAreaNo());   //放入市级
                    cityResultIOS.add(mapCityIOS);      //cities
                    map2.put("child", list2);
                    list1.add(map2);
                }
            }
            map1.put("child", list1);
            addressDateH5.add(map1);
            mapProvIOS.put("cities",cityResultIOS);
            mapProvIOS.put("state",prov.getAreaName()+","+prov.getAreaNo());
            addressDateIOS.add(mapProvIOS);
        }
        Long end = System.currentTimeMillis();
        System.out.println("*********************************地址加載耗費時間："+(end-begin)+"毫秒*********************************");
    }
}

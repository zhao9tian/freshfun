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
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AddressUtilService addressUtilService;
    public static List<Map<String, Object>> provResult = new ArrayList<Map<String, Object>>();
    public static Map<String, Object> mapC = new HashMap<String, Object>();
    public static Map<String, Object> mapProv = new HashMap<String, Object>();
    public static List<Map<String, Object>> provResultIOS = new ArrayList<Map<String, Object>>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("*************************************开始初始化全国地址*************************************");
        List<AddressUtilPOJO> addressProvList = addressUtilService.queryCityOrArea(0);
        if (addressProvList != null && addressProvList.size() > 0) {
            for (AddressUtilPOJO address : addressProvList) {
                Map<String, Object> mapProvIOS = new HashMap<String, Object>();     //ios省级
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("text", address.getAreaName());
                map.put("value", address.getAreaNo());
                provResult.add(map);
                List<AddressUtilPOJO> addressCityList = addressUtilService.queryCityOrArea(address.getAreaNo());
                if (addressCityList != null && addressCityList.size() > 0) {
                    List<Map<String, Object>> cityResult = new ArrayList<Map<String, Object>>();
                    List<Map<String, Object>> cityResultIOS = new ArrayList<Map<String, Object>>();//ios市级

                    for (AddressUtilPOJO addressCity : addressCityList) {
                        Map<String, Object> mapCity = new HashMap<String, Object>();
                        mapCity.put("text", addressCity.getAreaName());
                        mapCity.put("value", addressCity.getAreaNo());
                        cityResult.add(mapCity);
                        List<AddressUtilPOJO> addressAreaList = addressUtilService.queryCityOrArea(addressCity.getAreaNo());
                        if (addressAreaList != null && addressAreaList.size() > 0) {
                            List<String> distsResultIOS = new ArrayList<String>();     //ios县区级，名称
                            List<Map<String, Object>> distsResult = new ArrayList<Map<String, Object>>();
                            for (AddressUtilPOJO addressArea : addressAreaList) {
                                Map<String, Object> mapD = new HashMap<String, Object>();
                                mapD.put("text", addressArea.getAreaName());
                                mapD.put("value", addressArea.getAreaNo());
                                distsResult.add(mapD);
                                //ios
                                distsResultIOS.add(addressArea.getAreaName());
                            }
                            mapC.put(addressCity.getAreaNo().toString(), distsResult);
                            //ios
                            Map<String, Object> mapCityIOS = new HashMap<String, Object>();   //ios市级
                            mapCityIOS.put("areas",distsResultIOS);     //放入县区
                            mapCityIOS.put("city",addressCity.getAreaName());   //放入市级
                            cityResultIOS.add(mapCityIOS);      //cities
                        }
                    }
                    mapProv.put(address.getAreaNo().toString(), cityResult);
                    //ios
                    mapProvIOS.put("cities",cityResultIOS);
                    mapProvIOS.put("state",address.getAreaName());
                    provResultIOS.add(mapProvIOS);
                }
            }
        }
    }
}

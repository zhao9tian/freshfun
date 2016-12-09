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
    public static List<Map<String, Object>> addressDateH5 = new ArrayList<Map<String, Object>>();//xgb 修改版代码,下同

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("*************************************开始初始化全国地址*************************************");
        //获取省级信息列表
        List<AddressUtilPOJO> addressProvList = addressUtilService.queryCityOrArea(0);
        //如果列表不为空，遍历取市级信息
        if (addressProvList != null && addressProvList.size() > 0) {
            for (AddressUtilPOJO address : addressProvList) {
                Map<String, Object> mapProvIOS = new HashMap<String, Object>();     //ios省级
                Map<String, Object> mapH5 = new HashMap<String, Object>();    //xgb
                mapH5.put("id",address.getAreaNo());
                mapH5.put("value",address.getAreaName());
                Map<String, Object> map = new HashMap<String, Object>();    //h5
                map.put("text", address.getAreaName());
                map.put("value", address.getAreaNo());
                provResult.add(map);
                List<AddressUtilPOJO> addressCityList = addressUtilService.queryCityOrArea(address.getAreaNo());
                //如果列表不为空，遍历取县区级信息
                if (addressCityList != null && addressCityList.size() > 0) {
                    List<Map<String, Object>> cityResult = new ArrayList<Map<String, Object>>();
                    List<Map<String, Object>> cityResultH5 = new ArrayList<Map<String, Object>>();//xgb
                    List<Map<String, Object>> cityResultIOS = new ArrayList<Map<String, Object>>();//ios市级

                    for (AddressUtilPOJO addressCity : addressCityList) {
                        Map<String, Object> mapCityH5 = new HashMap<String, Object>();//xgb
                        mapCityH5.put("id",addressCity.getAreaNo());
                        mapCityH5.put("value", addressCity.getAreaName());
                        Map<String, Object> mapCity = new HashMap<String, Object>();
                        mapCity.put("text", addressCity.getAreaName());
                        mapCity.put("value", addressCity.getAreaNo());
                        cityResult.add(mapCity);
                        List<AddressUtilPOJO> addressAreaList = addressUtilService.queryCityOrArea(addressCity.getAreaNo());
                        //如果列表不为空，遍历县区级信息添加map
                        if (addressAreaList != null && addressAreaList.size() > 0) {
                            List<String> distsResultIOS = new ArrayList<String>();     //ios县区级，名称
                            List<Map<String, Object>> distsResult = new ArrayList<Map<String, Object>>();
                            List<Map<String, Object>> distsResultH5 = new ArrayList<Map<String, Object>>();//xgb
                            for (AddressUtilPOJO addressArea : addressAreaList) {
                                Map<String, Object> mapD = new HashMap<String, Object>();
                                mapD.put("text", addressArea.getAreaName());
                                mapD.put("value", addressArea.getAreaNo());
                                distsResult.add(mapD);
                                //xgb
                                Map<String, Object> mapDistH5 = new HashMap<String, Object>();
                                mapDistH5.put("value", addressArea.getAreaName());
                                mapDistH5.put("id", addressArea.getAreaNo());
                                distsResultH5.add(mapDistH5);
                                //ios
                                distsResultIOS.add(addressArea.getAreaName()+","+addressArea.getAreaNo());
                            }
                            mapCityH5.put("child",distsResultH5);//xgb
                            cityResultH5.add(mapCityH5);
                            mapC.put(addressCity.getAreaNo().toString(), distsResult);
                            //ios
                            Map<String, Object> mapCityIOS = new HashMap<String, Object>();   //ios市级
                            mapCityIOS.put("areas",distsResultIOS);     //放入县区
                            mapCityIOS.put("city",addressCity.getAreaName()+","+addressCity.getAreaNo());   //放入市级
                            cityResultIOS.add(mapCityIOS);      //cities
                        }
                    }
                    //xgb
                    mapH5.put("child",cityResultH5);
                    addressDateH5.add(mapH5);
                    //h5
                    mapProv.put(address.getAreaNo().toString(), cityResult);
                    //ios
                    mapProvIOS.put("cities",cityResultIOS);
                    mapProvIOS.put("state",address.getAreaName()+","+address.getAreaNo());
                    provResultIOS.add(mapProvIOS);
                }
            }
        }
    }
}

package com.quxin.freshfun.controller.user;

import com.quxin.freshfun.model.pojo.address.AddressUtilPOJO;
import com.quxin.freshfun.service.address.AddressUtilService;
import com.quxin.freshfun.utils.AddressUtil;
import com.quxin.freshfun.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ziming on 2016/11/15.
 */
@Controller
@RequestMapping("/address")
public class AddressController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AddressUtilService addressUtilService;
    /**
     * 获取地址数据
     * @param parentNo 父级id
     */
    @ResponseBody
    @RequestMapping("/getAddressUtil")
    public Map<String, Object> getAddressUtil(String parentNo) {
        Map<String, Object> mapResult = new HashMap<String, Object>();
        List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
        if (parentNo == null || "".equals(parentNo)) {
            logger.warn("获取地址数据时，入参有误");
            return ResultUtil.fail(1004, "父级id入参有误");
        }
        List<AddressUtilPOJO> addressList = addressUtilService.queryCityOrArea(Integer.parseInt(parentNo.trim()));
        if (addressList != null && addressList.size() > 0) {
            for (AddressUtilPOJO address : addressList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("area", address.getAreaName());
                map.put("code", address.getAreaNo());
                listResult.add(map);
            }
        }
        mapResult.put("address", listResult);
        return ResultUtil.success(mapResult);
    }

    /**
     * 获取地址数据
     */
    @ResponseBody
    @RequestMapping("/getAddresses")
    public Map<String, Object> getAddresses(){
        Map<String, Object> mapResult = new HashMap<String, Object>();
        mapResult.put("provs_data", AddressUtil.provResult);
        mapResult.put("citys_data", AddressUtil.mapProv);
        mapResult.put("dists_data", AddressUtil.mapC);
        return ResultUtil.success(mapResult);
    }

    /**
     * 获取地址数据
     */
    @ResponseBody
    @RequestMapping("/addAddress")
    public Map<String,Object> addAddress(HttpServletRequest request){

        return null;
    }

}

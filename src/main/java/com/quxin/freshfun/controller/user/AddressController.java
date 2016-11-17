package com.quxin.freshfun.controller.user;

import com.quxin.freshfun.model.pojo.address.AddressPOJO;
import com.quxin.freshfun.model.pojo.address.AddressUtilPOJO;
import com.quxin.freshfun.service.address.AddressService;
import com.quxin.freshfun.service.address.AddressUtilService;
import com.quxin.freshfun.utils.AddressUtil;
import com.quxin.freshfun.utils.CookieUtil;
import com.quxin.freshfun.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @Autowired
    private AddressService addressService;

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
    @RequestMapping("/getAddressesIos")
    public Map<String, Object> getAddressesIos(){
        Map<String, Object> mapResult = new HashMap<String, Object>();
        mapResult.put("address_data", AddressUtil.provResultIOS);
        return ResultUtil.success(mapResult);
    }

    /**
     * 添加地址数据
     */
    @ResponseBody
    @RequestMapping(value = "/addAddress",method = {RequestMethod.POST})
    public Map<String,Object> addAddress(HttpServletRequest request, @RequestBody AddressPOJO address){
        Map<String,Object> map = new HashMap<String,Object>();
        Long userId = CookieUtil.getUserIdFromCookie(request);
        address.setUserId(userId);
        Integer result = addressService.addAddress(address);
        if(result>0){
            map.put("addressId",address.getId());
            return ResultUtil.success(map);
        }else
            return ResultUtil.fail(1004,"新增失败");
    }

    /**
     * 更新地址数据
     */
    @ResponseBody
    @RequestMapping(value = "/modifyAddress",method = {RequestMethod.POST})
    public Map<String,Object> modifyAddress(HttpServletRequest request, @RequestBody AddressPOJO address){
        Map<String,Object> map = new HashMap<String,Object>();
        Long userId = CookieUtil.getUserIdFromCookie(request);
        address.setUserId(userId);
        Integer result = addressService.modifyAddress(address);
        if(result>0){
            map.put("addressId",address.getId());
            return ResultUtil.success(map);
        }else
            return ResultUtil.fail(1004,"更新失败");
    }

    /**
     * 获取默认地址数据
     */
    @ResponseBody
    @RequestMapping("/getDefaultAddress")
    public Map<String,Object> getDefaultAddress(HttpServletRequest request){
        Map<String,Object> map = new HashMap<String,Object>();
        Long userId = CookieUtil.getUserIdFromCookie(request);
        AddressPOJO address = addressService.queryDefaultAddress(userId);
        if(address==null){
            List<AddressPOJO> addressList = addressService.queryAddress(userId);
            if(addressList!=null&&addressList.size()>0)
                address = addressList.get(0);
        }
        if(address!=null){
            map.put("addressId",address.getId());
            map.put("name",address.getName());
            map.put("tel",address.getTel());
            map.put("provCode",address.getProvCode());
            map.put("cityCode",address.getCityCode());
            map.put("distCode",address.getDistCode());
            String city = addressUtilService.queryNameByCode(address.getProvCode(),address.getCityCode(),address.getDistCode());
            map.put("city",city);
            map.put("address",address.getAddress());
            return ResultUtil.success(map);
        }else
            return ResultUtil.fail(1005,"获取默认收货失败");
    }

    /**
     * 获取地址数据
     */
    @ResponseBody
    @RequestMapping("/getAllAddress")
    public Map<String,Object> getAllAddress(HttpServletRequest request){

        Map<String,Object> result = new HashMap<String,Object>();
        List<Map<String,Object>> mapResult = new ArrayList<Map<String, Object>>();
        Long userId = CookieUtil.getUserIdFromCookie(request);
        AddressPOJO addressPOJO = addressService.queryDefaultAddress(userId);
        if(addressPOJO!=null){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("addressId",addressPOJO.getId());
            map.put("name",addressPOJO.getName());
            map.put("tel",addressPOJO.getTel());
            map.put("provCode",addressPOJO.getProvCode());
            map.put("cityCode",addressPOJO.getCityCode());
            map.put("distCode",addressPOJO.getDistCode());
            String city = addressUtilService.queryNameByCode(addressPOJO.getProvCode(),addressPOJO.getCityCode(),addressPOJO.getDistCode());
            map.put("city",city);
            map.put("address",addressPOJO.getAddress());
            map.put("isDefault",addressPOJO.getIsDefault());
            mapResult.add(map);
        }
        List<AddressPOJO> addressList = addressService.queryAddress(userId);
        if(addressList!=null&&addressList.size()>0){
            for(AddressPOJO address : addressList){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("addressId",address.getId());
                map.put("name",address.getName());
                map.put("tel",address.getTel());
                map.put("provCode",address.getProvCode());
                map.put("cityCode",address.getCityCode());
                map.put("distCode",address.getDistCode());
                String city = addressUtilService.queryNameByCode(address.getProvCode(),address.getCityCode(),address.getDistCode());
                map.put("city",city);
                map.put("address",address.getAddress());
                map.put("isDefault",address.getIsDefault());
                mapResult.add(map);
            }
            result.put("addressId",mapResult);
            return ResultUtil.success(result);
        }else
            return ResultUtil.fail(1004,"获取数据失败");
    }

    /**
     * 删除地址
     */
    @ResponseBody
    @RequestMapping("/removeAddress")
    public Map<String,Object> removeAddress(String addressId){
        if(addressId==null||"".equals(addressId)){
            logger.warn("删除地址入参有误");
            return ResultUtil.fail(1004,"地址删除失败，入参有误");
        }
        Integer result = addressService.removeAddress(Integer.parseInt(addressId));
        if(result==1){
            return ResultUtil.success(null);
        }else{
            return ResultUtil.fail(1004,"删除失败");
        }
    }

}

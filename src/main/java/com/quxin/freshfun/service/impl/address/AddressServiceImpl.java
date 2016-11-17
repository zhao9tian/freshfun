package com.quxin.freshfun.service.impl.address;

import com.quxin.freshfun.dao.AddressMapper;
import com.quxin.freshfun.model.pojo.address.AddressPOJO;
import com.quxin.freshfun.service.address.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ASus on 2016/11/17.
 */
@Service("addressService")
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressMapper addressMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 新增用户收货地址信息
     * @param address 用户收货地址信息
     * @return 受影响行数
     */
    @Override
    public Integer addAddress(AddressPOJO address) {
        if(address==null){
            logger.warn("新增用户收货地址信息入参有误");
            return 0;
        }
        if(address.getIsDefault()==1){
            Map<String ,Object> map = new HashMap<String,Object>();
            map.put("updated",System.currentTimeMillis()/1000);
            map.put("userId",address.getUserId());
            addressMapper.deleteDefaultAddress(map);
        }
        address.setCreated(System.currentTimeMillis()/1000);
        address.setUpdated(System.currentTimeMillis()/1000);
        return addressMapper.insertAddress(address);
    }

    @Override
    public Integer modifyAddress(AddressPOJO address) {
        if(address==null){
            logger.warn("修改用户收货地址信息入参有误");
            return 0;
        }
        if(address.getIsDefault()==1){
            Map<String ,Object> map = new HashMap<String,Object>();
            map.put("updated",System.currentTimeMillis()/1000);
            map.put("userId",address.getUserId());
            addressMapper.deleteDefaultAddress(map);
        }
        address.setUpdated(System.currentTimeMillis()/1000);
        return addressMapper.updateAddress(address);
    }

    @Override
    public Integer removeAddress(Integer addressId) {
        if(addressId==null||addressId==0){
            logger.warn("删除用户收货地址信息入参有误");
            return 0;
        }
        Map<String ,Object> map = new HashMap<String,Object>();
        map.put("updated",System.currentTimeMillis()/1000);
        map.put("addressId",addressId);
        return addressMapper.deleteAddress(map);
    }

    @Override
    public Integer removeDefaultAddress(Long userId) {
        if(userId==null||userId==0){
            logger.warn("删除用户收货地址信息入参有误");
            return 0;
        }
        Map<String ,Object> map = new HashMap<String,Object>();
        map.put("updated",System.currentTimeMillis()/1000);
        map.put("userId",userId);
        return addressMapper.deleteDefaultAddress(map);
    }

    @Override
    public List<AddressPOJO> queryAddress(Long userId) {
        if(userId==null||userId==0){
            logger.warn("获取用户收货地址信息入参有误");
            return null;
        }
        return addressMapper.selectAddress(userId);
    }

    @Override
    public AddressPOJO queryDefaultAddress(Long userId) {
        if(userId==null||userId==0){
            logger.warn("获取用户默认收货地址信息入参有误");
            return null;
        }
        return addressMapper.selectDefaultAddress(userId);
    }

    @Override
    public AddressPOJO queryAddressById(Integer addressId) {
        if(addressId==null||addressId==0){
            logger.warn("根据id获取收货地址信息入参有误");
            return null;
        }
        return addressMapper.selectAddressById(addressId);
    }
}

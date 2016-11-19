package com.quxin.freshfun.service.address;

import com.quxin.freshfun.model.pojo.address.AddressPOJO;

import java.util.List;
import java.util.Map;

/**
 * Created by ASus on 2016/11/17.
 */
public interface AddressService {
    /**
     * 新增用户收货地址信息
     * @param address 用户收货地址信息
     * @return 受影响行数
     */
    Integer addAddress(AddressPOJO address);

    /**
     * 修改用户收货地址信息
     * @param address 用户收货地址信息
     * @return 受影响行数
     */
    Integer modifyAddress(AddressPOJO address);

    /**
     * 删除收货地址
     * @param addressId 地址id
     * @return 受影响行数
     */
    Integer removeAddress(Integer addressId);

    /**
     * 取消默认收货地址
     * @param userId 用户id
     * @return 受影响行数
     */
    Integer removeDefaultAddress(Long userId);

    /**
     * 获取非默认地址列表
     * @param userId 用户id
     * @return 地址列表
     */
    List<AddressPOJO> queryAddress(Long userId);

    /**
     * 获取用户默认收货地址
     * @param userId 用户id
     * @return 默认收货地址
     */
    AddressPOJO queryDefaultAddress(Long userId);

    /**
     * 根据id获取地址信息
     * @param addressId 地址id
     * @return 地址信息
     */
    AddressPOJO queryAddressById(Integer addressId);
}

package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.pojo.address.AddressPOJO;

import java.util.List;
import java.util.Map;

/**
 * Created by Ziming
 * on 2016/11/16.
 * 用户地址管理mapper
 */
public interface AddressMapper {

    /**
     * 新增用户地址信息
     * @param address 用户地址信息
     * @return 受影响行数
     */
    Integer insertAddress(AddressPOJO address);

    /**
     * 更新收货地址
     * @param address 收货地址
     * @return 受影响行数
     */
    Integer updateAddress(AddressPOJO address);

    /**
     * 删除收货地址
     * @param map addressId，updated
     * @return 受影响行数
     */
    Integer deleteAddress(Map<String,Object> map);

    /**
     * 取消默认收货地址
     * @param map userId，updated
     * @return 受影响行数
     */
    Integer deleteDefaultAddress(Map<String,Object> map);

    /**
     * 获取非默认地址列表
     * @param userId 用户id
     * @return 地址列表
     */
    List<AddressPOJO> selectAddress(Long userId);

    /**
     * 获取用户默认收货地址
     * @param userId 用户id
     * @return 默认收货地址
     */
    AddressPOJO selectDefaultAddress(Long userId);
}

package com.quxin.freshfun.mongodb;

import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.OrderAddress;
@Repository
public interface AddressManagerMongo {
	/**
	 * 根据地址编号查询地址信息
	 * @param addressId
	 * @return
	 */
	OrderAddress findAddressById(String addressId);
}

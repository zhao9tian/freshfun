package com.quxin.freshfun.mongodb.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.OrderAddress;
import com.quxin.freshfun.mongodb.AddressManagerMongo;
@Repository
public class AddressManagerMongoImpl implements AddressManagerMongo {
	@Autowired
	private MongoTemplate mongoTemplate;
	/**
	 * 根据地址编号查询地址信息
	 */
	@Override
	public OrderAddress findAddressById(String addressId) {
		return mongoTemplate.findById(addressId,OrderAddress.class,"order_address");
	}
}
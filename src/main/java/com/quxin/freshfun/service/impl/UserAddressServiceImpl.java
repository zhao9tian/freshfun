package com.quxin.freshfun.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.dao.UserAddressMapper;
import com.quxin.freshfun.dao.UserMessageMapper;
import com.quxin.freshfun.dao.UsersMapper;
import com.quxin.freshfun.model.UserAddress;
import com.quxin.freshfun.model.UserMessage;
import com.quxin.freshfun.model.UsersPOJO;
import com.quxin.freshfun.service.UserAddressService;
@Service("userAddressService")
public class UserAddressServiceImpl implements UserAddressService {
	
	@Autowired
	private UserAddressMapper userAddressMapper;
	
	@Autowired
	private UserMessageMapper usersMessageMapper;

	@Override
	public List<UserAddress> userAddress(Long userId) {
		return userAddressMapper.selectAllByUserID(userId);
	}
	
	@Override
	public UserAddress userDefaultAddress(Long userId) {
		List<UserAddress> userAddresses = userAddressMapper.selectdefaultByUserID(userId);
		//获取默认地址
		if(userAddresses != null) {
			for (int i = 0; i < userAddresses.size(); i++) {
				if (userAddresses.get(i).getIsDefault() == 1) {
					return userAddresses.get(i);
				} else {
					userAddresses.get(i).setIsDefault(null);
				}
			}
		}
		//如果没有默认地址
		if(userAddresses != null && userAddresses.size() > 0){
			return userAddresses.get(0);
		}
		return null;
	}

	@Override
	public Integer addNewAddress(Map<String, Object> addressMap) {
		return userAddressMapper.insertNewAddress(addressMap);
	}

	@Override
	public Integer deleteByPrimaryKey(Integer id) {
		return userAddressMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Integer updateByPrimaryKey(UserAddress record) {
		return userAddressMapper.updateByPrimaryKeySelective(record);
	}


	@Override
	public Integer updateDefaultAddress(Long userId) {
		return userAddressMapper.updateIsDefault(userId);
	}

	@Override
	public Integer insertUserMessage(UserMessage userMessage) {
		return usersMessageMapper.insertSelective(userMessage);
	}

	@Override
	public UserAddress selectByPrimaryKey(Integer id) {
		return userAddressMapper.selectByPrimaryKey(id);
	}

}

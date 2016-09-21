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
@Service
public class UserAddressServiceImpl implements UserAddressService {
	
	@Autowired
	private UserAddressMapper userAddressMapper;
	
	@Autowired
	private UsersMapper usersMapper;
	
	@Autowired
	private UserMessageMapper usersMessageMapper;

	@Override
	public List<UserAddress> userAddress(Long userID) {
		return userAddressMapper.selectAllByUserID(userID);
	}
	
	@Override
	public List<UserAddress> userDefaultAddress(Long userID) {
		return userAddressMapper.selectdefaultByUserID(userID);
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
	public Integer updateUserAddress(UsersPOJO record) {
		return usersMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public Integer updateDefaultAddress(Long userID) {
		return userAddressMapper.updateIsDefault(userID);
	}

	@Override
	public Integer insertUserMessage(UserMessage userMessage) {
		return usersMessageMapper.insertSelective(userMessage);
	}

	@Override
	public UserAddress selectByPrimaryKey(Integer id) {
		return userAddressMapper.selectByPrimaryKey(id);
	}

	@Override
	public UsersPOJO findIsMobile(Long userID) {
		return usersMapper.findIsMobile(userID);
	}

	@Override
	public Integer updateUserIdentify(Long userID) {
		return usersMapper.updateUserIdentify(userID);
	}

}

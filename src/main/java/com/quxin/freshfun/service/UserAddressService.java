package com.quxin.freshfun.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.quxin.freshfun.model.UserAddress;
import com.quxin.freshfun.model.UserMessage;
import com.quxin.freshfun.model.UsersPOJO;

@Service
public interface UserAddressService {
	/**
	 * 显示用户地址
	 * @param userId
	 * @return
	 */
	public List<UserAddress> userAddress(Long userId);
	
	public UserAddress userDefaultAddress(Long userId);
	/**
	 * 添加用户地址
	 * @param addressMap
	 * @return
	 */
	public Integer addNewAddress(Map<String, Object> addressMap);
	/**
	 * 删除用户地址
	 * @param id
	 * @return
	 */
	public Integer deleteByPrimaryKey(Integer id);
	/**
	 * 修改用户地址
	 * @param record
	 * @return
	 */
	public Integer updateByPrimaryKey(UserAddress record);
	/**
	 * 修改默认地址
	 * @param userId
	 * @return
	 */
	public Integer updateDefaultAddress(Long userId);
	/**
	 * 添加反馈
	 * @param userMessage
	 * @return
	 */
	public Integer insertUserMessage(UserMessage userMessage);
	/**
	 * 根据地址id查询地址
	 * @param id
	 * @return
	 */
	public UserAddress selectByPrimaryKey(Integer id);

}

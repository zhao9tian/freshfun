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
	 * @param userID
	 * @return
	 */
	public List<UserAddress> userAddress(Long userID);
	
	public List<UserAddress> userDefaultAddress(Long userID);
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
	 * 修改用户表中地址
	 * @param record
	 * @return
	 */
	public Integer updateUserAddress(UsersPOJO record);
	/**
	 * 修改默认地址
	 * @param userID
	 * @return
	 */
	public Integer updateDefaultAddress(Long userID);
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
	/**
	 * 查询手机号
	 * @param userID
	 * @return
	 */
	public UsersPOJO findIsMobile(Long userID);
	
	/**
	 * 成为捕手
	 * @param user_id
	 * @return
	 */
	
	public Integer updateUserIdentify(Long userID);

}

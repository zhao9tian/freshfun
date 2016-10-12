package com.quxin.freshfun.dao;

import java.util.Map;

import com.quxin.freshfun.model.UserDetailPOJO;
import com.quxin.freshfun.model.UserInfoPOJO;
import org.apache.ibatis.annotations.Param;

import com.quxin.freshfun.model.Message;
import com.quxin.freshfun.model.UsersPOJO;

public interface UsersMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(UsersPOJO record);
    int insertSelective(UsersPOJO record);
    UsersPOJO selectByPrimaryKey(Long id);
    int updateByPrimaryKeySelective(UsersPOJO record);
    int updateByPrimaryKey(UsersPOJO record);
    int updateParentIdByUserId(Map<String, Long> params);
    UsersPOJO findEnterByID(Integer id);
    /**
     * 注册用户,在数据库新增用户信息
     * @param user
     * @return
     */
    public int insertUser(UsersPOJO user);

	/**
	 * 根据userId获取user
	 * @param id userId
	 * @return  用户信息
	 */
	UsersPOJO selectUserById(Long id);

    /**
     * Query entity according to id not user_id 
     * @param id
     * @return
     */
	public UsersPOJO getUser(Integer id);
	
	/**
	 * Query userId according to phone number
	 * @param phoneNum
	 * @return
	 */
	public Long getUserIdByPhoneNum(String phoneNum);
	/**
	 * Query deviceId according to phone number
	 * @param phoneNum
	 * @return
	 */
	public String getDeviceIdByPhoneNum(String phoneNum);
	/**
	 * update deviceId according to user_id
	 * @param map
	 * @return
	 */
	public Integer updateUser(Map<String, Object> map);
	
	/**
	 * Query userId according to wxId
	 * @param wxId
	 * @return
	 */
	public Long getUserIdByWxId(String wxId);
	/**
	 * 查询用户id
	 * @param userId
	 * @return
	 */
	public Integer selectId(Long userId);
	/**
	 * 修改用户标记
	 * @param parentId
	 * @return
	 */
	public Integer updateUserParentId(@Param("parentId") Long parentId,@Param("userId") Integer id);
	
	/**
     * 查询用户分享标记
     * @param userId
     * @return
     */
    UsersPOJO selectParentIdByUserId(Long userId);

	/**
	 * 查询用户头像和昵称
	 * @param userId
	 * @return
	 */
	UsersPOJO selectInfoByUserId(Long userId);
	
	/**
	 * 根据wxId获取wzId
	 * @param wxId
	 * @return
	 */
	public String getWzIdBywxId(String wxId);
	/**
	 * 根据wxId获取设备Id
	 * @param wxId
	 * @return
	 */
	public String getDeviceIdBywxId(String wxId);
	
	/**
	 * 插入短信信息到数据库
	 * @param message
	 * @return
	 */
	Integer insertMessage(Message message);
	
	/**
	 * 验证该用户是否绑定了手机号
	 * @param userId
	 * @return  返回1就是已经绑定了,返回0就是可以绑定
	 */
	Integer validateExistPhoneNum(String userId);
	
	/**
	 * 验证手机号是自己的还是别人的
	 * @param map 查询条件
	 * @return
	 */
	Integer validatePhoneNum(Map<String, Object> map);
	
	/**
	 * 验证短信验证码是否正确
	 * @param message
	 * @return 1-验证码正确  0-验证码不正确
	 */
	Integer validateCode(Message message);

	/**
	 * App验证验证码
	 * @param message
	 * @return
	 */
	String validateAppCode(Message message);
	/**
	 * 验证是否超时
	 * @param message
	 * @return
	 */
	Integer validateOvertime(Message message);
	
	/**
	 * 查询手机号是否为空
	 * @param user_id
	 * @return
	 */
	UsersPOJO findIsMobile(Long user_id);
	
	/**
	 * 成为捕手
	 * @param user_id
	 * @return
	 */
	Integer updateUserIdentify(Long user_id);

    /**
     * 插入用户微信信息
     * @param userDetailPOJO
     */
	int insertUserDetails(UserDetailPOJO userDetailPOJO);

	/**
	 * 定时删除验证码
	 * @return
	 */
	Integer deleteVerifyCode();
}
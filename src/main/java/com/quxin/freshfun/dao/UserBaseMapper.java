package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.pojo.UserBasePOJO;

import java.util.Map;

/**
 * Created by ziming on 2016/10/15.
 */
public interface UserBaseMapper {
    /**
     * 插入用户信息
     * @param userBase  用户信息对象
     * @return 受影响行数
     */
    Integer insertUserBaseInfo(UserBasePOJO userBase);

    /**
     * 根据userId查询上级捕手Id
     * @param userId userId
     * @return fetcherId
     */
    Long selectFetcherIdByUserId(Long userId);

    /**
     * 根据userId获取用户的信息（userId，头像，昵称，手机号）
     * @param userId userId
     * @return user对象
     */
    UserBasePOJO selectUserInfoByUserId(Long userId);

    /**
     * 根据userId获取用户手机号
     * @param userId userId
     * @return 手机号
     */
    String selectPhoneNumberByUserId(Long userId);

    /**
     * 根据userId判断该用户是否为捕手
     * @param userId userId
     * @return 总数
     */
    Integer validateIsFetcherByUserId(Long userId);

    /**
     * 更新用户成为捕手
     * @param map 手机号，用户id
     * @return 受影响行数
     */
    Integer updateToBeFetcher(Map<String,Object> map);

    /**
     * 更新用户信息（设备号，微站号，手机号），合并用户使用
     * @param map 设备号，微站号，手机号，用户id
     * @return 受影响行数
     */
    Integer updateUserToMesh(Map<String,Object> map);

    /**
     * 根据手机号查询userId
     * @param phoneNumber 手机号
     * @return 用户id
     */
    Long selectUserIdByPhoneNumber(String phoneNumber);

    /**
     * 根据手机号查询设备id
     * @param phoneNumber  手机号
     * @return  设备id
     */
    String selectDeviceIdByPhoneNumber(String phoneNumber);

    /**
     * 根据微信id查询用户id
     * @param unionId  微信id
     * @return 用户id
     */
    Long selectUserIdByUnionId(String unionId);

    /**
     * 根据微信id查询微站id
     * @param unionId  微信id
     * @return 微站id
     */
    String selectOpenIdByUnionId(String unionId);

    /**
     * 根据微信id查询设备号
     * @param unionId  微信id
     * @return 设备号
     */
    String selectDeviceIdByUnionId(String unionId);

    /**
     * 根据userId查询微站id
     * @param userId  用户id
     * @return 微站id
     */
    String selectOpenIdByUserId(Long userId);
}

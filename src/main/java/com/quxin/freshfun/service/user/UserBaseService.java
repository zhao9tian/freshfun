package com.quxin.freshfun.service.user;

import com.quxin.freshfun.model.pojo.UserBasePOJO;

import java.util.Map;

/**
 * Created by ziming on 2016/10/15.
 */
public interface UserBaseService {
    /**
     * 插入用户信息
     * @param userBase  用户信息对象
     * @return 受影响行数
     */
    Integer addUserBaseInfo(UserBasePOJO userBase);

    /**
     * 根据userId查询上级捕手Id
     * @param userId userId
     * @return fetcherId
     */
    Long queryFetcherIdByUserId(Long userId);

    /**
     * 为用户添加父级捕手id
     * @param userId 用户id
     * @param fetcherId 捕手id
     * @return 受影响行数
     */
    Integer modifyFetcherForUser(Long userId,Long fetcherId);

    /**
     * 根据userId获取用户的信息（userId，头像，昵称，手机号）
     * @param userId userId
     * @param type 0是微站，1是APP
     * @return map对象（userId，头像，昵称，手机号）
     */
    Map<String,Object> queryUserInfoByUserId(Long userId,Integer type);

    /**
     * 根据userId获取用户手机号
     * @param userId userId
     * @return 手机号
     */
    String queryPhoneNumberByUserId(Long userId);

    /**
     * 根据userId判断该用户是否为捕手
     * @param userId userId
     * @return 结果
     */
    boolean checkIsFetcherByUserId(Long userId);

    /**
     * 更新用户成为捕手
     * @param phoneNumber 手机号
     * @param userId   用户id
     * @return 受影响行数
     */
    Integer modifyToBeFetcher(String phoneNumber,Long userId);

    /**
     * 更新用户信息（设备号，微站号，手机号），合并用户使用
     * @param userId 用户id
     * @param deviceId 设备号
     * @param openId 微站号
     * @param phoneNumber 手机号
     * @return 受影响行数
     */
    Integer modifyUserToMesh(Long userId,String deviceId,String openId,String phoneNumber);

    /**
     * 根据手机号查询userId
     * @param phoneNumber 手机号
     * @return 用户id
     */
    Long queryUserIdByPhoneNumber(String phoneNumber);

    /**
     * 根据手机号查询设备号
     * @param phoneNumber 手机号
     * @return 设备号
     */
    String queryDeviceIdByPhoneNumber(String phoneNumber);

    /**
     * 根据微信id查询用户id
     * @param unionId  微信id
     * @return 用户id
     */
    Long queryUserIdByUnionId(String unionId);

    /**
     * 根据微信id查询微站id
     * @param unionId  微信id
     * @return 微站id
     */
    String queryOpenIdByUnionId(String unionId);

    /**
     * 根据微信id查询设备号
     * @param unionId  微信id
     * @return 设备号
     */
    String queryDeviceIdByUnionId(String unionId);

    /**
     * 根据userId查询微站id
     * @param userId  用户id
     * @return 微站id
     */
    String queryOpenIdByUserId(Long userId);

    /**
     * 校验userId是否有效
     * @param userId 用户id
     * @return 总数
     */
    Integer checkUserId(Long userId);

    /**
     * 根据设备号查询
     * @param deviceId  设备号
     * @return userID
     */
    Long queryUserIdByDeviceId(String deviceId);
}

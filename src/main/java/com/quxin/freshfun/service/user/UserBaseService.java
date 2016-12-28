package com.quxin.freshfun.service.user;

import com.quxin.freshfun.model.outparam.UserInfoOutParam;
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
    Long addUserBaseInfo(UserBasePOJO userBase);

    /**
     * 为用户添加父级捕手id
     * @param userId 用户id
     * @param fetcherId 捕手id
     * @return 受影响行数
     */
    Integer modifyFetcherForUser(Long userId,Long fetcherId);

    /**
     * 根据userId获取用户的信息
     * @param userId userId
     * @return user对象
     */
    UserInfoOutParam queryUserInfoByUserId(Long userId);

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
     * 校验userId是否有效
     * @param userId 用户id
     * @return 总数
     */
    Integer checkUserId(Long userId);

    /**
     * 根据设备号查询userId
     * @param deviceId  设备号
     * @return userID
     */
    Long queryUserIdByDeviceId(String deviceId);

    /**
     * 查询昵称的使用次数
     * @param nickName  昵称
     * @return  目标用户信息
     */
    UserBasePOJO queryUserNameCount(String nickName);

    /**
     * 更新随机昵称的使用次数
     * @param userId  用户id
     * @param  count 使用次数
     * @return  受影响行数
     */
    Integer modifyUserNameCount(Long userId,Integer count);

    /**
     * 根据手机号查询用户信息
     * @param phoneNumber  手机号
     * @return 用户信息
     */
    UserInfoOutParam queryUserInfoByPhoneNumber(String phoneNumber);

    /**
     * 根据微信id查询用户信息
     * @param unionId  微信id
     * @return 用户信息
     */
    UserInfoOutParam queryUserInfoByUnionId(String unionId);

    /**
     * 更新用户的appId，如果用户的appId为888888则更新，否则不更新
     * @param userId 用户id
     * @param appId appId
     * @return 受影响行数
     */
    Integer modifyUserAppId(Long userId,Long appId);
}

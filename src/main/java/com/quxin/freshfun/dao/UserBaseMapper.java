package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.outparam.UserInfoOutParam;
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
     * 根据id更新userId
     * @param id
     * @return
     */
    Integer updateUserIdById(Long id);

    /**
     * 根据userId查询用户信息
     * @param userId  userId
     * @return 用户信息
     */
    UserInfoOutParam selectUserInfoByUserId(Long userId);
    /**
     * 根据手机号查询用户信息
     * @param phoneNumber  手机号
     * @return 用户信息
     */

    UserInfoOutParam selectUserInfoByPhoneNumber(String phoneNumber);
    /**
     * 根据微信id查询用户信息
     * @param unionId  微信id
     * @return 用户信息
     */

    UserInfoOutParam selectUserInfoByUnionId(String unionId);

    /**
     * 为用户添加父级捕手id
     * @param map 参数map（userId，fetcherId）
     * @return 受影响行数
     */
    Integer updateFetcherForUser(Map<String,Object> map);

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
     * 根据设备号查询userId
     * @param deviceId  设备号
     * @return userId
     */
    Long selectUserIdByDeviceId(String deviceId);

    /**
     * 校验userId是否有效
     * @param userId 用户id
     * @return 总数
     */
    Integer validateUserId(Long userId);

    /**
     * 查询昵称的使用次数
     * @param nickName  昵称
     * @return  目标用户信息
     */
    UserBasePOJO selectUserNameCount(String nickName);

    /**
     * 更新随机昵称的使用次数
     * @param map  参数map
     * @return  受影响行数
     */
    Integer updateUserNameCount(Map<String,Object> map);
}

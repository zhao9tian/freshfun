package com.quxin.freshfun.service.impl.user;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.dao.UserBaseMapper;
import com.quxin.freshfun.model.outparam.UserInfoOutParam;
import com.quxin.freshfun.model.pojo.UserBasePOJO;
import com.quxin.freshfun.service.user.UserBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created bg on 2016/10/15.
 * 用户信息Service实现类
 */
@Service("userBaseService")
public class UserBaseServiceImpl implements UserBaseService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    //声明dao层接口对象
    @Autowired
    private UserBaseMapper userBaseMapper;    //用户Dao层接口

     /**
     * 插入用户信息
     * @param userBase  用户信息对象
     * @return 受影响行数
     */
     public Integer addUserBaseInfo(UserBasePOJO userBase) {
         //校验参数
         boolean checkPram = true;
         if (userBase == null) {
             logger.warn("插入用户信息时，入参为空");
             checkPram = false;
         } else {
             if (userBase.getOpenId() == null || "".equals(userBase.getOpenId())) {
                 logger.warn("插入用户信息时，入参openId为空");
                 checkPram = false;
             } else if (userBase.getUnionId() == null || "".equals(userBase.getUnionId())) {
                 logger.warn("插入用户信息时，入参unionId为空");
                 checkPram = false;
             } else if (userBase.getUserHeadImg() == null || "".equals(userBase.getUserHeadImg()) || userBase.getUserName() == null || "".equals(userBase.getUserName())) {
                 logger.warn("插入用户信息时，入参头像信息或昵称为空");
             }
         }
         if (checkPram) {
             Integer resultId = userBaseMapper.insertUserBaseInfo(userBase);
             Integer resultUserId = userBaseMapper.updateUserIdById(userBase.getId());
             if (resultId == 1 && resultUserId == 1)
                 return 1;
             else
                 return 0;
         }
         return 0;
     }

    /**
     * 校验userId是否有效
     * @param userId 用户id
     * @return 总数
     */
    public Integer checkUserId(Long userId) {
        if (userId == null || userId == 0) {
            logger.warn("校验userId是否有效时，userId为空");
            return null;
        } else {
            Integer count = userBaseMapper.validateUserId(userId);
            return count;
        }
    }

    /**
     * 根据userId获取用户的信息
     * @param userId userId
     * @return user对象
     */
    @Override
    public UserInfoOutParam queryUserInfoByUserId(Long userId) {
        if (userId == null || userId == 0) {
            logger.warn("根据userId获取用户的信息（userId，头像，昵称，手机号）时，userId为空");
            return null;
        } else {
            UserInfoOutParam user = userBaseMapper.selectUserInfoByUserId(userId);
            if (user == null) {
                logger.warn("根据userId获取用户的信息时，user不存在或已注销");
                return null;
            } else {
                return user;
            }
        }
    }

    /**
     * 根据userId判断该用户是否为捕手
     * @param userId userId
     * @return 结果
     */
    @Override
    public boolean checkIsFetcherByUserId(Long userId) {
        if (userId == null||userId==0) {
            logger.warn("根据userId判断该用户是否为捕手时，userId为空");
            return false;
        } else {
            Integer count = userBaseMapper.validateIsFetcherByUserId(userId);
            if (count == 1) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 更新用户成为捕手
     * @param phoneNumber 手机号
     * @param userId   用户id
     * @return 受影响行数
     */
    @Override
    public Integer modifyToBeFetcher(String phoneNumber, Long userId) {
        if (userId == null || userId == 0) {
            logger.warn("更新用户成为捕手时，userId为空");
            return null;
        } else if (phoneNumber == null || "".equals(phoneNumber)) {
            logger.warn("更新用户成为捕手时，手机号为空");
            return null;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("phoneNumber", phoneNumber);
            return userBaseMapper.updateToBeFetcher(map);
        }
    }

    /**
     * 更新用户信息（设备号，微站号，手机号），合并用户使用
     * @param userId 用户id
     * @param deviceId 设备号
     * @param openId 微站号
     * @param phoneNumber 手机号
     * @return 受影响行数
     */
    @Override
    public Integer modifyUserToMesh(Long userId, String deviceId, String openId, String phoneNumber) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean pramBoo = false;
        if (userId == null || userId == 0) {
            logger.error("更新用户信息（设备号，微站号，手机号）时，userId为空");
            return null;
        }
        map.put("userId", userId);
        if (deviceId != null && !"".equals(deviceId)) {
            map.put("deviceId", deviceId);
            pramBoo = true;
        }
        if (openId != null && !"".equals(openId)) {
            map.put("openId", openId);
            pramBoo = true;
        }
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            map.put("phoneNumber", phoneNumber);
            pramBoo = true;
        }
        if (pramBoo) {
            return userBaseMapper.updateUserToMesh(map);
        } else {
            return null;
        }
    }

    /**
     * 为用户添加父级捕手id
     * @param userId 用户id
     * @param fetcherId 捕手id
     * @return 受影响行数
     */
    @Override
    public Integer modifyFetcherForUser(Long userId, Long fetcherId) {
        if (userId == null || fetcherId == null) {
            logger.warn("为用户添加父级捕手id时，入参有误");
            return null;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            map.put("fetcherId", fetcherId);
            return userBaseMapper.updateFetcherForUser(map);
        }
    }

    /**
     * 根据设备号查询userId
     * @param deviceId  设备号
     * @return userID
     */
    @Override
    public Long queryUserIdByDeviceId(String deviceId) {
        if (deviceId == null || "".equals(deviceId)) {
            logger.error("根据设备号查询userId时，deviceId为空");
            return null;
        } else {
            return userBaseMapper.selectUserIdByDeviceId(deviceId);
        }
    }

    /**
     * 查询昵称的使用次数
     * @param nickName  昵称
     * @return  目标用户信息
     */
    @Override
    public UserBasePOJO queryUserNameCount(String nickName){
        if(nickName==null||"".equals(nickName)){
            return null;
        }
        UserBasePOJO userInfo = userBaseMapper.selectUserNameCount(nickName);
        return userInfo;
    }

    /**
     * 更新昵称的使用次数
     * @param userId  用户id
     * @param count  使用次数
     * @return  受影响行数
     */
    @Override
    public Integer modifyUserNameCount(Long userId,Integer count){
        if(userId==null||userId==0||count==null){
            return 0;
        }
        Map<String,Object> mapUser = new HashMap<String,Object>();
        mapUser.put("userId",userId);
        mapUser.put("count",count);
        return userBaseMapper.updateUserNameCount(mapUser);
    }

    /**
     * 根据手机号查询用户信息
     * @param phoneNumber  手机号
     * @return 用户信息
     */
    @Override
    public UserInfoOutParam queryUserInfoByPhoneNumber(String phoneNumber) {
        if (phoneNumber==null||"".equals(phoneNumber)){
            return null;
        }
        return userBaseMapper.selectUserInfoByPhoneNumber(phoneNumber);
    }

    /**
     * 根据微信id查询用户信息
     * @param unionId  微信id
     * @return 用户信息
     */
    @Override
    public UserInfoOutParam queryUserInfoByUnionId(String unionId) {
        if (unionId==null||"".equals(unionId)){
            return null;
        }
        return userBaseMapper.selectUserInfoByUnionId(unionId);
    }
}

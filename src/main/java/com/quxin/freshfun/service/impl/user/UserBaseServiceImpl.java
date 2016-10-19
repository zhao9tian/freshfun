package com.quxin.freshfun.service.impl.user;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.dao.UserBaseMapper;
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
    public Integer addUserBaseInfo(UserBasePOJO userBase){
        //校验参数
        boolean checkPram = true;
        if(userBase==null){
            logger.error("插入用户信息时，入参为空");
            checkPram=false;
        }else{
            if(userBase.getOpenId()==null||"".equals(userBase.getOpenId())){
                logger.error("插入用户信息时，入参openId为空");
                checkPram=false;
            }else if(userBase.getUnionId()==null||"".equals(userBase.getUnionId())){
                logger.error("插入用户信息时，入参unionId为空");
                checkPram=false;
            }else if(userBase.getUserHeadImg()==null||"".equals(userBase.getUserHeadImg())||userBase.getUserName()==null||"".equals(userBase.getUserName())){
                logger.error("插入用户信息时，入参头像信息或昵称为空");
            }
        }
        if(checkPram){
            return userBaseMapper.insertUserBaseInfo(userBase);
        }
        return 0;
    }
    /**
     * 根据userId查询上级捕手Id
     * @param userId userId
     * @return fetcherId
     */
    public Long queryFetcherIdByUserId(Long userId){
        if(userId==null){
            logger.error("根据userId查询上级捕手Id时，userId为空");
            return null;
        }else{
            Long fetcherId = userBaseMapper.selectFetcherIdByUserId(userId);
            if(fetcherId==null){
                logger.error("根据userId查询上级捕手Id时，user不存在或已注销");
            }
            return fetcherId;
        }
    }

    /**
     * 根据userId获取用户的信息（userId，头像，昵称，手机号）    --app使用
     * @param userId userId
     * @return map对象（userId，头像，昵称，手机号）
     */
    public Map<String,Object> queryUserInfoByUserId(Long userId){
        if(userId==null){
            logger.error("根据userId获取用户的信息（userId，头像，昵称，手机号）时，userId为空");
            return null;
        }else{
            UserBasePOJO user = userBaseMapper.selectUserInfoByUserId(userId);
            if(user==null){
                logger.error("根据userId获取用户的信息手机号时，user不存在或已注销");
                return null;
            }else{
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("userId",user.getUserId());
                map.put("userName",user.getUserName());
                map.put("userHeadImg",user.getUserHeadImg());
                map.put("phoneNumber",user.getPhoneNumber());
                return map;
            }
        }
    }

    /**
     * 根据userId获取用户手机号
     * @param userId userId
     * @return 手机号
     */
    public String queryPhoneNumberByUserId(Long userId){
        if(userId==null){
            logger.error("根据userId获取用户的信息手机号时，userId为空");
            return null;
        }else{
            String phoneNumber = userBaseMapper.selectPhoneNumberByUserId(userId);
            if(phoneNumber==null){
                logger.error("根据userId获取用户的信息手机号时，user不存在或已注销");
            }
            return phoneNumber;
        }
    }

    /**
     * 根据userId判断该用户是否为捕手
     * @param userId userId
     * @return 结果
     */
    public boolean checkIsFetcherByUserId(Long userId){
        if(userId==null){
            logger.error("根据userId判断该用户是否为捕手时，userId为空");
            return false;
        }else{
            Integer count = userBaseMapper.validateIsFetcherByUserId(userId);
            if(count==1){
                return true;
            }else{
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
    public Integer modifyToBeFetcher(String phoneNumber,Long userId){
        if(userId==null){
            logger.error("更新用户成为捕手时，userId为空");
            return null;
        }else if(phoneNumber==null||"".equals(phoneNumber)){
            logger.error("更新用户成为捕手时，手机号为空");
            return null;
        }else{
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("userId",userId);
            map.put("phoneNumber",phoneNumber);
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
    public Integer modifyUserToMesh(Long userId,String deviceId,String openId,String phoneNumber){
        Map<String,Object> map = new HashMap<String,Object>();
        boolean pramBoo = false;
        if(userId==null){
            logger.error("更新用户信息（设备号，微站号，手机号）时，userId为空");
            return null;
        }
        map.put("userId",userId);
        if(deviceId!=null&&!"".equals(deviceId)){
            map.put("deviceId",deviceId);
            pramBoo=true;
        }
        if(openId!=null&&!"".equals(openId)){
            map.put("openId",openId);
            pramBoo=true;
        }
        if(phoneNumber!=null&&!"".equals(phoneNumber)){
            map.put("phoneNumber",phoneNumber);
            pramBoo=true;
        }
        if(pramBoo){
            return userBaseMapper.updateUserToMesh(map);
        }else{
            return null;
        }
    }

    /**
     * 根据手机号查询userId
     * @param phoneNumber 手机号
     * @return 用户id
     */
    public Long queryUserIdByPhoneNumber(String phoneNumber){
        if(phoneNumber==null||"".equals(phoneNumber)){
            logger.error("根据手机号查询userId时，手机号phoneNumber为空");
            return 0l;
        }else{
            return userBaseMapper.selectUserIdByPhoneNumber(phoneNumber);
        }
    }

    /**
     * 根据手机号查询设备号
     * @param phoneNumber 手机号
     * @return 设备号
     */
    public String queryDeviceIdByPhoneNumber(String phoneNumber){
        if(phoneNumber==null||"".equals(phoneNumber)){
            logger.error("根据手机号查询设备号时，手机号phoneNumber为空");
            return null;
        }else{
            return userBaseMapper.selectDeviceIdByPhoneNumber(phoneNumber);
        }
    }

    /**
     * 根据微信id查询用户id
     * @param unionId  微信id
     * @return 用户id
     */
    public Long queryUserIdByUnionId(String unionId){
        if(unionId==null||"".equals(unionId)){
            logger.error("根据微信id查询用户id时，unionId为空");
            return null;
        }else{
            return userBaseMapper.selectUserIdByUnionId(unionId);
        }
    }

    /**
     * 根据微信id查询微站id
     * @param unionId  微信id
     * @return 微站id
     */
    public String queryOpenIdByUnionId(String unionId){
        if(unionId==null||"".equals(unionId)){
            logger.error("根据微信id查询微站id时，unionId为空");
            return null;
        }else{
            return userBaseMapper.selectOpenIdByUnionId(unionId);
        }
    }

    /**
     * 根据微信id查询设备号
     * @param unionId  微信id
     * @return 设备号
     */
    public String queryDeviceIdByUnionId(String unionId){
        if(unionId==null||"".equals(unionId)){
            logger.error("根据微信id查询设备号时，unionId为空");
            return null;
        }else{
            return userBaseMapper.selectDeviceIdByUnionId(unionId);
        }
    }

    /**
     * 根据userId查询微站id
     * @param userId  用户id
     * @return 微站id
     */
    public String queryOpenIdByUserId(Long userId){
        if(userId==null){
            logger.error("根据userId查询微站id时，userId为空");
            return null;
        }else{
            return userBaseMapper.selectOpenIdByUserId(userId);
        }
    }
}

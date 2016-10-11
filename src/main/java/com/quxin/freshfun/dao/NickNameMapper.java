package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.UsersPOJO;
import com.quxin.freshfun.model.pojo.NickNamePOJO;

import java.util.Map;

/**
 * Created by ziming on 2016/10/10.
 */
public interface NickNameMapper {
    /**
     * 获取随机昵称
     * @return  随机昵称
     */
    NickNamePOJO selectRandNickName();

    /**
     * 查询随机昵称的使用次数
     * @param map  参数map
     * @return  目标用户信息
     */
    UsersPOJO selectNickNameCount(Map<String,Object> map);

    /**
     * 更新随机昵称的使用次数
     * @param map  参数map
     * @return  受影响行数
     */
    Integer updateNickNameCount(Map<String,Object> map);
}

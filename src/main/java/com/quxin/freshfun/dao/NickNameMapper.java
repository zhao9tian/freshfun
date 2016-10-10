package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.pojo.NickNamePOJO;

/**
 * Created by ziming on 2016/10/10.
 */
public interface NickNameMapper {
    /**
     * 获取随机昵称
     * @return  随机昵称
     */
    NickNamePOJO selectRandNickName();
}

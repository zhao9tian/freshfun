package com.quxin.freshfun.service.impl.user;

import com.quxin.freshfun.dao.NickNameMapper;
import com.quxin.freshfun.model.pojo.NickNamePOJO;
import com.quxin.freshfun.service.user.NickNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ziming on 2016/10/10.
 */
@Service("nickNameService")
public class NickNameServiceImpl implements NickNameService{
    @Autowired
    private NickNameMapper nickNameMapper;
    /**
     * 获取随机昵称
     * @return  随机昵称
     */
    public NickNamePOJO queryRandNickName(){
        NickNamePOJO nickName = nickNameMapper.selectRandNickName();
        return nickName;
    }

}

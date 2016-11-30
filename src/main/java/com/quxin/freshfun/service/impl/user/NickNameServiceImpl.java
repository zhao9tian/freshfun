package com.quxin.freshfun.service.impl.user;

import com.quxin.freshfun.dao.NickNameMapper;
import com.quxin.freshfun.model.UsersPOJO;
import com.quxin.freshfun.model.pojo.NickNamePOJO;
import com.quxin.freshfun.model.pojo.UserBasePOJO;
import com.quxin.freshfun.service.user.NickNameService;
import com.quxin.freshfun.service.user.UserBaseService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ziming on 2016/10/10.
 */
@Service("nickNameService")
public class NickNameServiceImpl implements NickNameService{
    @Autowired
    private NickNameMapper nickNameMapper;

    @Autowired
    private UserBaseService userBaseService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取随机昵称
     * @return  随机昵称
     */
    @Override
    public String queryRandNickName(String userName){
        //获取随机昵称nickName
        String nickNameStr = userName==null?nickNameMapper.selectRandNickName().getNickName():userName;
        UserBasePOJO user = null;
        do{
            //查询nickName在users表中的使用次数
            user = userBaseService.queryUserNameCount(nickNameStr);

            if(user!=null){//已被使用
                logger.warn("获取随机昵称使用次数:"+user.getUserNameCount());
                nickNameStr = nickNameStr + "_" + user.getUserNameCount().toString();
                //更新受影响行数
                Integer result = userBaseService.modifyUserNameCount(user.getUserId(),user.getUserNameCount()+1);
            }
        }while (user!=null);
        return nickNameStr;
    }

}

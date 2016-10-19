package com.quxin.freshfun.service.impl.user;

import com.quxin.freshfun.dao.NickNameMapper;
import com.quxin.freshfun.model.UsersPOJO;
import com.quxin.freshfun.model.pojo.NickNamePOJO;
import com.quxin.freshfun.model.pojo.UserBasePOJO;
import com.quxin.freshfun.service.user.NickNameService;
import org.apache.commons.collections.map.HashedMap;
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
            Map<String,Object> map = new HashMap<String,Object>();
            Map<String,Object> mapUser = new HashMap<String,Object>();
            map.put("nickName",nickNameStr);
            //查询nickName在users表中的使用次数
            user = nickNameMapper.selectNickNameCount(map);

            if(user!=null){//已被使用
                System.out.println("user.getNicknameCount():"+user.getUserNameCount());
                nickNameStr = nickNameStr + "_" + user.getUserNameCount().toString();
                mapUser.put("userId",user.getId());
                mapUser.put("count",user.getUserNameCount()+1);
                //更新受影响行数
                Integer result = nickNameMapper.updateNickNameCount(mapUser);
                System.out.println("受影响行数："+result);
            }
        }while (user!=null);
        return nickNameStr;
    }

}

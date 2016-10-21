package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.Message;
import com.quxin.freshfun.model.UsersPOJO;
import com.quxin.freshfun.model.pojo.NickNamePOJO;

import java.util.Map;

/**
 * Created by ziming on 2016/10/19.
 */
public interface IdentifiedCodeMapper {
    /**
     * 插入手机验证码
     * @param message 验证码对象
     * @return 受影响行数
     */
    Integer insertMessageInfo(Message message);

    /**
     * 根据验证码信息获取手机号(wz)
     * @param map 参数map(phoneNumber,code)
     * @return 总数
     */
    Integer validateCode(Map<String,Object> map);

    /**
     * 根据验证码信息获取手机号(app)
     * @param map 参数map(tooken,code)
     * @return 手机号
     */
    String selectPhoneNumberByCode(Map<String,Object> map);

    /**
     * 验证验证码是否过期
     * @param map 参数map(phoneNumber,code-可空)
     * @return id
     */
    Integer validateCodeOvertime(Map<String,Object> map);

    /**
     * 删除2分钟前的验证码
     * @return 受影响行数
     */
    Integer deleteIdentifiedCode();


}

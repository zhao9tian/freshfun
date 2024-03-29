package com.quxin.freshfun.service.user;

import com.quxin.freshfun.utils.BusinessException;

import java.util.Map;

/**
 * Created by Ziming on 2016/10/19.
 */
public interface IdentifiedCodeService {

    /**
     * 插入手机验证码
     * @param phoneNumber 手机号
     * @param code 验证码
     * @param token 用户凭证
     * @return 受影响行数
     */
    Integer insertMessageInfo(String phoneNumber,String code,String token);

    /**
     * 根据验证码信息获取手机号(wz)
     * @param phoneNumber 手机号
     * @param code  验证码
     * @return 总数
     */
    Integer checkCode(String phoneNumber,String code);

    /**
     * 根据验证码信息获取手机号(app)
     * @param token 用户凭证
     * @param code  验证码
     * @return 手机号
     */
    String queryPhoneNumberByCode(String token,String code);

    /**
     * 验证验证码是否过期
     * @param phoneNumber 手机号
     * @param code 验证码（可空）
     * @return 是或否
     */
    boolean checkCodeOvertime(String phoneNumber,String code);

    /**
     * 删除2分钟前的验证码
     * @return 受影响行数
     */
    Integer removeIdentifiedCode();

    /**
     * 生成验证码
     * @param phone 手机号
     * @return token凭证
     */
    public String genertCode(String phone) throws BusinessException;

    /**
     *
     * @param userId
     * @param phoneNum
     * @return
     */
    Integer sentVerifyCode(String userId, String phoneNum);
}

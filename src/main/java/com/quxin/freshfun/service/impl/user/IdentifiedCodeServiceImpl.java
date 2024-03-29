package com.quxin.freshfun.service.impl.user;

import com.quxin.freshfun.dao.IdentifiedCodeMapper;
import com.quxin.freshfun.model.Message;
import com.quxin.freshfun.model.outparam.UserInfoOutParam;
import com.quxin.freshfun.service.user.IdentifiedCodeService;
import com.quxin.freshfun.service.user.UserBaseService;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.DateUtils;
import com.quxin.freshfun.utils.IdGenerate;
import com.quxin.freshfun.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASus on 2016/10/19.
 */
@Service("identifiedCodeService")
public class IdentifiedCodeServiceImpl implements IdentifiedCodeService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private IdentifiedCodeMapper identifiedCodeMapper;
    @Autowired
    private UserBaseService userBaseService;

    /**
     * 插入手机验证码
     * @param phoneNumber 手机号
     * @param code 验证码
     * @param token 用户凭证
     * @return 受影响行数
     */
    public Integer insertMessageInfo(String phoneNumber, String code, String token) {
        if ((phoneNumber == null || "".equals(phoneNumber)) || (code == null || "".equals(code))) {
            logger.error("插入手机验证码时，入参有误");
            return null;
        } else {
            Message message = new Message();
            message.setCode(code);
            message.setPhoneNum(phoneNumber);
            message.setToken(token);
            message.setDate(System.currentTimeMillis() / 1000);
            return identifiedCodeMapper.insertMessageInfo(message);
        }
    }

    /**
     * 根据验证码信息获取手机号(wz)
     * @param phoneNumber 手机号
     * @param code  验证码
     * @return 总数
     */
    public Integer checkCode(String phoneNumber, String code) {
        if ((phoneNumber == null || "".equals(phoneNumber)) || (code == null || "".equals(code))) {
            logger.error("根据验证码信息获取手机号(wz)时，入参有误");
            return null;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", code);
            map.put("phoneNumber", phoneNumber);
            return identifiedCodeMapper.validateCode(map);
        }
    }

    /**
     * 根据验证码信息获取手机号(app)
     * @param token 用户凭证
     * @param code  验证码
     * @return 手机号
     */
    public String queryPhoneNumberByCode(String token, String code) {
        if ((token == null || "".equals(token)) || (code == null || "".equals(code))) {
            logger.error("根据验证码信息获取手机号(app)时，入参有误");
            return null;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", code);
            map.put("token", token);
            return identifiedCodeMapper.selectPhoneNumberByCode(map);
        }
    }

    /**
     * 验证验证码是否过期
     * @param phoneNumber 手机号
     * @param code 验证码（可空）
     * @return 是或否
     */
    public boolean checkCodeOvertime(String phoneNumber, String code) {
        if (phoneNumber == null || "".equals(phoneNumber)) {
            logger.error("验证验证码是否过期时，入参有误");
            return false;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", code);
            map.put("phoneNumber", phoneNumber);
            Integer result = identifiedCodeMapper.validateCodeOvertime(map);
            if (result == null)
                return false;
            else
                return true;
        }
    }

    /**
     * 删除2分钟前的验证码
     * @return 受影响行数
     */
    public Integer removeIdentifiedCode(){
        Integer result = identifiedCodeMapper.deleteIdentifiedCode();
        if (result==0){
            logger.error("删除2分钟前的验证码时，删除失败");
        }
        return result;
    }

    /**
     * 生成验证码
     * @return
     */
    @Override
    public String genertCode(String phone) throws BusinessException {
        //发送短信
        String code = MessageUtils.createMessage(phone);
        IdGenerate idGenerate = new IdGenerate();
        String token = idGenerate.generateStr();

        Message message = new Message();
        message.setCode(code);
        message.setDate(DateUtils.getCurrentDate());
        message.setToken(token);
        message.setPhoneNum(phone);
        //添加数据库
        int status = identifiedCodeMapper.insertMessageInfo(message);
        if (status <= 0) {
            logger.error("用户登录添加验证码信息失败");
            throw new BusinessException("登录添加验证码失败");
        }
        return token;
    }

    /**
     * 发送验证码
     * @param userId  用户id
     * @param phoneNum 手机号
     * @return 状态
     */
    @Override
    public Integer sentVerifyCode(String userId, String phoneNum) {
        Integer status = 0;
        if (userId != null && phoneNum != null && !"".equals(userId) && !"".equals(phoneNum)) {
            userId = userId.replace("\"", "");
            Integer count = 0;
            //1.通过用户id来判断是否已经绑定了
            UserInfoOutParam userInfo = userBaseService.queryUserInfoByUserId(Long.parseLong(userId));
            if (userInfo != null&&!"".equals(userInfo.getPhoneNumber())) {
                //2.判断绑定的手机号是该手机号还是其他
                if (phoneNum.equals(userInfo.getPhoneNumber())) {
                    status = 0;
                } else {
                    status = 1;
                }
            } else {
                //3.判断手机号是否已经绑定其他用户
                UserInfoOutParam userpInfo= userBaseService.queryUserInfoByPhoneNumber(phoneNum);
                if (userpInfo != null) {
                    status = 3;
                } else {
                    //4.防止短信重复发送
                    Message msg = new Message();
                    msg.setPhoneNum(phoneNum);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("phoneNumber", phoneNum);
                    Integer codeCount = identifiedCodeMapper.validateCodeOvertime(map);
                    if (codeCount == null) {
                        String code = MessageUtils.createMessage(phoneNum);
                        msg.setCode(code);
                        msg.setDate(System.currentTimeMillis() / 1000);
                        identifiedCodeMapper.insertMessageInfo(msg);//设置绑定手机号
                    }
                    status = 2;
                }

            }
        }
        return status;
    }

}

package com.quxin.freshfun.service.user;

import com.quxin.freshfun.model.Message;
import com.quxin.freshfun.model.UsersPOJO;
import com.quxin.freshfun.model.WxInfo;
import com.quxin.freshfun.model.outparam.WxUserInfo;
import com.quxin.freshfun.utils.BusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author TuZl
 * @time 2016年8月21日下午2:45:41
 */
public interface UserService {
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public int insertUser(UsersPOJO user);
	/**
	 * 手机用户登录
	 * @param phoneNum
	 * @param deviceId
	 * @return
	 */
	public Long PhoneLogin(String phoneNum, String deviceId,String nickName,String headUrl);
	
	/**
	 * 微信用户登录
	 * @return
	 */
	public WxUserInfo WXLogin(String code, String deviceId) throws BusinessException;
	
	/**
	 * 微站登录
	 * @param wxinfo
	 * @return
	 */
	public Long WZLogin(WxInfo wxinfo) throws BusinessException;

	/**
	 * 公众平台微站登录
	 * @return
	 */
	Long WzPlatformLogin(String code) throws BusinessException;
	
	/**
	 * 绑定手机号
	 * status 0   已绑定该手机号
	 * status 1  已绑定其他手机号
	 * status 2  可以绑定,并发送短信
	 * status 3 手机号已绑定其他用户
	 * @param userId
	 * @param phoneNum
	 * @return
	 */
	public Integer getVerifyCode(String userId , String phoneNum);
	
	
	/**
	 * 匹配验证码
	 * status 返回0 是验证码不对
	 * status 返回1 是验证码超时
	 * status 返回2 是验证码有效
	 */
	public Integer validateMessage(Message message);

	/**
	 * App验证验证码
	 * @param message
	 * @return
	 */
	String validateAppCode(Message message);

	/**
	 * 生成验证码
	 * @return
	 */
	String genertCode(String phone) throws BusinessException;
	
}

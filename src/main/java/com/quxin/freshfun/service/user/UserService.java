package com.quxin.freshfun.service.user;

import com.quxin.freshfun.model.Message;
import com.quxin.freshfun.model.UserDetailPOJO;
import com.quxin.freshfun.model.UsersPOJO;
import com.quxin.freshfun.model.WxInfo;
import com.quxin.freshfun.model.outparam.WxUserInfo;
import com.quxin.freshfun.utils.BusinessException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

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
	 * 根据userId获取user
	 * @param id userId
	 * @return  用户信息
	 */
	UsersPOJO queryUserById(Long id);

	/**
	 * 根据user Id 获取user信息，支付登陆使用
	 * @param id
	 * @return
	 */
	UsersPOJO queryUserByPrimaryKey(Long id);

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
	public WxUserInfo WXLogin(String code, String deviceId) throws BusinessException, UnsupportedEncodingException;
	
	/**
	 * 微站登录
	 * @param wxinfo
	 * @return
	 */
	public Long WZLogin(WxInfo wxinfo) throws BusinessException, NullPointerException;

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
	 * 根据userId获取头像昵称
	 * @param userId  userId
	 * @return 头像昵称
	 */
	UsersPOJO queryInfoByUserId(Long userId);

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

	public boolean findIsMobile(Long userId);

	/**
	 * 根据userId查询是否为捕手，0：不是捕手，1：是捕手
	 * @param userId  userId
	 * @return 数量
	 */
	Integer queryFetcherByUserId(Long userId);
}

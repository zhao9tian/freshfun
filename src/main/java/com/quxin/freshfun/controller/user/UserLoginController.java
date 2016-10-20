package com.quxin.freshfun.controller.user;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.common.FreshFunEncoder;
import com.quxin.freshfun.model.*;
import com.quxin.freshfun.model.outparam.UserInfoOutParam;
import com.quxin.freshfun.model.outparam.WxUserInfo;
import com.quxin.freshfun.model.param.WxAccessTokenInfo;
import com.quxin.freshfun.model.pojo.UserBasePOJO;
import com.quxin.freshfun.service.user.IdentifiedCodeService;
import com.quxin.freshfun.service.user.NickNameService;
import com.quxin.freshfun.service.user.UserBaseService;
import com.quxin.freshfun.utils.*;
import com.quxin.freshfun.utils.weixinPayUtils.ConstantUtil;
import com.quxin.freshfun.utils.weixinPayUtils.WXUtil;
import com.quxin.freshfun.utils.weixinPayUtils.WxConstantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quxin.freshfun.service.user.UserService;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class UserLoginController {



	@Autowired
	private NickNameService nickNameService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserBaseService userBaseService;
	@Autowired
	private IdentifiedCodeService identifiedCodeService;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 清除cookie，测试专用
	 */
	@RequestMapping("/cleanCookie")
	@ResponseBody
	public Map<String, Object> cleanCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("userId", null);
		cookie.setMaxAge(0);
		cookie.setDomain(".freshfun365.com");
		cookie.setPath("/");
		response.addCookie(cookie);
		return ResultUtil.success("成功清除cookie");
	}

	/**
	 * 校验cookie
	 * @param code
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/checkLogin")
	@ResponseBody
	public Map<String, Object> checkLogin(String code, HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		//校验cookie    true为可用cookie
		Map<String, Object> map = new HashMap<String, Object>();
		boolean authResult = CookieUtil.checkAuth(request);
		//判断userId是否有效，在数据库中是否存在
		if (authResult) {
			//校验cookie  存在cookie，开始校验userId有效性
			Integer count = userBaseService.checkUserId(CookieUtil.getUserIdFromCookie(request));
			if (count == 0) {
				//校验cookie  存在cookie，userId无效
				authResult = false;
				//清除cookie
				Cookie cookie = new Cookie("userId", null);
				cookie.setMaxAge(0);
				cookie.setDomain(".freshfun365.com");
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		if (!authResult) {//cookie不存在
			//校验cookie  不存在cookie，开始校验code
			if (code == null || "".equals(code)) {  //没有code
				//校验cookie  不存在cookie，并且没有接收到code
				return ResultUtil.fail(1022, "无效cookie并且没有code");
			} else {//有code
				//校验cookie  不存在cookie，有接收到code
				Long userId = null;  //获取userId
				try {
					//userId = userService.WzPlatformLogin(code);
					//获取微信中的用户信息
					WxInfo weChatInfo = getWxUserInfo(code, WxConstantUtil.APP_ID, WxConstantUtil.APP_SECRET);
					String unionId = weChatInfo.getUnionid();
					String openId = weChatInfo.getOpenid();
					if (unionId == null || openId == null) {
						throw new BusinessException("微信信息获取不为空，但是openId或unionId为空");
					} else {
						//校验用户unionId是否存在
						UserInfoOutParam userInfo = userBaseService.queryUserInfoByUnionId(unionId);
						if (userInfo != null) {//用户存在
							//存在unionId，判断openId是否一致，不一致就修改
							userId = userInfo.getUserId();
							if (!openId.equals(userInfo.getOpenId())) {//openId不一致
								Integer meshResult = userBaseService.modifyUserToMesh(userInfo.getUserId(), null, openId, null);
								if (meshResult < 1) {
									logger.error("更新users表失败，userId：" + userInfo.getUserId());
									throw new BusinessException("更新users表失败，userId：" + userInfo.getUserId());
								}
							}
						} else {//用户不存在,插入新用户
							UserBasePOJO userBase = new UserBasePOJO();
							String userName = nickNameService.queryRandNickName(weChatInfo.getNickname());
							userBase.setUserName(userName);
							String wxHeadUrl = OSSUtils.uploadWxHeadImg(weChatInfo.getHeadimgurl());
							userBase.setUserHeadImg(wxHeadUrl);
							userBase.setUnionId(unionId);
							userBase.setOpenId(openId);
							userBase.setDeviceId("");
							userBase.setCity(weChatInfo.getCity());
							userBase.setProvince(weChatInfo.getProvince());
							userBase.setCountry(weChatInfo.getCountry());
							userBase.setSource((byte) 0);
							userBase.setFetcherId(0l);
							userBase.setPhoneNumber("");
							userBase.setIdentity((byte) 0);
							userBase.setLoginType((byte) 3);
							userBase.setIsFetcher((byte) 0);
							userBase.setUserNameCount(0);
							userBase.setCreated(System.currentTimeMillis() / 1000);
							userBase.setUpdated(System.currentTimeMillis() / 1000);
							userBase.setIsDeleted(0);
							Integer result = userBaseService.addUserBaseInfo(userBase);
							if (result == 1) {
								userId = userBase.getId();
							} else {
								logger.error("用户数据插入失败");
							}
						}
					}
					//校验cookie  有code,获取到微信信息，插入数据
				} catch (Exception e) {
					logger.error("插入用户信息失败", e);
					throw e;
				}
				if (userId != null) {
					//种植code，校验cookie  有code,获取到微信信息，插入数据
					Cookie cookie = new Cookie("userId", CookieUtil.getCookieValueByUserId(userId));
					cookie.setMaxAge(CookieUtil.getCookieMaxAge());
					cookie.setDomain(".freshfun365.com");
					cookie.setPath("/");
					response.addCookie(cookie);
					//检查用户是否为捕手
					boolean result = userBaseService.checkIsFetcherByUserId(userId);
					if (result)
						map.put("userId", FreshFunEncoder.idToUrl(userId));
					else
						map.put("userId", "");
					return ResultUtil.success(map);
				} else {
					//校验cookie  有code,获取到微信信息，插入数据，userId为空
					return ResultUtil.fail(1023, "用户创建失败");
				}
			}
		}
		//校验cookie  获取有效cookie
		Long userId = CookieUtil.getUserIdFromCookie(request);
		map.put("userId", FreshFunEncoder.idToUrl(userId));
		return ResultUtil.success(map);
	}

	/**
	 * 获取微信用户基本信息
	 * @param code code
	 * @param appId  appId
	 * @param appSecret  appSecret
	 * @return  微信信息
	 */
	private WxInfo getWxUserInfo(String code, String appId, String appSecret) {
		WxAccessTokenInfo accessToken = WXUtil.getOauthAccessToken(appId, appSecret, code);
		if (accessToken != null) {
			String token = accessToken.getAccess_token();
			if (token != null) {
				return WXUtil.getWxUserInfo(token, accessToken.getOpenid());
			} else {
				logger.warn("用户token为null");
			}
		} else {
			logger.warn("获取用户accessToken失败");
		}
		return null;
	}

	@ResponseBody
	@RequestMapping("/phoneLogin")
	public Map<String, Object> phoneLogin(String token, String code, String deviceId) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfoOutParam userInfo = null;
		Long userId = null;
		if (token != null && code != null && deviceId != null) {
			String phoneNum = "";
			//app store 审核专用号
			if ("0000".equals(code) && "XXXXXXXXXXXXXXXX".equals(token))
				phoneNum = "15890658117";
			else
				//phoneNum = userService.validateAppCode(message);
				phoneNum = identifiedCodeService.queryPhoneNumberByCode(token, code);
			if (phoneNum != null) {
				String nickName = nickNameService.queryRandNickName(null);
				String headUrl = "/image/2016/9/13/1473757743180.jpg";
				userInfo = userBaseService.queryUserInfoByPhoneNumber(phoneNum);
				//Long userId =userService.PhoneLogin(phoneNum, deviceId,nickName,headUrl);
				if (userInfo != null) {
					userId = userInfo.getUserId();
					//3.判断该手机号是否有设备号（之前在微站上登录过,没在手机登录）,如果没有插入设备号
					String device = userInfo.getDeviceId();
					if (device == null || "".equals(device)) {
						userBaseService.modifyUserToMesh(userInfo.getUserId(), deviceId, null, null);
					}
				} else {
					UserBasePOJO userBase = new UserBasePOJO();
					String userName = nickNameService.queryRandNickName(nickName);
					userBase.setUserName(userName);
					String wxHeadUrl = OSSUtils.uploadWxHeadImg(headUrl);
					userBase.setUserHeadImg(wxHeadUrl);
					userBase.setPhoneNumber(phoneNum);
					userBase.setUnionId("");
					userBase.setOpenId("");
					userBase.setDeviceId("");
					userBase.setCity("");
					userBase.setProvince("");
					userBase.setCountry("");
					userBase.setSource((byte) 0);
					userBase.setFetcherId(0l);
					userBase.setIdentity((byte) 0);
					userBase.setLoginType((byte) 1);
					userBase.setIsFetcher((byte) 0);
					userBase.setUserNameCount(0);
					userBase.setCreated(System.currentTimeMillis() / 1000);
					userBase.setUpdated(System.currentTimeMillis() / 1000);
					userBase.setIsDeleted(0);
					Integer result = userBaseService.addUserBaseInfo(userBase);
					if (result == 1) {
						userId = userBase.getId();
					} else {
						logger.warn("用户数据插入失败");
					}
				}
				if (userId != null) {
					userInfo = userBaseService.queryUserInfoByUserId(userId);
					map.put("userId", userInfo.getUserId());
					map.put("userName", userInfo.getUserName());
					map.put("userHeadImg", userInfo.getUserHeadImg());
					map.put("phoneNumber", userInfo.getPhoneNumber());
				} else {
					return ResultUtil.fail(1004, "账户有误");
				}
			}
		}
		return ResultUtil.success(map);
	}

	/**
	 * 登录生成验证码
	 * @return
	 */
	@RequestMapping("/generateCode")
	@ResponseBody
	public Map<String, Object> generateVerificationCode(String phone) throws BusinessException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (null == phone || "".equals(phone)) {
			if (!ValidateUtil.isChinaPhoneLegal(phone)) {
				map.put("code", 1004);
				map.put("msg", "手机号格式不正确");
				resultMap.put("status", map);
			}
			map.put("code", 1004);
			map.put("msg", "手机号码不能为空");
			resultMap.put("status", map);
			return resultMap;
		}
		String token = identifiedCodeService.genertCode(phone);
		if (token != null) {
			map.put("code", 1001);
			map.put("msg", "请求成功");
			resultMap.put("status", map);
			resultMap.put("data", token);
		} else {
			map.put("code", 1004);
			map.put("msg", "添加验证码失败");
			resultMap.put("status", map);
		}

		return resultMap;
	}

	/**
	 * 微信登录
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/wxLogin")
	public Map<String, Object> wxLogin(String code, String deviceId) throws UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> mapUser = new HashMap<String, Object>();
		UserInfoOutParam userInfo = new UserInfoOutParam();
		if (code != null) {
			try {
				//获取用户信息
				WxInfo weChatInfo = getWxUserInfo(code, ConstantUtil.APP_ID, ConstantUtil.APP_SECRET);
				if (weChatInfo == null) {
					logger.error("获取不到微信信息 code:" + code);
					throw new BusinessException("获取不到微信信息 code:" + code);
				}
				Long userId = null;
				if (weChatInfo != null) {
					String unionId = weChatInfo.getUnionid();
					//1.判断wxid是否存在数据库中
					userInfo = userBaseService.queryUserInfoByUnionId(unionId);
					if (userInfo != null) {
						userId = userInfo.getUserId();
						//3.判断deviceId是否在用户表里面,deviceId唯一
						if (deviceId != null && !"".equals(deviceId)) {
							userId = userBaseService.queryUserIdByDeviceId(deviceId);//*****可改进*****
							if (userId != null) {
								//修改用户信息
								userBaseService.modifyUserToMesh(userId, deviceId, null, null);
							}
						}
						//查询用户头像信息
						userInfo = userBaseService.queryUserInfoByUserId(userId);
					} else {
						UserBasePOJO userBase = new UserBasePOJO();
						String userName = nickNameService.queryRandNickName(weChatInfo.getNickname());
						userBase.setUserName(userName);
						String wxHeadUrl = OSSUtils.uploadWxHeadImg(weChatInfo.getHeadimgurl());
						userBase.setUserHeadImg(wxHeadUrl);
						userBase.setPhoneNumber("");
						userBase.setUnionId("");
						userBase.setOpenId("");
						userBase.setDeviceId("");
						userBase.setCity("");
						userBase.setProvince("");
						userBase.setCountry("");
						userBase.setSource((byte) 0);
						userBase.setFetcherId(0l);
						userBase.setIdentity((byte) 0);
						userBase.setLoginType((byte) 2);
						userBase.setIsFetcher((byte) 0);
						userBase.setUserNameCount(0);
						userBase.setCreated(System.currentTimeMillis() / 1000);
						userBase.setUpdated(System.currentTimeMillis() / 1000);
						userBase.setIsDeleted(0);
						Integer result = userBaseService.addUserBaseInfo(userBase);
						if (result == 1) {
							userId = userBase.getId();
							userInfo = userBaseService.queryUserInfoByUserId(userId);
						} else {
							logger.warn("用户数据插入失败");
						}
					}
				}
				mapUser.put("userId", userInfo.getUserId());
				mapUser.put("userName", userInfo.getUserName());
				mapUser.put("userHeadImg", userInfo.getUserHeadImg());
				mapUser.put("phoneNumber", userInfo.getPhoneNumber());
				map.put("code", 1001);
				map.put("msg", "请求成功");
				resultMap.put("status", map);
				resultMap.put("data", mapUser);
			} catch (BusinessException e) {
				logger.error("添加用户失败", e);
			}
		} else {
			map.put("code", 1004);
			map.put("msg", "参数错误");
			resultMap.put("status", map);
			return resultMap;
		}
		return resultMap;
	}

	@ResponseBody
	@RequestMapping("/getVerifyCode")
	public Map<String, Object> getVerifyCode(@RequestBody Message message, HttpServletRequest request) {
		String userId = "";
		if (message.getUserId() != null && !"".equals(message.getUserId())) {
			userId = message.getUserId();
		} else {
			userId = CookieUtil.getUserIdFromCookie(request).toString();
		}
		Integer status = identifiedCodeService.sentVerifyCode(userId, message.getPhoneNum());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		return map;
	}

	@ResponseBody
	@RequestMapping("/getUserInfo")
	public Map<String, Object> getUserInfo(HttpServletRequest request) {
		Long userId = CookieUtil.getUserIdFromCookie(request);
		if (userId == null) {
			return ResultUtil.fail(1004, "获取用户cookie失败");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfoOutParam userInfo = userBaseService.queryUserInfoByUserId(userId);
		map.put("userImg", userInfo.getUserHeadImg());
		map.put("nickName", userInfo.getUserName());
		return ResultUtil.success(map);
	}
	
	@ResponseBody
	@RequestMapping("/validateVerifyCode")
	public Map<String, Object> bindPhone(@RequestBody Message message, HttpServletRequest request) {
		Long userId = null;
		if (message.getUserId() != null && !"".equals(message.getUserId()))
			userId = Long.parseLong(message.getUserId());
		else
			userId = CookieUtil.getUserIdFromCookie(request);
		Integer status = 0;
		Integer count = identifiedCodeService.checkCode(message.getPhoneNum(), message.getCode());
		if (count > 0) {
			boolean result = identifiedCodeService.checkCodeOvertime(message.getPhoneNum(), message.getCode());
			if (result) {
				//1.1id不为空说明该验证码有效
				status = 2;
				userBaseService.modifyUserToMesh(userId, null, null, message.getPhoneNum());
			} else {
				//1.2id为空说明验证码已经过期
				status = 1;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		return map;
	}
	
	
}

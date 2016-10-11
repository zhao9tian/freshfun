package com.quxin.freshfun.controller.user;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quxin.freshfun.model.outparam.WxUserInfo;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.CookieUtil;
import com.quxin.freshfun.utils.MessageUtils;
import com.quxin.freshfun.utils.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quxin.freshfun.model.Message;
import com.quxin.freshfun.model.ReturnStatus;
import com.quxin.freshfun.model.WxInfo;
import com.quxin.freshfun.service.user.UserService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class UserLoginController {

	@Autowired
	private UserService userService;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@ResponseBody
	@RequestMapping("/phoneLogin")
	public Map<String, Object> phoneLogin(String token,String code,String deviceId){
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();

		if(token != null && code != null && deviceId != null){
			//查询验证码信息
			Message message = new Message();
			message.setToken(token);
			message.setCode(code);
			String phoneNum = userService.validateAppCode(message);
			if(phoneNum != null){
				Long userId =userService.PhoneLogin(phoneNum, deviceId);
			}
		}

		return resultMap;
	}

	/**
	 * 登录生成验证码
	 * @return
	 */
	@RequestMapping("/generateCode")
	@ResponseBody
	public Map<String,Object> generateVerificationCode(String phone) throws BusinessException {
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(null == phone || "".equals(phone)){
			if(!ValidateUtil.isChinaPhoneLegal(phone)){
				map.put("code",1004);
				map.put("msg","手机号格式不正确");
				resultMap.put("status",map);
			}
			map.put("code",1004);
			map.put("msg","手机号码不能为空");
			resultMap.put("status",map);
			return resultMap;
		}
		String token = userService.genertCode(phone);
		if(token != null){
			map.put("code",1001);
			map.put("msg","请求成功");
			resultMap.put("status",map);
			resultMap.put("data",token);
		}else{
			map.put("code",1004);
			map.put("msg","添加验证码失败");
			resultMap.put("status",map);
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
	public Map<String, Object>  wxLogin(String code,String deviceId){
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(code != null){
			try {
				WxUserInfo wxInfo =userService.WXLogin(code,deviceId);
				map.put("code",1001);
				map.put("msg","请求成功");
				resultMap.put("status",map);
				resultMap.put("data",wxInfo);
			} catch (BusinessException e) {
				logger.error("添加用户失败",e);
			}
		}else{
			map.put("code",1004);
			map.put("msg","参数错误");
			resultMap.put("status",map);
			return resultMap;
		}
		return resultMap;
	}
	@ResponseBody
	@RequestMapping("/wzLogin")
	public String wzLogin(@RequestBody WxInfo wxinfo,HttpServletResponse response){
		Long userId = null;
		try {
			userId = userService.WZLogin(wxinfo);
			Cookie cookie = new Cookie("userId",CookieUtil.getCookieValueByUserId(userId));
			cookie.setMaxAge(CookieUtil.getCookieMaxAge());
			cookie.setDomain(".freshfun365.com");
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (BusinessException e) {
			logger.error("用户生成失败",e);
		}
		return userId.toString();
	}

	@ResponseBody
	@RequestMapping("/getVerifyCode")
	public ReturnStatus getVerifyCode(@RequestBody Message message, HttpServletResponse response){
		Integer status = userService.getVerifyCode(message.getUserId(), message.getPhoneNum());
		ReturnStatus rs = new ReturnStatus();
		rs.setStatus(status);
		return rs;
	}
	
	@ResponseBody
	@RequestMapping("/validateVerifyCode")
	public ReturnStatus bindPhone(@RequestBody Message message , HttpServletResponse response){
		Integer status = userService.validateMessage(message);
		ReturnStatus rs = new ReturnStatus();
		rs.setStatus(status);
		return rs;
	}
	
	
}

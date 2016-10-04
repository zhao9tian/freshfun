package com.quxin.freshfun.controller.user;

import javax.servlet.http.HttpServletResponse;

import com.quxin.freshfun.utils.BusinessException;
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
	public String phoneLogin(String phoneNum , String deviceId , HttpServletResponse response){
		Long userId =userService.PhoneLogin(phoneNum, deviceId);
		return userId.toString();
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
				Long userId =userService.WXLogin(code,deviceId);
				map.put("code",1001);
				map.put("msg","请求成功");
				resultMap.put("status",map);
				resultMap.put("data",userId);
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
	public String wzLogin(@RequestBody WxInfo wxinfo){
		Long userId = null;
		try {
			userId = userService.WZLogin(wxinfo);
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

package com.quxin.freshfun.controller.user;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quxin.freshfun.model.Message;
import com.quxin.freshfun.model.ReturnStatus;
import com.quxin.freshfun.model.WxInfo;
import com.quxin.freshfun.service.user.UserService;

@Controller
@RequestMapping("/login")
public class UserLogin {
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping("/phoneLogin")
	public String phoneLogin(String phoneNum , String deviceId , HttpServletResponse response){
		Long userId =userService.PhoneLogin(phoneNum, deviceId);
		return userId.toString();
	}
	@ResponseBody
	@RequestMapping("/wxLogin")
	public String wxLogin(WxInfo wxInfo , String deviceId , HttpServletResponse response){
		Long userId =userService.WXLogin(wxInfo, deviceId);
		return userId.toString();
	}
	@ResponseBody
	@RequestMapping("/wzLogin")
	public String wzLogin(@RequestBody WxInfo wxinfo){
		Long userId =userService.WZLogin(wxinfo);
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

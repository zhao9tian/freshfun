package com.quxin.freshfun.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.quxin.freshfun.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.quxin.freshfun.model.AddressInfo;
import com.quxin.freshfun.model.UserAddress;
import com.quxin.freshfun.service.UserAddressService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/useraddress")
public class UserController {
	@Autowired
	private UserAddressService userAddressService;

	@RequestMapping("/setCookie")
	@ResponseBody
	public String setCookie(HttpServletResponse response){
		Cookie cookie = new Cookie("userId",CookieUtil.getCookieValueByUserId(557144l));
		cookie.setMaxAge(CookieUtil.getCookieMaxAge());
		cookie.setDomain(".freshfun365.com");
		cookie.setPath("/");
		response.addCookie(cookie);
		return  "557144";
	}

	/**
	 * 通过userID获取用户地址
	 * @return
	 */
	@RequestMapping("/useraddress")
	@ResponseBody
	public List<UserAddress> FindUserAddress(HttpServletRequest request){
		Long ui = CookieUtil.getUserIdFromCookie(request);
		return userAddressService.userAddress(ui);
	}
	
	/**
	 * 通过userID获取用户默认地址
	 * @return
	 */
	@RequestMapping("/userdefaultaddress")
	@ResponseBody
	public List<UserAddress> FindUseDefaultrAddress(HttpServletRequest request){
		Long ui = CookieUtil.getUserIdFromCookie(request);
		return userAddressService.userDefaultAddress(ui);
	}
	
	/**
	 * 通过addressID获取用户地址
	 * @return
	 */
	@RequestMapping("/useraddressid")
	@ResponseBody
	public UserAddress FindUserAddressByAddressId(Integer addressId){
		return userAddressService.selectByPrimaryKey(addressId);
	}
	
	/**
	 * 添加用户地址
	 * @return
	 */
	@RequestMapping(value="/addaddress",method={RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> AddNewAddress(@RequestBody AddressInfo addressInfo,HttpServletRequest request){
		Map<String, Object> stateMap = new HashMap<String, Object>(1);
		Long userID = null;
		if(addressInfo.getUserId()==null||"".equals(addressInfo.getUserId())){
			userID = CookieUtil.getUserIdFromCookie(request);
		}else{
			userID = Long.parseLong(addressInfo.getUserId().replace("\"",""));
		}
		String name = addressInfo.getName();
		String tel = addressInfo.getTel();
		String city = addressInfo.getCity();
		String address = addressInfo.getAddress();
		Integer isDefault = addressInfo.getIsDefault();
		if (isDefault == 1){
			userAddressService.updateDefaultAddress(userID);
		}
		if (userID != null && name != null && tel != null && city != null && address != null){
			Map<String, Object> addressMap = new HashMap<String, Object>(5);
			addressMap.put("userID", userID);
			addressMap.put("name", name);
			addressMap.put("tel", tel); 
			addressMap.put("city", city);
			addressMap.put("address", address);
			addressMap.put("isDefault", isDefault);
			long nowTime = System.currentTimeMillis()/1000;
			addressMap.put("gmtCreat", nowTime);
			addressMap.put("gmtModified", nowTime);
			
			int demo = userAddressService.addNewAddress(addressMap);
			if (demo == 1){
				stateMap.put("state", "1");			
			}else{
				stateMap.put("state", "0");	
			}
			
		}else{
			stateMap.put("state", "0");
		}
		
		return stateMap;
//		CodingTools codingTools = new CodingTools();
//		name = codingTools.enCodeStr(name);
//		city = codingTools.enCodeStr(city);
//		address = codingTools.enCodeStr(address);
		
	}
	
	/**
	 * 删除用户地址
	 * @return
	 */
	@RequestMapping("/deleteaddress")
	@ResponseBody
	public Map<String, Object> DeleteUserAddress(Integer addressId){
		Map<String, Object> stateMap = new HashMap<String, Object>(1);
		int demo = userAddressService.deleteByPrimaryKey(addressId);
		if (demo == 1){
			stateMap.put("state", "1");			
		}else{
			stateMap.put("state", "0");	
		}
		return stateMap;
	}
	
	/**
	 * 更改用户地址
	 * @return
	 */
	@RequestMapping(value="/updateaddress",method={RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> UpdateUserAddress(@RequestBody AddressInfo addressInfo,HttpServletRequest request){
		Map<String, Object> stateMap = new HashMap<String, Object>(1);

		Long userId = null;
		if(addressInfo.getUserId()==null||"".equals(addressInfo.getUserId())){
			userId = CookieUtil.getUserIdFromCookie(request);
		}else{
			userId = Long.parseLong(addressInfo.getUserId().replace("\"",""));
		}
		Integer addressId = addressInfo.getAddressId();
		String name = addressInfo.getName();
		String tel = addressInfo.getTel();
		String city = addressInfo.getCity();
		String address = addressInfo.getAddress();
		Integer isDefault = addressInfo.getIsDefault();
		if (isDefault == 1){
			userAddressService.updateDefaultAddress(userId);
		}
		if (addressId != null && name != null && tel != null && city != null && address != null){
			UserAddress userAddress = new UserAddress();
			userAddress.setId(addressId);
			userAddress.setName(name);
			userAddress.setTel(tel);
			userAddress.setAddress(address);
			userAddress.setCity(city);
			userAddress.setIsDefault(isDefault);
			long nowTime = System.currentTimeMillis()/1000;
			userAddress.setGmtModified(nowTime);
			int demo = userAddressService.updateByPrimaryKey(userAddress);
			if (demo == 1){
				stateMap.put("state", "1");			
			}else{
				stateMap.put("state", "0");	
			}
		}
		
		return stateMap;
	}

}
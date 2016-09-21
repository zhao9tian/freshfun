package com.quxin.freshfun.controller.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quxin.freshfun.model.AddressInfo;
import com.quxin.freshfun.model.UserAddress;
import com.quxin.freshfun.service.UserAddressService;

@Controller
@RequestMapping("/useraddress")
public class UserController {
	@Autowired
	private UserAddressService userAddressService;
	
	/**
	 * 通过userID获取用户地址
	 * @return
	 */
	@RequestMapping("/useraddress")
	@ResponseBody
	public List<UserAddress> FindUserAddress(String userID){
		Long ui = Long.parseLong(userID.replace("\"", ""));
		return userAddressService.userAddress(ui);
	}
	
	/**
	 * 通过userID获取用户默认地址
	 * @return
	 */
	@RequestMapping("/userdefaultaddress")
	@ResponseBody
	public List<UserAddress> FindUseDefaultrAddress(String userID){
		Long ui = Long.parseLong(userID.replace("\"", ""));
		return userAddressService.userDefaultAddress(ui);
	}
	
	/**
	 * 通过addressID获取用户地址
	 * @return
	 */
	@RequestMapping("/useraddressid")
	@ResponseBody
	public UserAddress FindUserAddressByAddressId(Integer address_id){
		System.out.println(address_id);
		return userAddressService.selectByPrimaryKey(address_id);
	}
	
	/**
	 * 添加用户地址
	 * @return
	 */
	@RequestMapping(value="/addaddress",method={RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> AddNewAddress(@RequestBody AddressInfo addressInfo){
		Map<String, Object> stateMap = new HashMap<String, Object>(1);
		String uId = addressInfo.getUser_id();
		Long userID = Long.parseLong(uId.replace("\"", ""));
		String name = addressInfo.getName();
		String tel = addressInfo.getTel();
		String city = addressInfo.getCity();
		String address = addressInfo.getAddress();
		Integer is_default = addressInfo.getIs_default();
		if (is_default == 1){
			userAddressService.updateDefaultAddress(userID);
		}
		if (userID != null && name != null && tel != null && city != null && address != null){
			Map<String, Object> addressMap = new HashMap<String, Object>(5);
			addressMap.put("userID", userID);
			addressMap.put("name", name);
			addressMap.put("tel", tel); 
			addressMap.put("city", city);
			addressMap.put("address", address);
			addressMap.put("is_default", is_default);
			long nowTime = System.currentTimeMillis()/1000;
			addressMap.put("gmt_creat", nowTime);
			addressMap.put("gmt_modified", nowTime);
			
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
	public Map<String, Object> DeleteUserAddress(Integer addressID){
		Map<String, Object> stateMap = new HashMap<String, Object>(1);
		int demo = userAddressService.deleteByPrimaryKey(addressID);
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
	public Map<String, Object> UpdateUserAddress(@RequestBody AddressInfo addressInfo){
		Map<String, Object> stateMap = new HashMap<String, Object>(1);
		String uId = addressInfo.getUser_id();
		Long userID = Long.parseLong(uId.replace("\"", ""));
		Integer addressID = addressInfo.getAddress_id();
		String name = addressInfo.getName();
		String tel = addressInfo.getTel();
		String city = addressInfo.getCity();
		String address = addressInfo.getAddress();
		Integer is_default = addressInfo.getIs_default();
		if (is_default == 1){
			userAddressService.updateDefaultAddress(userID);
		}
		if (addressID != null && name != null && tel != null && city != null && address != null){
			UserAddress userAddress = new UserAddress();
			userAddress.setId(addressID);
			userAddress.setName(name);
			userAddress.setTel(tel);
			userAddress.setAddress(address);
			userAddress.setCity(city);
			userAddress.setIs_default(is_default);
			long nowTime = System.currentTimeMillis()/1000;
			userAddress.setGmt_modified(nowTime);
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
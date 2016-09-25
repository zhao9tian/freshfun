package com.quxin.freshfun.service.impl.user;

import com.quxin.freshfun.dao.UsersMapper;
import com.quxin.freshfun.model.*;
import com.quxin.freshfun.service.user.UserService;
import com.quxin.freshfun.utils.AESUtil;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.IdGenerate;
import com.quxin.freshfun.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
/**
 * @author TuZl
 * @time 2016年8月21日下午2:46:07
 */
@Service("userService")
public class UserServiceImpl implements UserService{

	@Autowired
	public UsersMapper userDao;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public int insertUser(UsersPOJO user) {
		return userDao.insert(user);
	}
	
	/**
	 * 根据手机号是否存在于数据库之中判断是否是新用户,
	 * 如果是,就在数据库注册该用户信息,此处设备号没有实际意义(之前是为了限制一台设备只能领一次红包)
	 */
	@Override
	public Long PhoneLogin(String phoneNum, String deviceId) {
		Long userId = null;
		//1.手机号和设备号都不为空继续执行
		if(phoneNum != null && deviceId !=null && !"".equals(phoneNum) && !"".equals(deviceId) ){
			//2.判断手机号是否存在数据库中
			userId = userDao.getUserIdByPhoneNum(phoneNum);
			if(userId!=null){
				//3.判断该手机号是否有设备号（之前在微站上登录过,没在手机登录）,如果没有插入设备号
				if(userDao.getDeviceIdByPhoneNum(phoneNum)==null || "".equals(userDao.getDeviceIdByPhoneNum(phoneNum))){
					Map<String, Object> map = new HashMap<>();
					map.put("userId", userId);
					map.put("deviceId", deviceId);
					userDao.updateUser(map);
				}
			}else{
				//4.手机号不存在,就要创建新用户
				IdGenerate idGenerate = new IdGenerate();
				userId = idGenerate.nextId();
				UsersPOJO user = new UsersPOJO();
				user.setUserId(userId);
				user.setMobilePhone(phoneNum);
				user.setDeviceId(deviceId);
				
				//不能为空的属性
				user.setGmtCreate(System.currentTimeMillis()/1000);
				user.setGmtModified(System.currentTimeMillis()/1000);
				user.setLoginMethod("mobilePhone");
				user.setUserCredit((byte)1);
				user.setIncomeIdentify((byte)1);
				user.setUserEnter((byte)1);
				user.setIsReceived((byte)1);
				userDao.insert(user);
			}
		}
		return userId;
	}

	@Override
	public Long WZLogin(WxInfo wxinfo) throws BusinessException {
		Long userId = null;
		String wxId = wxinfo.getUnionid();
		String wzId = wxinfo.getOpenid();
		if(wxId!=null && wzId != null){

			//1.判断wxId是否存在数据库中
			userId = userDao.getUserIdByWxId(wxId);
			if(userId != null){
				//3.判断wzId是否在用户表里面,wzId唯一
				if(userDao.getWzIdBywxId(wxId) == null || "".equals(userDao.getWzIdBywxId(wxId))){
					Map<String, Object> map = new HashMap<>();
					map.put("userId", userId);
					map.put("wzId", wzId);
					userDao.updateUser(map);
				}
			}else{
				//2.插入新用户
				UsersPOJO user = new UsersPOJO();
				user.setWxId(wxId);
				user.setWzId(wzId);
				if(null != wxinfo.getCode() && !"".equals(wxinfo.getCode())){
					String parentId = AESUtil.decodeStr(wxinfo.getCode()).split("\\|")[0];
					user.setParentId(Long.parseLong(parentId.replace("\"", "")));
				}
				//不为空
				user.setGmtCreate(System.currentTimeMillis()/1000);
				user.setGmtModified(System.currentTimeMillis()/1000);
				user.setLoginMethod("wz");
				user.setUserCredit((byte)1);
				user.setUserIdentify((byte)1);
				user.setUserEnter((byte)1);
				user.setIsReceived((byte)1);
				int status = userDao.insert(user);
				if(status <= 0){
					logger.error("用户添加失败");
					throw new BusinessException("用户添加失败");
				}else{
					userId = user.getId();
				}
				//插入一条用户信息到DB
				UserDetailPOJO userDetailPOJO = new UserDetailPOJO();
				userDetailPOJO.setUserId(userId);
				userDetailPOJO.setProvince(wxinfo.getProvince());
				userDetailPOJO.setCity(wxinfo.getCity());
				userDetailPOJO.setCode(wxinfo.getCode());
				userDetailPOJO.setCountry(wxinfo.getCountry());
				userDetailPOJO.setHeadimgurl(wxinfo.getHeadimgurl());
				userDetailPOJO.setLanguage(wxinfo.getLanguage());
				userDetailPOJO.setNickname(wxinfo.getNickname());
				userDetailPOJO.setUnionid(wxinfo.getUnionid());
				userDetailPOJO.setOpenid(wxinfo.getOpenid());
				userDao.insertUserDetails(userDetailPOJO);

			}
		}
		return userId;
	}

	@Override
	public Long WXLogin(WxInfo wxInfo, String deviceId) {
		Long userId = null;
		if(wxInfo!=null && deviceId != null && !"".equals(deviceId) && !"".equals(wxInfo)){
			UserInfoPOJO userInfo = new UserInfoPOJO();
			userInfo.setWxInfo(wxInfo);
			String wxId = wxInfo.getUnionid();
			//1.判断wxid是否存在数据库中
			userId = userDao.getUserIdByWxId(wxId);
			if(userId != null){
				//3.判断deviceId是否在用户表里面,deviceId唯一
				if(userDao.getDeviceIdBywxId(wxId) == null || "".equals(userDao.getDeviceIdBywxId(wxId))){
					Map<String, Object> map = new HashMap<>();
					map.put("userId", userId);
					map.put("deviceId", deviceId);
					userDao.updateUser(map);
				}
			}else{
				//2.插入新用户
				UsersPOJO user = new UsersPOJO();
				IdGenerate idGenerate = new IdGenerate();
				userId = idGenerate.nextId();
				user.setUserId(userId);
				user.setWxId(wxId);
				user.setDeviceId(deviceId);
				user.setGmtCreate(System.currentTimeMillis()/1000);
				user.setGmtModified(System.currentTimeMillis()/1000);
				user.setLoginMethod("wx");
				user.setUserCredit((byte)1);
				user.setUserIdentify((byte)1);
				user.setIncomeIdentify((byte)1);
				user.setUserEnter((byte)1);
				user.setIsReceived((byte)1);
				userDao.insert(user);
				//插入一条用户信息到mongoDB
				userInfo.setUserId(userId);
			}
		}
		return userId;
	}
	

	@Override
	public Integer getVerifyCode(String userId, String phoneNum) {
		Integer status = 0;
		if(userId!=null && phoneNum!=null && !"".equals(userId) && !"".equals(phoneNum)){
			userId = userId.replace("\"", "");
			Integer count = 0;
			//1.通过用户id来判断是否已经绑定了
			count = userDao.validateExistPhoneNum(userId);
			if(count > 0){
				//2.判断绑定的手机号是该手机号还是其他
				Map<String, Object> qc = new HashMap<>();
				qc.put("userId", userId);
				qc.put("phoneNum", phoneNum);
				count = userDao.validatePhoneNum(qc);
				if(count > 0){
					status = 0;
				}else{
					status = 1;
				}
			}else{
				//3.判断手机号是否已经绑定其他用户
				Long uId = userDao.getUserIdByPhoneNum(phoneNum);
				if(uId!=null){
					status = 3 ;
				}else{
					//4.防止短信重复发送
					Message msg = new Message();
					msg.setPhoneNum(phoneNum);
					Integer codeCount = userDao.validateOvertime(msg);
					if(codeCount == null){
						String code = MessageUtils.createMessage(phoneNum);
						msg.setCode(code);
						msg.setDate(System.currentTimeMillis()/1000);
						userDao.insertMessage(msg);//设置绑定手机号
					}
					status = 2;
				}
				
			}
		}
		return status;
	}

	@Override
	public Integer validateMessage(Message message) {
		Integer status = 0;
		Integer count = userDao.validateCode(message);
		if(count > 0){
			Integer id  = userDao.validateOvertime(message);
			if(id != null){
				//1.1id不为空说明该验证码有效
				status = 2;
				//2.去绑定手机号
				Map<String, Object> map = new HashMap<>();
				map.put("userId", message.getUserId().replace("\"", ""));
				map.put("phoneNum", message.getPhoneNum());
				userDao.updateUser(map);
			}else{
				//1.2id为空说明验证码已经过期
				status = 1;
			}
		}
		return status;
	}
	
}

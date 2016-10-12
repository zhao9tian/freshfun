package com.quxin.freshfun.service.impl.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quxin.freshfun.dao.UsersMapper;
import com.quxin.freshfun.model.*;
import com.quxin.freshfun.model.outparam.WxUserInfo;
import com.quxin.freshfun.model.param.WxAccessTokenInfo;
import com.quxin.freshfun.service.user.UserService;
import com.quxin.freshfun.utils.*;
import com.quxin.freshfun.utils.weixinPayUtils.ConstantUtil;
import com.quxin.freshfun.utils.weixinPayUtils.WxConstantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
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
	private Logger logger = LoggerFactory.getLogger("info_log");
	private Logger error_log = LoggerFactory.getLogger("error_log");

	@Override
	public UsersPOJO queryUserById(Long id){
		return userDao.selectUserById(id);
	}

	/**
	 * 根据user Id 获取user信息，支付登陆使用
	 * @param id
	 * @return
	 */
	public UsersPOJO queryUserByPrimaryKey(Long id){
		return userDao.selectByPrimaryKey(id);
	}

	@Override
	public int insertUser(UsersPOJO user) {
		return userDao.insert(user);
	}
	
	/**
	 * 根据手机号是否存在于数据库之中判断是否是新用户,
	 * 如果是,就在数据库注册该用户信息,此处设备号没有实际意义(之前是为了限制一台设备只能领一次红包)
	 */
	@Override
	public Long PhoneLogin(String phoneNum, String deviceId,String nickName,String headUrl) {
		Long userId = null;
		UsersPOJO user = null;
		//1.手机号和设备号都不为空继续执行
		if(phoneNum != null && deviceId !=null && !"".equals(phoneNum) && !"".equals(deviceId) ){
			//2.判断手机号是否存在数据库中
			userId = userDao.getUserIdByPhoneNum(phoneNum);
			if(userId!=null){
				//3.判断该手机号是否有设备号（之前在微站上登录过,没在手机登录）,如果没有插入设备号
				String device = userDao.getDeviceIdByPhoneNum(phoneNum);
				if(device==null || "".equals(device)){
					modifyUser(deviceId, userId);
				}
			}else{
				//4.手机号不存在,就要创建新用户
				user = new UsersPOJO();
				user.setMobilePhone(phoneNum);
				user.setDeviceId(deviceId);
				user.setUserName(nickName);
				user.setUserHeadUrl(headUrl);
				
				//不能为空的属性
				user.setGmtCreate(System.currentTimeMillis()/1000);
				user.setGmtModified(System.currentTimeMillis()/1000);
				user.setLoginMethod("mobilePhone");
				user.setUserCredit((byte)1);
				user.setIncomeIdentify((byte)1);
				user.setUserEnter((byte)1);
				user.setIsReceived((byte)1);
				user.setIsDeleted((byte)0);
				userDao.insert(user);
			}
		}
		return userId;
	}

	/**
	 * 修改用户信息
	 * @param deviceId
	 * @param userId
	 */
	private void modifyUser(String deviceId, Long userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("deviceId", deviceId);
		userDao.updateUser(map);
	}

	@Override
	public Long WZLogin(WxInfo wxinfo) throws BusinessException, NullPointerException {
		if(wxinfo == null){
			error_log.error("获取用户微信信息失败");
			throw new NullPointerException();
		}
		Long userId = null;
		String wxId = wxinfo.getUnionid();
		String wzId = wxinfo.getOpenid();
		if(wxId!=null && wzId != null){

			//1.判断wxId是否存在数据库中
			userId = userDao.getUserIdByWxId(wxId);
			if(userId != null){
				//3.判断wzId是否在用户表里面,wzId唯一
				String wzIdDB = userDao.getWzIdBywxId(wxId);
				if( wzIdDB!= null && !wzId.equals(wzIdDB)){
					updateUser(userId, wzId);
				}
			}else{
				//2.插入新用户
				UsersPOJO user = new UsersPOJO();
				user.setWxId(wxId);
				user.setWzId(wzId);
				if(null != wxinfo.getCode() && !"".equals(wxinfo.getCode())){
					try{
						String parentId = AESUtil.decodeStr(wxinfo.getCode()).split("\\|")[0];
						user.setParentId(Long.parseLong(parentId.replace("\"", "")));
					}catch (Exception e){
						logger.error("code不正确导致, parentId出错", e);
					}
				}
				String wxHeadUrl = OSSUtils.uploadWxHeadImg(wxinfo.getHeadimgurl());
				//不为空
				user.setUserName(wxinfo.getNickname());
				user.setUserHeadUrl(wxHeadUrl);
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

	/**
	 * 微站登录
	 * @return
	 */
	@Override
	public Long WzPlatformLogin(String code) throws BusinessException {
		if(code == null || "".equals(code))
			return null;
		//获取用户信息
		WxInfo wxInfo = getUserInfo(code, WxConstantUtil.APP_ID,WxConstantUtil.APP_SECRET);
		//WxInfo userInfo = getUserInfo(code);
		Long userId = WZLogin(wxInfo);
        return userId;
	}

	/**
	 * 获取用户信息
	 * @param code
	 */
	private WxInfo getUserInfo(String code) {
		StringBuilder sb = new StringBuilder();
		String url = "https://www.freshfun365.com/wz_pay/wx/wx_access.php?method=getWXUserInfo&code=";
		sb.append(url);
		sb.append(code);
		WxInfo wxInfo = new WxInfo();
		wxInfo = sendWxRequest(sb,wxInfo);
		return wxInfo;
	}

	/**
	 * 修改微站用户登录信息
	 * @param userId
	 * @param wzId
	 */
	private void updateUser(Long userId, String wzId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put( "wzId", wzId);
		userDao.updateUser(map);
	}

	@Override
	public WxUserInfo WXLogin(String code,String deviceId) throws BusinessException, UnsupportedEncodingException {
		if(code == null){
			return null;
		}
		WxUserInfo info = null;
		//获取用户信息
		WxInfo wxInfo = getUserInfo(code,ConstantUtil.APP_ID,ConstantUtil.APP_SECRET);
		Long userId = null;
		if(wxInfo!=null){
			UserInfoPOJO userInfo = new UserInfoPOJO();
			userInfo.setWxInfo(wxInfo);
			String wxId = wxInfo.getUnionid();
			//1.判断wxid是否存在数据库中
			userId = userDao.getUserIdByWxId(wxId);
			if(userId != null){
				//3.判断deviceId是否在用户表里面,deviceId唯一
				if(deviceId != null && !"".equals(deviceId)) {
					String id = userDao.getDeviceIdBywxId(wxId);
					if (id == null || "".equals(id)) {
						//修改用户信息
						modifyUser(deviceId, userId);
					}
				}
				//查询用户头像信息
				info = userDao.selectUserInfo(userId);
				if(info != null)
					info.setUserId(userId);
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
				user.setUserName(wxInfo.getNickname());
				user.setUserHeadUrl(OSSUtils.uploadWxHeadImg(wxInfo.getHeadimgurl()));
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
				//插入一条用户信息到mongoDB
				//插入一条用户信息到DB
				int userInfoStatus = generateUserInfo(wxInfo, userId);
				if(userInfoStatus <= 0){
					logger.error("添加用户详细信息失败");
					throw new BusinessException("添加用户详细信息失败");
				}
				info = userDao.selectUserInfo(userId);
				info.setUserId(userId);
			}
		}

		return info;
	}

	/**
	 * 生成传递客户端用户信息
	 * @param wxInfo
	 * @param userId
	 * @return
	 */
	private WxUserInfo generateOutUserInfo(WxInfo wxInfo, Long userId) {
		WxUserInfo info = new WxUserInfo();
		info.setHeadimgurl(wxInfo.getHeadimgurl());
		info.setNickname(wxInfo.getNickname());
		info.setUserId(userId);
		return info;
	}

	/**
	 * 生成用户详细信息
	 * @param wxInfo
	 * @param userId
	 */
	private int generateUserInfo(WxInfo wxInfo, Long userId) throws UnsupportedEncodingException {
		UserDetailPOJO userDetailPOJO = new UserDetailPOJO();
		userDetailPOJO.setUserId(userId);
		userDetailPOJO.setProvince(wxInfo.getProvince());
		userDetailPOJO.setCity(wxInfo.getCity());
		userDetailPOJO.setCountry(wxInfo.getCountry());
		userDetailPOJO.setHeadimgurl(wxInfo.getHeadimgurl());
		userDetailPOJO.setLanguage(wxInfo.getLanguage());
		userDetailPOJO.setNickname(new String(wxInfo.getNickname().getBytes(),"utf-8"));
		userDetailPOJO.setUnionid(wxInfo.getUnionid());
		userDetailPOJO.setOpenid(wxInfo.getOpenid());
		return userDao.insertUserDetails(userDetailPOJO);
	}

	/**
	 * 通过code获取用户信息
	 * @param code
	 */
	private WxInfo getUserInfo(String code,String appId,String appSecert) {
		StringBuilder sb = new StringBuilder();
		sb.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=");
		sb.append(appId);
		sb.append("&secret=");
		sb.append(appSecert);
		sb.append("&code=");
		sb.append(code);
		sb.append("&grant_type=authorization_code");
		WxAccessTokenInfo wxToken = new WxAccessTokenInfo();
		wxToken = sendWxRequest(sb,wxToken);

		StringBuilder sbl = new StringBuilder();
		sbl.append("https://api.weixin.qq.com/sns/userinfo?access_token=");
		sbl.append(wxToken.getAccess_token());
		sbl.append("&openid=");
		sbl.append(wxToken.getOpenid());
		WxInfo wxInfo = new WxInfo();
		wxInfo = sendWxRequest(sbl,wxInfo);
		return wxInfo;
	}

	private <T> T sendWxRequest(StringBuilder sb,T t) {
		Object o = new Object();
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String StrJson = gson.toJson(o);
		String str = HttpClientUtil.jsonToPost(sb.toString(), StrJson);
		return strToJson(str,t);
	}

	/**
	 * String转对象
	 * @param str
	 * @return
	 */
	public <T> T strToJson(String str,T t){
		Gson gson = new Gson();
		T wxInfo = (T) gson.fromJson(str,t.getClass());
		return wxInfo;
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
	public UsersPOJO queryInfoByUserId(Long userId){
		UsersPOJO user = userDao.selectInfoByUserId(userId);
		return user;
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
				map.put("userId", message.getUserId());
				map.put("phoneNum", message.getPhoneNum());
				userDao.updateUser(map);
			}else{
				//1.2id为空说明验证码已经过期
				status = 1;
			}
		}
		return status;
	}

	@Override
	public String validateAppCode(Message message) {
		return userDao.validateAppCode(message);
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
		int status = userDao.insertMessage(message);
		if(status <= 0){
			error_log.error("用户登录添加验证码信息失败");
			throw new BusinessException("登录添加验证码失败");
		}
		return token;
	}
	@Override
	public boolean findIsMobile(Long userId){
		UsersPOJO user = userDao.findIsMobile(userId);
		if(user.getMobilePhone()!=null&&!"".equals(user.getMobilePhone()))
			return true;
		return false;
	}

}

package com.quxin.freshfun.controller.user;

import com.quxin.freshfun.model.UserMessage;
import com.quxin.freshfun.model.UserMessageInfo;
import com.quxin.freshfun.model.UsersPOJO;
import com.quxin.freshfun.model.param.CommentParam;
import com.quxin.freshfun.model.pojo.CommentPOJO;
import com.quxin.freshfun.service.UserAddressService;
import com.quxin.freshfun.service.comment.CommentService;
import com.quxin.freshfun.service.order.OrderManager;
import com.quxin.freshfun.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserMessageController {
	@Autowired
	private UserAddressService userAddressService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private OrderManager orderManager;
	
	/**userismobile
	 * 添加用户反馈
	 * @return
	 */
	@RequestMapping(value="/usermessage",method={RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> UpdateUserDefaultAddress(@RequestBody UserMessageInfo userMessageInfo){
		Long userId = Long.parseLong(userMessageInfo.getUserId().replace("\"", ""));
	    String message = userMessageInfo.getMessage();
	    
	    System.out.println(userId);
	    System.out.println(message);
	    
		UserMessage userMessage = new UserMessage();
		userMessage.setUserId(userId);
		userMessage.setMessage(message);
		long nowTime = System.currentTimeMillis()/1000;
		userMessage.setGmtModified(nowTime);
		userMessage.setGmtCreate(nowTime);
		Map<String, Object> stateMap = new HashMap<String, Object>(1);
		int demo = userAddressService.insertUserMessage(userMessage);
		if (demo == 1){
			stateMap.put("state", "1");			
		}else{
			stateMap.put("state", "0");	
		}
		return stateMap;
	}
	
	/**
	 * 通过userID获取用户手机号
	 * @return
	 */
	@RequestMapping("/userismobile")
	@ResponseBody
	public Map<String, Object> FindUserIsMobile(String userId){
		Long ui = Long.parseLong(userId.replace("\"", ""));
		Map<String, Object> stateMap = new HashMap<String, Object>(1);
		UsersPOJO userInfo = userAddressService.findIsMobile(ui);
		String mobile = userInfo.getMobilePhone();
		Byte identify = userInfo.getUserIdentify();
		if(mobile == null || mobile == ""){
			stateMap.put("state", 0);
		}else if(identify == 0){
			userAddressService.updateUserIdentify(ui);
			stateMap.put("state", 1);
			
		}else{
			stateMap.put("state", 2);
		}
		return stateMap;
	}
	
	/**
	 * 添加用户留言
	 * @return
	 */
	@RequestMapping(value = "/addusercomment" , method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addUserComment(@RequestBody CommentParam commentParam){
		CommentPOJO comment = new CommentPOJO();
		if(commentParam.getOrderId() != null && !"".equals(commentParam.getOrderId())){
			comment.setOrderId(Long.parseLong(commentParam.getOrderId()));
			comment.setGoodsId(Integer.parseInt(commentParam.getGoodsId()));
			comment.setUserId(Long.parseLong(commentParam.getUserId()));
			comment.setContent(commentParam.getContent());
			comment.setGeneralLevel(commentParam.getGeneralLevel());
			comment.setPackLevel(commentParam.getPackLevel());
			comment.setTasteLevel(commentParam.getTasteLevel());
			comment.setLogisticsLevel(commentParam.getLogisticsLevel());
			comment.setCreated(System.currentTimeMillis()/1000);
			comment.setUpdated(System.currentTimeMillis()/1000);
			Integer count = commentService.addComment(comment);
			if(count == null){
				return ResultUtil.fail(1004,"新增失败");
			}
			orderManager.confirmGoodsComment(commentParam.getOrderId());
			return ResultUtil.success("success");
		}
		return null;
	}

	@RequestMapping(value = "/findComment" , method = RequestMethod.GET)
	@ResponseBody
	public Map<String ,Object> getComment(String orderId){
		if(orderId == null || "".equals(orderId)){
			return ResultUtil.fail(1004,"订单id不能为空");
		}else{
			CommentPOJO commentPOJO = commentService.queryCommentDetailByOrderId(Long.parseLong(orderId));
			if(commentPOJO == null){
				return ResultUtil.fail(1004,"没有评价");
			}else{
				return ResultUtil.success(commentPOJO);
			}
		}
	}
}
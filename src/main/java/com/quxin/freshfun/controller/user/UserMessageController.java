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


import com.quxin.freshfun.model.Comment;
import com.quxin.freshfun.model.CommentInfo;
import com.quxin.freshfun.model.UserMessage;
import com.quxin.freshfun.model.UserMessageInfo;
import com.quxin.freshfun.model.UsersPOJO;
import com.quxin.freshfun.service.UserAddressService;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.service.order.OrderManager;
import com.quxin.freshfun.utils.CodingTools;

@Controller
@RequestMapping("/")
public class UserMessageController {
	@Autowired
	private UserAddressService userAddressService;
	
	@Autowired
	private GoodsService goodsService;
	
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
	@RequestMapping("/addusercomment")
	@ResponseBody
	public Map<String, Object> adduserComment(String orderId,String userId, String goodsId, String content, String commentLevel){
		Map<String, Object> stateMap = new HashMap<String, Object>(1);
		CodingTools codingTools = new CodingTools();
		Comment comment = new Comment();
		
		comment.setUserId(Long.parseLong(userId));
		comment.setGoodsId(Integer.parseInt(goodsId));
		comment.setOrderId(orderId);
		comment.setContent(codingTools.enCodeStr(content));
		comment.setCommentLevel(commentLevel);
		String nowTime = String.valueOf(System.currentTimeMillis()/1000);
		comment.setGmtCreate(System.currentTimeMillis()/1000);
		comment.setGmtModified(System.currentTimeMillis()/1000);
		comment.setIsDeleted((byte)0);
		goodsService.addComment(comment);
		orderManager.confirmGoodsComment(orderId);
		stateMap.put("state", "chenggong");
		return stateMap;
	}

}
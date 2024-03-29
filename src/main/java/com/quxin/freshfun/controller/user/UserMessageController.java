package com.quxin.freshfun.controller.user;

import com.quxin.freshfun.model.UserMessage;
import com.quxin.freshfun.model.UserMessageInfo;
import com.quxin.freshfun.model.UsersPOJO;
import com.quxin.freshfun.model.param.CommentParam;
import com.quxin.freshfun.model.pojo.CommentPOJO;
import com.quxin.freshfun.service.UserAddressService;
import com.quxin.freshfun.service.comment.CommentService;
import com.quxin.freshfun.service.order.OrderManager;
import com.quxin.freshfun.service.user.UserBaseService;
import com.quxin.freshfun.utils.CookieUtil;
import com.quxin.freshfun.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
	@Autowired
	private UserBaseService userBaseService;
	
	/**userismobile
	 * 添加用户反馈
	 * @return 返回是否成功
	 */
	@RequestMapping(value="/usermessage",method={RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> UpdateUserDefaultAddress(@RequestBody UserMessageInfo userMessageInfo,HttpServletRequest request){
	    String message = userMessageInfo.getMessage();

		UserMessage userMessage = new UserMessage();
		//判断参数中是否带有userId
		if(userMessageInfo.getUserId()==null){
			Long userId = CookieUtil.getUserIdFromCookie(request);
			userMessage.setUserId(userId);
		}
		userMessage.setMessage(message);
		long nowTime = System.currentTimeMillis()/1000;
		userMessage.setGmtModified(nowTime);
		userMessage.setGmtCreate(nowTime);
		Map<String, Object> stateMap = new HashMap<String, Object>();
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
	 * @return map
	 */
	@RequestMapping("/userismobile")
	@ResponseBody
	public Map<String, Object> toBeFetcher(HttpServletRequest request){
		Long userId = CookieUtil.getUserIdFromCookie(request);
		Map<String, Object> stateMap = new HashMap<String, Object>();
		boolean isFetcher = userBaseService.checkIsFetcherByUserId(userId);
		if(isFetcher){
			stateMap.put("state", 2);
			return stateMap;
		}else{
			String mobile = userBaseService.queryUserInfoByUserId(userId).getPhoneNumber();
			if(mobile!=null&&!"".equals(mobile)){
				userBaseService.modifyToBeFetcher(mobile,userId);
				stateMap.put("state", 1);
				return stateMap;
			}else{
				stateMap.put("state", 0);
				return stateMap;
			}
		}
	}
	
	/**
	 * 添加用户留言
	 * @return map
	 */
	@RequestMapping(value = "/addusercomment" , method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addUserComment(@RequestBody CommentParam commentParam,HttpServletRequest request){
		CommentPOJO comment = new CommentPOJO();
		if(commentParam.getOrderId() != null && !"".equals(commentParam.getOrderId())){
			try{
				if(commentService.queryCommentDetailByOrderId(Long.parseLong(commentParam.getOrderId()))!=null){
					return ResultUtil.fail(1004,"该订单已经被评论");
				}
			}catch(Exception e){
				return ResultUtil.fail(1004,"该订单已经被评论");
			}
			comment.setOrderId(Long.parseLong(commentParam.getOrderId()));
			comment.setGoodsId(Integer.parseInt(commentParam.getGoodsId()));
			//判断参数中是否带有userId
			if(commentParam.getUserId()!=null&&!"".equals(commentParam.getUserId())){
				comment.setUserId(Long.parseLong(commentParam.getUserId()));
			}else{
				comment.setUserId(CookieUtil.getUserIdFromCookie(request));
			}
			comment.setContent(commentParam.getContent());
			comment.setGeneralLevel(commentParam.getGeneralLevel());
			comment.setPackLevel(commentParam.getPackLevel());
			comment.setTasteLevel(commentParam.getTasteLevel());
			comment.setLogisticsLevel(commentParam.getLogisticsLevel());
			comment.setCreated(System.currentTimeMillis()/1000);
			comment.setUpdated(System.currentTimeMillis()/1000);
			Integer count = commentService.addComment(comment);
			if(count == null){
				return ResultUtil.fail(1004,"新增评论失败");
			}
			int num = orderManager.confirmGoodsComment(commentParam.getOrderId());
			if(num == 0){
				return ResultUtil.fail(1004,"订单"+commentParam.getOrderId()+"状态不是确认收货");
			}
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
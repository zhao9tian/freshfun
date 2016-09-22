package com.quxin.freshfun.controller.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.quxin.freshfun.model.AgentWithdrawPOJO;
import com.quxin.freshfun.model.AgentWithdrawWayPOJO;
import com.quxin.freshfun.service.AgentWithdrawService;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.utils.DateUtils;

/**
 * 商户提现控制器
 * @author ziming
 * 2016-9-20
 */
@Controller
@RequestMapping("/agentwithdraw")
public class AgentWithdrawContorller {
	@Autowired
	private AgentWithdrawService agentWithdrawService;   //商户提现service
	
	@Autowired
	private OrderService orderService;   //order service
	
	/**
	 * 根据userId查询
	 * @return 商户提现方式
	 */
	@RequestMapping("/findAgentWithdrawWay")
	@ResponseBody
	public List<AgentWithdrawWayPOJO> findAgentWithdrawWay(String userId){
		List<AgentWithdrawWayPOJO> listWithdrawWay = null;
		if(userId!=null||!"".equals(userId)){
			listWithdrawWay = agentWithdrawService.queryWithdrawWay(Integer.parseInt(userId));
		}
		return listWithdrawWay;
	}
	
	/**
	 * 根据userId查询m默认提现方式
	 * @return 商户提现方式
	 */
	@RequestMapping("/findDefaultWithdrawWay")
	@ResponseBody
	public AgentWithdrawWayPOJO findDefaultWithdrawWay(String userId){
		AgentWithdrawWayPOJO withdrawWay = new AgentWithdrawWayPOJO();
		if(userId!=null||!"".equals(userId)){
			withdrawWay = agentWithdrawService.queryDefaultWay(Integer.parseInt(userId));
		}
		return withdrawWay;
	}
	
	/**
	 * 新增用户提现方式
	 * @param agentWithdrawWay 用户提现方式
	 * @return 结果： 1成功  0失败
	 */
	@RequestMapping("/addAgentWithdrawWay")
	@ResponseBody
	public Map<String,Object> addAgentWithdrawWay(String userId,String withdrawChannel,String withdrawAccount){
		Map<String,Object> map = Maps.newHashMap();
		AgentWithdrawWayPOJO agentWithdrawWay = new AgentWithdrawWayPOJO();
		agentWithdrawWay.setUserId(Integer.parseInt(userId));
		agentWithdrawWay.setWithdrawChannel(Integer.parseInt(withdrawChannel));
		agentWithdrawWay.setWithdrawAccount(withdrawAccount);
		agentWithdrawWay.setIsDefault(0);
		agentWithdrawWay.setIsDelete(0);
		agentWithdrawWay.setGmtCreate(DateUtils.getCurrentDate());
		agentWithdrawWay.setGmtUpdate(DateUtils.getCurrentDate());
		int result = agentWithdrawService.addWithdrawWay(agentWithdrawWay);
		if(result==1)
			map.put("result", 1);
		else
			map.put("result", 0);
		return map;
	}
	
	@RequestMapping("/setDefaultWay")
	@ResponseBody
	public Map<String,Object> setDefaultWay(String id,String userId){
		Map<String,Object> map = Maps.newHashMap();
		if(id==null||"".equals(id)||userId==null||"".equals(userId)){
			map.put("result", 2);
		}else{
			agentWithdrawService.modifyEscDefaultWay(Integer.parseInt(userId));
			int result = agentWithdrawService.modifySetDefaultWay(Integer.parseInt(id));
			if(result==1)
				map.put("result", 1);
			else
				map.put("result", 0);
		}
		return map;
	}
	/**
	 * 获取可提现金额
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getTheMoney")
	@ResponseBody
	public Map<String,Object> getTheMoney(String userId){
		Map<String,Object> map = Maps.newHashMap();
		//判断商户可提现金额
		Integer earnedIncome = orderService.queryEarnedIncome(Long.parseLong(userId));
		List<AgentWithdrawPOJO> withdrawList = agentWithdrawService.queryWithdraw(Integer.parseInt(userId));
		double moneyAll = 0.0;
		for(AgentWithdrawPOJO awp : withdrawList){
			moneyAll+=awp.getWithdrawMoney();
		}
		double theMoney = ((double)earnedIncome)*0.2-moneyAll*100;
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
		map.put("theMoney",df.format(theMoney/100) );
		return map;
	}
	
	/**
	 * 保存商户申请提现
	 * @param agentWithdraw 申请提现对象
	 * @return 结果： 1成功  0失败
	 */
	@RequestMapping("/addAgentWithdraw")
	@ResponseBody
	public Map<String,Object> addAgentWithdraw(String userId,String wayId,String money){
		Map<String,Object> map = Maps.newHashMap();
		//判断商户可提现金额
		Integer earnedIncome = orderService.queryEarnedIncome(Long.parseLong(userId));
		List<AgentWithdrawPOJO> withdrawList = agentWithdrawService.queryWithdraw(Integer.parseInt(userId));
		double moneyAll = 0.0;
		for(AgentWithdrawPOJO awp : withdrawList){
			moneyAll+=awp.getWithdrawMoney();
		}
		Integer cha = earnedIncome-(int)(moneyAll*100);
		if(cha<0){
			map.put("result", 0);//失败
			return map;
		}
		//提现申请对象赋值
		AgentWithdrawPOJO agentWithdraw = new AgentWithdrawPOJO();
		agentWithdraw.setMerchantProxyId(Integer.parseInt(userId));
		agentWithdraw.setWithdrawId(Integer.parseInt(wayId));
		agentWithdraw.setWithdrawMoney(Double.valueOf(money));
		agentWithdraw.setWithdrawSchedule(0);
		agentWithdraw.setGetCreate(DateUtils.getCurrentDate());
		agentWithdraw.setGmtModified(DateUtils.getCurrentDate());
		//执行保存方法
		int result = agentWithdrawService.addWithdraw(agentWithdraw);
		if(result==1)
			map.put("result", 1);//成功
		else
			map.put("result", 0);//失败
		return map;
	}
	/**
	 * 处理商户申请提现
	 * @param agentWithdraw 申请提现对象
	 * @return 结果： 1成功  0失败
	 */
	@RequestMapping("/modifyAgentWithdraw")
	@ResponseBody
	public Map<String,Object> modifyAgentWithdraw(String id , String result){
		AgentWithdrawPOJO agentWithdraw = new AgentWithdrawPOJO();
		agentWithdraw.setId(Integer.parseInt(id));
		agentWithdraw.setWithdrawSchedule(Integer.parseInt(result));
		agentWithdraw.setGmtModified(DateUtils.getCurrentDate());
		Map<String,Object> map = Maps.newHashMap();
		int result1 = agentWithdrawService.modifyWithdraw(agentWithdraw);
		if(result1==1)
			map.put("result", 1);
		else
			map.put("result", 0);
		return map;
	}
	
	
}

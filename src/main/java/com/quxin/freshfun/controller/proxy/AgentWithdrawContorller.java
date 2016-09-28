package com.quxin.freshfun.controller.proxy;

import java.util.List;
import java.util.Map;

import com.quxin.freshfun.model.parm.AgentExtractMoney;
import com.quxin.freshfun.service.bill.BillService;
import com.quxin.freshfun.utils.MoneyFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@Autowired
	private BillService billService;
	
	/**
	 * 根据userId查询
	 * @return 商户提现方式
	 */
	@RequestMapping("/findAgentWithdrawWay")
	@ResponseBody
	public List<AgentWithdrawWayPOJO> findAgentWithdrawWay(String userId){
		List<AgentWithdrawWayPOJO> listWithdrawWay = null;
		if(userId!=null&&!"".equals(userId)){
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
		if(userId!=null&&!"".equals(userId)){
			withdrawWay = agentWithdrawService.queryDefaultWay(Integer.parseInt(userId));
		}
		return withdrawWay;
	}
	
	/**
	 * 新增用户提现方式
	 * @param userId 用户id
	 * @param withdrawChannel 用户提现方式
	 * @param withdrawAccount 用户提现帐号
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

	/**
	 * 设置用户的默认提现方式
	 * @param id  提现方式id
	 * @param userId  用户id
	 * @return  map结果集
	 */
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
	 * @param userId  用户id
	 * @return map 结果集
	 */
	@RequestMapping("/getTheMoney")
	@ResponseBody
	public Map<String,Object> getTheMoney(String userId){
		Map<String,Object> map = Maps.newHashMap();
		//判断商户可提现金额
		Integer money = billService.selectAgentExtractMoney(Long.parseLong(userId));
		String extractMoney = MoneyFormat.priceFormatString(money);
//		double moneyAll = 0.0;
//		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
//		extractMoney = df.format(extractMoney);
//		String theMoney = extractMoney;
//		moneyAll = Double.valueOf(theMoney);
//		if(moneyAll==0){
//			map.put("theMoney",0.0);
//		}else{
			map.put("theMoney",extractMoney);
//		}
		return map;
	}

	/**
	 * 保存商户申请提现
	 * @param extractMoney 提现参数对象
	 * @return 结果： 1成功  0失败
	 */
	@RequestMapping(value="/addAgentWithdraw",method={RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> addAgentWithdraw(@RequestBody AgentExtractMoney extractMoney) {
		Long money = Math.round(Double.valueOf(extractMoney.getMoney())*100);
		Map<String, Object> map = Maps.newHashMap();
		Integer extractmoney = billService.selectAgentExtractMoney(Long.parseLong(extractMoney.getUserId()));
		String result = billService.addAgentWithdraw(Integer.parseInt(extractMoney.getUserId()),Integer.parseInt(extractMoney.getWayId()),money,extractmoney.longValue());
		map.put("result",result);
		return map;
	}
}

package com.quxin.freshfun.service;

import java.util.List;

import com.quxin.freshfun.model.AgentWithdrawPOJO;
import com.quxin.freshfun.model.AgentWithdrawWayPOJO;

/**
 * 商户提现service接口
 * @author ziming
 * 2016-9-20
 */
public interface AgentWithdrawService {
	/**
	 * 新增商户提现方式
	 * @param agentWithdrawWay 商户提现方式
	 */
	int addWithdrawWay(AgentWithdrawWayPOJO agentWithdrawWay);
	/**
	 * 根据商户id查询商户提现方式
	 * @param userId   商户id
	 * @return  商户提现方式
	 */
	List<AgentWithdrawWayPOJO> queryWithdrawWay(Integer userId);
	
	/**
	 * 根据商户id查询商户提现
	 * @param userId   商户id
	 * @return  商户提现
	 */
	List<AgentWithdrawPOJO> queryWithdraw(Integer userId);
	
	/**
	 * 根据商户id查询商户所有提现方式
	 * @param userId   商户id
	 * @return  商户所有提现方式
	 */
	AgentWithdrawWayPOJO queryDefaultWay(Integer userId);
	
	/**
	 * 取消用户的默认提现渠道
	 * @return 受影响行数
	 */
	int modifyEscDefaultWay(Integer userId);
	
	/**
	 * 设置用户的默认提现渠道
	 * @return 受影响行数
	 */
	int modifySetDefaultWay(Integer id);
	
	/**
	 * 新增商户提现申请
	 * @param agentWithdraw  商户提现申请
	 * @return 结果   1成功   0失败
	 */
	int addWithdraw(AgentWithdrawPOJO agentWithdraw);
	/**
	 * 修改商户提现申请
	 * @param agentWithdraw  商户提现申请
	 * @return 结果   1成功   0失败
	 */
	int modifyWithdraw(AgentWithdrawPOJO agentWithdraw);
}

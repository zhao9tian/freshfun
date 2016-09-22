package com.quxin.freshfun.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.quxin.freshfun.model.AgentWithdrawPOJO;

/**
 * 商户提现申请dao层接口
 * @author ziming
 * 2016-9-20
 */
public interface AgentWithdrawMapper {
	/**
	 * 新增商户提现申请
	 * @param agentWithdraw 商户提现申请
	 * @return 受影响行数
	 */
	int insertSelective(AgentWithdrawPOJO agentWithdraw);
	/**
	 * 查询商户的提现记录
	 * @param userId 商户id
	 * @return  商户的提现记录列表
	 */
	List<AgentWithdrawPOJO> selectAgentWithdrawByUserId(@Param("userId") Integer userId);
	/**
	 * 更改商户的提现记录
	 * @param agentWithdraw 商户提现申请
	 * @return 受影响行数
	 */
	int updateByPrimaryKeySelective(AgentWithdrawPOJO agentWithdraw);
}

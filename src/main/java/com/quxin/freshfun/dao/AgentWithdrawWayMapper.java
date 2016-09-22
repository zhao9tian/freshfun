package com.quxin.freshfun.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.quxin.freshfun.model.AgentWithdrawWayPOJO;

/**
 * 商户提现方式dao层接口
 * @author ziming
 * 2016-9-20
 */
public interface AgentWithdrawWayMapper {
	/**
	 * 根据用户id查询所有提现方式
	 * @param userId  用户id
	 * @return
	 */
	List<AgentWithdrawWayPOJO> selectWithdrawWayByUserId(@Param("userId") Integer userId);
	
	/**
	 * 根据用户id查询默认提现方式
	 * @param userId  用户id
	 * @return
	 */
	List<AgentWithdrawWayPOJO> selectDefaultWay(@Param("userId") Integer userId);
	
	/**
	 * 新增用户提现方式
	 * @param agentWithdrawWay
	 */
	int insertSelective(AgentWithdrawWayPOJO agentWithdrawWay);
	/**
	 * 取消用户的默认提现渠道
	 * @return 受影响行数
	 */
	int updateWayEscDefault(@Param("userId") Integer userId);
	
	/**
	 * 设置用户的默认提现渠道
	 * @return 受影响行数
	 */
	int updateWaySetDefault(@Param("id") Integer id);
}

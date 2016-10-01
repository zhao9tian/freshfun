package com.quxin.freshfun.service.flow;

import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.model.pojo.FlowPOJO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface FlowService {

	/**
	 * 添加财务流水
	 * @param flowParam
	 * @return
	 */
	Boolean add(FlowParam flowParam);

	/**
	 * 通过用户ID查流水
	 * @param userId
	 * @return
	 */
	List<FlowPOJO> queryFlowListByUserId(Long userId, Integer start,Integer pageSize);

	/**
	 * 通过单号查询流水明细
	 * @param orderId
	 * @return
	 */
	FlowPOJO queryFlowByOrderId(Long orderId);

	/**
	 * 查询总数
	 * @param userId
	 * @return
	 */
	Integer getCount(Long userId);
}

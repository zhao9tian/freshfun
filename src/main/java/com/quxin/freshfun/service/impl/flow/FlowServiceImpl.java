package com.quxin.freshfun.service.impl.flow;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.dao.*;
import com.quxin.freshfun.model.*;
import com.quxin.freshfun.model.parm.FlowParam;
import com.quxin.freshfun.model.pojo.FlowPOJO;
import com.quxin.freshfun.service.CommentService;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.utils.MoneyFormat;
import com.quxin.freshfun.utils.ReflectionUtils;
import com.quxin.freshfun.utils.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("flowService")
public class FlowServiceImpl implements FlowService {

	private static Logger logger = LoggerFactory.getLogger(FlowServiceImpl.class);

	@Autowired
	private FlowMapper flowMapper;

	@Override
	public Boolean add(FlowParam flowParam) {

		ValidateUtil validate =validate(flowParam);
		if (validate.isSuc() == false) {
			logger.error(validate.getMsg() +" 入参:" + JSON.toJSONString(flowParam));
			return false;
		}

		Long userId = flowParam.getUserId();

		// 查询这个用户最新的流水
		int id = flowMapper.selectLastedFlowByUserId(userId);

		// 查询流水详情
		FlowPOJO lastedFlowDetail = flowMapper.selectFlowById(id);

		// 余额 = 最新一条的余额 + 本次的flow
		flowParam.setFetcherBalance(lastedFlowDetail.getFetcherBalance() + flowParam.getFetcherFlow());
		flowParam.setAgentBalance(lastedFlowDetail.getAgentBalance() + flowParam.getAgentFlow());

		int insertId = flowMapper.insert(flowParam);
		if (insertId == 0) {
			logger.error("添加流水失败 入参:" + JSON.toJSONString(flowParam));
			return false;
		}

		return true;
	}

	@Override
	public List<FlowPOJO> queryFlowListByUserId(Long userId, Integer start,Integer pageSize) {

		if (userId == null || userId == 0) {
			logger.error("用户ID为空");
			return null;
		}
		if (start == null || start == 0) {
			start = 0;
		}
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId);
		map.put("start",start);
		map.put("pageSize",pageSize);

		// 通过用户ID查流水
		List<FlowPOJO> flowList = flowMapper.selectFlowListByUserId(map);

		return flowList;
	}

	@Override
	public FlowPOJO queryFlowByOrderId(Long orderId) {

		if (orderId == null || orderId == 0) {
			logger.error("订单ID为空");
			return null;
		}

		// 通过单号查询流水明细
		FlowPOJO flowDetail = flowMapper.selectFlowByOrderId(orderId);

		return flowDetail;
	}

	@Override
	public Integer getCount(Long userId) {

		if (userId == null || userId == 0) {
			logger.error("用户ID为空");
			return null;
		}

		// 查询总数
		int cnt = flowMapper.getCount(userId);

		return cnt;
	}

	/**
	 * 参数校验
	 * @param flowParam
	 * @return
	 */
	private ValidateUtil validate(FlowParam flowParam) {

		if(flowParam.getUserId() == null || flowParam.getUserId() == 0) {
			return ValidateUtil.fail("用户ID不能为空");
		}
		if(flowParam.getOrderId() == null || flowParam.getOrderId() == 0) {
			return ValidateUtil.fail("订单ID不能为空");
		}

		return ValidateUtil.success();
	}
}
package com.quxin.freshfun.service.impl.flow;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.dao.*;
import com.quxin.freshfun.model.FlowBasePOJO;
import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.model.pojo.FlowPOJO;
import com.quxin.freshfun.service.flow.FlowService;
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

		long fetcherBalance = 0;
		long agentBalance = 0;

		// 查询这个用户最新的流水
		Integer id = flowMapper.selectLastedFlowByUserId(userId);
		if (id != null) {
			// 查询流水详情
			FlowPOJO lastedFlowDetail = flowMapper.selectFlowById(id);
			fetcherBalance = lastedFlowDetail.getFetcherBalance();
			agentBalance = lastedFlowDetail.getAgentBalance();
		}

		// 余额 = 最新一条的余额 + 本次的flow
		flowParam.setFetcherBalance(fetcherBalance + flowParam.getFetcherFlow());
		flowParam.setAgentBalance(agentBalance + flowParam.getAgentFlow());

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

	@Override
	public Boolean addFlowBase(FlowBasePOJO flowBasePOJO) {
		if (validateFlow(flowBasePOJO)) {
			//最后一条记录的余额
			Integer lastestBalance = flowMapper.selectBalanceByAppId(flowBasePOJO.getAppId());
			Integer newBalance = 0;
			if (lastestBalance != null) {//存在余额
				if (flowBasePOJO.getFlowType() == 0) {//入账
					newBalance = lastestBalance + flowBasePOJO.getFlowMoney();
				} else if (flowBasePOJO.getFlowType() == 1) {//提现
					newBalance = lastestBalance - flowBasePOJO.getFlowMoney();
					if (newBalance < 0) {
						logger.error("提现金额大于余额");
						return false;
					}
				}
			} else {//不存在余额,无法提现,只会入账
				newBalance = flowBasePOJO.getFlowMoney();
				if (flowBasePOJO.getFlowType() == 1) {
					logger.error("提现金额大于余额");
					return false;
				}
			}
			flowBasePOJO.setBalance(newBalance);
			flowBasePOJO.setIsDeleted(0);//默认值
			try {
				if (flowMapper.insertFlow(flowBasePOJO) == 1) {
					return true;
				}
			} catch (Exception e) {
				logger.error("mybatis插入异常" + e);
			}

		}
		return false;
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


	/**
	 * 校验新的流水
	 *
	 * @param flow 流水对象
	 * @return 是否通过校验
	 */
	private boolean validateFlow(FlowBasePOJO flow) {
		if (flow != null) {
			if (flow.getAppId() == null || 0 == flow.getAppId()) {
				logger.error("appId为空");
				return false;
			}
			if (flow.getFlowType() == null) {
				logger.error("流水类型为空");
				return false;
			} else {
				if (flow.getFlowType() == 0) {
					if (flow.getOrderId() == null) {
						logger.error("入账记录orderId为空");
						return false;
					}
				}
			}
			if (flow.getFlowMoney() == null) {
				logger.error("流水金额为空");
				return false;
			}
			if (flow.getCreated() == null) {
				logger.error("流水记录生成时间为空");
				return false;
			}
			if (flow.getUpdated() == null){
				logger.error("流水记录编辑时间为空");
				return false;
			}

		} else {
			logger.error("流水对象为空");
			return false;
		}
		return true;
	}
}
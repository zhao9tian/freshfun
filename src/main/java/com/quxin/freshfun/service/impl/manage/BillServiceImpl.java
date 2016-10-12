package com.quxin.freshfun.service.impl.manage;


import java.util.List;

import com.quxin.freshfun.dao.*;
import com.quxin.freshfun.model.*;
import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.utils.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.service.bill.BillService;
import com.quxin.freshfun.utils.DateUtils;
@Service("billService")
public class BillServiceImpl implements BillService {
	@Autowired
	private OrderDetailsMapper orderDetailsMapper;
	@Autowired
	private FlowService flowService;

	private Logger logger = LoggerFactory.getLogger("info_log");

	/**
	 * 7天订单自动走账单流程
	 * @return
	 */
	@Override
	public Integer addAutoConfirmRecording() throws BusinessException {
		logger.info("订单自动走账单流程");
		Long currentDate = DateUtils.getCurrentDate();
		List<OrderDetailsPOJO> orderDetails = orderDetailsMapper.selectAwaitPayMoney();
		Integer status = 0;
		if(orderDetails != null) {
			for (OrderDetailsPOJO order : orderDetails) {
				status = orderDetailsMapper.updateAlreadyPayMoney(order.getId());
				if (status <= 0) {
					logger.error("修改订单状态为已完成失败");
					throw new BusinessException("修改订单状态为已完成状态失败");
				}else {
					//添加账单流水信息
					if (order.getAgentId() != 0) {
						//添加代理商户账单信息
						FlowParam flowParam = new FlowParam();
						flowParam.setOrderId(order.getId());
						flowParam.setUserId(order.getAgentId());
						flowParam.setCreated(currentDate);
						flowParam.setUpdated(currentDate);
						flowParam.setAgentFlow(order.getAgentPrice().longValue());
						flowService.add(flowParam);
					}
					if (order.getFetcherId() != 0) {
						//添加捕手账单信息
						FlowParam flowParam = new FlowParam();
						flowParam.setOrderId(order.getId());
						flowParam.setUserId(order.getFetcherId());
						flowParam.setCreated(currentDate);
						flowParam.setUpdated(currentDate);
						flowParam.setFetcherFlow(order.getFetcherPrice().longValue());
						flowService.add(flowParam);
					}
				}
			}
		}
		return status;
	}

}

package com.quxin.freshfun.service.order;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.quxin.freshfun.dao.ResponseResult;
import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.model.OrderInfo;
import com.quxin.freshfun.model.OrderPayPOJO;
import com.quxin.freshfun.model.OrdersPOJO;
import com.quxin.freshfun.utils.BusinessException;

@Service
public interface OrderService {
	/**
	 * 查询所有订单
	 * @return
	 */
	public List<OrdersPOJO> findAllOrders();
	/**
	 * 添加订单信息
	 * @param order
	 * @return
	 */
	ResponseResult addOrder(OrderInfo orderInfo) throws BusinessException;
	/**
	 * 订单支付
	 * @param order
	 * @return
	 */
	ResponseResult awaitPayOrder(OrderPayPOJO order);
	
	int PayCallback(InputStream in);
	
	/**
	 * 添加订单信息
	 * @param order
	 * @return
	 */
	ResponseResult addLimitOrder(OrderInfo orderInfo);
	
	/**
	 * 根据订单详情id查询订单详情
	 * @param orderDetailId
	 * @return
	 */
	OrderDetailsPOJO getOrderLogistic(String orderDetailId);
	
}
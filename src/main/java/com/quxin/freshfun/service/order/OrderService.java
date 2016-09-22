package com.quxin.freshfun.service.order;

import java.io.InputStream;
import java.util.List;

import com.quxin.freshfun.model.*;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.dao.ResponseResult;
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
	 * @param orderInfo
	 * @return
	 */
	ResponseResult addOrder(OrderInfo orderInfo) throws BusinessException;

	/**
	 * 全名电商支付
	 * @param userId
	 * @param goodsId
	 * @return
	 */
	ResponseResult addQuanMingPay(QuanMingPayInfo payInfo) throws BusinessException;
	/**
	 * 订单支付
	 * @param order
	 * @return
	 */
	ResponseResult awaitPayOrder(OrderPayPOJO order);
	
	int PayCallback(InputStream in);
	
	/**
	 * 添加订单信息
	 * @param orderInfo
	 * @return
	 */
	ResponseResult addLimitOrder(OrderInfo orderInfo);
	
	/**
	 * 根据订单详情id查询订单详情
	 * @param orderDetailId
	 * @return
	 */
	OrderDetailsPOJO getOrderLogistic(String orderDetailId);


	/**
	 * 获取总收益
	 * @param id
	 * @return
	 */
	Integer queryAllIncome(Long id);

	/**
	 * 获取已入账收益
	 * @param id
	 * @return
	 */
	Integer queryEarnedIncome(Long id);
}
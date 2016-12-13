package com.quxin.freshfun.service.order;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.quxin.freshfun.model.*;
import com.quxin.freshfun.model.outparam.WxPayInfo;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.dao.ResponseResult;
import com.quxin.freshfun.utils.BusinessException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public interface OrderService {

	/**
	 * 添加订单信息
	 * @param orderInfo
	 * @return
	 */
	WxPayInfo addOrder(OrderInfo orderInfo,HttpServletRequest request) throws BusinessException, JSONException;

	/**
	 * 全名电商支付
	 * @param payInfo
	 * @return
	 */
	ResponseResult addQuanMingPay(QuanMingPayInfo payInfo,Long userId) throws BusinessException;

	/**
	 * App订单支付
	 * @param orderId
	 * @return
	 */
	WxPayInfo appOrderPay(String orderId,HttpServletRequest request) throws UnsupportedEncodingException, JSONException;
	/**
	 * 订单支付
	 * @param order
	 * @return
	 */
	WxPayInfo awaitPayOrder(HttpServletRequest request,OrderPayPOJO order,Long userId) throws BusinessException, JSONException;

	/**
	 * 二维码支付
	 */
	String QRCodePay(HttpServletRequest request,String productId,String openId) throws BusinessException, JSONException;

	/**
	 * 支付回调
	 * @param in 输出流
	 * @return
	 */
	int PayCallback(InputStream in);
	
	/**
	 * 根据订单详情id查询订单详情
	 * @param orderId 订单Id
	 * @return 返回物流信息
	 */
	OrderDetailsPOJO getOrderLogistic(Long orderId);
	/**
	 * 根据订单编号查询二维码支付url
	 * @param orderId
	 * @return
	 */
	String findPayUrl(Long orderId) throws BusinessException;

	/**
	 * 原生支付
	 * @return
	 */
	WxPayInfo addWeixinAppPay(OrderInfo orderInfo, HttpServletRequest request) throws BusinessException, UnsupportedEncodingException, JSONException;

	/**
	 * 根据父级订单编号查询支付信息
	 * @return
	 */
	List<OrderDetailsPOJO> selectPayId(Long parentOrderId);

	void senderMail(OrderDetailsPOJO order,int sign);
}
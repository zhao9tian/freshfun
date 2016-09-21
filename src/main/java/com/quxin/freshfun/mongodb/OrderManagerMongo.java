package com.quxin.freshfun.mongodb;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.OrderInPieces;
import com.quxin.freshfun.model.OrdersPOJO;
@Repository
public interface OrderManagerMongo {
	/**
	 * 添加订单详情
	 * @param order
	 */
	void addOrderDetail(OrderInPieces order);
	/**
	 * 修改订单支付状态
	 * @return
	 */
	int updateOrderDetailPayStatus(String orderId);
	/**
	 * 删除订单
	 * @param orderId
	 * @return
	 */
	int delOrder(Integer orderId);
	/**
	 * 根据用户编号查询所有订单信息
	 * @param userId
	 * @return
	 */
	List<OrdersPOJO> findAll(Integer userId,int currentPage,int pageSize);
	/**
	 * 根据用户编号查询待付款订单
	 * @param userId
	 * @return
	 */
	List<OrderInPieces> selectObligationOrder(String userId,Integer currentPage,Integer pageSize);
	/**
	 * 根据用户编号查询待收货订单
	 * @param userId
	 * @return
	 */
	List<OrderInPieces> selectStayReceiveOrder(String userId,Integer currentPage,Integer pageSize);
	/**
	 * 根据用户编号查询待发货订单
	 * @return
	 */
	List<OrderInPieces> selectStayDeliveryGoodsOrders(String userId,Integer currentPage,Integer pageSize);
	/**
	 * 根据用户编号查询待评价订单
	 * @param userId
	 * @return
	 */
	List<OrderInPieces> selectStayAssessOrder(String userId,Integer currentPage,Integer pageSize);
	/**
	 * 根据用户编号查询退货订单
	 * @param userId
	 * @return
	 */
	List<OrderInPieces> selectCancelOrder(String userId,Integer currentPage,Integer pageSize);
}

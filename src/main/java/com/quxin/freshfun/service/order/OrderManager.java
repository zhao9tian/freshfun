package com.quxin.freshfun.service.order;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.model.OrderStatusInfo;
@Service
public interface OrderManager {
	/**
	 * 删除订单
	 * @param orderId
	 * @return
	 */
	int delOrder(String orderId);
	/**
     * 根据订单编号查询订单
     * @param orderDetailsId
     * @return
     */
   OrderDetailsPOJO selectSigleOrder(String orderDetailsId);
	/**
	 * 根据用户编号查询所有订单信息
	 * @param userId
	 * @return
	 */
	List<OrderDetailsPOJO> findAll(Long userId,int currentPage,int pageSize);
	/**
	 * 根据用户编号查询待付款订单
	 * @param userId
	 * @return
	 */
	List<OrderDetailsPOJO> selectPendingPaymentOrder(Long userId,Integer currentPage,Integer pageSize);
	/**
	 * 根据用户编号查询待收货订单
	 * @param userId
	 * @return
	 */
	List<OrderDetailsPOJO> selectAwaitGoodsReceipt(Long userId,Integer currentPage,Integer pageSize);
	/**
	 * 根据用户编号查询待发货订单
	 * @return
	 */
	List<OrderDetailsPOJO> selectAwaitDeliverOrder(Long userId,Integer currentPage,Integer pageSize);
	/**
	 * 根据用户编号查询待评价订单
	 * @param userId
	 * @return
	 */
	List<OrderDetailsPOJO> selectAwaitComment(Long userId,Integer currentPage,Integer pageSize);
	/**
	 * 根据用户编号查询退货订单
	 * @param userId
	 * @return
	 */
	List<OrderDetailsPOJO> selectCancelOrder(Long userId,Integer currentPage,Integer pageSize);
	/**
	 * 根据用户编号查询购物车信息
	 * @param userId
	 * @return
	 */
	Map<String,Object> selectShoppingCartByUserId(Long userId);
	/**
	 * 添加到购物车
	 * @param userId
	 * @param goodsId
	 * @return
	 */
	Integer addShoppingCart(Long userId,Integer goodsId);
	/**
     * 查询商品数量
     * @return
     */
    Integer selectGoodsTotals(Integer scId);
    /**
     * 添加商品数量
     * @param scId
     * @return
     */
    Integer addGoodsTotals(Integer scId);
    /**
     * 减少商品数量
     * @param scId
     * @return
     */
    Integer reduceGoodsTotals(Integer scId);
    /**
     * 确认收货
     * @param orderId
     * @return
     */
    Integer confirmGoodsReceipt(String orderId);
    /**
     * 刪除訂單
     * @param scId
     * @return
     */
    Integer delShoppingCartOrder(Integer scId);
    
    /**
     * 确认评价
     * @param orderId
     * @return
     */
    int confirmGoodsComment(String orderId);
	
	
	/**
     * 查询所有未退款订单状态数量
     * @param userID
     * @return
     */
	List<OrderStatusInfo> selectStatusCounts(Long userID);
	
	/**
     * 查询退款订单数量
     * @param userID
     * @return
     */
	List<OrderStatusInfo> selectRefundCounts(Long userID);
	
	/**
     * 查询未付款订单数量
     * @param userID
     * @return
     */
	List<OrderStatusInfo> selectPayCounts(Long userID);
	
	/**
	 * 根据订单ID修改订单状态
	 * @param orderDetailId
	 * @return
	 */
	Integer confirmReceipt(String orderDetailId);
	
	/**
	 * 申请退款
	 * @param orderDetailId
	 * @return
	 */
	Integer applyRefund(String orderDetailId);
	
	
}

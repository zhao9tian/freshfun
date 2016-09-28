package com.quxin.freshfun.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.model.OrderStatusInfo;
import com.quxin.freshfun.model.PayModify;

public interface OrderDetailsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_details
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_details
     *
     * @mbggenerated
     */
    int insert(OrderDetailsPOJO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_details
     *
     * @mbggenerated
     */
    int insertSelective(OrderDetailsPOJO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_details
     *
     * @mbggenerated
     */
    OrderDetailsPOJO selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_details
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(OrderDetailsPOJO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_details
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(OrderDetailsPOJO record);
    /**
     * 根据订单编号查询订单
     * @param orderDetailsId
     * @return
     */
    List<OrderDetailsPOJO> selectSigleOrder(String orderDetailsId);
    /**
     * 修改订单支付状态
     * @param orderId
     * @return
     */
    int updatePayStatus(Long orderId);
    /**
     * 分页查询所有订单信息
     * @param currentPage
     * @param pageSize
     * @param userId
     * @return
     */
    List<OrderDetailsPOJO> selectAll(@Param("currentPage")int currentPage,@Param("pageSize")int pageSize,@Param("userId")Long userId);
    /**
     * 查询代发货订单
     * @param currentPage
     * @param pageSize
     * @param userId
     * @return
     */
    List<OrderDetailsPOJO> selectAwaitDeliverOrder(@Param("currentPage")int currentPage,@Param("pageSize")int pageSize,@Param("userId")Long userId);
    /**
     * 查询待付款订单
     * @param currentPage
     * @param pageSize
     * @param userId
     * @return
     */
    List<OrderDetailsPOJO> selectPendingPaymentOrder(@Param("currentPage")int currentPage,@Param("pageSize")int pageSize,@Param("userId")Long userId);
    /**
     * 待收货订单
     * @return
     */
    List<OrderDetailsPOJO> selectAwaitGoodsReceipt(@Param("currentPage")int currentPage,@Param("pageSize")int pageSize,@Param("userId")Long userId);
    /**
     * 查询待评价订单
     * @param currentPage
     * @param pageSize
     * @param userId
     * @return
     */
    List<OrderDetailsPOJO> selectAwaitComment(@Param("currentPage")int currentPage,@Param("pageSize")int pageSize,@Param("userId")Long userId);
    /**
     * 查询退货订单
     * @return
     */
    List<OrderDetailsPOJO> selectCancelOrder(@Param("currentPage")int currentPage,@Param("pageSize")int pageSize,@Param("userId")Long userId);
    /**
     * 删除一条订单
     * @param orderId
     * @return
     */
    int delOrder(@Param("date") Long date,@Param("orderId") String orderId);
    /**
     * 确认收货
     * @param orderId
     * @return
     */
    int confirmGoodsReceipt(String orderId);
    
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
     * 查询未付款订单数
     * @param userID
     * @return
     */
    List<OrderStatusInfo> selectPayCounts(Long userID);
    /**
     * 查询待支付订单
     * @param userId
     * @return
     */
    OrderDetailsPOJO selectPayOrder(String userId);

    /**
     * 根据订单Id查询物流信息
     * @param orderDetailId
     * @return
     */
	OrderDetailsPOJO getLogistic(String orderDetailId);
	/**
	 * 查询支付编号
	 * @return
	 */
	Integer selectPayIdByOrderDetailsId(String orderId);
	/**
	 * 修改订单支付状态
	 * @param date
	 * @param orderId
	 * @return
	 */
	Integer updateOrderDetailPayStatus(@Param("date") Long date,@Param("orderId") Long orderId);

	/**
	 * 修改成功将状态改为3
	 * @param orderDetailId
	 * @return
	 */
	Integer updateOrderStatus(Map<String , Object> condition);

    void updateInState(String orderId);
	/**
	 * 查询支付订单编号
	 * @return
	 */
	List<Long> selectPayId(Long orderId);
	/**
	 * 批量修改订单状态
	 * @param orderDetails
	 * @return
	 */
	public int bachUpdateOrder(List<PayModify> list);

	/**
	 * 申请退款是否成功
	 * @param orderDetailId
	 * @return
	 */
	Integer applyRefund(String orderDetailId);

    /**
     * 获取总收益
     * @param id
     * @return
     */
    Integer selectAllIncome(Long id);

    /**
     * 获取已入账收益
     * @param id
     * @return
     */
    Integer selectEarnedIncome(Long id);
}
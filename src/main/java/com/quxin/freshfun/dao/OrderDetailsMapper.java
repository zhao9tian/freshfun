package com.quxin.freshfun.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.model.OrderStatusInfo;

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
    List<OrderDetailsPOJO> selectSigleOrder(Long orderDetailsId);
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
     * 确认评价
     * @param orderId
     * @return
     */
    int confirmGoodsComment(String orderId);

    /**
     * 14天自动收货
     * @return
     */
    List<OrderDetailsPOJO> selectUnConfirmOrder();

    /**
     * 查询订单确认收货超过7天
     * @return
     */
    List<OrderDetailsPOJO> selectAwaitPayMoney();

    /**
     * 修改订单状态为已打款
     * @param orderId
     * @return
     */
    Integer updateAlreadyPayMoney(Long orderId);

    /**
     * 查询所有未退款订单状态数量
     * @param userId
     * @return
     */
    List<OrderStatusInfo> selectStatusCounts(Long userId);

    /**
     * 查询退款订单数量
     * @param userId
     * @return
     */
    List<OrderStatusInfo> selectRefundCounts(Long userId);
    /**
     * 查询未付款订单数
     * @param userId
     * @return
     */
    Integer selectPayCounts(Long userId);
    /**
     * 查询待支付订单
     * @param userId
     * @return
     */
    OrderDetailsPOJO selectPayOrder(String userId);

    /**
     * 根据订单Id查询物流信息
     * @param orderId
     * @return
     */
    OrderDetailsPOJO getLogistic(Long orderId);
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
    Integer updateOrderDetailPayStatus(@Param("date") Long date,@Param("orderId") Long orderId,@Param("transactionId") String transactionId);

    /**
     * 修改成功将状态改为70
     * @return
     */
    Integer updateOrderStatus(Map<String , Object> condition);

    /**
     * 根据父级订单编号查询支付信息
     * @return
     */
    List<OrderDetailsPOJO> selectPayId(Long parentOrderId);

    /**
     * 根据订单编号查询支付信息
     * @param orderId
     * @return
     */
    OrderDetailsPOJO selectPayOrderInfoById(Long orderId);

    /**
     * 申请退款是否成功
     * @param orderDetailId
     * @return
     */
    Long applyRefund(String orderDetailId);

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

    /**
     * 查询待评价数量
     * @param userId
     * @return
     */
    Integer selectCommentCount(Long userId);

    /**
     * 根据订单编号查询账单需要的信息
     * @param orderId
     * @return
     */
    OrderDetailsPOJO selectConfirmOrderInfo(String orderId);

    List<OrderDetailsPOJO> selectOrders();

    Integer updatePayPrice(@Param("payPrice") Integer payPrice,@Param("id") Long id);
}
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
    int confirmGoodsReceipt(@Param("reciveTime") Long reciveTime,@Param("orderId") Long orderId);

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
    List<OrderStatusInfo> selectPayCounts(Long userId);
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
     * 修改成功将状态改为70
     * @param condition
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
     * @param list
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


    //后台订单管理

    /**
     * 查询关闭订单
     * @param currentPage
     * @param pageSize
     * @return
     */
    List<OrderDetailsPOJO> selectBackstageOrderClose(@Param("currentPage") int currentPage,@Param("pageSize") int pageSize);

    /**
     * 查询关闭订单数量
     * @return
     */
    Integer selectBackstageOrderCloseCount();
    /**
     * 所有订单
     * @param currentPage
     * @param pageSize
     * @return
     */
    List<OrderDetailsPOJO>  selectBackstageOrders(@Param("currentPage") int currentPage,@Param("pageSize") int pageSize);

    /**
     * 查询所有订单总数量
     * @return
     */
    Integer selectBackstageOrdersCount();
    /**
     * 待付款订单
     * @param currentPage
     * @param pageSize
     * @return
     */
    List<OrderDetailsPOJO> selectBackstagePendingPaymentOrder(@Param("currentPage") int currentPage,@Param("pageSize") int pageSize);

    /**
     * 查询待付款数量
     * @return
     */
    Integer selectBackstagePendingPaymentOrderCount();
    /**
     * 待发货
     * @param currentPage
     * @param pageSize
     * @return
     */
    List<OrderDetailsPOJO> selectBackstageAwaitDeliverOrder(@Param("currentPage") int currentPage,@Param("pageSize") int pageSize);

    /**
     * 查询代发货数量
     * @return
     */
    Integer selectBackstageAwaitDeliverOrderCount();
    /**
     * 待收货
     * @param currentPage
     * @param pageSize
     * @return
     */
    List<OrderDetailsPOJO> selectBackstageAwaitGoodsReceipt(@Param("currentPage") int currentPage,@Param("pageSize") int pageSize);

    /**
     * 查询待收货数量
     * @return
     */
    Integer selectBackstageAwaitGoodsReceiptCount();
    /**
     * 查询已完成订单
     * @param currentPage
     * @param pageSize
     * @return
     */
    List<OrderDetailsPOJO> selectFinishOrder(@Param("currentPage") int currentPage,@Param("pageSize") int pageSize);

    /**
     * 查询已完成订单数量
     * @return
     */
    Integer selectFinishOrderCount();

    /**
     * 发货
     * @param map
     * @return
     */
    Integer deliverOrder(Map<String,Object> map);

    /**
     * 订单备注
     * @return
     */
    Integer orderRemark(Map<String,Object> map);

}
package com.quxin.freshfun.model.param;

/**
 * 退款入参
 * Created by qucheng on 2016/10/20.
 */
public class RefundParam {

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 服务类型
     */
    private String serverType;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 退款金额
     */
    private String refundMoney;

    /**
     * 退款描述
     */
    private String refundDes;


    /******************************兼容IOS*************************************/
    private String orderDetailsId;
    private String serviceType;
    private String returnReason;
    private String returnDes;
    private String returnMoney;


    public String getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(String orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getReturnDes() {
        return returnDes;
    }

    public void setReturnDes(String returnDes) {
        this.returnDes = returnDes;
    }

    public String getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(String returnMoney) {
        this.returnMoney = returnMoney;
    }

    /******************************兼容IOS*************************************/


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(String refundMoney) {
        this.refundMoney = refundMoney;
    }

    public String getRefundDes() {
        return refundDes;
    }

    public void setRefundDes(String refundDes) {
        this.refundDes = refundDes;
    }
}

package com.quxin.freshfun.model;

/**
 * 流水实体类
 * Created by qucheng on 16/11/22.
 */
public class FlowBasePOJO {

    /**
     * 流水ID
     */
    private Long flowId ;
    /**
     * 平台Id
     */
    private Long appId ;
    /**
     *订单ID
     */
    private Long orderId ;
    /**
     *流水金额
     */
    private Integer flowMoney;
    /**
     *余额
     */
    private Integer balance;
    /**
     *流水类型  0:入账 1:提现
     */
    private Integer flowType ;
    /**
     *创建时间
     */
    private Long created;
    /**
     *修改时间
     */
    private Long updated;
    /**
     *是否删除 0:未删除 1:已删除
     */
    private Integer isDeleted;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getFlowMoney() {
        return flowMoney;
    }

    public void setFlowMoney(Integer flowMoney) {
        this.flowMoney = flowMoney;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getFlowType() {
        return flowType;
    }

    public void setFlowType(Integer flowType) {
        this.flowType = flowType;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "FlowPOJO{" +
                "flowId=" + flowId +
                ", appId=" + appId +
                ", orderId=" + orderId +
                ", flowMoney=" + flowMoney +
                ", balance=" + balance +
                ", flowType=" + flowType +
                ", created=" + created +
                ", updated=" + updated +
                ", isDeleted=" + isDeleted +
                '}';
    }
}

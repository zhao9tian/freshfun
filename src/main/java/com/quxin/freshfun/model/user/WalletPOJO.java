package com.quxin.freshfun.model.user;

/**
 * 钱包实体
 * Created by qucheng on 17/1/18.
 */
public class WalletPOJO {

    /**
     * 用户id
     */
    private Long userId ;

    /**
     * 订单id
     */
    private Long orderId ;

    /**
     * 类型- 0:返现  1:支出  2:退款
     */
    private Integer type ;

    /**
     * 流水金额
     */
    private Long flowMoney ;

    /**
     * 余额
     */
    private Long balance ;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getFlowMoney() {
        return flowMoney;
    }

    public void setFlowMoney(Long flowMoney) {
        this.flowMoney = flowMoney;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "WalletPOJO{" +
                "userId=" + userId +
                ", orderId=" + orderId +
                ", type=" + type +
                ", flowMoney=" + flowMoney +
                ", balance=" + balance +
                '}';
    }
}

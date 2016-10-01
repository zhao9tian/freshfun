package com.quxin.freshfun.model.pojo;

/**
 * Created by qucheng on 2016/9/28.
 */
public class WithdrawPOJO {
    private String id ;
    /**用户id*/
    private Long userId;
    /*提现金额*/
    private Long withDrawPrice;
    /**提现方式*/
    private String withDrawType;
    /**提现账户*/
    private String paymentAccount;
    /**申请状态*/
    private Integer state;

    private Integer withdrawSource;
    /**处理人*/
    private String dealPerson;
    /**处理描述*/
    private String handlerReason;
    /**退款申请时间*/
    private Long createDate;
    /**处理时间*/
    private Long updateDate;
    /**是否已删除*/
    private Integer isDelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getWithDrawPrice() {
        return withDrawPrice;
    }

    public void setWithDrawPrice(Long withDrawPrice) {
        this.withDrawPrice = withDrawPrice;
    }

    public String getWithDrawType() {
        return withDrawType;
    }

    public void setWithDrawType(String withDrawType) {
        this.withDrawType = withDrawType;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDealPerson() {
        return dealPerson;
    }

    public void setDealPerson(String dealPerson) {
        this.dealPerson = dealPerson;
    }

    public String getHandlerReason() {
        return handlerReason;
    }

    public void setHandlerReason(String handlerReason) {
        this.handlerReason = handlerReason;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getWithdrawSource() {
        return withdrawSource;
    }

    public void setWithdrawSource(Integer withdrawSource) {
        this.withdrawSource = withdrawSource;
    }
}

package com.quxin.freshfun.model;

public class UserRevenueDetails {

    private Integer id;

    private Integer userRevenueId;

    private String orderInfoId;

    private Integer revenueDetailPrice;

    private Long createDate;

    private Long updateDate;

    private Integer revenueType;

    private Integer isDelete;

    private String reset;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserRevenueId() {
        return userRevenueId;
    }

    public void setUserRevenueId(Integer userRevenueId) {
        this.userRevenueId = userRevenueId;
    }

    public String getOrderInfoId() {
        return orderInfoId;
    }

    public void setOrderInfoId(String orderInfoId) {
        this.orderInfoId = orderInfoId;
    }

    public Integer getRevenueDetailPrice() {
        return revenueDetailPrice;
    }

    public void setRevenueDetailPrice(Integer revenueDetailPrice) {
        this.revenueDetailPrice = revenueDetailPrice;
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

    public Integer getRevenueType() {
        return revenueType;
    }

    public void setRevenueType(Integer revenueType) {
        this.revenueType = revenueType;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getReset() {
        return reset;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    public UserRevenueDetails() {
		super();
	}
}
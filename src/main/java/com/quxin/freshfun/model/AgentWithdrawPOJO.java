package com.quxin.freshfun.model;

public class AgentWithdrawPOJO {
    private Integer id;

    private Long getCreate;

    private Long gmtModified;

    private Integer merchantProxyId;

    private Double withdrawMoney;

    private Integer withdrawId;

    private Integer withdrawSchedule;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getGetCreate() {
        return getCreate;
    }

    public void setGetCreate(Long getCreate) {
        this.getCreate = getCreate;
    }

    public Long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getMerchantProxyId() {
        return merchantProxyId;
    }

    public void setMerchantProxyId(Integer merchantProxyId) {
        this.merchantProxyId = merchantProxyId;
    }

    public Double getWithdrawMoney() {
        return withdrawMoney;
    }

    public void setWithdrawMoney(Double withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }

    public Integer getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(Integer withdrawId) {
        this.withdrawId = withdrawId;
    }

    public Integer getWithdrawSchedule() {
        return withdrawSchedule;
    }

    public void setWithdrawSchedule(Integer withdrawSchedule) {
        this.withdrawSchedule = withdrawSchedule;
    }
}
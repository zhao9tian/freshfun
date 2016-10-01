package com.quxin.freshfun.model.param;

import com.quxin.freshfun.model.entity.BaseEntity;

/**
 * Created by tianmingzhao on 16/9/29.
 */
public class FlowParam extends BaseEntity {
    private Long userId;
    private Long orderId;
    private Long agentFlow;
    private Long fetcherFlow;
    private Long agentBalance;
    private Long fetcherBalance;


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

    public Long getAgentFlow() {
        return agentFlow;
    }

    public void setAgentFlow(Long agentFlow) {
        this.agentFlow = agentFlow;
    }

    public Long getFetcherFlow() {
        return fetcherFlow;
    }

    public void setFetcherFlow(Long fetcherFlow) {
        this.fetcherFlow = fetcherFlow;
    }

    public Long getAgentBalance() {
        return agentBalance;
    }

    public void setAgentBalance(Long agentBalance) {
        this.agentBalance = agentBalance;
    }

    public Long getFetcherBalance() {
        return fetcherBalance;
    }

    public void setFetcherBalance(Long fetcherBalance) {
        this.fetcherBalance = fetcherBalance;
    }

}

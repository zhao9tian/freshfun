package com.quxin.freshfun.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrderStatusInfo {
	private Integer orderStatus;
	
	private Integer statusCounts;
	
	private Integer refundCounts;
	
	private Integer payCounts;


	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getStatusCounts() {
		return statusCounts;
	}

	public void setStatusCounts(Integer statusCounts) {
		this.statusCounts = statusCounts;
	}

	public Integer getRefundCounts() {
		return refundCounts;
	}

	public void setRefundCounts(Integer refundCounts) {
		this.refundCounts = refundCounts;
	}

	public Integer getPayCounts() {
		return payCounts;
	}

	public void setPayCounts(Integer payCounts) {
		this.payCounts = payCounts;
	}
}

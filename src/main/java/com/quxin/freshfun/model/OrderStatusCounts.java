package com.quxin.freshfun.model;

public class OrderStatusCounts {
	
    private Integer order_status;
	
	private Integer status_counts;
	
	private Integer refund_counts;

	public Integer getOrder_status() {
		return order_status;
	}

	public void setOrder_status(Integer order_status) {
		this.order_status = order_status;
	}

	public Integer getStatus_counts() {
		return status_counts;
	}

	public void setStatus_counts(Integer status_counts) {
		this.status_counts = status_counts;
	}

	public Integer getRefund_counts() {
		return refund_counts;
	}

	public void setRefund_counts(Integer refund_counts) {
		this.refund_counts = refund_counts;
	}

}

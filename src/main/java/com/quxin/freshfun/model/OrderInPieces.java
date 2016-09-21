package com.quxin.freshfun.model;

public class OrderInPieces {
	private String id;
	private String orderId;
	private String orderPieceId;
	private String userId;
	private Integer goodsId;
	private Integer deliberyNum;
	private Integer commentId;
	private Byte paymentMethod;
	private Long payTime;
	private Long deliveryDate;
	private Integer orderStatus;
	private Integer refundStatus;
	private Integer commentStatus;
	private Integer shippingFee;
	private Integer originPrice;
	private Integer actualPrice;
	private String deliveryCompany;
	private Integer isDeleted;
	private Long createDate;
	
	public Long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	private OrderAddress address;
	
	public OrderAddress getAddress() {
		return address;
	}
	public void setAddress(OrderAddress address) {
		this.address = address;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderPieceId() {
		return orderPieceId;
	}
	public void setOrderPieceId(String orderPieceId) {
		this.orderPieceId = orderPieceId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getDeliberyNum() {
		return deliberyNum;
	}
	public void setDeliberyNum(Integer deliberyNum) {
		this.deliberyNum = deliberyNum;
	}
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public Byte getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(Byte paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public Long getPayTime() {
		return payTime;
	}
	public void setPayTime(Long payTime) {
		this.payTime = payTime;
	}
	public Long getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Long deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Integer getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}
	public Integer getCommentStatus() {
		return commentStatus;
	}
	public void setCommentStatus(Integer commentStatus) {
		this.commentStatus = commentStatus;
	}
	public Integer getShippingFee() {
		return shippingFee;
	}
	public void setShippingFee(Integer shippingFee) {
		this.shippingFee = shippingFee;
	}
	public Integer getOriginPrice() {
		return originPrice;
	}
	public void setOriginPrice(Integer originPrice) {
		this.originPrice = originPrice;
	}
	public Integer getActualPrice() {
		return actualPrice;
	}
	public void setActualPrice(Integer actualPrice) {
		this.actualPrice = actualPrice;
	}
	public String getDeliveryCompany() {
		return deliveryCompany;
	}
	public void setDeliveryCompany(String deliveryCompany) {
		this.deliveryCompany = deliveryCompany;
	}
	@Override
	public String toString() {
		return "OrderInPieces [id=" + id + ", orderId=" + orderId
				+ ", orderPieceId=" + orderPieceId + ", userId=" + userId
				+ ", goodsId=" + goodsId + ", deliberyNum=" + deliberyNum
				+ ", commentId=" + commentId + ", paymentMethod="
				+ paymentMethod + ", payTime=" + payTime + ", deliveryDate="
				+ deliveryDate + ", orderStatus=" + orderStatus
				+ ", refundStatus=" + refundStatus + ", commentStatus="
				+ commentStatus + ", shippingFee=" + shippingFee
				+ ", originPrice=" + originPrice + ", actualPrice="
				+ actualPrice + ", deliveryCompany=" + deliveryCompany + "]";
	}
	
}
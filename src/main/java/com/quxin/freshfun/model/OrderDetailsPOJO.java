package com.quxin.freshfun.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrderDetailsPOJO {
    private Integer id;
    private Long userId;
    private Long orderId;
    private String orderDetailsId;
    private Integer goodsId;
    private Integer actualPrice;
    private String deliveryNum;
    private Integer addressId;
    private Integer commentId;
    private Integer paymentMethod;
    private Long payTime;
    private Integer count;
    private Integer orderStatus;
    private Integer refundStatus;
    private Integer commentStatus;
    private Integer payPlateform;
    private Integer isDeleted;
    private Long createDate;
    private Long updateDate;
    private Integer payStatus;
    private String name;
    private String tel;
    private String city;
    private String address;
//    private GoodsPOJO goods;
    private Object goods;
    private String deliveryName;
    private String actualMoney;
    /**
     * 是否是限时商品
     */
    private Integer isLimit;
	public Integer getIsLimit() {
		return isLimit;
	}

	public void setIsLimit(Integer isLimit) {
		this.isLimit = isLimit;
	}
    
    public String getActualMoney() {
		return actualMoney;
	}

	public void setActualMoney(String actualMoney) {
		this.actualMoney = actualMoney;
	}


	/**
     * 是否可以申请退款
     */
    private Integer isRefund;
    /**
     * 确认收货时间
     */
    private Long reciveTime;
    
    public Long getReciveTime() {
		return reciveTime;
	}

	public void setReciveTime(Long reciveTime) {
		this.reciveTime = reciveTime;
	}

	public Integer getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(Integer isRefund) {
		this.isRefund = isRefund;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getGoods() {
		return goods;
	}

	public void setGoods(Object goods) {
		this.goods = goods;
	}

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
    public String getOrderDetailsId() {
        return orderDetailsId;
    }
    public void setOrderDetailsId(String orderDetailsId) {
        this.orderDetailsId = orderDetailsId == null ? null : orderDetailsId.trim();
    }
    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getActualPrice() {
        return actualPrice;
    }
    public void setActualPrice(Integer actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getDeliveryNum() {
        return deliveryNum;
    }

    public void setDeliveryNum(String deliveryNum) {
        this.deliveryNum = deliveryNum == null ? null : deliveryNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_details.address_id
     *
     * @return the value of order_details.address_id
     *
     * @mbggenerated
     */
    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }
    
    public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
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

    public Integer getPayPlateform() {
        return payPlateform;
    }

    public void setPayPlateform(Integer payPlateform) {
        this.payPlateform = payPlateform;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	
	/***********************************************显示倒计时时间戳*********************************************************/
	
	/**
	 * 倒计时还有多久
	 */
	private Long outTimeStamp;

	public Long getOutTimeStamp() {
		return outTimeStamp;
	}

	public void setOutTimeStamp(Long outTimeStamp) {
		this.outTimeStamp = outTimeStamp;
	}	
    
}
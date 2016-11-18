package com.quxin.freshfun.model.param;

/**
 * Created by fanyanlin on 2016/11/16.
 */
public class OrderParam {
    /**
     * 订单编号
     */
    private Long orderId;
    /**
     * 商品名
     */
    private String goodsName;
    /**
     * 支付金额
     */
    private String payPrice;
    /**
     * 支付时间
     */
    private String payTime;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 单价
     */
    private String unitPrice;
    /**
     * 成本价
     */
    private String costPrice;
    /**
     * 订单来源
     */
    private String orderSource;
    /**
     * 收货人
     */
    private String consignee;
    /**
     * 电话
     */
    private String tel;
    /**
     * 地址
     */
    private String address;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

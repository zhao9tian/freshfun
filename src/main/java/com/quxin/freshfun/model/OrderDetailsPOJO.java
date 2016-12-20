package com.quxin.freshfun.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.quxin.freshfun.model.param.GoodsParam;

@JsonInclude(Include.NON_NULL)
public class OrderDetailsPOJO {
    private Long id;
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
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 商户编号
     */
    private Long agentId;
    /**
     * 商户单笔订单提成
     */
    private Integer agentPrice;
    /**
     * 捕手编号
     */
    private Long fetcherId;
    /**
     * 捕手单笔订单提成
     */
    private Integer fetcherPrice;
    /**
     * 流量来源
     */
    private Integer payPlateform;
    private Integer isDeleted;
    private Long createDate;
    private Long updateDate;
    /**
     * 评论状态
     */
    private Integer commentStatus;
    /**
     * 发货时间
     */
    private Long deliveryTime;
    /**
     * 商品快照价格
     */
    private Integer payPrice;
    /**
     * 商品成本价
     */
    private Integer goodsCost;
    private String name;
    private String tel;
    private String city;
    private String address;
    /**
     * 省code
     */
    private Integer provCode;
    /**
     * 市code
     */
    private Integer cityCode;
    /**
     * 区code
     */
    private Integer distCode;
    /**
     * 备注
     */
    private String remark;
    /**
     * 发货备注
     */
    private String deliveryRemark;
    /**
     * 粉丝来源平台标识
     */
    private Long fansAppId;
    /**
     * 平台标识
     */
    private Long appId;
    /**
     * 二维码支付url
     */
    private String payUrl;

    //    private GoodsPOJO goods;
    private GoodsParam goods;
    private String deliveryName;
    /**
     * 订单来源
     */
    private String orderSource;
    /**
     * 用于前端价格显示
     */
    private String actualMoney;
    private String payMoney;
    /**
     * 是否是限时商品
     */
    private Integer isLimit;
    /**
     * 订单用户昵称
     */
    private String nickName;

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getFansAppId() {
        return fansAppId;
    }

    public void setFansAppId(Long fansAppId) {
        this.fansAppId = fansAppId;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public Long getAppId() {
        return appId;
    }
    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public Integer getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Integer payPrice) {
        this.payPrice = payPrice;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

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

    public Integer getProvCode() {
        return provCode;
    }

    public void setProvCode(Integer provCode) {
        this.provCode = provCode;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getDistCode() {
        return distCode;
    }

    public void setDistCode(Integer distCode) {
        this.distCode = distCode;
    }

    public Integer getGoodsCost() {
        return goodsCost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getAgentPrice() {
        return agentPrice;
    }

    public void setAgentPrice(Integer agentPrice) {
        this.agentPrice = agentPrice;
    }

    public Integer getFetcherPrice() {
        return fetcherPrice;
    }

    public void setFetcherPrice(Integer fetcherPrice) {
        this.fetcherPrice = fetcherPrice;
    }

    public String getDeliveryRemark() {
        return deliveryRemark;
    }

    public void setDeliveryRemark(String deliveryRemark) {
        this.deliveryRemark = deliveryRemark;
    }

    public void setGoodsCost(Integer goodsCost) {
        this.goodsCost = goodsCost;
    }

    public Long getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public GoodsParam getGoods() {
        return goods;
    }

    public void setGoods(GoodsParam goods) {
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

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getFetcherId() {
        return fetcherId;
    }

    public void setFetcherId(Long fetcherId) {
        this.fetcherId = fetcherId;
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

    public Integer getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(Integer commentStatus) {
        this.commentStatus = commentStatus;
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

    @Override
    public String toString() {
        return "OrderDetailsPOJO{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", orderDetailsId='" + orderDetailsId + '\'' +
                ", goodsId=" + goodsId +
                ", actualPrice=" + actualPrice +
                ", deliveryNum='" + deliveryNum + '\'' +
                ", addressId=" + addressId +
                ", commentId=" + commentId +
                ", paymentMethod=" + paymentMethod +
                ", payTime=" + payTime +
                ", count=" + count +
                ", orderStatus=" + orderStatus +
                ", agentId=" + agentId +
                ", agentPrice=" + agentPrice +
                ", fetcherId=" + fetcherId +
                ", fetcherPrice=" + fetcherPrice +
                ", payPlateform=" + payPlateform +
                ", isDeleted=" + isDeleted +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", commentStatus=" + commentStatus +
                ", deliveryTime=" + deliveryTime +
                ", goodsCost=" + goodsCost +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", remark='" + remark + '\'' +
                ", deliveryRemark='" + deliveryRemark + '\'' +
                ", goods=" + goods +
                ", deliveryName='" + deliveryName + '\'' +
                ", actualMoney='" + actualMoney + '\'' +
                ", isLimit=" + isLimit +
                ", isRefund=" + isRefund +
                ", reciveTime=" + reciveTime +
                ", outTimeStamp=" + outTimeStamp +
                '}';
    }
}
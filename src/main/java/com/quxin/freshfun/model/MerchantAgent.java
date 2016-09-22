package com.quxin.freshfun.model;

public class MerchantAgent {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column merchant_agent.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column merchant_agent.merchant_id
     *
     * @mbggenerated
     */
    private Long merchantId;
    /**
     * 商品编号
     */
    private Integer goodsId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column merchant_agent.price
     *
     * @mbggenerated
     */
    private Integer price;
    /**
     * 支付状态
     */
    private Integer payStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column merchant_agent.create_date
     *
     * @mbggenerated
     */
    private Long createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column merchant_agent.update_date
     *
     * @mbggenerated
     */
    private Long updateDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column merchant_agent.is_delete
     *
     * @mbggenerated
     */
    private Integer isDelete;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column merchant_agent.id
     *
     * @return the value of merchant_agent.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column merchant_agent.id
     *
     * @param id the value for merchant_agent.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column merchant_agent.merchant_id
     *
     * @return the value of merchant_agent.merchant_id
     *
     * @mbggenerated
     */
    public Long getMerchantId() {
        return merchantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column merchant_agent.merchant_id
     *
     * @param merchantId the value for merchant_agent.merchant_id
     *
     * @mbggenerated
     */
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column merchant_agent.price
     *
     * @return the value of merchant_agent.price
     *
     * @mbggenerated
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column merchant_agent.price
     *
     * @param price the value for merchant_agent.price
     *
     * @mbggenerated
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column merchant_agent.create_date
     *
     * @return the value of merchant_agent.create_date
     *
     * @mbggenerated
     */
    public Long getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column merchant_agent.create_date
     *
     * @param createDate the value for merchant_agent.create_date
     *
     * @mbggenerated
     */
    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column merchant_agent.update_date
     *
     * @return the value of merchant_agent.update_date
     *
     * @mbggenerated
     */
    public Long getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column merchant_agent.update_date
     *
     * @param updateDate the value for merchant_agent.update_date
     *
     * @mbggenerated
     */
    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column merchant_agent.is_delete
     *
     * @return the value of merchant_agent.is_delete
     *
     * @mbggenerated
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column merchant_agent.is_delete
     *
     * @param isDelete the value for merchant_agent.is_delete
     *
     * @mbggenerated
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
}
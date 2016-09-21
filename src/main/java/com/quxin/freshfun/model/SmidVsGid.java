package com.quxin.freshfun.model;

public class SmidVsGid {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column smid_vs_gid.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column smid_vs_gid.mall_id
     *
     * @mbggenerated
     */
    private Integer mall_id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column smid_vs_gid.goods_id
     *
     * @mbggenerated
     */
    private Integer goods_id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column smid_vs_gid.reserved_field
     *
     * @mbggenerated
     */
    private String reserved_field;
    
    private GoodsPOJO goods;

	public GoodsPOJO getGoods() {
		return goods;
	}

	public void setGoods(GoodsPOJO goods) {
		this.goods = goods;
	}

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column smid_vs_gid.id
     *
     * @return the value of smid_vs_gid.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column smid_vs_gid.id
     *
     * @param id the value for smid_vs_gid.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column smid_vs_gid.mall_id
     *
     * @return the value of smid_vs_gid.mall_id
     *
     * @mbggenerated
     */
    public Integer getMall_id() {
        return mall_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column smid_vs_gid.mall_id
     *
     * @param mall_id the value for smid_vs_gid.mall_id
     *
     * @mbggenerated
     */
    public void setMall_id(Integer mall_id) {
        this.mall_id = mall_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column smid_vs_gid.goods_id
     *
     * @return the value of smid_vs_gid.goods_id
     *
     * @mbggenerated
     */
    public Integer getGoods_id() {
        return goods_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column smid_vs_gid.goods_id
     *
     * @param goods_id the value for smid_vs_gid.goods_id
     *
     * @mbggenerated
     */
    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column smid_vs_gid.reserved_field
     *
     * @return the value of smid_vs_gid.reserved_field
     *
     * @mbggenerated
     */
    public String getReserved_field() {
        return reserved_field;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column smid_vs_gid.reserved_field
     *
     * @param reserved_field the value for smid_vs_gid.reserved_field
     *
     * @mbggenerated
     */
    public void setReserved_field(String reserved_field) {
        this.reserved_field = reserved_field == null ? null : reserved_field.trim();
    }
}
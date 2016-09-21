package com.quxin.freshfun.dao;


import com.quxin.freshfun.model.GoodsLimit;

public interface GoodsLimitMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_limit
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_limit
     *
     * @mbggenerated
     */
    int insert(GoodsLimit record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_limit
     *
     * @mbggenerated
     */
    int insertSelective(GoodsLimit record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_limit
     *
     * @mbggenerated
     */
    GoodsLimit selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_limit
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(GoodsLimit record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_limit
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(GoodsLimit record);
    
    GoodsLimit findIsLimit(Long now_time);
    
    GoodsLimit findGoingLimit(Long now_time);
    GoodsLimit findById(Integer goodsID);
    
    GoodsLimit selectShoppingInfo(Integer goodsId);
}
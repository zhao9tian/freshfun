package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.GoodsStorePOJO;

public interface GoodsStoreMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_store
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_store
     *
     * @mbggenerated
     */
    int insert(GoodsStorePOJO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_store
     *
     * @mbggenerated
     */
    int insertSelective(GoodsStorePOJO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_store
     *
     * @mbggenerated
     */
    GoodsStorePOJO selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_store
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(GoodsStorePOJO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table goods_store
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(GoodsStorePOJO record);
}
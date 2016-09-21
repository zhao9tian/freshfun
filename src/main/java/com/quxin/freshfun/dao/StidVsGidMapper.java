package com.quxin.freshfun.dao;

import java.util.List;
import java.util.Map;

import com.quxin.freshfun.model.StidVsGid;

public interface StidVsGidMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stid_vs_gid
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stid_vs_gid
     *
     * @mbggenerated
     */
    int insert(StidVsGid record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stid_vs_gid
     *
     * @mbggenerated
     */
    int insertSelective(StidVsGid record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stid_vs_gid
     *
     * @mbggenerated
     */
    StidVsGid selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stid_vs_gid
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(StidVsGid record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stid_vs_gid
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(StidVsGid record);
    
    List<StidVsGid> selectGoodsLimit(Map<String, Integer> themeMap);
}
package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.UserOutcomeDetails;

public interface UserOutcomeDetailsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_outcome_details
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_outcome_details
     *
     * @mbggenerated
     */
    int insert(UserOutcomeDetails record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_outcome_details
     *
     * @mbggenerated
     */
    int insertSelective(UserOutcomeDetails record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_outcome_details
     *
     * @mbggenerated
     */
    UserOutcomeDetails selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_outcome_details
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(UserOutcomeDetails record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_outcome_details
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(UserOutcomeDetails record);
}
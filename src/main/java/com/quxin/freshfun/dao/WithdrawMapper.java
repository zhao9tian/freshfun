package com.quxin.freshfun.dao;

import java.util.List;

import com.quxin.freshfun.model.pojo.InOutDetailsPOJO;
import com.quxin.freshfun.model.pojo.WithdrawPOJO;
import org.apache.ibatis.annotations.Param;
import com.quxin.freshfun.model.Withdraw;

public interface WithdrawMapper {

    int deleteByPrimaryKey(@Param("id") Integer id, @Param("userId") Integer userId);


    int insert(Withdraw record);


    int insertSelective(Withdraw record);


    Withdraw selectByPrimaryKey(@Param("id") Integer id, @Param("userId") Integer userId);


    int updateByPrimaryKeySelective(Withdraw record);


    int updateByPrimaryKey(Withdraw record);
    /**
     * 查询提现记录
     * @param userId
     * @return
     */
    List<Withdraw> selectPresentRecord(Long userId);


    /**
     * 根据用户ID查询B总收益
     * @param userId
     * @return
     */
    Integer selectTotalMoneyB(String userId);

    /**
     * 根据用户ID查询C总收益
     * @param userId
     * @return
     */
    Integer selectTotalMoneyC(String userId);

    /**
     * 根据用户ID查询未入账收益
     * @param userId
     * @return
     */
    Integer selectUnrecordMoneyB(String userId);

    /**
     * 根据用户ID查询未入账收益
     * @param userId
     * @return
     */
    Integer selectUnrecordMoneyC(String userId);

    /**
     * 查询可提现收益
     * @param userId
     * @return
     */
    Integer selectWithdrawCashB(String userId);

    /**
     * 查询可提现收益
     * @param userId
     * @return
     */
    Integer selectWithdrawCashC(String userId);

    /**
     * 查询B提现记录金额总和
     * @param userId
     * @return
     */
    Integer selectWithdrawRecordMoneyB(String userId);
    /**
     * 查询C提现记录金额总和
     * @param userId
     * @return
     */
    Integer selectWithdrawRecordMoneyC(String userId);


    /**
     * 申请提现
     * @param withdrawPOJO
     * @return
     */
    Integer insertWithdraw(WithdrawPOJO withdrawPOJO);

    /**
     * 根据userId查询支出记录
     * @param userId
     * @return
     */
    List<InOutDetailsPOJO> selectWithdrawList(String userId);

    /**
     * 根据userId查询收入记录
     * @param userId
     * @return
     */
    List<InOutDetailsPOJO> selectIncomeRecords(String userId);

    /**
     * 根据userId查询累计入账收益
     * @param userId
     * @return
     */
    List<InOutDetailsPOJO> selectRecordDetails(String userId);

    /**
     * 根据userId查询未入账收益
     * @param userId
     * @return
     */
    List<InOutDetailsPOJO> selectUnrecordDetails(String userId);
}
package com.quxin.freshfun.service.withdraw;

import com.quxin.freshfun.model.pojo.InOutDetailsPOJO;
import com.quxin.freshfun.model.pojo.WithdrawPOJO;

import java.util.List;

/**
 * BC端退款接口service
 * Created by qucheng on 2016/9/30.
 */
public interface WithdrawService {
    /**
     * 根据用户ID查询用户B总收益
     * @param userId
     * @return
     */
    Double queryTotalMoneyB(String userId);
    /**
     * 根据用户ID查询用户C总收益
     * @param userId
     * @return
     */
    Double queryTotalMoneyC(String userId);

    /**
     * 根据用户ID查询用户未入账收益
     * @param userId
     * @return
     */
    Double queryUnrecordMoneyB(String userId);

    /**
     * 根据用户ID查询用户未入账收益
     * @param userId
     * @return
     */
    Double queryUnrecordMoneyC(String userId);

    /**
     * 根据用户ID查询用户可提现金额
     * @param userId
     * @return
     */
    Double queryWithdrawCashB(String userId);
    /**
     * 根据用户ID查询用户可提现金额
     * @param userId
     * @return
     */
    Double queryWithdrawCashC(String userId);

    /**
     * 提交申请
     * @param withdrawPOJO
     * @return
     */
    Integer addWithdraw(WithdrawPOJO withdrawPOJO);

    /**
     * 根据userID查询提现记录
     * @param userId
     * @return
     */
    List<InOutDetailsPOJO> queryWithdrawRecord(String userId);

    /**
     * 根据userId查询收入记录
     * @param userId
     * @return
     */
    List<InOutDetailsPOJO> queryIncomeRecords(String userId);

    /**
     * 根据userId查询累计入账收益明细
     * @param userId
     * @return
     */
    List<InOutDetailsPOJO> queryRecordDetails(String userId);

    /**
     * 根据UserId查询未入账收益明细
     * @param userId
     * @return
     */
    List<InOutDetailsPOJO> queryUnrecordDetails(String userId);

}

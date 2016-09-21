package com.quxin.freshfun.service.bill;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.quxin.freshfun.model.ExtractMoney;
import com.quxin.freshfun.model.RevenueOrExpenses;
import com.quxin.freshfun.model.UserRevenue;
import com.quxin.freshfun.model.Withdraw;

@Service
public interface BillService {
    /**
     * 查询用户账单明细
     * @param userId
     * @return
     */
    Map<String, String> selectUserBillDetailed(Long userId);
    /**
     * 申请提现
     * @param extractMoney
     * @return
     */
    public Integer addExtractMoney(ExtractMoney extractMoney);
    /**
	 * 查询用户收支明细
	 * @param userId
	 * @return
	 */
	public List<RevenueOrExpenses> selectIncomeExpenditrueDetails(Long userId);
	/**
     * 查詢可提現金額詳情 
     * @return
     */
    List<UserRevenue> selectExtractDetails(Long userId);
    /**
     * 查詢累計收入詳情
     * @param userId
     * @return
     */
    List<UserRevenue> selectCumulativeDetails(Long userId);
    /**
     * 根据用户查询收入账单
     * @param userId
     * @return
     */
    List<UserRevenue> selectRreezeMoneyDetails(Long userId);
    /**
     * 提现记录
     * @param userId
     * @return
     */
    List<Withdraw> selectPresentRecord(Long userId);
}

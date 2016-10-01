package com.quxin.freshfun.service.bill;

import java.util.List;
import java.util.Map;

import com.quxin.freshfun.utils.BusinessException;
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
    /**
     * 查询代理商户的总收益
     * @param userId
     * @return
     */
    String selectAgentsIncome(Long userId);

    /**
     * 代理商户的未入账收益
     * @param userId
     * @return
     */
    String selectAgentsRreezeMoney(Long userId);

    /**
     * 查询代理商户可提现金额
     * @param userId
     * @return
     */
    Integer selectAgentExtractMoney(Long userId);

    /**
     * 新增代理商户体现申请
     * @param userId
     * @param wayId
     * @param money
     * @param extractMoney
     * @return
     */
    String addAgentWithdraw(Integer userId ,Integer wayId,Long money ,Long extractMoney);

    /**
     * 自动确认收货
     * @return
     */
    Integer autoConfirmRecording() throws BusinessException;
}

package com.quxin.freshfun.service.impl.bill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.dao.UserOutcomeMapper;
import com.quxin.freshfun.dao.UserRevenueMapper;
import com.quxin.freshfun.dao.WithdrawMapper;
import com.quxin.freshfun.model.ExtractMoney;
import com.quxin.freshfun.model.RevenueOrExpenses;
import com.quxin.freshfun.model.UserOutcome;
import com.quxin.freshfun.model.UserRevenue;
import com.quxin.freshfun.model.Withdraw;
import com.quxin.freshfun.service.bill.BillService;
import com.quxin.freshfun.utils.DateUtils;
import com.quxin.freshfun.utils.MoneyFormat;
@Service
public class BillServiceImpl implements BillService {
	@Autowired
	private UserRevenueMapper revenue;
	@Autowired
	private UserOutcomeMapper outcome;
	@Autowired
	private WithdrawMapper withdrawals;

	@Override
	public Map<String, String> selectUserBillDetailed(Long userId) {
		int extractMoney = revenue.selectExtractMoney(userId);
		int cumulativeMoney = revenue.selectCumulativeMoney(userId);
		int reezeMoney = revenue.selectRreezeMoney(userId);
		String extractMoneyStr = MoneyFormat.priceFormatString(extractMoney);
		String cumulativeMoneyStr = MoneyFormat.priceFormatString(cumulativeMoney);
		String reezeMoneyStr = MoneyFormat.priceFormatString(reezeMoney);
		Map<String, String> moneyMap = new HashMap<>();
		moneyMap.put("extractMoney", extractMoneyStr);
		moneyMap.put("cumulativeMoney", cumulativeMoneyStr);
		moneyMap.put("reezeMoney", reezeMoneyStr);
		return moneyMap;
	}

	@Override
	public Integer addExtractMoney(ExtractMoney extractMoney) {
		Withdraw withdraw = new Withdraw();
		Long uId = Long.parseLong(extractMoney.getUserId().replace("\"", ""));
		//查询可提现金额
		int allowExtractMoney = revenue.selectExtractMoney(uId);
		int submitMoney = (int) (extractMoney.getMoney()*100);
		if(submitMoney > allowExtractMoney){
			return 0;
		}else if(submitMoney <= 0){
			return 0;
		}
		if(null == extractMoney.getPaymentAccount() && "".equals(extractMoney.getPaymentAccount())){
			return 0;
		}
		Long currentDate = DateUtils.getCurrentDate();
		withdraw.setUserId(uId);
		withdraw.setWithdrawPrice(submitMoney);
		withdraw.setState(0);
		withdraw.setCreateDate(currentDate);
		withdraw.setUpdateDate(currentDate);
		withdraw.setWithdrawType(1);
		withdraw.setPaymentAccount(extractMoney.getPaymentAccount());
		int status = withdrawals.insertSelective(withdraw);
		
		if(status > 0){
			UserRevenue ur = new UserRevenue();
			ur.setUser_id(uId);
			ur.setRevenue_name("提现金额");
			ur.setPrice(-submitMoney);
			ur.setCreate_date(currentDate);
			ur.setUpdate_date(currentDate);
			int u = revenue.insertSelective(ur);
			if(u > 0){
				return 1;				
			}
		}
		return 0;
	}
	/**
	 * 查询用户收支明细
	 * @param userId
	 * @return
	 */
	@Override
	public List<RevenueOrExpenses> selectIncomeExpenditrueDetails(Long userId){
		List<RevenueOrExpenses> revenueOrExpenses = new ArrayList<>();
		List<UserRevenue> revenueBill = revenue.selectRevenueBill(userId);
		for (UserRevenue userRevenue : revenueBill) {
			RevenueOrExpenses re = new RevenueOrExpenses();
			re.setOrderId(userRevenue.getOrder_id());
			re.setName(userRevenue.getRevenue_name());
			re.setPrice(MoneyFormat.priceFormatString(userRevenue.getPrice()));
			re.setDate(userRevenue.getCreate_date());
			revenueOrExpenses.add(re);
		}
		List<UserOutcome> outcomeBill = outcome.selectOutcomeBill(userId);
		for (UserOutcome userOutcome : outcomeBill) {
			RevenueOrExpenses re = new RevenueOrExpenses();
			re.setOrderId(userOutcome.getOrder_id());
			re.setName(userOutcome.getOutcome_name());
			re.setPrice(MoneyFormat.priceFormatString(-userOutcome.getOut_price()));
			re.setDate(userOutcome.getCreate_date());
			revenueOrExpenses.add(re);
		}
		Collections.sort(revenueOrExpenses, new SortByDate());
		System.out.println(revenueOrExpenses.toString());
		return revenueOrExpenses;
	}
	
	class SortByDate implements Comparator<RevenueOrExpenses>{

		@Override
		public int compare(RevenueOrExpenses o1, RevenueOrExpenses o2) {
			return o2.getDate().compareTo(o1.getDate());
		}
	}

	@Override
	public List<UserRevenue> selectExtractDetails(Long userId) {
		List<UserRevenue> extractDetails = revenue.selectExtractDetails(userId);
		for (UserRevenue revenue : extractDetails) {
			revenue.setMoney(MoneyFormat.priceFormatString(revenue.getPrice()));
			revenue.setPrice(null);
		}
		return extractDetails;
	}

	@Override
	public List<UserRevenue> selectCumulativeDetails(Long userId) {
		List<UserRevenue> extractDetails = revenue.selectCumulativeDetails(userId);
		for (UserRevenue revenue : extractDetails) {
			revenue.setMoney(MoneyFormat.priceFormatString(revenue.getPrice()));
			revenue.setPrice(null);
		}
		return extractDetails;
	}

	@Override
	public List<UserRevenue> selectRreezeMoneyDetails(Long userId) {
		List<UserRevenue> extractDetails = revenue.selectRreezeMoneyDetails(userId);
		for (UserRevenue revenue : extractDetails) {
			revenue.setMoney(MoneyFormat.priceFormatString(revenue.getPrice()));
			revenue.setPrice(null);
		}
		return extractDetails;
	}

	@Override
	public List<Withdraw> selectPresentRecord(Long userId) {
		List<Withdraw> presentRecord = withdrawals.selectPresentRecord(userId);
		for (Withdraw withdraw : presentRecord) {
			withdraw.setWithdrawMoney(MoneyFormat.priceFormatString(withdraw.getWithdrawPrice()));
			withdraw.setWithdrawPrice(null);
		}
		return presentRecord;
	}
	
}

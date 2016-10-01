package com.quxin.freshfun.service.impl.bill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.quxin.freshfun.dao.*;
import com.quxin.freshfun.model.*;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.utils.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.service.bill.BillService;
import com.quxin.freshfun.utils.DateUtils;
import com.quxin.freshfun.utils.MoneyFormat;
@Service("billService")
public class BillServiceImpl implements BillService {
	@Autowired
	private UserRevenueMapper userRevenueMapper;
	@Autowired
	private UserOutcomeMapper userOutcomeMapper;
	@Autowired
	private WithdrawMapper withdrawMapper;
	@Autowired
	private AgentWithdrawMapper agentWithdrawMapper;
	@Autowired
	private OrderDetailsMapper orderDetailsMapper;
	@Autowired
	private FlowService flowService;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Map<String, String> selectUserBillDetailed(Long userId) {
		int extractMoney = userRevenueMapper.selectExtractMoney(userId);
		int cumulativeMoney = userRevenueMapper.selectCumulativeMoney(userId);
		int reezeMoney = userRevenueMapper.selectRreezeMoney(userId);
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
		int allowExtractMoney = userRevenueMapper.selectExtractMoney(uId);
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
		int status = withdrawMapper.insertSelective(withdraw);
		
		if(status > 0){
			UserRevenue ur = new UserRevenue();
			ur.setUserId(uId);
			ur.setRevenueName("提现金额");
			ur.setPrice(-submitMoney);
			ur.setCreateDate(currentDate);
			ur.setUpdateDate(currentDate);
			int u = userRevenueMapper.insertSelective(ur);
			if(u > 0){
				return 1;				
			}
		}
		return 0;
	}
	/**
	 * 查询用户收支明细
	 * @param userId  用户id
	 * @return  结果集
	 */
	@Override
	public List<RevenueOrExpenses> selectIncomeExpenditrueDetails(Long userId){
		List<RevenueOrExpenses> revenueOrExpenses = new ArrayList<>();
		List<UserRevenue> revenueBill = userRevenueMapper.selectRevenueBill(userId);
		for (UserRevenue userRevenue : revenueBill) {
			RevenueOrExpenses re = new RevenueOrExpenses();
			re.setOrderId(userRevenue.getOrderId());
			re.setName(userRevenue.getRevenueName());
			re.setPrice(MoneyFormat.priceFormatString(userRevenue.getPrice()));
			re.setDate(userRevenue.getCreateDate());
			revenueOrExpenses.add(re);
		}
		List<UserOutcome> outcomeBill = userOutcomeMapper.selectOutcomeBill(userId);
		for (UserOutcome userOutcome : outcomeBill) {
			RevenueOrExpenses re = new RevenueOrExpenses();
			re.setOrderId(userOutcome.getOrderId());
			re.setName(userOutcome.getOutcomeName());
			re.setPrice(MoneyFormat.priceFormatString(-userOutcome.getOutPrice()));
			re.setDate(userOutcome.getCreateDate());
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
		List<UserRevenue> extractDetails = userRevenueMapper.selectExtractDetails(userId);
		for (UserRevenue revenue : extractDetails) {
			revenue.setMoney(MoneyFormat.priceFormatString(revenue.getPrice()));
			revenue.setPrice(null);
		}
		return extractDetails;
	}

	@Override
	public List<UserRevenue> selectCumulativeDetails(Long userId) {
		List<UserRevenue> extractDetails = userRevenueMapper.selectCumulativeDetails(userId);
		for (UserRevenue revenue : extractDetails) {
			revenue.setMoney(MoneyFormat.priceFormatString(revenue.getPrice()));
			revenue.setPrice(null);
		}
		return extractDetails;
	}

	@Override
	public List<UserRevenue> selectRreezeMoneyDetails(Long userId) {
		List<UserRevenue> extractDetails = userRevenueMapper.selectRreezeMoneyDetails(userId);
		for (UserRevenue revenue : extractDetails) {
			revenue.setMoney(MoneyFormat.priceFormatString(revenue.getPrice()));
			revenue.setPrice(null);
		}
		return extractDetails;
	}

	@Override
	public List<Withdraw> selectPresentRecord(Long userId) {
		List<Withdraw> presentRecord = withdrawMapper.selectPresentRecord(userId);
		for (Withdraw withdraw : presentRecord) {
			withdraw.setWithdrawMoney(MoneyFormat.priceFormatString(withdraw.getWithdrawPrice()));
			withdraw.setWithdrawPrice(null);
		}
		return presentRecord;
	}
	/**
	 * 商户总收益
	 * @param userId
	 * @return
	 */
	@Override
	public String selectAgentsIncome(Long userId) {
		Integer agentsIncome = userRevenueMapper.selectAgentsIncome(userId);
		String money = MoneyFormat.priceFormatString(agentsIncome);
		return money;

	}

	/**
	 * 未入账收入
	 * @param userId
	 * @return
	 */
	@Override
	public String selectAgentsRreezeMoney(Long userId) {
		Integer unbilledRevenue = userRevenueMapper.selectAgentsRreezeMoney(userId);
		String money = MoneyFormat.priceFormatString(unbilledRevenue);
		return money;
	}

	/**
	 * 可提现收入
	 * @param userId
	 * @return
	 */
	@Override
	public Integer selectAgentExtractMoney(Long userId) {
		Integer agentExtractMoney = userRevenueMapper.selectAgentExtractMoney(userId);
		return agentExtractMoney;
	}
	@Override
	public String addAgentWithdraw(Integer userId ,Integer wayId,Long money ,Long extractMoney){
		if(money<=extractMoney){
			//提现申请对象赋值
			AgentWithdrawPOJO agentWithdraw = new AgentWithdrawPOJO();
			agentWithdraw.setMerchantProxyId(userId);
			agentWithdraw.setWithdrawId(wayId);
			agentWithdraw.setWithdrawMoney(money);
			agentWithdraw.setWithdrawSchedule(0);
			agentWithdraw.setGetCreate(DateUtils.getCurrentDate());
			agentWithdraw.setGmtModified(DateUtils.getCurrentDate());
			//执行保存方法
			int result = agentWithdrawMapper.insertSelective(agentWithdraw);
			if(result > 0){
				UserRevenue ur = new UserRevenue();
				ur.setUserId(Long.valueOf(userId));
				ur.setRevenueName("提现金额");
				ur.setPrice(Integer.valueOf(money.toString())*(-1));
				ur.setDeliveryType(2);
				ur.setInState(2);
				ur.setCreateDate(DateUtils.getCurrentDate());
				ur.setUpdateDate(DateUtils.getCurrentDate());
				int u = userRevenueMapper.insertSelective(ur);
				if(u > 0){
					return "1";
				}else{
					return "0";
				}
			}
			return "1";
		}
		return "0";
	}

	/**
	 * 7天订单自动走账单流程
	 * @return
	 */
	@Override
	public Integer autoConfirmRecording() throws BusinessException {
		logger.info("订单自动走账单流程");
		List<OrderDetailsPOJO> orderDetails = orderDetailsMapper.selectAwaitPayMoney();
		Integer status = 0;
		for (OrderDetailsPOJO order: orderDetails) {
			status = orderDetailsMapper.updateAlreadyPayMoney(order.getId());
			if(status <= 0){
				logger.error("修改订单状态为已完成失败");
				throw new BusinessException("修改订单状态为已完成状态失败");
			}
		}
		return status;
	}

}

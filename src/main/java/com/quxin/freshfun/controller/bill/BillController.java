//package com.quxin.freshfun.controller.bill;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.google.common.collect.Maps;
//import com.quxin.freshfun.model.ExtractMoney;
//import com.quxin.freshfun.model.RevenueOrExpenses;
//import com.quxin.freshfun.model.UserRevenue;
//import com.quxin.freshfun.model.Withdraw;
//import com.quxin.freshfun.service.bill.BillService;
//
//@Controller
//public class BillController {
//	@Autowired
//	private BillService billService;
//	/**
//	 * 查询用户账单详情
//	 * @param userId
//	 * @return
//	 */
//	@RequestMapping("/selectUserBillDetailed")
//	@ResponseBody
//	public Map<String, String> selectUserBillDetailed(String userId){
//		Long ui = Long.parseLong(userId.replace("\"", ""));
//		Map<String, String> map = billService.selectUserBillDetailed(ui);
//		return map;
//	}
//	/**
//	 * 申请提现
//	 * @param extractMoney
//	 * @return
//	 */
//	@RequestMapping(value="/applyExtractMoney",method={RequestMethod.POST})
//	@ResponseBody
//	public Map<String, Integer> applyExtractMoney(@RequestBody ExtractMoney extractMoney){
//		Integer applystatus = billService.addExtractMoney(extractMoney);
//		Map<String, Integer> map = Maps.newHashMap();
//		map.put("status", applystatus);
//		return map;
//	}
//	/**
//	 * 查询收入支出账单
//	 * @param userId
//	 */
//	@RequestMapping("/selectIncomeExpenditrueDetails")
//	@ResponseBody
//	public List<RevenueOrExpenses> selectIncomeExpenditrueDetails(String userId){
//		Long ui = Long.parseLong(userId.replace("\"", ""));
//		return billService.selectIncomeExpenditrueDetails(ui);
//	}
//	/**
//     * 查詢可提現金額詳情
//     * @return
//     */
//	@RequestMapping("/selectExtractDetails")
//	@ResponseBody
//    public List<UserRevenue> selectExtractDetails(String userId){
//		Long ui = Long.parseLong(userId.replace("\"", ""));
//    	return billService.selectExtractDetails(ui);
//    }
//    /**
//     * 查詢累計收入詳情
//     * @param userId
//     * @return
//     */
//	@RequestMapping("/selectCumulativeDetails")
//	@ResponseBody
//    List<UserRevenue> selectCumulativeDetails(String userId){
//		Long ui = Long.parseLong(userId.replace("\"", ""));
//    	return billService.selectCumulativeDetails(ui);
//    }
//    /**
//     * 根据用户查询收入账单
//     * @param userId
//     * @return
//     */
//	@RequestMapping("/selectRreezeMoneyDetails")
//	@ResponseBody
//    List<UserRevenue> selectRreezeMoneyDetails(String userId){
//		Long ui = Long.parseLong(userId.replace("\"", ""));
//    	return billService.selectRreezeMoneyDetails(ui);
//    }
//	/**
//	 * 查询提现记录
//	 * @param userId
//	 * @return
//	 */
//	@RequestMapping("/selectPresentRecord")
//	@ResponseBody
//	public List<Withdraw> selectPresentRecord(String userId){
//		Long ui = Long.parseLong(userId.replace("\"", ""));
//		return billService.selectPresentRecord(ui);
//	}
//
//}

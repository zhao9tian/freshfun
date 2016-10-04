package com.quxin.freshfun.controller.withdraw;

import com.quxin.freshfun.model.outparam.InOutDetailsOutParam;
import com.quxin.freshfun.model.pojo.InOutDetailsPOJO;
import com.quxin.freshfun.model.pojo.WithdrawPOJO;
import com.quxin.freshfun.model.outparam.WithdrawOutParam;
import com.quxin.freshfun.model.param.WithdrawParam;
import com.quxin.freshfun.service.withdraw.WithdrawService;
import com.quxin.freshfun.utils.DateUtils;
import com.quxin.freshfun.utils.ListSortUtil;
import com.quxin.freshfun.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bc端提现处理
 * Created by qucheng on 2016/9/29.
 */
@Controller
@RequestMapping("/withdrawController")
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;

    /**
     * b端提现数据显示
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getMyMoneyB" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String , Object>  getAllMoneyB(String userId){
        Map<String , Object> resultMap = new HashMap<String , Object>();
        if(userId != null && !"".equals(userId)){
            Double totalMoney = withdrawService.queryTotalMoneyB(userId);
            Double unrecordMoney = withdrawService.queryUnrecordMoneyB(userId);
            Double withdrawCash = withdrawService.queryWithdrawCashB(userId);
            DecimalFormat df = new DecimalFormat("#0.00");
            resultMap.put("totalMoney" , df.format(totalMoney));
            resultMap.put("unrecordMoney" , df.format(unrecordMoney));
            resultMap.put("withdrawCash" , df.format(withdrawCash));
            resultMap = ResultUtil.success(resultMap);
        }else{
            ResultUtil.fail(1004,"用户Id不能为空");
        }
        return resultMap;
    }

    /**
     * c端提现数据显示
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getMyMoneyC" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String , Object>  getAllMoneyC(String userId){
        Map<String , Object> resultMap = new HashMap<String , Object>();
        if(userId != null && !"".equals(userId)){
            Double totalMoney = withdrawService.queryTotalMoneyC(userId);
            Double unrecordMoney = withdrawService.queryUnrecordMoneyC(userId);
            Double withdrawCash = withdrawService.queryWithdrawCashC(userId);
            DecimalFormat df = new DecimalFormat("#0.00");
            resultMap.put("totalMoney" , df.format(totalMoney));
            resultMap.put("unrecordMoney" , df.format(unrecordMoney));
            resultMap.put("withdrawCash" , df.format(withdrawCash));
            resultMap = ResultUtil.success(resultMap);
        }else{
            ResultUtil.fail(1004,"用户Id不能为空");
        }
        return resultMap;
    }

    /**
     * b端申请提现
     * @param extractMoney
     * @return
     */
    @RequestMapping(value="/applyWithdrawB",method={RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> applyWithdrawB(@RequestBody WithdrawParam extractMoney) {
        Map<String, Object> resultmap;
        Double extractmoney = withdrawService.queryWithdrawCashB(extractMoney.getUserId());
        String userId = extractMoney.getUserId();
        String extractMoneyStr = extractMoney.getMoney();

        if(userId == null){
            resultmap = ResultUtil.fail(1004,"userId 为空");
        }else if(extractmoney == null){
            resultmap = ResultUtil.fail(1004,"该用户提现金额为0");
        }else if(extractMoneyStr == null){
            resultmap = ResultUtil.fail(1004,"提现金额必须大于0");
        }else if(Double.parseDouble(extractMoneyStr) > extractmoney){
            resultmap = ResultUtil.fail(1004,"提现金额不能大于可提现金额");
        }else{
            WithdrawPOJO withdrawPOJO = new WithdrawPOJO();
            withdrawPOJO.setCreateDate(System.currentTimeMillis()/1000);
            withdrawPOJO.setUserId(Long.parseLong(extractMoney.getUserId()));
            withdrawPOJO.setWithDrawType(extractMoney.getPayway());
            withdrawPOJO.setPaymentAccount(extractMoney.getAccount());
            withdrawPOJO.setWithDrawPrice((long)(Double.parseDouble(extractMoney.getMoney())*100));
            withdrawPOJO.setWithdrawSource(20);
            Integer record = withdrawService.addWithdraw(withdrawPOJO);
            if(record == null){
                resultmap = ResultUtil.fail(1004,"申请提现失败");
            }else{
                resultmap = ResultUtil.success(1);
            }
        }
        return resultmap;
    }
    /**
     * c端申请提现
     * @param extractMoney
     * @return
     */
    @RequestMapping(value="/applyWithdrawC",method={RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> applyWithdrawC(@RequestBody WithdrawParam extractMoney) {
        Map<String, Object> resultmap;
        Double extractmoney = withdrawService.queryWithdrawCashC(extractMoney.getUserId());
        String userId = extractMoney.getUserId();
        String extractMoneyStr = extractMoney.getMoney();

        if(userId == null){
            resultmap = ResultUtil.fail(1004,"userId 为空");
        }else if(extractmoney == null){
            resultmap = ResultUtil.fail(1004,"该用户提现金额为0");
        }else if(extractMoneyStr == null){
            resultmap = ResultUtil.fail(1004,"提现金额必须大于0");
        }else if(Double.parseDouble(extractMoneyStr) > extractmoney){
            resultmap = ResultUtil.fail(1004,"提现金额不能大于可提现金额");
        }else{
            WithdrawPOJO withdrawPOJO = new WithdrawPOJO();
            withdrawPOJO.setCreateDate(System.currentTimeMillis()/1000);
            withdrawPOJO.setUserId(Long.parseLong(extractMoney.getUserId()));
            withdrawPOJO.setWithDrawType(extractMoney.getPayway());
            withdrawPOJO.setPaymentAccount(extractMoney.getAccount());
            withdrawPOJO.setWithDrawPrice((long)(Double.parseDouble(extractMoney.getMoney())*100));
            withdrawPOJO.setWithdrawSource(10);
            Integer record = withdrawService.addWithdraw(withdrawPOJO);
            if(record == null){
                resultmap = ResultUtil.fail(1004,"申请提现失败");
            }else{
                resultmap = ResultUtil.success(1);
            }
        }
        return resultmap;
    }

    /**
     * 收支明细
     * @return
     */
    @RequestMapping(value = "/inOutDetails" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String , Object> inOutDetails(String userId){
        List<InOutDetailsPOJO> inOutDetailsPOJOs  = withdrawService.queryIncomeRecords(userId);
        List<InOutDetailsPOJO> withdraws = withdrawService.queryWithdrawRecord(userId);
        List<InOutDetailsOutParam> allInOut = new ArrayList<>();

        /** 将两个list整在一起 处理金额/100*/
        for(InOutDetailsPOJO inOutDetailsPOJO : withdraws ){
            InOutDetailsOutParam inOutDetailsOutParam = new InOutDetailsOutParam();
            inOutDetailsOutParam.setGoodsName("提现");//操作
            inOutDetailsOutParam.setTime(inOutDetailsPOJO.getTime());//时间戳
            DecimalFormat df = new DecimalFormat("#0.00");
            inOutDetailsOutParam.setPrice(df.format(((double)inOutDetailsPOJO.getPrice())/100));//金额
            allInOut.add(inOutDetailsOutParam);
        }
        for(InOutDetailsPOJO inOutDetailsPOJO : inOutDetailsPOJOs ){
            InOutDetailsOutParam inOutDetailsOutParam = new InOutDetailsOutParam();
            inOutDetailsOutParam.setGoodsName(inOutDetailsPOJO.getGoodsName());//操作
            inOutDetailsOutParam.setTime(inOutDetailsPOJO.getTime());
            DecimalFormat df = new DecimalFormat("#0.00");
            inOutDetailsOutParam.setPrice(df.format(((double)inOutDetailsPOJO.getPrice())/100));
            allInOut.add(inOutDetailsOutParam);
        }
        //处理页面显示数据
        //list按时间戳排序
        ListSortUtil<InOutDetailsOutParam> sortList = new ListSortUtil<InOutDetailsOutParam>();
        sortList.sort(allInOut , "time" , "desc");

        Map resultMap = ResultUtil.success(allInOut);
        return resultMap;
    }

    /**
     * 查询申请记录
     * @param userId
     * @return
     */
    @RequestMapping(value="/withdrawRecords" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String , Object> withdrawRecords(String userId){
        List<InOutDetailsPOJO> inOutDetailsPOJOs  = withdrawService.queryWithdrawRecord(userId);
        List<WithdrawOutParam> withdrawOutParams = new  ArrayList<>();
        for(InOutDetailsPOJO inOutDetailsPOJO : inOutDetailsPOJOs){
            WithdrawOutParam withdrawOP = new WithdrawOutParam();
            withdrawOP.setWithdrawName("提现");
            DecimalFormat df = new DecimalFormat("#0.00");
            withdrawOP.setPrice(df.format(((double)inOutDetailsPOJO.getPrice())/100));
            try {
                withdrawOP.setTime(DateUtils.longToString(inOutDetailsPOJO.getTime(), "yyyy-MM-dd HH:mm:ss"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            withdrawOutParams.add(withdrawOP);
        }
        return ResultUtil.success(withdrawOutParams);
    }

    /**
     * 查询C端累计入账收益明细
     * @param userId
     * @return
     */
    @RequestMapping(value="/getRecordDetails" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String , Object> getRecordList(String userId){
        List<InOutDetailsPOJO> inOutDetailsPOJOs  = withdrawService.queryRecordDetails(userId);
        List<InOutDetailsOutParam> incomeDetails = new ArrayList<>();
        for(InOutDetailsPOJO inOutDetailsPOJO : inOutDetailsPOJOs ){
            InOutDetailsOutParam inOutDetailsOutParam = new InOutDetailsOutParam();
            inOutDetailsOutParam.setGoodsName(inOutDetailsPOJO.getGoodsName());//操作
            inOutDetailsOutParam.setTime(inOutDetailsPOJO.getTime());//时间戳
            DecimalFormat df = new DecimalFormat("#0.00");
            inOutDetailsOutParam.setPrice(df.format(((double)inOutDetailsPOJO.getPrice())/100));//金额
            incomeDetails.add(inOutDetailsOutParam);
        }
        return ResultUtil.success(incomeDetails);
    }

    /**
     * 查询C端未入账收益明细
     * @param userId
     * @return
     */
    @RequestMapping(value="/getUnrecordDetails" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String , Object> getUnrecordList(String userId){
        List<InOutDetailsPOJO> inOutDetailsPOJOs  = withdrawService.queryUnrecordDetails(userId);
        List<InOutDetailsOutParam> incomeDetails = new ArrayList<>();
        for(InOutDetailsPOJO inOutDetailsPOJO : inOutDetailsPOJOs ){
            InOutDetailsOutParam inOutDetailsOutParam = new InOutDetailsOutParam();
            inOutDetailsOutParam.setGoodsName(inOutDetailsPOJO.getGoodsName());//操作
            inOutDetailsOutParam.setTime(inOutDetailsPOJO.getTime());//时间戳
            DecimalFormat df = new DecimalFormat("#0.00");
            inOutDetailsOutParam.setPrice(df.format(((double)inOutDetailsPOJO.getPrice())/100));//金额
            incomeDetails.add(inOutDetailsOutParam);
        }
        return ResultUtil.success(incomeDetails);
    }



}
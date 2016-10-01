package com.quxin.freshfun.service.impl.withdraw;

import com.quxin.freshfun.dao.WithdrawMapper;
import com.quxin.freshfun.model.pojo.InOutDetailsPOJO;
import com.quxin.freshfun.model.pojo.WithdrawPOJO;
import com.quxin.freshfun.service.withdraw.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提现接口实现类
 * Created by qucheng on 2016/9/30.
 */
@Service("withdrawService")
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Override
    public Double queryTotalMoneyB(String userId) {
        Integer totalMoney = withdrawMapper.selectTotalMoneyB(userId);
        if(totalMoney != null){
            return ((double)totalMoney)/100;
        }
        return 0.00;
    }
    @Override
    public Double queryTotalMoneyC(String userId) {
        Integer totalMoney = withdrawMapper.selectTotalMoneyC(userId);
        if(totalMoney != null){
            return ((double)totalMoney)/100;
        }
        return 0.00;
    }

    @Override
    public Double queryUnrecordMoneyB(String userId) {
        Integer unrecordMoney = withdrawMapper.selectUnrecordMoneyB(userId);
        if(unrecordMoney != null){
            return ((double)unrecordMoney)/100;
        }
        return 0.00;
    }

    @Override
    public Double queryUnrecordMoneyC(String userId) {
        Integer unrecordMoney = withdrawMapper.selectUnrecordMoneyC(userId);
        if(unrecordMoney != null){
            return ((double)unrecordMoney)/100;
        }
        return 0.00;
    }

    @Override
    public Double queryWithdrawCashB(String userId) {
        //查询出可提现金额，减去提现记录中不为驳回的金额
        Integer withdrawCash = withdrawMapper.selectWithdrawCashB(userId);
        //查询提现记录中状态不为驳回的金额
        Integer withdrawRecordMoney = withdrawMapper.selectWithdrawRecordMoneyB(userId);
        if(withdrawCash != null ){
            if(withdrawRecordMoney != null){
                Integer withdrawMoney = withdrawCash - withdrawRecordMoney ;
                if(withdrawMoney > 0){
                    return ((double)withdrawMoney)/100;
                }else{
                    return 0.00;
                }
            }
            return ((double)withdrawCash)/100;
        }
        return 0.00;
    }

    @Override
    public Double queryWithdrawCashC(String userId) {
        //查询出可提现金额，减去提现记录中不为驳回的金额
        Integer withdrawCash = withdrawMapper.selectWithdrawCashC(userId);
        //查询提现记录中状态不为驳回的金额
        Integer withdrawRecordMoney = withdrawMapper.selectWithdrawRecordMoneyC(userId);
        if(withdrawCash != null){
            if(withdrawRecordMoney != null){
                Integer withdrawMoney = withdrawCash - withdrawRecordMoney ;
                if(withdrawMoney > 0){
                    return ((double)withdrawMoney)/100;
                }else{
                    return 0.00;
                }
            }
            return ((double)withdrawCash)/100;
        }
        return 0.00;
    }

    @Override
    public Integer addWithdraw(WithdrawPOJO withdrawPOJO) {
        return withdrawMapper.insertWithdraw(withdrawPOJO);
    }

    @Override
    public List<InOutDetailsPOJO> queryWithdrawRecord(String userId) {
        return withdrawMapper.selectWithdrawList(userId);
    }

    @Override
    public List<InOutDetailsPOJO> queryIncomeRecords(String userId) {
        return withdrawMapper.selectIncomeRecords(userId);
    }

    @Override
    public List<InOutDetailsPOJO> queryRecordDetails(String userId) {
        return withdrawMapper.selectRecordDetails(userId);
    }

    @Override
    public List<InOutDetailsPOJO> queryUnrecordDetails(String userId) {
        return withdrawMapper.selectUnrecordDetails(userId);
    }
}

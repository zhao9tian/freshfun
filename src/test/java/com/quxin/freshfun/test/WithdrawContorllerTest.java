package com.quxin.freshfun.test;

import com.quxin.freshfun.controller.withdraw.WithdrawController;
import com.quxin.freshfun.model.param.WithdrawParam;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by qucheng on 2016/9/28.
 */
public class WithdrawContorllerTest extends TestBase{


    private WithdrawController withdrawController;

    @Before
    public void setUp() throws Exception {
        withdrawController = getContext().getBean("withdrawController", WithdrawController.class);
    }


    @After
    public void tearDown() throws Exception {
        getContext().close();
    }
    @Test
    public void getAllMoneyB() throws Exception {
        System.out.println(withdrawController.getAllMoneyB("556677"));
    }
    @Test
    public void getAllMoneyC() throws Exception {
        System.out.println(withdrawController.getAllMoneyC("556677"));
    }

    @Test
    public void getWithdrawRecord() {
        //提现记录
        //System.out.println(withdrawController.withdrawRecords("556677"));
    }

    @Test
    public void addWithdraw() throws Exception {
        WithdrawParam withdrawParam = new WithdrawParam();
        withdrawParam.setUserId("556677");
        withdrawParam.setMoney("0.6");
        withdrawParam.setPayway("银行卡");
        withdrawParam.setAccount("6286549251531o15315");
        //b端提现
//        System.out.println(withdrawController.applyWithdrawB(withdrawParam));
        //c端提现
        System.out.println(withdrawController.applyWithdrawC(withdrawParam));
    }


    @Test
    public void getAllList(){
        //收支明细--收入是累计入账收益 --支出是提现
        //System.out.println(withdrawController.inOutDetails("556677"));
    }
    @Test
    public void getRecordList(){
        //收入记录--累计入账收益
        //System.out.println(withdrawController.getRecordList("556677"));
    }
    @Test
    public void getUnrecordList(){
        //未入账收益记录
        //System.out.println(withdrawController.getUnrecordList("556677"));
    }

}
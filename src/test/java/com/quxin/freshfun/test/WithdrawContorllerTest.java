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
        System.out.println(withdrawController.withdrawRecords("556677"));
    }

    @Test
    public void addWithdraw() throws Exception {
        WithdrawParam withdrawParam = new WithdrawParam();
        withdrawParam.setUserId("556677");
        withdrawParam.setMoney("0.5");
        withdrawParam.setPayway("银行卡");
        withdrawParam.setAccount("6286549251531515315");
        System.out.println(withdrawController.applyWithdrawB(withdrawParam));
        System.out.println(withdrawController.applyWithdrawC(withdrawParam));
    }


    @Test
    public void getAllList(){
        System.out.println(withdrawController.inOutDetails("556677"));
    }
    @Test
    public void getRecordList(){
        System.out.println(withdrawController.getRecordList("556677"));
    }
    @Test
    public void getUnrecordList(){
        System.out.println(withdrawController.getUnrecordList("556677"));
    }

}
package com.quxin.freshfun.test;

import com.quxin.freshfun.controller.user.UserLoginController;
import com.quxin.freshfun.controller.withdraw.WithdrawController;
import com.quxin.freshfun.model.WxInfo;
import com.quxin.freshfun.model.param.WithdrawParam;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 用户登录测试用例
 * Created by qucheng on 2016/9/28.
 */
public class UserLoginControllerTest extends TestBase{


    private UserLoginController userLoginController;

    @Before
    public void setUp() throws Exception {
        userLoginController = getContext().getBean("userLoginController", UserLoginController.class);
    }


    @After
    public void tearDown() throws Exception {
        getContext().close();
    }
    @Test
    public void getWzlogin() throws Exception {
        WxInfo wxInfo = new WxInfo();
        wxInfo.setCity("长春");
//        wxInfo.setCode("");
//        wxInfo.setOpenid();
//        wxInfo.setUnionid();
//        wxInfo.setProvince();
//        wxInfo.setLanguage();
//        wxInfo.setCountry();
//        wxInfo.setNickname();
//        System.out.println(userLoginController.wzLogin());
    }
    @Test
    public void getVerifyCode() throws Exception {
//        System.out.println(withdrawController.getAllMoneyC("556677"));
    }

    @Test
    public void bindPhone() {
//        System.out.println(withdrawController.withdrawRecords("556677"));
    }


}
package com.quxin.freshfun.test;

import com.quxin.freshfun.controller.user.UserLoginController;
import com.quxin.freshfun.controller.withdraw.WithdrawController;
import com.quxin.freshfun.model.WxInfo;
import com.quxin.freshfun.model.param.WithdrawParam;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.service.impl.user.UserServiceImpl;
import com.quxin.freshfun.service.user.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 用户登录测试用例
 * Created by qucheng on 2016/9/28.
 */
public class UserLoginControllerTest extends TestBase{


    private UserService userService;

    @Before
    public void setUp() throws Exception {
        userService = getContext().getBean("userService", UserService.class);

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
        userService.WXLogin("011MK0H81jU28S1HK8K81Cp4H81MK0H1","12313");
    }

    @Test
    public void bindPhone() {
//        System.out.println(withdrawController.withdrawRecords("556677"));
    }


}
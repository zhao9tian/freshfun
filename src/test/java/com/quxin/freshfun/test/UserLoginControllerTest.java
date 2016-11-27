package com.quxin.freshfun.test;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.common.FreshFunEncoder;
import com.quxin.freshfun.controller.user.UserLoginController;
import com.quxin.freshfun.controller.withdraw.WithdrawController;
import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.model.UsersPOJO;
import com.quxin.freshfun.model.WxInfo;
import com.quxin.freshfun.model.outparam.UserInfoOutParam;
import com.quxin.freshfun.model.param.WithdrawParam;
import com.quxin.freshfun.model.pojo.UserBasePOJO;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.service.impl.user.UserServiceImpl;
import com.quxin.freshfun.service.order.OrderManager;
import com.quxin.freshfun.service.user.UserBaseService;
import com.quxin.freshfun.service.user.UserService;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.CookieUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 用户登录测试用例
 * Created by qucheng on 2016/9/28.
 */
public class UserLoginControllerTest extends TestBase{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private UserService userService;
    private OrderManager orderManager;
    private UserBaseService userBaseService;
    private UserLoginController userLoginController;

    @Before
    public void setUp() throws Exception {
        userService = getContext().getBean("userService", UserService.class);
        orderManager = getContext().getBean("orderManager", OrderManager.class);
        userBaseService = getContext().getBean("userBaseService", UserBaseService.class);
        userLoginController = getContext().getBean("userLoginController", UserLoginController.class);
    }


    @After
    public void tearDown() throws Exception {
        getContext().close();
    }
    @Test
    public void getWzlogin() throws Exception {
        userService.queryFetcherByUserId(557233L);
    }
    @Test
    public void getVerifyCode() throws Exception {
        userService.WzPlatformLogin("031N7y8e1xorHu00fV5e1Iiu8e1N7y8h");
    }

    @Test
    public void bindPhone() {
        /*List<UsersPOJO> list = userService.selectAllUser();
        for(UsersPOJO user : list){
            UserBasePOJO userBase = new UserBasePOJO();
            userBase.setId(user.getId());
            userBase.setUserId(user.getId());
            userBase.setUserName(user.getUserName());
            userBase.setUserHeadImg(user.getUserHeadUrl());
            userBase.setPhoneNumber(user.getMobilePhone()!=null&&!"".equals(user.getMobilePhone())?user.getMobilePhone():"");
            userBase.setUnionId(user.getWxId()!=null&&!"".equals(user.getWxId())?user.getWxId():"");
            userBase.setOpenId(user.getWzId()!=null&&!"".equals(user.getWzId())?user.getWzId():"");
            userBase.setDeviceId(user.getDeviceId()!=null&&!"".equals(user.getDeviceId())?user.getDeviceId():"");
            userBase.setCity("");
            userBase.setProvince("");
            userBase.setCountry("");
            userBase.setSource((byte) 0);
            userBase.setFetcherId(0l);
            userBase.setIdentity((byte) 0);
            userBase.setLoginType((byte) 3);
            userBase.setIsFetcher((byte) 0);
            userBase.setUserNameCount(0);
            userBase.setCreated(user.getGmtCreate());
            userBase.setUpdated(user.getGmtModified());
            userBase.setIsDeleted(0);
            Integer result = userBaseService.addUserBaseInfo(userBase);
        }*/
        try {
            userLoginController.wxLogin("011zRJzx1luwMd0W76wx10kMzx1zRJzD","DE5EA356-B2A5-46E3-AD45-0F50623E56C6");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserIdFromFetcherId() throws BusinessException {
        Long userId = FreshFunEncoder.urlToId("12afdr");
        UserInfoOutParam user = userBaseService.queryUserInfoByUserId(userId);
        System.out.print(user.getUserName());
    }

    @Test
    public void getFetcherIdFromUserId(){
        Long userId = 1544l;
        UserInfoOutParam user = userBaseService.queryUserInfoByUserId(userId);
        String fetcherId = FreshFunEncoder.idToUrl(userId);
        System.out.print(user.getUserName());
        System.out.print(fetcherId);
    }

    @Test
    public void getUserIdFrom64(){
        Long userId = CookieUtil.test("MzcyODkzLTE0NzY5NjE1NzktMjU5MjAwMA");
        UserInfoOutParam user = userBaseService.queryUserInfoByUserId(userId);
        System.out.print(user.getUserName());
    }

}
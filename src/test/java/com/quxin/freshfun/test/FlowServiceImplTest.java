package com.quxin.freshfun.test;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quxin.freshfun.model.WxInfo;
import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.model.param.WxAccessTokenInfo;
import com.quxin.freshfun.model.pojo.FlowPOJO;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.service.goods.HomePageService;
import com.quxin.freshfun.service.impl.user.UserServiceImpl;
import com.quxin.freshfun.service.user.UserService;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.HttpClientUtil;
import com.quxin.freshfun.utils.weixinPayUtils.WxConstantUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowServiceImplTest extends TestBase {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private FlowService flowService;

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        flowService = getContext().getBean("flowService", FlowService.class);
        userService = getContext().getBean("userService", UserService.class);
    }


    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void add() {
        boolean res;
        long now = System.currentTimeMillis() / 1000;
        long agentBalance = 100l;
        long orderId = 13565665;
        FlowParam flowParam = new FlowParam();
        flowParam.setUserId(111l);
        flowParam.setOrderId(13565665l);
        flowParam.setAgentBalance(agentBalance);
        flowParam.setAgentFlow(100l);
        flowParam.setFetcherBalance(200l);
        flowParam.setFetcherFlow(200l);

        flowParam.setCreated(now);
        flowParam.setUpdated(now);
        res = flowService.add(flowParam);
        flowParam.setOrderId(++orderId);

        logger.info(String.valueOf(res));
    }

    @Test
    public void queryFlowListByUserId() {

        List<FlowPOJO> resList = flowService.queryFlowListByUserId(556677l, 11, 10);
        logger.info(JSON.toJSONString(resList));
    }

    @Test
    public void queryFlowByOrderId() {
        FlowPOJO res = flowService.queryFlowByOrderId(13565667l);
        logger.info(JSON.toJSONString(res));
    }

    @Test
    public void getCount() {

        int cnt = flowService.getCount(556677l);
        logger.info(String.valueOf(cnt));

        logger.error("error~~~");

        logger.error("哈哈哈哈");
        logger.error("哈哈哈哈");
    }

    @Test
    public void test() throws BusinessException {

        String code = "001EbLu20Ubbqz1RO3y20chGu20EbLus";

        userService.WzPlatformLogin(code);
//        String userName = "%E4%BC%9A%E5%8F%91%E5%85%89%E7%9A%84m%E8%AE%B0%F0%9F%8D%9F";
//        try {
//            System.out.println(URLDecoder.decode(userName, "utf-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }



}



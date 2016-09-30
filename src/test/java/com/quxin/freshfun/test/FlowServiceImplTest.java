package com.quxin.freshfun.test;


import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.model.parm.FlowParam;
import com.quxin.freshfun.model.pojo.FlowPOJO;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.service.goods.HomePageService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FlowServiceImplTest extends TestBase{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private FlowService flowService;

    @Before
    public void setUp() throws Exception {
        flowService = getContext().getBean("flowService", FlowService.class);
    }

    
    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void add() {
        boolean res;
        long now = System.currentTimeMillis()/1000;
        long agentBalance = 100l;
        long orderId = 13565665;
        FlowParam flowParam = new FlowParam();
        flowParam.setUserId(556677);
        flowParam.setOrderId(13565665l);
        flowParam.setAgentBalance(agentBalance);
        flowParam.setAgentFlow(10l);
        flowParam.setFetcherBalance(200l);
        flowParam.setFetcherFlow(20l);

        flowParam.setCreated(now);
        flowParam.setUpdated(now);
        res = flowService.add(flowParam);
        flowParam.setOrderId(++orderId);

        logger.info(String.valueOf(res));
    }

    @Test
    public void queryFlowListByUserId() {

        List<FlowPOJO> resList = flowService.queryFlowListByUserId(556677l,11,10);
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
    }
}


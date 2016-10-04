package com.quxin.freshfun.test;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.model.pojo.FlowPOJO;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.service.goods.HomePageService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void test() {
        String result_code = "{\"result_code\":\"0\",\"reason\":\"SUCCESS\",\"order\":{\"carrier_code\":\"yuantong\",\"status\":\"8\",\"bill_code\":\"500407865990\"},\"gtex_traces\":[{\"time\":\"2016-09-29 22:47:08\",\"content\":\"快件在【河北省衡水市公司】已发出 苏文胜\"},{\"time\":\"2016-09-29 21:58:20\",\"content\":\"快件在【河北省衡水市公司】已收件 取件人: 由海军\"}]}";

        Map map = JSON.parseObject(result_code,java.util.Map.class);
        JSONArray jsonArray = (JSONArray) map.get("gtex_traces");
        Object[] objArr = jsonArray.toArray();

        for(int i=0;i<objArr.length;i++) {
            Map  ma =  (Map)objArr[0];
            System.out.println(ma.get("content"));
            System.out.println(ma.get("time"));
        }


    }
}


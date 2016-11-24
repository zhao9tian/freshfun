package com.quxin.freshfun.test;


import com.alibaba.fastjson.JSONArray;
import com.quxin.freshfun.controller.logistical.LogisticalController;
import com.quxin.freshfun.service.bill.BillService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class BillTest extends TestBase{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private BillService billService;


    
    @Before
    public void setUp() throws Exception {
        billService = getContext().getBean("billService", BillService.class);
    }

    
    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void test() throws Exception {
        billService.addAutoConfirmRecording();
    }





}


package com.quxin.freshfun.test;


import com.quxin.freshfun.controller.activity.DoubleElevenController;
import com.quxin.freshfun.controller.logistical.LogisticalController;
import com.quxin.freshfun.utils.BusinessException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogisticalTest extends TestBase{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private LogisticalController logisticalController;


    
    @Before
    public void setUp() throws Exception {
        logisticalController = getContext().getBean("logisticalController", LogisticalController.class);
    }

    
    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void test() throws Exception {
        System.out.println(logisticalController.getLogistical(19572867));
    }

    @Test
    public void querySortGoods(){
    }




}


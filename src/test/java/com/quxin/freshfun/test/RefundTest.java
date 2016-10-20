package com.quxin.freshfun.test;


import com.quxin.freshfun.controller.goods.GoodsController;
import com.quxin.freshfun.controller.goods.HomePage;
import com.quxin.freshfun.controller.refund.RefundController;
import com.quxin.freshfun.model.param.RefundParam;
import com.quxin.freshfun.service.goods.GoodsService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RefundTest extends TestBase{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private RefundController refundController;

    @Before
    public void setUp() throws Exception {
        refundController = getContext().getBean("refundController", RefundController.class);
    }

    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void test() {
        RefundParam rp = new RefundParam();
        rp.setOrderId(1234567L);
        rp.setRefundDes("我想退款");
        rp.setRefundMoney("12.32");
        rp.setServerType("tuikuan");
        rp.setRefundReason("原因是啥");
        refundController.insertRefund(rp);
    }





}


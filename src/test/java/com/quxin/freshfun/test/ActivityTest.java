package com.quxin.freshfun.test;


import com.quxin.freshfun.controller.activity.DoubleElevenController;
import com.quxin.freshfun.controller.goods.GoodsController;
import com.quxin.freshfun.controller.goods.HomePage;
import com.quxin.freshfun.service.goods.GoodsBaseService;
import com.quxin.freshfun.service.goods.GoodsService;
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

public class ActivityTest extends TestBase{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private DoubleElevenController doubleElevenController;


    
    @Before
    public void setUp() throws Exception {
        doubleElevenController = getContext().getBean("doubleElevenController", DoubleElevenController.class);
    }

    
    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void test() throws BusinessException {
        Map<String , List<Integer>> map = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        list.add(55);
        list.add(55);
        map.put("goodsIds" , list);
        System.out.println(doubleElevenController.queryActivityGoodsByGoodsId(map));
    }

    @Test
    public void querySortGoods(){
    }




}


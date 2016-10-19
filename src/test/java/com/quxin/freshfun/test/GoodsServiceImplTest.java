package com.quxin.freshfun.test;


import com.quxin.freshfun.controller.goods.GoodsController;
import com.quxin.freshfun.controller.goods.HomePage;
import com.quxin.freshfun.service.goods.GoodsService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GoodsServiceImplTest extends TestBase{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private GoodsService goodsService;

    private HomePage homePage;

    private GoodsController goodsController;

    
    @Before
    public void setUp() throws Exception {
        goodsService = getContext().getBean("goodsService", GoodsService.class);
        homePage = getContext().getBean("homePage", HomePage.class);
        goodsController = getContext().getBean("goodsDetails", GoodsController.class);
    }

    
    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void test() {
//        System.out.println(homePage.ShowHomePage());
//        System.out.println(goodsDetails.queryGoodsInfo(113 , null));
        Map<String , Integer> map = new HashMap<>();
        map.put("mallId", 25);
        map.put("page", 1);
        System.out.println(goodsService.findMallGoods(map));
    }

    @Test
    public void querySortGoods(){
        System.out.println(goodsService.querySortGoods().size());
    }




}


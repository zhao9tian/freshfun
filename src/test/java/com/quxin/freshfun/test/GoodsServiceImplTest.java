package com.quxin.freshfun.test;


import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.service.goods.HomePageService;
import com.quxin.freshfun.service.refund.RefundService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoodsServiceImplTest extends TestBase{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private GoodsService goodsService;

    private HomePageService homePageService;

    
    @Before
    public void setUp() throws Exception {
        goodsService = getContext().getBean("goodsService", GoodsService.class);
        homePageService = getContext().getBean("homePageService", HomePageService.class);
    }

    
    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void test() {

//        GoodsPOJO s = goodsService.findGoodsMysql(55);
//
//        logger.info(s.getGoodsName());
        homePageService.homeGoodsTheme();
    }




}


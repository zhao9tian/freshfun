package com.quxin.freshfun.test;


import com.quxin.freshfun.service.goods.GoodsGroupPurService;
import com.quxin.freshfun.service.goods.GoodsService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoodsServiceImplTest extends TestBase{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private GoodsService goodsService;
    private GoodsGroupPurService groupPurService;

    
    @Before
    public void setUp() throws Exception {
        goodsService = getContext().getBean("goodsService", GoodsService.class);
        groupPurService = getContext().getBean("goodsGroupPurService", GoodsGroupPurService.class);
    }

    
    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void test() {
        System.out.println(groupPurService.queryGoodsGroupPurByGoodsId(99L));
    }

    @Test
    public void querySortGoods(){
        System.out.println(goodsService.querySortGoods());
    }


}


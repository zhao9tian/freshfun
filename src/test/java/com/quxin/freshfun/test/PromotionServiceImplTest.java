package com.quxin.freshfun.test;


import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.model.goods.PromotionGoodsPOJO;
import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.model.pojo.FlowPOJO;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.service.promotion.PromotionService;
import com.quxin.freshfun.service.user.UserService;
import com.quxin.freshfun.utils.BusinessException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PromotionServiceImplTest extends TestBase {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private PromotionService promotionService;

    @Before
    public void setUp() throws Exception {
        promotionService = getContext().getBean("promotionService", PromotionService.class);
    }

    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void add() throws BusinessException {

        PromotionGoodsPOJO promotionGoodsPOJO = new PromotionGoodsPOJO();
        promotionGoodsPOJO.setGoodsId(2071090l);
        promotionGoodsPOJO.setDiscountPrice(7800l);
        promotionGoodsPOJO.setStartTime(1478164068l);
        promotionGoodsPOJO.setEndTime(1478174068l);

        int res = promotionService.addDiscountForGoods(promotionGoodsPOJO);
        System.out.println(res);
    }

    @Test
    public void query() throws BusinessException {


        PromotionGoodsPOJO promotionGoodsPOJO = promotionService.queryDiscountPriceByGoodsId(2071090l);

        System.out.println(JSON.toJSONString(promotionGoodsPOJO));
    }
}



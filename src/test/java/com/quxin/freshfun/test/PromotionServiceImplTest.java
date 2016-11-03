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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    public void add() throws BusinessException{
        PromotionGoodsPOJO promotionGoodsPOJO = new PromotionGoodsPOJO();
        promotionGoodsPOJO.setGoodsId(2071292L);//91  127  2071301  113  2071198  2071269  2071354   2071353  2071292
        promotionGoodsPOJO.setDiscountPrice(2900L);//42   109   99  72   38   95   28.9   165  29
        promotionGoodsPOJO.setStartTime(1478880000L); // 1478188800  1478275200  1478361600  1478448000   1478534400  1478620800  1478707200  1478793600  1478880000
        promotionGoodsPOJO.setEndTime(1478966399L);//1478275199 1478361599  1478447999 1478534399  1478620799  1478707199 1478793599 1478879999 1478966399

        int res = promotionService.addDiscountForGoods(promotionGoodsPOJO);
        System.out.println(res);
    }

    @Test
    public void query() throws BusinessException {


        PromotionGoodsPOJO promotionGoodsPOJO = promotionService.queryDiscountPriceByGoodsId(2071090l);

        System.out.println(JSON.toJSONString(promotionGoodsPOJO));
    }

    public static void main(String[] args) throws ParseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = df.parse("2016-11-04 00:00:00");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        System.out.println(calendar.getTimeInMillis()/1000);
    }
}



package com.quxin.freshfun.test;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.model.pojo.CommentPOJO;
import com.quxin.freshfun.model.pojo.FlowPOJO;
import com.quxin.freshfun.service.comment.CommentService;
import com.quxin.freshfun.service.flow.FlowService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class CommentServiceImplTest extends TestBase{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private CommentService commentService;

    @Before
    public void setUp() throws Exception {
        commentService = getContext().getBean("commentService", CommentService.class);
    }


    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void add() {
        long now = System.currentTimeMillis()/1000;

        CommentPOJO commentPOJO = new CommentPOJO();

        commentPOJO.setOrderId(10088l);
        commentPOJO.setUserId(20088l);
        commentPOJO.setGoodsId(3017);
        commentPOJO.setGeneralLevel(5);
        commentPOJO.setTasteLevel(5);
        commentPOJO.setLogisticsLevel(5);
        commentPOJO.setPackLevel(5);
        commentPOJO.setCreated(now);
        commentPOJO.setUpdated(now);

        Integer res = commentService.addComment(commentPOJO);

        logger.info(String.valueOf(res));
    }

    @Test
    public void query() {

        CommentPOJO commentPOJO = new CommentPOJO();

        commentPOJO = commentService.queryCommentDetailByOrderId(10088l);

        logger.info(JSON.toJSONString(commentPOJO));
    }

}


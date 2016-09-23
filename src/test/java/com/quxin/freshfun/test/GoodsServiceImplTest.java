package com.quxin.freshfun.test;


import com.quxin.freshfun.model.Comment;
import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.WxInfo;
import com.quxin.freshfun.mongodb.impl.MongoCommentImpl;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.service.refund.RefundService;
import com.quxin.freshfun.service.user.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoodsServiceImplTest extends TestBase{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private GoodsService goodsService;

    private UserService userService;

    private MongoCommentImpl mongoCommentImpl;

    @Before
    public void setUp() throws Exception {
        goodsService = getContext().getBean("goodsService", GoodsService.class);
        userService = getContext().getBean("userService", UserService.class);
        mongoCommentImpl = getContext().getBean("mongoCommentImpl", MongoCommentImpl.class);
    }

    
    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void test() {

        GoodsPOJO s = goodsService.findGoodsMysql(55);
        logger.info(s.getGoods_name());
    }

    @Test
    public void testWZlogin() {
        WxInfo wxInfo = new WxInfo();

        wxInfo.setOpenid("wzidzheshiyicijunittest2");
        wxInfo.setUnionid("wxidzheyeshiyicitest2");
        userService.WZLogin(wxInfo);
    }

    @Test
    public void testAddComment() {
        Comment comment = new Comment();
        comment.setCommentLevel("2254");
        comment.setContent("this is a content ");
        comment.setGmtCreate(System.currentTimeMillis()/1000);
        comment.setGmtModified(System.currentTimeMillis()/1000);
        comment.setGoodsId(16);
        comment.setIsDeleted((byte)0);
        comment.setUserId(13518315L);
        comment.setOrderId("wx135183135153");
        mongoCommentImpl.addComment(comment);
    }


}


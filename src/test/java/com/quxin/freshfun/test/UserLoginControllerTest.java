package com.quxin.freshfun.test;

import com.quxin.freshfun.controller.user.UserLoginController;
import com.quxin.freshfun.controller.withdraw.WithdrawController;
import com.quxin.freshfun.model.WxInfo;
import com.quxin.freshfun.model.param.WithdrawParam;
import com.quxin.freshfun.model.pojo.UserBasePOJO;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.service.impl.user.UserServiceImpl;
import com.quxin.freshfun.service.user.UserBaseService;
import com.quxin.freshfun.service.user.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * 用户登录测试用例
 * Created by qucheng on 2016/9/28.
 */
public class UserLoginControllerTest extends TestBase{


    private UserService userService;
    private UserBaseService userBaseService;

    @Before
    public void setUp() throws Exception {
        userService = getContext().getBean("userService", UserService.class);
        userBaseService = getContext().getBean("userBaseService", UserBaseService.class);

    }


    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void validateIsFetcherByUserId(){//校验该用户是不是捕手
        boolean result = userBaseService.checkIsFetcherByUserId(372897l);
        if(result){
            System.out.println("该用户是捕手");
        }else{
            System.out.println("该用户不是捕手");
        }
    }

    @Test
    public void insertUserBaseInfo(){//插入用户
        UserBasePOJO userBase = new UserBasePOJO();
        userBase.setUserName("子铭");
        userBase.setUserHeadImg("test/img.jpg");
        userBase.setUnionId("setUnionId");
        userBase.setOpenId("setOpenId");
        userBase.setCity("杭州");
        userBase.setDeviceId("setDeviceId");
        userBase.setCountry("中国");
        userBase.setSource((byte)0);
        userBase.setFetcherId(0l);
        userBase.setPhoneNumber("15038292019");
        userBase.setProvince("浙江省");
        userBase.setIdentity((byte)0);
        userBase.setLoginType((byte)3);
        userBase.setIsFetcher((byte)0);
        userBase.setUserNameCount(0);
        userBase.setCreated(System.currentTimeMillis()/1000);
        userBase.setUpdated(System.currentTimeMillis()/1000);
        userBase.setIsDeleted(0);
        Integer result = userBaseService.addUserBaseInfo(userBase);
        if(result==1){
            System.out.println("用户新增成功！"+userBase.getId());
        }
    }

    @Test
    public void updateToBeFetcher(){//成为捕手
        Integer result = userBaseService.modifyToBeFetcher("15038292019",372899l);
        if(result==null){
            System.out.println("入参有误");
        }else if(result==1){
            System.out.println("成为捕手成功");
        }else{
            System.out.println("成为捕手失败");
        }
    }


    @Test
    public void updateUserToMesh(){//更新合并用户信息
        Integer result = userBaseService.modifyUserToMesh(372899l,"SADJ_SDFNO55DAS6F","MSASF6-AS8A4","15038292019");
        if(result==null){
            System.out.println("入参有误");
        }else if(result==1){
            System.out.println("信息更新成功");
        }else{
            System.out.println("信息更新失败失败");
        }
    }

    @Test
    public void updateFetcherForUser(){//为用户添加父级捕手
        Integer result = userBaseService.modifyFetcherForUser(372913l,372912l);
        if(result==null){
            System.out.println("入参有误");
        }else if(result==1){
            System.out.println("添加成功");
        }else{
            System.out.println("添加失败");
        }
    }

    @Test
    public void checkCode(){//检查验证码
        Integer result = userBaseService.modifyFetcherForUser(372913l,372912l);
        if(result==null){
            System.out.println("入参有误");
        }else if(result==1){
            System.out.println("添加成功");
        }else{
            System.out.println("添加失败");
        }
    }

}
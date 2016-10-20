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
    public void selectFetcherIdByUserId(){//获取上级捕手userId
        Long fetcherId = userBaseService.queryFetcherIdByUserId(372899l);
        if(fetcherId==null){
            System.out.println("用户不存在或已注销");
        }else if(fetcherId==0){
            System.out.println("用户不存在上级捕手");
        }else{
            System.out.println("用户存在上级捕手，fetcherId="+fetcherId);
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
    public void selectUserIdByPhoneNumber(){//根据手机号查询用户id
        String phoneNumber = "15038092019";
        Long userId = userBaseService.queryUserIdByPhoneNumber(phoneNumber);
        String deviceId = userBaseService.queryDeviceIdByPhoneNumber(phoneNumber);
        if(userId==null){
            System.out.print("未找到手机号为"+phoneNumber+"的用户");
        }else if(0==userId){
            System.out.print("入参错误");
        }else{
            System.out.print("手机号为"+phoneNumber+"的用户userId为："+userId);
        }
        if(deviceId==null){
            System.out.print("入参错误");
        }else{
            System.out.print("手机号为"+phoneNumber+"的用户设备号为："+deviceId);
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
    public void selectPhoneNumberByUserId(){//获取用户手机号
        String phoneNumber = userBaseService.queryPhoneNumberByUserId(372898l);
        if(phoneNumber==null){
            System.out.println("用户不存在或已注销");
        }else if("".equals(phoneNumber)){
            System.out.println("用户未绑定手机号");
        }else{
            System.out.println("用户的手机号为："+phoneNumber);
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
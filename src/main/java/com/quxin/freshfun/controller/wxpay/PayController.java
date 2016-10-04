package com.quxin.freshfun.controller.wxpay;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.model.OrderInfo;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.weixinPayUtils.ConstantUtil;
import com.quxin.freshfun.utils.weixinPayUtils.TenpayUtil;
import com.quxin.freshfun.utils.weixinPayUtils.WXUtil;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gsix on 2016/10/3.
 */
@Controller
@RequestMapping("/")
public class PayController{
    @Autowired
    private OrderService orderService;

    private Logger log = LoggerFactory.getLogger("info_log");


    @RequestMapping(value="/weixinPay",method={RequestMethod.POST})
    @ResponseBody
    public OrderInfo weixinPay(@RequestBody OrderInfo orderInfo,HttpServletRequest request, HttpServletResponse response){

        try {
            orderService.addWeixinAppPay(orderInfo,request,response);
        } catch (BusinessException e) {
            log.error("App支付失败",e);
        } catch (JSONException e) {
            log.error("App支付失败",e);
        } catch (UnsupportedEncodingException e) {
            log.error("App支付失败",e);
        }
        return orderInfo;
    }

    /**
     * 微信登录
     * @param code
     * @return
     */
    @RequestMapping("/weixinLogin")
    public String weixinLogin(String code){


        return null;
    }


}

package com.quxin.freshfun.controller.wxpay;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.model.OrderInfo;
import com.quxin.freshfun.model.outparam.WxPayInfo;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.CookieUtil;
import com.quxin.freshfun.utils.weixinPayUtils.ConstantUtil;
import com.quxin.freshfun.utils.weixinPayUtils.TenpayUtil;
import com.quxin.freshfun.utils.weixinPayUtils.WXUtil;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
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
    public Map<String, Object> weixinPay(@RequestBody OrderInfo orderInfo,HttpServletRequest request, HttpServletResponse response){
        Map<String, Object>  map = new HashMap<String, Object>();
        Map<String, Object>  resultMap = new HashMap<String, Object>();
        try {
            if(orderInfo.getUserId()==null||"".equals(orderInfo.getUserId())){
                orderInfo.setUserId(CookieUtil.getUserIdFromCookie(request));
            }
            WxPayInfo info = orderService.addWeixinAppPay(orderInfo, request, response);
            map.put("code",1001);
            map.put("msg","请求成功");
            resultMap.put("status",map);
            resultMap.put("data",info);
        } catch (BusinessException e) {
            log.error("App支付失败",e);
        } catch (JSONException e) {
            log.error("App支付失败",e);
        } catch (UnsupportedEncodingException e) {
            log.error("App支付失败",e);
        }
        return resultMap;
    }



    /**
     * 订单支付
     * @param orderId
     * @return
     */
    @RequestMapping("/appOrderPay")
    @ResponseBody
    public Map<String, Object> appOrderPay(String orderId,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException, JSONException {
        Map<String, Object>  map = new HashMap<String, Object>();
        Map<String, Object>  resultMap = new HashMap<String, Object>();
        if(orderId == null || "".equals(orderId)){
            errorResultMsg(map, resultMap, "订单编号不能为空");
            return resultMap;
        }
        WxPayInfo payInfo = orderService.appOrderPay(orderId, request, response);
        if(payInfo == null){
            errorResultMsg(map,resultMap,"订单异常");
            return resultMap;
        }else{
            correctResultMsg(map,resultMap,payInfo);
        }
        return resultMap;
    }

    /**
     * 正确信息返回
     * @param map
     * @param resultMap
     * @param payInfo
     */
    private void correctResultMsg(Map<String, Object> map, Map<String, Object> resultMap, WxPayInfo payInfo) {
        map.put("code", 1001);
        map.put("msg", "请求成功");
        resultMap.put("status", map);
        resultMap.put("data", payInfo);
    }

    /**
     * 错误信息返回给前端
     * @param map
     * @param resultMap
     */
    private void errorResultMsg(Map<String, Object> map, Map<String, Object> resultMap, String msg) {
        map.put("code", 1004);
        map.put("msg", msg);
        resultMap.put("status", map);
    }

}

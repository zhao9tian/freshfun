package com.quxin.freshfun.controller.wxpay;

import com.google.common.collect.Maps;
import com.quxin.freshfun.model.OrderInfo;
import com.quxin.freshfun.model.outparam.WxPayInfo;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.CookieUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qingtian on 2016/10/3.
 */
@Controller
@RequestMapping("/")
public class PayController{
    @Autowired
    private OrderService orderService;

    private Logger logger = LoggerFactory.getLogger(getClass());


    @RequestMapping(value="/weixinPay",method={RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> weixinPay(@RequestBody OrderInfo orderInfo,HttpServletRequest request, HttpServletResponse response){
        Map<String, Object>  map = new HashMap<>();
        Map<String, Object>  resultMap = new HashMap<>();
        try {
            if(orderInfo.getUserId()==null||"".equals(orderInfo.getUserId())){
                orderInfo.setUserId(CookieUtil.getUserIdFromCookie(request));
            }
            WxPayInfo info = orderService.addWeixinAppPay(orderInfo, request);
            map.put("code",1001);
            map.put("msg","请求成功");
            resultMap.put("status",map);
            resultMap.put("data",info);
        } catch (Exception e) {
            logger.error("App支付失败",e);
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
        Map<String, Object>  map = new HashMap<>();
        Map<String, Object>  resultMap = new HashMap<>();
        if(orderId == null || "".equals(orderId)){
            errorResultMsg(map, resultMap, "订单编号不能为空");
            return resultMap;
        }
        WxPayInfo payInfo = orderService.appOrderPay(orderId, request);
        if(payInfo == null){
            errorResultMsg(map,resultMap,"订单异常");
            return resultMap;
        }else{
            correctResultMsg(map,resultMap,payInfo);
        }
        return resultMap;
    }

    @RequestMapping("/QRCodeCallback")
    public void QRCodeCallback(HttpServletRequest request,HttpServletResponse response){
        String wxPayInfo = null;
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            SAXReader sr = new SAXReader();
            Document document = sr.read(inputStream);
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            Map<String, String> map = Maps.newHashMap();
            for (Element e : elements) {
                map.put(e.getName(), e.getText());
            }
            String productId = map.get("product_id");
            String openId = map.get("openid");
            if(productId != null && openId != null) {
                wxPayInfo = orderService.QRCodePay(request, productId, openId);
                response.setContentType("application/xml");
                response.getWriter().write(wxPayInfo);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            logger.error("二维码支付异常",e);
        } catch (JSONException e) {
            logger.error("二维码支付Json转换异常",e);
        } catch (IOException e) {
            logger.error("二维码支付IO异常",e);
        } catch (DocumentException e) {
            logger.error("读取流异常",e);
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("二维码支付IO异常",e);
                }
            }
        }

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

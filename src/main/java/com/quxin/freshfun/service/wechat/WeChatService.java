package com.quxin.freshfun.service.wechat;

import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.model.outparam.WxPayInfo;
import com.quxin.freshfun.model.outparam.WxShareInfo;
import com.quxin.freshfun.utils.BusinessException;
import org.json.JSONException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by gsix on 2016/10/18.
 */
public interface WeChatService {
    /**
     * 微信分享
     */
    WxShareInfo wxShare(String url);

    /**
     * 微信公众号支付
     */
    WxPayInfo wzPay(HttpServletRequest request,String payId, String payMoney, String openId) throws JSONException;

    /**
     * 二维码扫描支付
     * @return
     */
    String QRCodePay(HttpServletRequest request,String payId, String payMoney, String openId) throws JSONException;

    /**
     * 订单创建成功发送消息
     * @return
     */
    void sendWxOrderMessage(OrderDetailsPOJO order);

}

package com.quxin.freshfun.service.wechat;

import com.quxin.freshfun.model.outparam.WxShareInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    void wzPay(HttpServletRequest request, HttpServletResponse response, String payId, String payMoney,String openId);

}

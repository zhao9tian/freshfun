package com.quxin.freshfun.service.wechat;

import com.quxin.freshfun.model.outparam.WxPayInfo;
import com.quxin.freshfun.model.outparam.WxShareInfo;
import org.json.JSONException;

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
    WxPayInfo wzPay(HttpServletRequest request,String payId, String payMoney, String openId) throws JSONException;

}

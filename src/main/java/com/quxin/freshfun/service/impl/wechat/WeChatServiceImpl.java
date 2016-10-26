package com.quxin.freshfun.service.impl.wechat;

import com.quxin.freshfun.model.outparam.WxPayInfo;
import com.quxin.freshfun.model.outparam.WxShareInfo;
import com.quxin.freshfun.model.param.WxAccessTokenInfo;
import com.quxin.freshfun.model.param.WxTicketInfo;
import com.quxin.freshfun.service.wechat.WeChatService;
import com.quxin.freshfun.utils.weixinPayUtils.*;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by gsix on 2016/10/18.
 */
public class WeChatServiceImpl implements WeChatService {


    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 微信分享
     */
    @Override
    public WxShareInfo wxShare(String url) {
        if(StringUtils.isEmpty(url))
            return null;
        //获取accessToken
        WxAccessTokenInfo accessTokenInfo = WXUtil.getAccessToken(WxConstantUtil.APP_ID, WxConstantUtil.APP_SECRET);
        if(accessTokenInfo != null){
            String token = accessTokenInfo.getAccess_token();
            if(token != null){
                WxTicketInfo ticket = WXUtil.getTicket(token);
                if("ok".equals(ticket.getErrmsg())){
                    // 签名并返回前段数据
                    WxShareInfo wxShareInfo = shareSign(url, ticket.getTicket());
                    return wxShareInfo;
                }else{
                    logger.error("微信分享请求ticket失败");
                }
            }else{
                logger.error("微信分享获取token失败");
            }
        }else{
            logger.error("微信分享获取accessTokenInfo失败");
        }
        return null;
    }

    /**
     * 微信公众号支付
     * @param request
     * @param response
     * @param payId
     * @param payMoney
     * @param openId
     */
    @Override
    public WxPayInfo wzPay(HttpServletRequest request, HttpServletResponse response, String payId, String payMoney, String openId) throws JSONException {
        WxPayInfo info = null;
        //预支付编号请求类
        PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(request, response);
        //签名随机串
        String noncestr = WXUtil.getNonceStr();
        //签名时间戳
        String timestamp = WXUtil.getTimeStamp();
        ////设置获取prepayid支付参数
        prepayReqHandler.setParameter("appid", WxConstantUtil.APP_ID);
        prepayReqHandler.setParameter("body", "悦选美食"); //商品描述
        prepayReqHandler.setParameter("attach", payId);
        prepayReqHandler.setParameter("mch_id", WxConstantUtil.PARTNER); //商户号
        prepayReqHandler.setParameter("nonce_str", noncestr);
        prepayReqHandler.setParameter("notify_url", WxConstantUtil.NOTIFY_URL);
        prepayReqHandler.setParameter("out_trade_no", payId);
        prepayReqHandler.setParameter("total_fee",payMoney); //商品金额,以分为单位
        prepayReqHandler.setParameter("spbill_create_ip",request.getRemoteAddr()); //订单生成的机器IP，指用户浏览器端IP
        prepayReqHandler.setParameter("fee_type", "1"); //币种，1人民币   66
        prepayReqHandler.setParameter("trade_type","JSAPI");
        prepayReqHandler.setParameter("openid",openId);
        //生成获取预支付签名
        String sign = prepayReqHandler.createMD5Sign();
        //增加非参与签名的额外参数
        prepayReqHandler.setParameter("sign", sign);
        String gateUrl = WxConstantUtil.GATEURL;
        prepayReqHandler.setGateUrl(gateUrl);
        //获取prepayId
        String prepayid = prepayReqHandler.sendPrepay();
        if(!StringUtils.isEmpty(prepayid)){
            //生成客户端支付签名
           info = paySign(prepayid,noncestr,timestamp);
        }
        return info;
    }

    /**
     * 生成客户端支付签名
     *
     * @param prepayid
     * @param noncestr
     *@param prepayid  @return
     */
    private WxPayInfo paySign(String prepayid, String noncestr, String timestamp) {
        StringBuilder sb = new StringBuilder();
        sb.append("appId=");
        sb.append(WxConstantUtil.APP_ID);
        sb.append("&nonceStr=");
        sb.append(noncestr);
        sb.append("&package=prepay_id=");
        sb.append(prepayid);
        sb.append("&signType=MD5&timeStamp=");
        sb.append(timestamp);
        sb.append("&key=");
        sb.append(WxConstantUtil.PARTNER_KEY);
        String payStr = MD5Util.MD5Encode(sb.toString(),"UTF-8").toUpperCase();

        WxPayInfo info = new WxPayInfo();
        info.setAppid(WxConstantUtil.APP_ID);
        info.setSign(payStr);
        info.setTimestamp(timestamp);
        info.setNoncestr(noncestr);
        info.setPrepayid(prepayid);
        info.setPackageInfo("MD5");
        return info;
    }

    /**
     * 微信分享签名
     * @param url 分享url
     * @param ticket 分享凭证
     */
    private WxShareInfo shareSign(String url,String ticket) {
        //生成签名随机穿
        String noncestr = WXUtil.getNonceStr();
        //生成签名时间戳
        String timestamp = WXUtil.getTimeStamp();
        StringBuilder sb = new StringBuilder();
        sb.append("jsapi_ticket=");
        sb.append(ticket);
        sb.append("&noncestr=");
        sb.append(noncestr);
        sb.append("&timestamp=");
        sb.append(timestamp);
        sb.append("&url=");
        sb.append(url);
        String sign = Sha1Util.getSha1(sb.toString());

        //生成前端分享数据
        WxShareInfo info = new WxShareInfo();
        info.setAppId(WxConstantUtil.APP_ID);
        info.setNonceStr(noncestr);
        info.setTimestamp(timestamp);
        info.setSignature(sign);
        return info;
    }
}

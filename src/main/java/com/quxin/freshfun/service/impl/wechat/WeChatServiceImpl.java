package com.quxin.freshfun.service.impl.wechat;

import com.google.gson.Gson;
import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.model.outparam.SendWxMessage;
import com.quxin.freshfun.model.outparam.SendWxMessageContent;
import com.quxin.freshfun.model.outparam.WxPayInfo;
import com.quxin.freshfun.model.outparam.WxShareInfo;
import com.quxin.freshfun.model.param.WxAccessTokenInfo;
import com.quxin.freshfun.model.param.WxPushMessageResult;
import com.quxin.freshfun.model.param.WxTicketInfo;
import com.quxin.freshfun.service.user.UserBaseService;
import com.quxin.freshfun.service.wechat.WeChatService;
import com.quxin.freshfun.utils.*;
import com.quxin.freshfun.utils.HttpClientUtil;
import com.quxin.freshfun.utils.weixinPayUtils.*;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qingtian on 2016/10/18.
 * 微信支付，分享
 */
@Service("weChatService")
public class WeChatServiceImpl implements WeChatService {

    @Autowired
    private UserBaseService userBaseService;

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
     * @param request  请求
     * @param payId 支付ID
     * @param payMoney 支付金额
     * @param openId
     */
    @Override
    public WxPayInfo wzPay(HttpServletRequest request, String payId, String payMoney, String openId) throws JSONException {
        WxPayInfo info = null;
        //预支付编号请求类
        PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler();
        //签名随机串
        String noncestr = WXUtil.getNonceStr();
        //签名时间戳
        String timestamp = WXUtil.getTimeStamp();
        //订单号
        StringBuilder outTradeNo = new StringBuilder();
        outTradeNo.append(payId);
        outTradeNo.append(String.valueOf(System.currentTimeMillis()));
        ////设置获取prepayid支付参数
        prepayReqHandler.setParameter("appid", WxConstantUtil.APP_ID);
        prepayReqHandler.setParameter("body", "悦选美食"); //商品描述
        prepayReqHandler.setParameter("attach", payId);
        prepayReqHandler.setParameter("mch_id", WxConstantUtil.PARTNER); //商户号
        prepayReqHandler.setParameter("nonce_str", noncestr);
        prepayReqHandler.setParameter("notify_url", WxConstantUtil.NOTIFY_URL);
        prepayReqHandler.setParameter("out_trade_no", outTradeNo.toString());
        prepayReqHandler.setParameter("total_fee",payMoney); //商品金额,以分为单位
        prepayReqHandler.setParameter("spbill_create_ip",request.getRemoteAddr()); //订单生成的机器IP，指用户浏览器端IP
        prepayReqHandler.setParameter("fee_type", "1"); //币种，1人民币
        prepayReqHandler.setParameter("trade_type","JSAPI");
        prepayReqHandler.setParameter("openid",openId);
        //生成获取预支付签名
        String sign = prepayReqHandler.createMD5Sign(WxConstantUtil.PARTNER_KEY);
        //增加非参与签名的额外参数
        prepayReqHandler.setParameter("sign", sign);
        String gateUrl = WxConstantUtil.GATEURL;
        prepayReqHandler.setGateUrl(gateUrl);
        //获取prepayId
        String prepayid = prepayReqHandler.sendPrepay();
        if(!StringUtils.isEmpty(prepayid)){
            //生成客户端支付签名
           info = paySign(prepayid,noncestr,timestamp,payId);
        }
        return info;
    }

    /**
     * 二维码扫描支付
     * @param request 请求
     * @param payId 订单编号
     * @param payMoney 支付金额
     * @param openId 标识
     * @return
     */
    @Override
    public String QRCodePay(HttpServletRequest request, String payId, String payMoney, String openId) throws JSONException {
        //预支付编号请求类
        PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler();
        ClientRequestHandler clientHandler = new ClientRequestHandler();
        //签名随机串
        String noncestr = WXUtil.getNonceStr();
        //签名时间戳
        String timestamp = WXUtil.getTimeStamp();
        //订单号
        StringBuilder outTradeNo = new StringBuilder();
        outTradeNo.append(payId);
        outTradeNo.append(String.valueOf(System.currentTimeMillis()));
        ////设置获取prepayid支付参数
        prepayReqHandler.setParameter("appid", WxConstantUtil.APP_ID);
        prepayReqHandler.setParameter("body", "悦选美食"); //商品描述
        prepayReqHandler.setParameter("attach", payId);
        prepayReqHandler.setParameter("mch_id", WxConstantUtil.PARTNER); //商户号
        prepayReqHandler.setParameter("nonce_str", noncestr);
        prepayReqHandler.setParameter("notify_url", WxConstantUtil.NOTIFY_URL);
        prepayReqHandler.setParameter("out_trade_no", outTradeNo.toString());
        prepayReqHandler.setParameter("total_fee",payMoney); //商品金额,以分为单位
        prepayReqHandler.setParameter("spbill_create_ip",WxConstantUtil.CREATE_IP); //订单生成的机器IP，指用户浏览器端IP
        prepayReqHandler.setParameter("fee_type", "1"); //币种，1人民币
        prepayReqHandler.setParameter("trade_type","NATIVE");
        prepayReqHandler.setParameter("openid",openId);
        //生成获取预支付签名
        String sign = prepayReqHandler.createMD5Sign(WxConstantUtil.PARTNER_KEY);
        //增加非参与签名的额外参数
        prepayReqHandler.setParameter("sign", sign);
        String gateUrl = WxConstantUtil.GATEURL;
        prepayReqHandler.setGateUrl(gateUrl);
        //获取prepayId
        String prepayid = prepayReqHandler.sendPrepay();
        if(!StringUtils.isEmpty(prepayid)){
            clientHandler.setParameter("return_code","SUCCESS");
            clientHandler.setParameter("appid",WxConstantUtil.APP_ID);
            clientHandler.setParameter("mch_id",WxConstantUtil.PARTNER);
            clientHandler.setParameter("nonce_str",noncestr);
            clientHandler.setParameter("prepay_id",prepayid);
            clientHandler.setParameter("result_code","SUCCESS");
            sign = clientHandler.createMD5Sign(WxConstantUtil.PARTNER_KEY);
            clientHandler.setParameter("sign",sign);
            String jsonBody = clientHandler.getJsonBody();
            return FieldUtil.jsonToXml(jsonBody);
        } else {
            clientHandler.setParameter("return_code","FAIL");
            clientHandler.setParameter("appid",WxConstantUtil.APP_ID);
            clientHandler.setParameter("mch_id",WxConstantUtil.PARTNER);
            clientHandler.setParameter("nonce_str",noncestr);
            clientHandler.setParameter("prepay_id",prepayid);
            clientHandler.setParameter("result_code","FAIL");
            clientHandler.setParameter("err_code_des","网络似乎有点问题，请重试");
            sign = clientHandler.createMD5Sign(WxConstantUtil.PARTNER_KEY);
            clientHandler.setParameter("sign",sign);
            String jsonBody = clientHandler.getJsonBody();
            return FieldUtil.jsonToXml(jsonBody);
        }
    }

    /**
     * 下单成功发送消息
     * @return
     */
    @Override
    public void sendWxOrderMessage(OrderDetailsPOJO order) {
        //获取accessToken
        WxAccessTokenInfo accessTokenInfo = WXUtil.getAccessToken(WxConstantUtil.APP_ID, WxConstantUtil.APP_SECRET);
        sendOrderMessage(order,accessTokenInfo);
    }

    /**
     * 发送订单信息
     * @param order
     * @param accessTokenInfo
     */
    private void sendOrderMessage(OrderDetailsPOJO order, WxAccessTokenInfo accessTokenInfo){
        if (order == null || accessTokenInfo == null)
            logger.error("往微信推订单信息，参数不能为空");
        StringBuilder msgUrl = new StringBuilder();
        msgUrl.append(WxConstantUtil.WX_MESSAGE);
        msgUrl.append(accessTokenInfo.getAccess_token());
        //获取用户openId
        String openId = userBaseService.queryUserInfoByUserId(order.getUserId()).getOpenId();
        SendWxMessage message = new SendWxMessage();
        message.setTouser(openId);
        message.setTemplate_id("gn8txiYywhVOOzqMaLsneOUAtmSoYBaCRQt_AHGbYvk");
        message.setUrl("https://www.freshfun365.com/app/beforePay?orderId="+order.getId());
        message.setData(getOrderContent(order));
        Gson gson = new Gson();
        //发送请求
        String result = HttpClientUtil.jsonToPost(msgUrl.toString(), gson.toJson(message));
        WxPushMessageResult resultMessage = new WxPushMessageResult();
        resultMessage = WXUtil.strToJson(result, resultMessage);
        if(!resultMessage.getErrmsg().equals("ok")){
            logger.error(new StringBuilder().append("用户编号：").append(order.getUserId()).append("推送消息失败").toString());
        }
    }

    /**
     * 获取订单内容
     * @param order
     */
    private Map<String,SendWxMessageContent> getOrderContent(OrderDetailsPOJO order) {
        Map<String,SendWxMessageContent> map = new HashMap<>();
        map.put("first",new SendWxMessageContent("您的订单已完成付款，商家即将为您发货。","#173177"));
        map.put("orderProductPrice",new SendWxMessageContent(MoneyFormat.priceFormatString(order.getActualPrice()),"#173177"));
        map.put("orderProductName",new SendWxMessageContent(order.getGoods().getGoodsName(),"#173177"));
        String address = new StringBuilder().append(order.getCity()).append(order.getAddress()).toString();
        map.put("orderAddress",new SendWxMessageContent(address,"#173177"));
        map.put("orderName",new SendWxMessageContent(order.getId(),"#173177"));
        return map;
    }

    /**
     * 生成客户端支付签名
     *
     * @param prepayid
     * @param noncestr
     *@param prepayid  @return
     */
    private WxPayInfo paySign(String prepayid, String noncestr, String timestamp,String payId) {
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
        //支付Url
        StringBuilder payUrl = new StringBuilder();
        StringBuilder signStr = new StringBuilder();
        StringBuilder encodeStr = new StringBuilder();
        payUrl.append("weixin://wxpay/bizpayurl?");
        signStr.append("appid=");
        signStr.append(WxConstantUtil.APP_ID);
        signStr.append("&mch_id=");
        signStr.append(WxConstantUtil.PARTNER);
        signStr.append("&nonce_str=");
        signStr.append(noncestr);
        signStr.append("&product_id=");
        signStr.append(payId);
        signStr.append("&time_stamp=");
        signStr.append(timestamp);

        encodeStr.append(signStr);
        encodeStr.append("&key=");
        encodeStr.append(WxConstantUtil.PARTNER_KEY);
        //签名
        String paySign = MD5Util.MD5Encode(encodeStr.toString(),"UTF-8").toUpperCase();
        signStr.append("&sign=");
        signStr.append(paySign);
        payUrl.append(signStr.toString());


        WxPayInfo info = new WxPayInfo();
        info.setAppid(WxConstantUtil.APP_ID);
        info.setSign(payStr);
        info.setTimestamp(timestamp);
        info.setNoncestr(noncestr);
        info.setPrepayid(prepayid);
        info.setPackageInfo("MD5");
        info.setPayUrl(payUrl.toString());
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

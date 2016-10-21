package com.quxin.freshfun.service.impl.wechat;

import com.quxin.freshfun.model.outparam.WxShareInfo;
import com.quxin.freshfun.model.param.WxAccessTokenInfo;
import com.quxin.freshfun.model.param.WxTicketInfo;
import com.quxin.freshfun.service.wechat.WeChatService;
import com.quxin.freshfun.utils.weixinPayUtils.Sha1Util;
import com.quxin.freshfun.utils.weixinPayUtils.TenpayUtil;
import com.quxin.freshfun.utils.weixinPayUtils.WXUtil;
import com.quxin.freshfun.utils.weixinPayUtils.WxConstantUtil;
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
    public void wzPay(HttpServletRequest request, HttpServletResponse response, String payId, String payMoney, String openId) {
        //当前时间 yyyyMMddHHmmss
        String currTime = TenpayUtil.getCurrTime();
        //8位日期
        String strTime = currTime.substring(8, currTime.length());
        //四位随机数
        String strRandom = TenpayUtil.buildRandom(4) + "";
        //10位序列号,可以自行调整。
        String strReq = strTime + strRandom;
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

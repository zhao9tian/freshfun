package com.quxin.freshfun.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quxin.freshfun.model.WxInfo;
import com.quxin.freshfun.model.param.WxAccessTokenInfo;

/**
 * Created by ASus on 2016/10/19.
 */
public class WeChatUtil {

    /**
     * 通过code获取微信信息
     * @param code
     */
    public static WxInfo getWeChatInfo(String code, String appId, String appSecert) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=");
        sb.append(appId);
        sb.append("&secret=");
        sb.append(appSecert);
        sb.append("&code=");
        sb.append(code);
        sb.append("&grant_type=authorization_code");
        WxAccessTokenInfo wxToken = new WxAccessTokenInfo();
        wxToken = sendWxRequest(sb,wxToken);

        StringBuilder sbl = new StringBuilder();
        sbl.append("https://api.weixin.qq.com/sns/userinfo?access_token=");
        sbl.append(wxToken.getAccess_token());
        sbl.append("&openid=");
        sbl.append(wxToken.getOpenid());
        WxInfo wxInfo = new WxInfo();
        wxInfo = sendWxRequest(sbl,wxInfo);
        return wxInfo;
    }
    private static <T> T sendWxRequest(StringBuilder sb,T t) {
        Object o = new Object();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String StrJson = gson.toJson(o);
        String str = HttpClientUtil.jsonToPost(sb.toString(), StrJson);
        return strToJson(str,t);
    }

    /**
     * String转对象
     * @param str
     * @return
     */
    public static <T> T strToJson(String str,T t){
        Gson gson = new Gson();
        T wxInfo = (T) gson.fromJson(str,t.getClass());
        return wxInfo;
    }
}

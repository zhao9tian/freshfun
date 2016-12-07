package com.quxin.freshfun.model.param;

/**
 * Created by fanyanlin on 2016/12/7.
 * 微信推送消息返回结果
 */
public class WxPushMessageResult {
    private String errcode;

    private String errmsg;

    private String msgid;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }
}

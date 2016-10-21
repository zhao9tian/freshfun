package com.quxin.freshfun.model.param;

/**
 * Created by gsix on 2016/10/19.
 */
public class WxTicketInfo {
    private String errcode;
    private String errmsg;
    /**
     * 分享凭证
     */
    private String ticket;
    /**
     * 有效时间
     */
    private String expires_in;

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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }
}

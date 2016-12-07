package com.quxin.freshfun.model.outparam;

import java.util.Map;

/**
 * Created by fanyanlin on 2016/12/2.
 */
public class SendWxMessage {
    //openId
    private String touser;
    //模板编号
    private String template_id;

    private String url;

    private Map<String,SendWxMessageContent> data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, SendWxMessageContent> getData() {
        return data;
    }

    public void setData(Map<String, SendWxMessageContent> data) {
        this.data = data;
    }
}

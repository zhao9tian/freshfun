package com.quxin.freshfun.model.outparam;

/**
 * Created by fanyanlin on 2016/12/2.
 */
public class SendWxMessageContent {
    private Object value;

    private String color;

    private static SendWxMessageContent sendWxMessageContent;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public SendWxMessageContent(){}

    public SendWxMessageContent(Object value, String color) {
        this.value = value;
        this.color = color;
    }

    public synchronized static SendWxMessageContent getSendMessageContent(){
        if(sendWxMessageContent == null){
            sendWxMessageContent = new SendWxMessageContent();
        }
        return sendWxMessageContent;
    }
}

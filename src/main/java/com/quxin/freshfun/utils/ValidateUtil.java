package com.quxin.freshfun.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tianmingzhao on 16/9/30.
 * 验证
 */
public class ValidateUtil {

    /** 是否成功 */
    private boolean isSuc;
    /** 错误信息 */
    private String msg;

    /**
     * 校验通过
     * @return
     */
    public static ValidateUtil success() {
        ValidateUtil result = new ValidateUtil();
        result.setSuc(true);
        result.setMsg("校验通过");
        return result;
    }

    /**
     * 校验失败
     * @param msg
     * @return
     */
    public static ValidateUtil fail(String msg) {
        ValidateUtil result = new ValidateUtil();
        result.setSuc(false);
        result.setMsg(msg);
        return result;
    }

    public boolean isSuc() {
        return isSuc;
    }

    public void setSuc(boolean suc) {
        isSuc = suc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 手机号格式验证
     * @param str
     * @return
     */
    public static boolean isChinaPhoneLegal(String str) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}

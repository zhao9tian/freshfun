package com.quxin.freshfun.utils;

import java.lang.reflect.Field;

/**
 * Created by fanyanlin on 2016/11/16.
 * 对象转Html
 */
public class ObjectToHtml {

    public static String getHtmlStr(Object obj,String [] title,int goodsId){
        Field[] fields = obj.getClass().getDeclaredFields();
        StringBuilder content = new StringBuilder();
        content.append("<!DOCTYPE html>");
        content.append("<html>");
        content.append("<head>");
        content.append("</head>");
        content.append("<body>");
        content.append("<table border=\"0\" cellspacing=\"1\" style=\"border:#fff solid 1px;color: #666\"");
        content.append("<tr><th style=\"width:30%;background-color: #8fc500\">订单信息</th><th align=\"center\" style=\"width:70%;background-color: #8fc500\">订单数据</th></tr>");
        for (int i = 0;i < title.length;i++){
            content.append("<tr style=\"background-color: #ddeeba\">");
            content.append(getTitle(title[i]));
            if ("商品名".equals(title[i])){
                content.append(getHtmlContent(obj,fields[i],goodsId,1));
            }else{
                content.append(getHtmlContent(obj,fields[i],goodsId,0));
            }
            content.append("</tr>");
        }
        content.append("</table>");
        content.append("</body>");
        content.append("</html>");
        return content.toString();
    }

    /**
     * 拼接标题
     * @param str
     * @return
     */
    private static String getTitle(String str) {
        StringBuilder titles = new StringBuilder();
        titles.append("<td align=\"center\" style=\"width:30%\">");
        titles.append(str);
        titles.append("</td>");
        return titles.toString();
    }

    /**
     * 拼接内容
     * @param field
     * @return
     */
    private static Object getHtmlContent(Object obj,Field field,int goodsId,int state) {
        StringBuilder content = new StringBuilder();
        content.append("<td align=\"center\" style=\"width:70%\">");
        if(state == 1) {
            content.append("<a href=\"https://www.freshfun365.com/app/goodsInfo?goodsId=");
            content.append(goodsId);
            content.append("\">");
            content.append(ReflectionUtils.invokeGetterMethod(obj, field.getName()));
            content.append("</a>");
        } else {
            content.append(ReflectionUtils.invokeGetterMethod(obj, field.getName()));
        }
        content.append("</td>");
        return content.toString();
    }

}

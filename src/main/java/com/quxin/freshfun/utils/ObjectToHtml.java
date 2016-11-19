package com.quxin.freshfun.utils;

import java.lang.reflect.Field;

/**
 * Created by fanyanlin on 2016/11/16.
 * 对象转Html
 */
public class ObjectToHtml {

    public static String getHtmlStr(Object obj,String [] title){
        Field[] fields = obj.getClass().getDeclaredFields();
        StringBuilder content = new StringBuilder();
        content.append("<!DOCTYPE html>");
        content.append("<html>");
        content.append("<head>");
        content.append("</head>");
        content.append("<body>");
        content.append("<table border=\"1\" cellspacing=\"0\" style=\"border-radius: 5px\">");
        for (int i = 0;i < title.length;i++){
            content.append("<tr style=\"border: none\">");
            content.append(getTitle(title[i]));
            content.append(getHtmlContent(obj,fields[i]));
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
        titles.append("<td style=\"background-color: #00bfff;width:30%\">");
        titles.append(str);
        titles.append("</td>");
        return titles.toString();
    }

    /**
     * 拼接内容
     * @param field
     * @return
     */
    private static Object getHtmlContent(Object obj,Field field) {
        StringBuilder content = new StringBuilder();
        content.append("<td align=\"center\" style=\"width:70%\">");
        content.append(ReflectionUtils.invokeGetterMethod(obj, field.getName()));
        content.append("</td>");
        return content.toString();
    }

}

package com.quxin.freshfun.common;

/**
 * Created by tianmingzhao on 16/10/12.
 */
public class FreshFunEncoder {

    /**
     * ID 加密
     * @param id
     * @return
     */
    public static String idToUrl(long id) {
        return 1 + Long.toString(id * 3L + 51L, 29);
    }

    /**
     * ID 解密
     * @param url
     * @return
     */
    public static long urlToId(String url) {
        return (Long.parseLong(url.substring(1), 29) - 51L) / 3L;
    }

//    public static void main(String[] args) {
//        Long userId = 1123l;
//        String key = idToUrl(userId);
//        System.out.println(key);
//        System.out.println(urlToId(key));
//    }
}

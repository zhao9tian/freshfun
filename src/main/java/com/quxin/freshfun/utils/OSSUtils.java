package com.quxin.freshfun.utils;

import com.aliyun.oss.OSSClient;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @author qucheng
 */
public class OSSUtils {
    private static OSSClient client = null;

    static {
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        String accessKeyId = "wdhNdpIWAjlQaNkG";
        String accessKeySecret = "4fhRflFow19uPPQjPs4Spjt98GdRWj";
        client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 获得url链接
     *
     * @param remotePath 服务器文件路径
     * @return URL链接
     */
    private static String getUrl(String remotePath) {
        // 设置URL过期时间为10年  3600L* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = client.generatePresignedUrl("freshfunpic", remotePath, expiration);
        if (url != null) {
            StringBuffer urlSb;
            urlSb = new StringBuffer(url.toString());
            return urlSb.substring(urlSb.indexOf("/image"), urlSb.indexOf("?"));
        }
        return null;
    }

    /**
     * 上传图片
     * 会自动生成文件夹
     *
     * @param localFilePath  本地文件字节流
     * @param remoteFilePath 服务器文件路径
     * @return 返回图片访问路径
     */
    private static String uploadPic(InputStream localFilePath, String remoteFilePath) {
        client.putObject("freshfunpic", remoteFilePath, localFilePath);
        return getUrl(remoteFilePath);
    }

    /**
     * 传入微信头像URL输出保存地址
     *
     * @param headImgUrl 微信头像URL
     * @return 微信头像保存在OSS上的地址
     */
    public static String uploadWxHeadImg(String headImgUrl) {
        String wxHeadImg = "";
        String fileName = "/" + UUID.randomUUID() + ".png";
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //生成远程的路径
        String remotePath = "image/" + year + month + day + fileName;
        //将URL转为可读取的文件流
        URL url;
        HttpURLConnection httpUrl = null;
        try {
            url = new URL(headImgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            //获取上传后的地址
            wxHeadImg = OSSUtils.uploadPic(httpUrl.getInputStream(), remotePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert httpUrl != null;
            httpUrl.disconnect();//关闭httpUrl
        }
        return wxHeadImg;
    }


    public static void main(String[] args) {
        String a1 = "http://wx.qlogo.cn/mmopen/FTEzkRiawu8hV5PFzXBEpxUgNbOnHc3Qcae6EQfe7dkpHEo3osRWPb4zBOjD259hjUS2OsbaJXAibTUgDez7wv9iaoXFaJMvxaC/0";
        String a2 = "http://wx.qlogo.cn/mmopen/nibxxlib1VaPfEnTqA3c1WjLAUmOouIIcvH6j1QV6PXoeSqK0z2UrxZ6akEwITiaMKM9lAZwyRp7CMFoHGMicicYfAJ5Jqz5s2P8y/0";
        String a3 = "http://wx.qlogo.cn/mmopen/FTEzkRiawu8iaa18qQUwUbmAEddXK5A7nicuPJtCScTWib9eia2DOxzoDZiaRbk8ibCfgreKpPTd1cicoumAtf69Fic940yZIN1ySnFzD/0";
        String a4 = "http://wx.qlogo.cn/mmopen/FTEzkRiawu8iaa18qQUwUbmHSO6Sjqt8VekzgPfnYyNVBfN1f4W9vPXKmTQ0RD2wI8SrkOsS3VXRgjd2S88EkCLDs7zLaWAC1v/0";
        String a5 = "http://wx.qlogo.cn/mmopen/ajNVdqHZLLAXNjBJH48SPQQbuhuRJMCa0Dibn8mGT6piblrjIrzrVbkianq6RDMHh13ScAMJkGLicIXV3ZZuibuRR9w/0";
        String a6 = "http://wx.qlogo.cn/mmopen/nibxxlib1VaPcGS3GQicD5HWCHJk2dHbeZwA15Qibn7odxDksLiadHgsricWc3PeGtv8UXeOKlcDmBzvzp4XO7U51QAQ/0";
        String a7 = "http://wx.qlogo.cn/mmopen/nibxxlib1VaPfEnTqA3c1WjBG1MP3AZUdHxRVUmGicbbicASia6l2Yvzpic6oAw0odMgbSjPLYolwNtnSoQA0ZttttWjtXKRcN7PPo/0";
        String a8 = "http://wx.qlogo.cn/mmopen/PiajxSqBRaELS4CawrbETfQibzF3OuaULdemT4fEjCA3GsEC1dtlibftDVJFDw5byaAHxucyS0ic1jGOZNibsEj3yJw/0";
        String a9 = "http://wx.qlogo.cn/mmopen/ajNVdqHZLLD5sY7adwWARfYzoaXOYMa61IuSkOibibBOhrF3WapXtonzkQPWpLNTIaTwWibwNfZglgGTHNn8S1KNA/0";
//        System.out.println(uploadWxHeadImg(a1));//556767   /image/20161013/b126450a-66c9-48f3-aaba-a5288712c9da.png
//        System.out.println(uploadWxHeadImg(a2));//556775   /image/20161013/7f848096-c85d-408a-8ef8-7f3ac11c618f.png
//        System.out.println(uploadWxHeadImg(a3));//556778   /image/20161013/0efa67df-cd14-4e8f-aff6-e8b7c9758fd7.png
//        System.out.println(uploadWxHeadImg(a4));//556782   /image/20161013/5ff83c1e-db89-4de0-9795-8e1c6ecbe55f.png
//        System.out.println(uploadWxHeadImg(a5));//556787   /image/20161013/49dc379c-b0d8-426e-ac8c-c7289278867b.png
//        System.out.println(uploadWxHeadImg(a6));//556788   /image/20161013/f40fd240-3231-40a5-a6d1-c2228f47f398.png
//        System.out.println(uploadWxHeadImg(a7));//556794   /image/20161013/b6b2d616-b253-439a-8ebb-273ed5fc42e0.png
//        System.out.println(uploadWxHeadImg(a8));//556825   /image/20161013/8f42e7c2-9ff5-45f6-841d-19fb763da998.png
//        System.out.println(uploadWxHeadImg(a9));//556909   /image/20161013/39d4a356-c660-4dc4-b936-d0084d22a5c3.png
    }
}
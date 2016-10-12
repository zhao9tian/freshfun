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

}
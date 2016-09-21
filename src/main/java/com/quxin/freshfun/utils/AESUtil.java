package com.quxin.freshfun.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.quxin.freshfun.utils.AESUtil;

public class AESUtil {
	public static final String DEAULT_CODING = "utf-8";
	/**
     * 客户端AUTH加密
     * 
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return
     */
    public static String encryptClient(String content, String password) {
        try {           
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            String resultStr = parseByte2HexStr(result);
            return resultStr;
        } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
        } catch (NoSuchPaddingException e) {
                e.printStackTrace();
        } catch (InvalidKeyException e) {
                e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
        } catch (BadPaddingException e) {
                e.printStackTrace();
        }
        return null;
    }
    
    /**客户端AUTH解密
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static String decryptClient(String encryptResultStr, String password) {
        try {
        	byte[] content = parseHexStr2Byte(encryptResultStr);
             KeyGenerator kgen = KeyGenerator.getInstance("AES");
             kgen.init(128, new SecureRandom(password.getBytes()));
             SecretKey secretKey = kgen.generateKey();
             byte[] enCodeFormat = secretKey.getEncoded();
             SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");            
             Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
             cipher.init(Cipher.DECRYPT_MODE, key);
             byte[] result = cipher.doFinal(content);
             String resultStr=new String(result);
             return resultStr;
        } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
        } catch (NoSuchPaddingException e) {
                e.printStackTrace();
        } catch (InvalidKeyException e) {
                e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
        } catch (BadPaddingException e) {
                e.printStackTrace();
        }
        return null;
    }
    
    /**将二进制转换成16进制
     * @param buf byte
     * @return String
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
                String hex = Integer.toHexString(buf[i] & 0xFF);
                if (hex.length() == 1) {
                        hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
    
    /**将16进制转换为二进制
     * @param hexStr String
     * @return byte[]
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
                return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
                result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
	
    /**
	 * PASSWORD加密
	 * @param sSrc 需要加密字段
	 * @param sKey 密钥
	 * @return String
	 * @throws Exception
	 */
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }
    
    /** 
     * Base64解密 
     *  
     * @param pwd 
     * @return 
     * @see [类、类#方法、类#成员] 
     */  
    public static String decodeStr(String pwd)  
    {  
        byte[] debytes = Base64.decodeBase64(new String(pwd).getBytes());
        return new String(debytes);  
    } 
    /**
     * Base64加密
     * 
     * @param pwd
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String encodeStr(String pwd)
    {
        byte[] enbytes = Base64.encodeBase64Chunked(pwd.getBytes());
        return new String(enbytes);
    }
 
    /**
     * PASSWORD解密
     * @param sSrc 需要解密字段
     * @param sKey 密钥
     * @return String 
     * @throws Exception
     */
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
	
    public static String DUBdecrypt(String encrypted, String seed) throws Exception{
		
		byte[] keyb = seed.getBytes(DEAULT_CODING);
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest = md.digest(keyb);
		SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");
		Cipher dcipher = Cipher.getInstance("AES");
		dcipher.init(Cipher.DECRYPT_MODE, skey);
		
		byte[] clearbyte = dcipher.doFinal(Hex.decodeHex(encrypted.toCharArray()));
		return new String(clearbyte);
	}
    
   // public static encryptionUserId()
	
 
    public static void main(String[] args) throws Exception {
    	//password 加密
        /*String cKey = "My DreamIsFlying";
        String cSrc = "www.yoyi.com";
       
        System.out.println(cSrc);
        String enString = AESUtil.Encrypt(cSrc, cKey);
        System.out.println("加密后的字串是：" + enString);
        String DeString = AESUtil.Decrypt(enString, cKey);
        System.out.println("解密后的字串是：" + DeString);
        //client加密
        String content = "test";
        Long ss = 772692428904726528L;
        String password = "My";
        System.out.println("加密前：" + content);
        String encryptResultStr = encryptClient(ss.toString(), password);
        System.out.println("加密后：" + encryptResultStr);
        String decryptResult = decryptClient(encryptResultStr,password);
        System.out.println("解密后：" + new String(decryptResult));*/
    	
    	String code = "773150887903232000|1473624866";
    	String string = new Base64().encodeToString(code.getBytes());
    	System.out.println(string);
    	
    }
}

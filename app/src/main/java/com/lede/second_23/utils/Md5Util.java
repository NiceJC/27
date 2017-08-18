package com.lede.second_23.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author ZhengHppy
 * @date 16/7/19
 */
public class Md5Util {

    public static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        System.out.println(time);
        String s = "abcdefg"+ time;
    }

    public static String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return (new BigInteger(1, md.digest())).toString(16);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    /**
     * 将字符串转成MD5值
     * @param
     * @return
     */
    public static byte[] bytesToMD5(byte[] data) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(data);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

        return hash;
    }

}
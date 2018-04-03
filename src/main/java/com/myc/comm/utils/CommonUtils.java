package com.myc.comm.utils;

import java.util.Random;

/**
 * @Description: 高频方法工具类
 * @author: :MaYong
 * @Date: 2018/3/22 12:29
 */
public class CommonUtils {

    /**
     * 获取指定位数的随机数
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}

package com.renyu.sostar.utils;

/**
 * Created by Administrator on 2017/8/8.
 */

public class Utils {
    /**
     * 计算距离
     * @param num
     * @return
     */
    public static String caculateDistance(int num) {
        if (num<1000) {
            return num+"m";
        }
        else {
            return num/1000+"km";
        }
    }

    /**
     * 去除小数点后一位是0的情况
     * @param number
     * @return
     */
     public static String removeZero(String number) {
        double num=Double.parseDouble(number);
        if (num==(int) num) {
            return ""+(int) num;
        }
        else {
                return ""+num;
            }
    }
}

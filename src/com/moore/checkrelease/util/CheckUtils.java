package com.moore.checkrelease.util;

public class CheckUtils {
    public static boolean isNotEmpty(String value) {
        return value != null && !"".equals(value);
    }

    public static boolean isEmpty(String value) {
        return value == null || "".equals(value);
    }

//    public static String data2json(){
//
//    }
}

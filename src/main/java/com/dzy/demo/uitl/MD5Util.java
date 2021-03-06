package com.dzy.demo.uitl;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt="1a2b3c4d";
    public static String inputPassToFormPass(String inputPass){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String formPassToDBPass(String inputPass,String salt){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String input,String saltDB){
        return formPassToDBPass(inputPassToFormPass(input),saltDB);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDBPass("234567","1a2b3c4d"));
    }

}

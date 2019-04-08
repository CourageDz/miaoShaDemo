package com.dzy.demo.redis;

public class MiaoShaKey extends BasePrefix {
    public static MiaoShaKey GOODS_OVER =new MiaoShaKey(0,"go");
    public static MiaoShaKey GET_MIAOSHA_PATH =new MiaoShaKey(60,"gmp");
    public static MiaoShaKey GET_MIAOSHA_VERIFY_CODE =new MiaoShaKey(60,"vc");


    public MiaoShaKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }
}

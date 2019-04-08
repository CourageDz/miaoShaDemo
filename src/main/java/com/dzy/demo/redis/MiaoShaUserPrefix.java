package com.dzy.demo.redis;

public class MiaoShaUserPrefix extends BasePrefix {
    public static int MIAOSHAUSER_EXPIRE=3600*24*2;
    public static MiaoShaUserPrefix TOKEN =new MiaoShaUserPrefix("tk",MIAOSHAUSER_EXPIRE);
    public static MiaoShaUserPrefix USER_ID =new MiaoShaUserPrefix("id",0);


    public MiaoShaUserPrefix(String prefix,int expire) {
        super(expire,prefix);
    }
}

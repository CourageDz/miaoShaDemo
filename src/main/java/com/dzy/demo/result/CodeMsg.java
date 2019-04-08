package com.dzy.demo.result;

public class CodeMsg {
    private int code;
    private String msg;
    //通用的错误代码
    public static CodeMsg SUCCESS=new CodeMsg(0,"success");
    public static CodeMsg SEVER_ERROR=new CodeMsg(500100,"服务端异常");
    public static CodeMsg BIND_ERROR=new CodeMsg(500101,"参数校验异常:%s");
    public static CodeMsg MIAOSHA_PATH_ERROR=new CodeMsg(500102,"秒杀路径错误");


    //登录模块 5002XX
    public static CodeMsg SESSION_ERROR=new CodeMsg(500210,"Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY=new CodeMsg(500211,"登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY=new CodeMsg(500212,"手机号不能为空");
    public static CodeMsg MOBILE_ERROR =new CodeMsg(500213,"手机号格式错误") ;
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214,"手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215,"密码错误");

    //订单模块 5003XX
    public static CodeMsg GOODS_OVER = new CodeMsg(500301,"抢购商品已售空");
    public static CodeMsg REPEAT_MIAOSHA = new CodeMsg(500302,"不能重复秒杀");
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500303,"订单不存在");
    public static CodeMsg MIAOSHA_FAILED = new CodeMsg(500304,"秒杀失败");





    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public CodeMsg fillArgs(Object ...args){
        int code=this.code;
        String message=String.format(this.msg,args);
        return new CodeMsg(code,message);
    }
}

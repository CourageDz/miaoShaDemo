package com.dzy.demo.redis;

public class GoodsKey extends BasePrefix {
    public static GoodsKey GOODS_LIST=new GoodsKey(60,"gl");
    public static GoodsKey GOODS_DETAIL=new GoodsKey(60,"gd");
    public static GoodsKey GOODS_COUNT=new GoodsKey("gc");



    private GoodsKey(int expiretime,String prefix) {
        super(expiretime,prefix);
    }
    private GoodsKey(String prefix) {
        super(prefix);
    }


}

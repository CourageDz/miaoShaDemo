package com.dzy.demo.redis;

public class OrderKey extends BasePrefix {
    public OrderKey( String prefix) {
        super( prefix);
    }
    public static OrderKey ORDER_KEY_BY_UID_GID=new OrderKey("okug");
}

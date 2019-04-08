package com.dzy.demo.redis;

public abstract class BasePrefix implements KeyPrefix{
    private int expireSeconds;
    private String prefix;

    public BasePrefix(String prefix) {
        this(0, prefix);
    }
    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    //0表示永不过期
    public int expireSeconds() {
        return expireSeconds;
    }
    public String getPrefix() {
        String className=getClass().getSimpleName();
        return className+":"+prefix;
    }

    @Override
    public int getExpireSeconds() {
        return expireSeconds;
    }
}

package com.dzy.demo.redis;

import java.nio.file.Path;
import java.util.Scanner;

public interface KeyPrefix {
    public int expireSeconds();
    public String getPrefix();
    public int getExpireSeconds();

}

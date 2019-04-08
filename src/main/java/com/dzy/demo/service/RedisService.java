package com.dzy.demo.service;

import com.alibaba.fastjson.JSON;
import com.dzy.demo.redis.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
public class RedisService {



@Autowired
JedisPool jedisPool;


    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String str=jedis.get(prefix.getPrefix()+key);
            T t=stringToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    public <T> boolean set(KeyPrefix prefix,String key,T value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String str=beanToString(value);
            if(str==null || str.length()<0){
                return false;
            }
            String realKey=prefix.getPrefix()+key;
            int seconds=prefix.getExpireSeconds();
            if(seconds<=0){
                jedis.set(realKey,str);
            }else {
                jedis.setex(realKey,seconds,str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    public boolean delete(KeyPrefix prefix,String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            Long result= jedis.del(realKey);
            return result>0;
        }finally {
            returnToPool(jedis);
        }
    }
    public Long incr(KeyPrefix prefix,String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            return jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }
    public boolean exists(KeyPrefix prefix,String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }
    public Long decr(KeyPrefix prefix,String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    public static <T> String beanToString(T value) {
        if(value==null)
            return null;
        Class<?> clazz=value.getClass();
        if(clazz==int.class || clazz==Integer.class){
            return ""+value;
        }else if(clazz==String.class){
            return (String) value;
        }else if(clazz==long.class || clazz==Long.class){
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    public static  <T> T stringToBean(String str,Class<T> clazz) {
        if(str==null||str.length()<0||clazz==null){
            return null;
        }
        if(clazz==int.class || clazz==Integer.class){
            return (T)Integer.valueOf(str);
        }else if(clazz==String.class){
            return (T)str;
        }else if(clazz==long.class || clazz==Long.class){
            return (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }


    private void returnToPool(Jedis jedis) {
        if(jedis!=null){
            jedis.close();
        }
    }
}

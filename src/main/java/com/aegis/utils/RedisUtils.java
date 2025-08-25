package com.aegis.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/20 13:08
 * @Description: Redis工具类
 */
@Component
@RequiredArgsConstructor
public final class RedisUtils {

    private final StringRedisTemplate redisTemplate;

    /**
     * 数据缓存至Redis
     */
    public <K, V> void set(K key, V value) {
        redisTemplate.opsForValue().set(String.valueOf(key), JacksonUtils.toJson(value));
    }

    /**
     * 数据缓存至Redis,并设置过期时间
     */
    public <K, V> void set(K key, V value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(String.valueOf(key), JacksonUtils.toJson(value), timeout, unit);
    }

    /**
     * 根据key获取值
     */
    public <K> String get(K key) {
        return redisTemplate.opsForValue().get(String.valueOf(key));
    }

    /**
     * 从Redis中获取缓存数据,转成对象
     */
    public <K, V> V getObject(K key, Class<V> clazz) {
        String value = this.get(key);
        V result = null;
        if (StringUtils.isNotEmpty(value)) {
            result = JacksonUtils.fromJson(value, clazz);
        }
        return result;
    }

    /**
     * 从Redis中获取缓存数据,转成list
     */
    public <K, V> List<V> getList(K key, Class<V> clazz) {
        String value = this.get(key);
        List<V> result = Collections.emptyList();
        if (StringUtils.isNotEmpty(value)) {
            result = JacksonUtils.fromJsonToList(value, clazz);
        }
        return result;
    }

    /**
     * 删除kry
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 序列化key
     */
    public byte[] dump(String key) {
        return redisTemplate.dump(key);
    }

    /**
     * 是否存在key
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据key设置过期时间
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 根据key设置过期时间
     */
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 移除 key 的过期时间，key 将持久保持
     */
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 返回 key 的剩余的过期时间
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 返回 key 的剩余的过期时间
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

}

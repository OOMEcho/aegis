package com.aegis.utils;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        redisTemplate.opsForValue().set(String.valueOf(key), JSONUtil.toJsonStr(value));
    }

    /**
     * 数据缓存至Redis,并设置过期时间
     */
    public <K, V> void set(K key, V value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(String.valueOf(key), JSONUtil.toJsonStr(value), timeout, unit);
    }

    /**
     * 根据key获取值
     */
    public <K> String get(K key) {
        return redisTemplate.opsForValue().get(String.valueOf(key));
    }

    /**
     * 原子递增
     */
    public void increment(String key, long delta) {
        redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 原子递减
     */
    public void decrement(String key, long delta) {
        redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 从Redis中获取缓存数据,转成对象
     */
    public <K, V> V getObject(K key, Class<V> clazz) {
        String value = this.get(key);
        V result = null;
        if (StringUtils.isNotEmpty(value)) {
            result = JSONUtil.toBean(value, clazz);
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
            result = JSONUtil.toList(value, clazz);
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

    /**
     * Hash 设置单个值
     */
    public <HK, HV> void hSet(String key, HK hashKey, HV value) {
        redisTemplate.opsForHash().put(key, String.valueOf(hashKey), JSONUtil.toJsonStr(value));
    }

    /**
     * Hash 批量设置
     */
    public <HK, HV> void hMSet(String key, Map<HK, HV> map) {
        Map<String, String> jsonMap = map.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> String.valueOf(e.getKey()),
                        e -> JSONUtil.toJsonStr(e.getValue())
                ));
        redisTemplate.opsForHash().putAll(key, jsonMap);
    }

    /**
     * Hash 获取单个值
     */
    public <V> V hGet(String key, String hashKey, Class<V> clazz) {
        Object value = redisTemplate.opsForHash().get(key, hashKey);
        return value != null ? JSONUtil.toBean(value.toString(), clazz) : null;
    }

    /**
     * Hash 获取所有值
     */
    public <V> Map<String, V> hGetAll(String key, Class<V> clazz) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        return entries.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> String.valueOf(e.getKey()),
                        e -> JSONUtil.toBean(String.valueOf(e.getValue()), clazz)
                ));
    }

    /**
     * Hash 删除字段
     */
    public void hDel(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 从左边推入元素
     */
    public <V> void lPush(String key, V value) {
        redisTemplate.opsForList().leftPush(key, JSONUtil.toJsonStr(value));
    }

    /**
     * 从右边推入元素
     */
    public <V> void rPush(String key, V value) {
        redisTemplate.opsForList().rightPush(key, JSONUtil.toJsonStr(value));
    }

    /**
     * 从左边弹出元素
     */
    public String lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从右边弹出元素
     */
    public String rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取列表范围内的数据
     */
    public List<String> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 添加 Set 元素
     */
    public <V> void sAdd(String key, V value) {
        redisTemplate.opsForSet().add(key, JSONUtil.toJsonStr(value));
    }

    /**
     * 获取 Set 所有成员
     */
    public Set<String> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 移除 Set 中的元素
     */
    public void sRemove(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * ZSet 添加元素
     */
    public <V> Boolean zAdd(String key, V value, double score) {
        return redisTemplate.opsForZSet().add(key, JSONUtil.toJsonStr(value), score);
    }

    /**
     * ZSet 范围查询（按分数升序）
     */
    public Set<String> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * ZSet 删除元素
     */
    public void zRemove(String key, Object... values) {
        redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 尝试获取分布式锁
     */
    public boolean tryLock(String key, String value, long expire, TimeUnit timeUnit) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, expire, timeUnit);
        return Boolean.TRUE.equals(success);
    }

    /**
     * 释放分布式锁
     */
    public void unlock(String key, String value) {
        String currentValue = redisTemplate.opsForValue().get(key);
        if (value.equals(currentValue)) {
            redisTemplate.delete(key);
        }
    }
}

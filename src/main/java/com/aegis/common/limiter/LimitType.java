package com.aegis.common.limiter;


/**
 * 限流类型
 *
 * @author xuesong.lei
 * @since 2025/09/19 10:29
 */
public enum LimitType {

    /**
     * 默认策略全局限流
     */
    DEFAULT,

    /**
     * 根据请求者IP进行限流
     */
    IP
}

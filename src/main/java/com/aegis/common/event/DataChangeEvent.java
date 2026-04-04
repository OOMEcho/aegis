package com.aegis.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据变更事件
 *
 * @author xuesong.lei
 * @since 2025-09-07
 */
@Getter
@AllArgsConstructor
public class DataChangeEvent {

    public enum Type {
        WHITELIST,  // 白名单变更
        RESOURCE,   // 资源变更
        LOG,        // 系统操作日志
        EMAIL,      // 邮件
    }

    /**
     * 事件类型
     */
    private final Type type;

    /**
     * 事件携带的具体数据
     */
    private final Object payload;

    /**
     * 事件描述信息
     */
    private final String description;
}

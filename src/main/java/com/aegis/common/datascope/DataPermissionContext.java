package com.aegis.common.datascope;

import lombok.Data;

import java.util.Set;

/**
 * 数据权限上下文
 *
 * @author xuesong.lei
 * @since 2025/09/10 14:08
 */
@Data
public class DataPermissionContext {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 数据范围
     */
    private String dataScope;

    /**
     * 自定义部门ID集合
     */
    private Set<Long> deptIds;
}

package com.aegis.common.constant;

/**
 * 数据权限常量类
 *
 * @author xuesong.lei
 * @since 2025-09-10
 */
public class DataScopeConstants {

    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 默认部门字段
     */
    public static final String DEFAULT_DEPT_FIELD = "dept_id";

    /**
     * 默认用户字段
     */
    public static final String DEFAULT_USER_FIELD = "create_by";
}

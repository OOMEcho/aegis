package com.aegis.common.constant;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/21 15:08
 * @Description: 通用常量
 */
public class CommonConstants {

    /**
     * token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * redis中存储的菜单key
     */
    public static final String MENUS = "menus:";

    /**
     * redis中存储的白名单key
     */
    public static final String WHITELIST = "whitelist";

    /**
     * 所有请求方式
     */
    public static final String REQUEST_METHOD_ALL = "all";

    /**
     * 正常/成功
     */
    public static final String NORMAL_STATUS = "0";

    /**
     * 停用/失败
     */
    public static final String DISABLE_STATUS = "1";

    /**
     * 菜单类型-目录
     */
    public static final String MENUS_CATALOG = "D";

    /**
     * 菜单类型-菜单
     */
    public static final String MENUS_MENU = "M";

    /**
     * 菜单类型-按钮
     */
    public static final String MENUS_BUTTON = "B";

    /**
     * 部门顶级id
     */
    public static final String DEPT_ANCESTOR_ID = "0";

    /**
     * 超级管理员角色编码
     */
    public static final String ADMIN_ROLE = "admin";

    /**
     * 无权限标识
     */
    public static final String NONE = "none";
}

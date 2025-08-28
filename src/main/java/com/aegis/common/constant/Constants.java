package com.aegis.common.constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/21 15:08
 * @Description: 通用常量
 */
public class Constants {

    /**
     * token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * redis中存储的菜单key
     */
    public static final String MENUS = "menus";

    /**
     * 正常标识
     */
    public static final String NORMAL_STATUS = "0";

    /**
     * 停用标识
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

    /**
     * 分隔符
     */
    public static final String SEPARATOR = "/";

    /**
     * 点
     */
    public static final String POINT = ".";

    /**
     * 文件存储目录
     * 按照日期进行分类存储，格式为yyyy-MM-dd
     */
    public static final String FILE_FOLDER = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    /**
     * 允许上传的文件类型
     */
    public static final List<String> ALLOWED_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/bmp",
            "application/pdf", "text/plain", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
}

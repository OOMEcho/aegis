package com.aegis.modules.menu.domain.entity;

import com.aegis.modules.role.domain.entity.Role;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025-08-30 10:47:13
 * @Description: 菜单权限表
 * @TableName t_menu
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_menu")
public class Menu implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 逻辑删除标记(0=正常,1=删除)
     */
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 版本号,用于乐观锁
     */
    @Version
    @TableField(value = "version")
    private Integer version;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 菜单名称
     */
    @TableField(value = "menu_name")
    private String menuName;

    /**
     * 父菜单ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 显示顺序
     */
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 请求方法,GET,POST,PUT,DELETE,ALL=不限制
     */
    @TableField(value = "request_method")
    private String requestMethod;

    /**
     * URL匹配模式,支持Ant风格,比如/api/user/**
     */
    @TableField(value = "request_uri")
    private String requestUri;

    /**
     * 路由地址
     */
    @TableField(value = "path")
    private String path;

    /**
     * 组件路径
     */
    @TableField(value = "component")
    private String component;

    /**
     * 是否为外链(0-是,1-否)
     */
    @TableField(value = "is_frame")
    private Integer isFrame;

    /**
     * 是否缓存(0-缓存,1-不缓存)
     */
    @TableField(value = "is_cache")
    private Integer isCache;

    /**
     * 菜单类型(D-目录,M-菜单,B-按钮)
     */
    @TableField(value = "menu_type")
    private String menuType;

    /**
     * 菜单状态(0-显示,1-隐藏)
     */
    @TableField(value = "visible")
    private String visible;

    /**
     * 菜单状态(0-正常,1-停用)
     */
    @TableField(value = "status")
    private String status;

    /**
     * 权限标识
     */
    @TableField(value = "perms")
    private String perms;

    /**
     * 菜单图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 角色列表
     */
    @TableField(exist = false)
    private List<Role> roleList;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();

}

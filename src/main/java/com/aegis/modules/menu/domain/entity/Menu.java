package com.aegis.modules.menu.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单表
 *
 * @author xuesong.lei
 * @since 2025-08-30 10:47:13
 * @TableName t_menu
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "菜单表")
@TableName(value = "t_menu")
public class Menu implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 逻辑删除标记(0=正常,1=删除)
     */
    @Schema(description = "逻辑删除标记(0=正常,1=删除)")
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 版本号,用于乐观锁
     */
    @Schema(description = "版本号,用于乐观锁")
    @Version
    @TableField(value = "version")
    private Integer version;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @TableField(value = "remark")
    private String remark;

    /**
     * 菜单编码
     */
    @Schema(description = "菜单编码")
    @TableField(value = "menu_code")
    private String menuCode;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
    @TableField(value = "menu_name")
    private String menuName;

    /**
     * 父菜单ID
     */
    @Schema(description = "父菜单ID")
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    @TableField(value = "order_num")
    private Integer orderNum;

    /**
     * 路由名称
     */
    @Schema(description = "路由名称")
    @TableField(value = "name")
    private String name;

    /**
     * 路由地址
     */
    @Schema(description = "路由地址")
    @TableField(value = "path")
    private String path;

    /**
     * 菜单类型(D-目录,M-菜单)
     */
    @Schema(description = "菜单类型(D-目录,M-菜单)")
    @TableField(value = "menu_type")
    private String menuType;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    @TableField(value = "icon")
    private String icon;

    /**
     * 菜单状态(0-显示,1-隐藏)
     */
    @Schema(description = "菜单状态(0-显示,1-隐藏)")
    @TableField(value = "hidden")
    private Boolean hidden;

    /**
     * 菜单状态(0-正常,1-停用)
     */
    @Schema(description = "菜单状态(0-正常,1-停用)")
    @TableField(value = "status")
    private String status;

    /**
     * 子菜单
     */
    @Schema(description = "子菜单")
    @TableField(exist = false)
    private List<Menu> children;

}

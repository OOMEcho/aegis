package com.aegis.modules.permission.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 功能权限表
 *
 * @author xuesong.lei
 * @since 2026-01-13
 * @TableName t_permission
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "功能权限表")
@TableName(value = "t_permission")
public class Permission implements Serializable {

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
     * 权限编码(全局唯一，如 system:user:add)
     */
    @Schema(description = "权限编码(全局唯一，如 system:user:add)")
    @TableField(value = "perm_code")
    private String permCode;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    @TableField(value = "perm_name")
    private String permName;

    /**
     * 权限类型(M=页面,B=按钮,A=API)
     */
    @Schema(description = "权限类型(M=页面,B=按钮,A=API)")
    @TableField(value = "perm_type")
    private String permType;

    /**
     * 状态(0-正常,1-停用)
     */
    @Schema(description = "状态(0-正常,1-停用)")
    @TableField(value = "status")
    private String status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @TableField(value = "remark")
    private String remark;

}

package com.aegis.modules.role.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;


import java.io.Serializable;

/**
 * 角色与权限关联表
 *
 * @author xuesong.lei
 * @since 2026-01-13
 * @TableName t_role_permission
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "角色与权限关联表")
@TableName(value ="t_role_permission")
public class RolePermission implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * 角色ID
    */
    @Schema(description = "角色ID")
    @TableField(value = "role_id")
    private Long roleId;

    /**
    * 权限编码
    */
    @Schema(description = "权限编码")
    @TableField(value = "perm_code")
    private String permCode;

}

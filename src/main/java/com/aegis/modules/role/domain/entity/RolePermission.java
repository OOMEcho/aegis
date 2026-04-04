package com.aegis.modules.role.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.io.Serializable;

/**
 * 角色与权限关联表
 *
 * @author xuesong.lei
 * @since 2026-01-11
 * @TableName t_role_permission
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("角色与权限关联表")
@TableName(value ="t_role_permission")
public class RolePermission implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * 角色ID
    */
    @ApiModelProperty("角色ID")
    @TableField(value = "role_id")
    private Long roleId;

    /**
    * 权限编码
    */
    @ApiModelProperty("权限编码")
    @TableField(value = "perm_code")
    private String permCode;

}

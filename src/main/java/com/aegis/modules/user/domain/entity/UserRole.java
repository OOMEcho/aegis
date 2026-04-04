package com.aegis.modules.user.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


import java.io.Serializable;

/**
 * 用户和角色关联表
 *
 * @author xuesong.lei
 * @since 2025-08-30
 * @TableName t_user_role
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("用户和角色关联表")
@TableName(value = "t_user_role")
public class UserRole implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    @TableField(value = "role_id")
    private Long roleId;

}

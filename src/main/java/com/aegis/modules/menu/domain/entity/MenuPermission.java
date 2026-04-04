package com.aegis.modules.menu.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.io.Serializable;

/**
 * 菜单与权限关联表
 *
 * @author xuesong.lei
 * @since 2026-01-11
 * @TableName t_menu_permission
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("菜单与权限关联表")
@TableName(value ="t_menu_permission")
public class MenuPermission implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * 菜单ID
    */
    @ApiModelProperty("菜单ID")
    @TableField(value = "menu_id")
    private Long menuId;

    /**
    * 权限编码
    */
    @ApiModelProperty("权限编码")
    @TableField(value = "perm_code")
    private String permCode;

}

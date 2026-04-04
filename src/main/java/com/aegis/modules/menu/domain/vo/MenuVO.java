package com.aegis.modules.menu.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 菜单VO
 *
 * @author xuesong.lei
 * @since 2026-02-27
 */
@Data
@ApiModel("菜单VO")
public class MenuVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("菜单编码")
    private String menuCode;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("父菜单ID")
    private Long parentId;

    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    @ApiModelProperty("前端路由名称")
    private String name;

    @ApiModelProperty("前端路由路径")
    private String path;

    @ApiModelProperty("菜单类型(D-目录,M-菜单)")
    private String menuType;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("菜单状态(0-显示,1-隐藏)")
    private Boolean hidden;

    @ApiModelProperty("菜单状态(0-正常,1-停用)")
    private String status;

    @ApiModelProperty("子菜单")
    private List<MenuVO> children;
}

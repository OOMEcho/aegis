package com.aegis.modules.menu.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "菜单VO")
public class MenuVO {

    @Schema(description = "主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "菜单编码")
    private String menuCode;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "父菜单ID")
    private Long parentId;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "前端路由名称")
    private String name;

    @Schema(description = "前端路由路径")
    private String path;

    @Schema(description = "菜单类型(D-目录,M-菜单)")
    private String menuType;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "菜单状态(0-显示,1-隐藏)")
    private Boolean hidden;

    @Schema(description = "菜单状态(0-正常,1-停用)")
    private String status;

    @Schema(description = "子菜单")
    private List<MenuVO> children;
}

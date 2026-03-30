package com.aegis.modules.menu.domain.dto;

import com.aegis.common.validator.EnumString;
import com.aegis.common.validator.ValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 菜单DTO
 *
 * @author xuesong.lei
 * @since 2025/9/13 14:40
 */
@Data
@ApiModel("菜单DTO")
public class MenuDTO {

    @ApiModelProperty("主键ID")
    @Null(groups = ValidGroup.Create.class, message = "应用ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @ApiModelProperty("菜单编码")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "菜单编码不能为空")
    @Size(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, max = 64, message = "菜单编码长度不能超过64个字符")
    private String menuCode;

    @ApiModelProperty("菜单名称")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "菜单名称不能为空")
    @Size(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;

    @ApiModelProperty("父菜单ID")
    @NotNull(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "父菜单ID不能为空")
    private Long parentId;

    @ApiModelProperty("显示顺序")
    @NotNull(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "显示顺序不能为空")
    private Integer orderNum;

    @ApiModelProperty("路由名称")
    @NotNull(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "路由名称不能为空")
    private String name;

    @ApiModelProperty("路由地址")
    @NotNull(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "路由地址不能为空")
    private String path;

    @ApiModelProperty("菜单类型(D-目录,M-菜单)")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "菜单类型不能为空")
    @EnumString(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, value = {"D", "M"}, message = "菜单类型参数错误")
    private String menuType;

    @ApiModelProperty("菜单状态(0-显示,1-隐藏)")
    private Boolean hidden;

    @ApiModelProperty("菜单状态(0-正常,1-停用)")
    @EnumString(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, value = {"0", "1"}, message = "状态只允许为0或1")
    private String status;

    @ApiModelProperty("菜单图标")
    private String icon;
}

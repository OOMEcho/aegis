package com.aegis.modules.menu.domain.dto;

import com.aegis.common.validator.EnumString;
import com.aegis.common.validator.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 菜单DTO
 *
 * @author xuesong.lei
 * @since 2025/9/13 14:40
 */
@Data
@Schema(description = "菜单DTO")
public class MenuDTO {

    @Schema(description = "主键ID")
    @Null(groups = ValidGroup.Create.class, message = "应用ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @Schema(description = "菜单编码")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "菜单编码不能为空")
    @Size(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, max = 64, message = "菜单编码长度不能超过64个字符")
    private String menuCode;

    @Schema(description = "菜单名称")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "菜单名称不能为空")
    @Size(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;

    @Schema(description = "父菜单ID")
    @NotNull(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "父菜单ID不能为空")
    private Long parentId;

    @Schema(description = "显示顺序")
    @NotNull(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "显示顺序不能为空")
    private Integer orderNum;

    @Schema(description = "路由名称")
    @NotNull(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "路由名称不能为空")
    private String name;

    @Schema(description = "路由地址")
    @NotNull(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "路由地址不能为空")
    private String path;

    @Schema(description = "菜单类型(D-目录,M-菜单)")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "菜单类型不能为空")
    @EnumString(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, value = {"D", "M"}, message = "菜单类型参数错误")
    private String menuType;

    @Schema(description = "菜单状态(0-显示,1-隐藏)")
    private Boolean hidden;

    @Schema(description = "菜单状态(0-正常,1-停用)")
    @EnumString(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, value = {"0", "1"}, message = "状态只允许为0或1")
    private String status;

    @Schema(description = "菜单图标")
    private String icon;
}

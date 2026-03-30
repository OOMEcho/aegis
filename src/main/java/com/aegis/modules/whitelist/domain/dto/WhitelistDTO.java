package com.aegis.modules.whitelist.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import com.aegis.common.validator.EnumString;
import com.aegis.common.validator.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * 白名单DTO
 *
 * @author xuesong.lei
 * @since 2025/9/8 11:44
 */
@Data
@Schema(description = "白名单DTO")
@EqualsAndHashCode(callSuper = true)
public class WhitelistDTO extends PageDTO {

    @Schema(description = "主键ID")
    @Null(groups = ValidGroup.Create.class, message = "应用ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @Schema(description = "请求方法,GET,POST,PUT,DELETE,ALL=不限制")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "请求方法参数不能为空")
    @EnumString(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, value = {"GET", "POST", "PUT", "DELETE", "ALL"}, message = "请求方法参数错误")
    private String requestMethod;

    @Schema(description = "URL匹配模式")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "请求地址参数不能为空")
    private String requestUri;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态(0-正常,1停用)")
    private String status;
}

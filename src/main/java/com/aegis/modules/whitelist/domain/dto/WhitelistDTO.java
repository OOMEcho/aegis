package com.aegis.modules.whitelist.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import com.aegis.common.validator.EnumString;
import com.aegis.common.validator.ValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 白名单DTO
 *
 * @author xuesong.lei
 * @since 2025-09-08
 */
@Data
@ApiModel("白名单DTO")
@EqualsAndHashCode(callSuper = true)
public class WhitelistDTO extends PageDTO {

    @ApiModelProperty("主键ID")
    @Null(groups = ValidGroup.Create.class, message = "应用ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @ApiModelProperty("请求方法,GET,POST,PUT,DELETE,ALL=不限制")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "请求方法参数不能为空")
    @EnumString(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, value = {"GET", "POST", "PUT", "DELETE", "ALL"}, message = "请求方法参数错误")
    private String requestMethod;

    @ApiModelProperty("URL匹配模式")
    @NotBlank(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, message = "请求地址参数不能为空")
    private String requestUri;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("状态(0-正常,1停用)")
    private String status;
}

package com.aegis.modules.common.domain.dto;

import com.aegis.common.domain.dto.CaptchaDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册DTO
 *
 * @author xuesong.lei
 * @since 2025-09-14
 */
@Data
@ApiModel("用户注册DTO")
@EqualsAndHashCode(callSuper = true)
public class UserRegisterDTO extends CaptchaDTO {

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 6, max = 20, message = "用户名长度必须在6到20个字符之间")
    private String username;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("确认密码")
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @ApiModelProperty("邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
}

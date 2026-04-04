package com.aegis.modules.common.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户修改信息DTO
 *
 * @author xuesong.lei
 * @since 2025-09-14
 */
@Data
@ApiModel("用户修改信息DTO")
public class UserUpdateDTO {

    @ApiModelProperty("呢称")
    private String nickname;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("旧密码")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 16, message = "密码长度必须在8到16个字符之间")
    private String password;

    @ApiModelProperty("确认密码")
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}

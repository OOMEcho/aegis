package com.aegis.modules.common.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 用户修改信息DTO
 *
 * @author xuesong.lei
 * @since 2025-09-14
 */
@Data
@Schema(description = "用户修改信息DTO")
public class UserUpdateDTO {

    @Schema(description = "呢称")
    private String nickname;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "旧密码")
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 16, message = "密码长度必须在8到16个字符之间")
    private String password;

    @Schema(description = "确认密码")
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}

package com.aegis.modules.role.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户和角色DTO
 *
 * @author xuesong.lei
 * @since 2025/09/13 18:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户和角色DTO")
public class UserAndRoleQueryDTO extends PageDTO {

    @Schema(description = "角色ID")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "电话")
    private String phone;
}

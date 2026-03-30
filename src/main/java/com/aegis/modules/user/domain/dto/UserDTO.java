package com.aegis.modules.user.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import com.aegis.common.validator.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 用户DTO
 *
 * @author xuesong.lei
 * @since 2025/9/14 18:31
 */
@Data
@Schema(description = "用户DTO")
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends PageDTO {

    @Schema(description = "主键ID")
    @Null(groups = ValidGroup.Create.class, message = "应用ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "用户名")
    @NotBlank(groups = ValidGroup.Create.class, message = "用户名不能为空")
    private String username;

    @Schema(description = "呢称")
    @NotBlank(groups = ValidGroup.Update.class, message = "呢称不能为空")
    private String nickname;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @Schema(description = "角色列表")
    private List<Long> roleIds;
}

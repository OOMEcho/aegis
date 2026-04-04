package com.aegis.modules.user.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import com.aegis.common.validator.ValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;
import java.util.List;

/**
 * 用户DTO
 *
 * @author xuesong.lei
 * @since 2025-09-14
 */
@Data
@ApiModel("用户DTO")
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends PageDTO {

    @ApiModelProperty("主键ID")
    @Null(groups = ValidGroup.Create.class, message = "应用ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @ApiModelProperty("部门ID")
    private Long deptId;

    @ApiModelProperty("用户名")
    @NotBlank(groups = ValidGroup.Create.class, message = "用户名不能为空")
    private String username;

    @ApiModelProperty("呢称")
    @NotBlank(groups = ValidGroup.Update.class, message = "呢称不能为空")
    private String nickname;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty("角色列表")
    private List<Long> roleIds;
}

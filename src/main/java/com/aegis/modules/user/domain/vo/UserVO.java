package com.aegis.modules.user.domain.vo;

import com.aegis.common.mask.DataMask;
import com.aegis.common.mask.MaskTypeEnum;
import com.aegis.modules.dept.domain.entity.Dept;
import com.aegis.modules.menu.domain.vo.RouterVo;
import com.aegis.modules.role.domain.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户VO
 *
 * @author xuesong.lei
 * @since 2025/9/13 18:44
 */
@Data
@Schema(description = "用户VO")
public class UserVO {

    @Schema(description = "主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "呢称")
    private String nickname;

    @Schema(description = "邮箱")
    @DataMask(type = MaskTypeEnum.EMAIL)
    private String email;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "电话")
    @DataMask(type = MaskTypeEnum.PHONE)
    private String phone;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "最后登录IP")
    private String lastLoginIp;

    @Schema(description = "最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    @Schema(description = "部门信息")
    private Dept dept;

    @Schema(description = "角色列表")
    private List<Role> roleList;

    @Schema(description = "权限列表")
    private List<String> permissions;

    @Schema(description = "路由列表")
    private List<RouterVo> routerVoList;

    @Schema(description = "是否在线")
    private Boolean online;
}

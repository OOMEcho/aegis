package com.aegis.modules.user.domain.vo;

import com.aegis.common.mask.DataMask;
import com.aegis.common.mask.MaskTypeEnum;
import com.aegis.modules.dept.domain.entity.Dept;
import com.aegis.modules.menu.domain.vo.RouterVo;
import com.aegis.modules.role.domain.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("用户VO")
public class UserVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("部门ID")
    private Long deptId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("呢称")
    private String nickname;

    @ApiModelProperty("邮箱")
    @DataMask(type = MaskTypeEnum.EMAIL)
    private String email;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("电话")
    @DataMask(type = MaskTypeEnum.PHONE)
    private String phone;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("最后登录IP")
    private String lastLoginIp;

    @ApiModelProperty("最后登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    @ApiModelProperty("部门信息")
    private Dept dept;

    @ApiModelProperty("角色列表")
    private List<Role> roleList;

    @ApiModelProperty("权限列表")
    private List<String> permissions;

    @ApiModelProperty("路由列表")
    private List<RouterVo> routerVoList;

    @ApiModelProperty("是否在线")
    private Boolean online;
}

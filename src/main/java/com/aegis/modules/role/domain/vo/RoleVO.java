package com.aegis.modules.role.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: xuesong.lei
 * @Date: 2026/2/27 14:07
 * @Description: 角色VO
 */
@Data
@ApiModel("角色VO")
public class RoleVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    @ApiModelProperty("数据范围(1-全部数据权限,2-自定数据权限,3-本部门数据权限,4-本部门及以下数据权限,5-仅本人数据权限)")
    private String dataScope;

    @ApiModelProperty("部门树选择项是否关联显示")
    private Integer deptCheckStrictly;

    @ApiModelProperty("角色状态(0-正常,1-停用)")
    private String status;
}

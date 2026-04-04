package com.aegis.modules.dept.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 部门VO
 *
 * @author xuesong.lei
 * @since 2026-02-27
 */
@Data
@ApiModel("部门VO")
public class DeptVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("祖级列表")
    private String ancestors;

    @ApiModelProperty("父部门ID")
    private Long parentId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    @ApiModelProperty("负责人")
    private String leader;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("部门状态(0-正常,1-停用)")
    private String status;

    @ApiModelProperty("子部门")
    private List<DeptVO> children;
}

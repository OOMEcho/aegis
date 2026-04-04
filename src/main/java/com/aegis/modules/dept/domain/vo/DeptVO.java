package com.aegis.modules.dept.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "部门VO")
public class DeptVO {

    @Schema(description = "主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "祖级列表")
    private String ancestors;

    @Schema(description = "父部门ID")
    private Long parentId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门状态(0-正常,1-停用)")
    private String status;

    @Schema(description = "子部门")
    private List<DeptVO> children;
}

package com.aegis.modules.role.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 角色VO
 *
 * @author xuesong.lei
 * @since 2026-02-27
 */
@Data
@Schema(description = "角色VO")
public class RoleVO {

    @Schema(description = "主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "数据范围(1-全部数据权限,2-自定数据权限,3-本部门数据权限,4-本部门及以下数据权限,5-仅本人数据权限)")
    private String dataScope;

    @Schema(description = "部门树选择项是否关联显示")
    private Integer deptCheckStrictly;

    @Schema(description = "角色状态(0-正常,1-停用)")
    private String status;
}

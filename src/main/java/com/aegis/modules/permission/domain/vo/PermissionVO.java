package com.aegis.modules.permission.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 权限VO
 *
 * @author xuesong.lei
 * @since 2026-02-27
 */
@Data
@Schema(description = "权限VO")
public class PermissionVO {

    @Schema(description = "主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "权限编码")
    private String permCode;

    @Schema(description = "权限名称")
    private String permName;

    @Schema(description = "权限类型(M=页面,B=按钮,A=API)")
    private String permType;

    @Schema(description = "状态(0-正常,1-停用)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}

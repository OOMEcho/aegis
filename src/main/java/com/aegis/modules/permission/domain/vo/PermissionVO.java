package com.aegis.modules.permission.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 权限VO
 *
 * @author xuesong.lei
 * @since 2026-02-27
 */
@Data
@ApiModel("权限VO")
public class PermissionVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("权限编码")
    private String permCode;

    @ApiModelProperty("权限名称")
    private String permName;

    @ApiModelProperty("权限类型(M=页面,B=按钮,A=API)")
    private String permType;

    @ApiModelProperty("状态(0-正常,1-停用)")
    private String status;

    @ApiModelProperty("备注")
    private String remark;
}

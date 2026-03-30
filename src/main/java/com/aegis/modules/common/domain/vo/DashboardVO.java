package com.aegis.modules.common.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Dashboard统计VO
 *
 * @author xuesong.lei
 * @since 2026/2/2
 */
@Data
@ApiModel("Dashboard统计VO")
public class DashboardVO {

    @ApiModelProperty("用户总数")
    private Long userCount;

    @ApiModelProperty("用户近7日增长百分比")
    private String userGrowthRate;

    @ApiModelProperty("角色总数")
    private Long roleCount;

    @ApiModelProperty("角色近7日增长百分比")
    private String roleGrowthRate;

    @ApiModelProperty("权限总数")
    private Long permissionCount;

    @ApiModelProperty("权限近7日增长百分比")
    private String permissionGrowthRate;

    @ApiModelProperty("通知总数")
    private Long noticeCount;

    @ApiModelProperty("通知近7日增长百分比")
    private String noticeGrowthRate;
}

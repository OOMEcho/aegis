package com.aegis.modules.common.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: xuesong.lei
 * @Date: 2026/2/2
 * @Description: 访问趋势VO
 */
@Data
@ApiModel("访问趋势VO")
public class AccessTrendVO {

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("访问量")
    private Long count;
}

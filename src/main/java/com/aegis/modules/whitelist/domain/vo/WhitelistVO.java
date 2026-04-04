package com.aegis.modules.whitelist.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 白名单VO
 *
 * @author xuesong.lei
 * @since 2026-02-27
 */
@Data
@ApiModel("白名单VO")
public class WhitelistVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("请求方法")
    private String requestMethod;

    @ApiModelProperty("URI匹配模式")
    private String requestUri;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("状态(0-正常,1-停用)")
    private String status;
}

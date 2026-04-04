package com.aegis.modules.resource.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 资源VO
 *
 * @author xuesong.lei
 * @since 2026-02-27
 */
@Data
@ApiModel("资源VO")
public class ResourceVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("请求方法(GET/POST/PUT/DELETE/ALL)")
    private String requestMethod;

    @ApiModelProperty("URI匹配模式(Ant风格)")
    private String requestUri;

    @ApiModelProperty("关联权限编码")
    private String permCode;

    @ApiModelProperty("状态(0-正常,1-停用)")
    private String status;

    @ApiModelProperty("备注")
    private String remark;
}

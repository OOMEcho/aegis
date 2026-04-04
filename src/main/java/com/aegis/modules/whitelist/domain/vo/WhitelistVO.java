package com.aegis.modules.whitelist.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 白名单VO
 *
 * @author xuesong.lei
 * @since 2026-02-27
 */
@Data
@Schema(description = "白名单VO")
public class WhitelistVO {

    @Schema(description = "主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "请求方法")
    private String requestMethod;

    @Schema(description = "URI匹配模式")
    private String requestUri;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态(0-正常,1-停用)")
    private String status;
}

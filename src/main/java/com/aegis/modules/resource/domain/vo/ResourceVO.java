package com.aegis.modules.resource.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 资源VO
 *
 * @author xuesong.lei
 * @since 2026/2/27 14:07
 */
@Data
@Schema(description = "资源VO")
public class ResourceVO {

    @Schema(description = "主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "请求方法(GET/POST/PUT/DELETE/ALL)")
    private String requestMethod;

    @Schema(description = "URI匹配模式(Ant风格)")
    private String requestUri;

    @Schema(description = "关联权限编码")
    private String permCode;

    @Schema(description = "状态(0-正常,1-停用)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}

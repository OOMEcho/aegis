package com.aegis.modules.resource.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import com.aegis.common.validator.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资源DTO
 *
 * @author xuesong.lei
 * @since 2026-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "资源DTO")
public class ResourceDTO extends PageDTO {

    @Schema(description = "主键ID")
    @NotNull(message = "ID不能为空", groups = {ValidGroup.Update.class})
    private Long id;

    @Schema(description = "请求方法(GET/POST/PUT/DELETE/ALL)")
    @NotBlank(message = "请求方法不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String requestMethod;

    @Schema(description = "URI匹配模式(Ant风格)")
    @NotBlank(message = "URI不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String requestUri;

    @Schema(description = "关联权限编码")
    @NotBlank(message = "权限编码不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String permCode;

    @Schema(description = "状态(0-正常,1-停用)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}

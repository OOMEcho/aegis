package com.aegis.modules.file.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件元数据分页查询DTO
 *
 * @author xuesong.lei
 * @since 2026/3/15 15:42
 */
@Data
@Schema(description = "文件元数据分页查询DTO")
@EqualsAndHashCode(callSuper = true)
public class FileMetadataPageDTO extends PageDTO {

    @Schema(description = "原始文件名称")
    private String originalFileName;

    @Schema(description = "存储平台")
    private String platform;

    @Schema(description = "文件类型")
    private String contentType;
}

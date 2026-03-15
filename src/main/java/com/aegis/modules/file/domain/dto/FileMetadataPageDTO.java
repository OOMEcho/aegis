package com.aegis.modules.file.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: xuesong.lei
 * @Date: 2026/3/15 15:42
 * @Description: 文件元数据分页查询DTO
 */
@Data
@ApiModel("文件元数据分页查询DTO")
@EqualsAndHashCode(callSuper = true)
public class FileMetadataPageDTO extends PageDTO {

    @ApiModelProperty("原始文件名称")
    private String originalFileName;

    @ApiModelProperty("存储平台")
    private String platform;

    @ApiModelProperty("文件类型")
    private String contentType;
}

package com.aegis.modules.file.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 预签名上传完成入库DTO
 *
 * @author xuesong.lei
 * @since 2026-03-15
 */
@Data
@ApiModel("预签名上传完成入库DTO")
public class PresignedUploadCompleteDTO {

    @ApiModelProperty("文件存储路径")
    @NotBlank(message = "文件路径不能为空")
    private String filePath;

    @ApiModelProperty("原始文件名")
    @NotBlank(message = "原始文件名不能为空")
    private String originalFileName;

    @ApiModelProperty("文件大小(字节)")
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;

    @ApiModelProperty("文件类型")
    private String contentType;
}

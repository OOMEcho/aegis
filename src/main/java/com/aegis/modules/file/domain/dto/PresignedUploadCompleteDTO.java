package com.aegis.modules.file.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author: xuesong.lei
 * @Date: 2026/3/15 15:42
 * @Description: 预签名上传完成入库DTO
 */
@Data
@Schema(description = "预签名上传完成入库DTO")
public class PresignedUploadCompleteDTO {

    @Schema(description = "文件存储路径")
    @NotBlank(message = "文件路径不能为空")
    private String filePath;

    @Schema(description = "原始文件名")
    @NotBlank(message = "原始文件名不能为空")
    private String originalFileName;

    @Schema(description = "文件大小(字节)")
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;

    @Schema(description = "文件类型")
    private String contentType;
}

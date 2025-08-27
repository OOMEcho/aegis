package com.aegis.common.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/27 21:24
 * @Description: 文件上传结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResult {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 原始文件名称
     */
    private String originalFileName;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件存储路径
     */
    private String filePath;

    /**
     * 文件访问URL
     */
    private String fileUrl;

    /**
     * 文件大小，单位字节
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 存储平台
     */
    private String platform;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 文件MD5值
     */
    private String md5;

}

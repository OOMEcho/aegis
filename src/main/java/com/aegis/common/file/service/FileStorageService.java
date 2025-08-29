package com.aegis.common.file.service;

import com.aegis.common.file.FileUploadResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/27 21:24
 * @Description: 文件存储服务接口
 */
public interface FileStorageService {

    /**
     * 上传文件
     */
    FileUploadResult upload(MultipartFile file, String directory);

    /**
     * 下载文件
     */
    InputStream download(String filePath);

    /**
     * 删除文件
     */
    boolean delete(String filePath);

    /**
     * 获取文件访问URL
     */
    String getFileUrl(String filePath);

    /**
     * 检查文件是否存在
     */
    boolean exists(String filePath);

    /**
     * 生成预签名URL（用于直接上传到云存储）
     */
    String generatePresignedUploadUrl(String filePath, Duration expiration);

    /**
     * 获取文件的临时下载链接
     */
    String getTemporaryDownloadUrl(String filePath, Duration expiration);
}

package com.aegis.common.file.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.aegis.common.constant.FileConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.FileUploadProperties;
import com.aegis.common.file.FileUploadResult;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/28 21:24
 * @Description: 抽象文件存储服务，提供通用方法
 */
public abstract class AbstractFileStorageService implements FileStorageService {

    protected final FileUploadProperties properties;

    protected AbstractFileStorageService(FileUploadProperties properties) {
        this.properties = properties;
    }

    /**
     * 文件上传前的统一校验
     */
    protected void validateFile(MultipartFile file, byte[] fileBytes) {
        validateFileBasic(file);
        validateFileSize(file);
        validateFileExtension(file);
        validateFileContent(file, fileBytes);
        validateFileSecurity(file, fileBytes);
    }

    /**
     * 基础文件校验
     */
    private void validateFileBasic(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        String originalFileName = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFileName)) {
            throw new BusinessException("文件名不能为空");
        }

        // 文件名长度校验
        if (originalFileName.length() > 255) {
            throw new BusinessException("文件名过长，最大支持255个字符");
        }

        // 文件名安全校验 - 防止路径穿越
        if (originalFileName.contains("..") || originalFileName.contains("/") ||
                originalFileName.contains("\\")) {
            throw new BusinessException("文件名包含非法字符");
        }
    }

    /**
     * 文件大小校验 - 可以从配置读取
     */
    private void validateFileSize(MultipartFile file) {
        long maxSize = 100 * 1024 * 1024; // 100MB，可以配置化
        if (file.getSize() > maxSize) {
            throw new BusinessException("文件大小超过限制，最大支持100MB");
        }

        if (file.getSize() == 0) {
            throw new BusinessException("文件内容为空");
        }
    }

    /**
     * 文件扩展名校验
     */
    private void validateFileExtension(MultipartFile file) {
        String extension = Objects.requireNonNull(FileUtil.extName(file.getOriginalFilename())).toLowerCase();

        // 检查是否在黑名单中
        if (FileConstants.DANGEROUS_EXTENSIONS.contains(extension)) {
            throw new BusinessException("不支持的文件类型: " + extension);
        }

        // 检查是否在白名单中
        if (!FileConstants.ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BusinessException("不支持的文件类型: " + extension);
        }
    }

    /**
     * 文件内容校验 - 通过文件头验证真实类型
     */
    private void validateFileContent(MultipartFile file, byte[] fileBytes) {
        String extension = Objects.requireNonNull(FileUtil.extName(file.getOriginalFilename())).toLowerCase();

        // 对于有文件头签名的文件类型进行验证
        if (FileConstants.FILE_SIGNATURES.containsKey(extension)) {
            byte[] expectedSignature = FileConstants.FILE_SIGNATURES.get(extension);
            if (fileBytes.length < expectedSignature.length) {
                throw new BusinessException("文件内容不完整");
            }

            // 检查文件头是否匹配
            for (int i = 0; i < expectedSignature.length; i++) {
                if (fileBytes[i] != expectedSignature[i]) {
                    throw new BusinessException("文件类型与扩展名不匹配");
                }
            }
        }
    }

    /**
     * 安全性校验
     */
    private void validateFileSecurity(MultipartFile file, byte[] fileBytes) {
        // 检查是否包含脚本内容
        String content = new String(Arrays.copyOf(fileBytes, Math.min(fileBytes.length, 1024)));

        for (String pattern : FileConstants.MALICIOUS_PATTERNS) {
            if (content.toLowerCase().contains(pattern)) {
                throw new BusinessException("文件包含潜在的恶意内容");
            }
        }
    }

    /**
     * 构建文件上传结果
     */
    protected FileUploadResult buildFileUploadResult(MultipartFile file, String fileName, String objectName, byte[] bytes, String platform) {
        return FileUploadResult.builder()
                .fileName(fileName) // 直接使用传入的fileName
                .originalFileName(file.getOriginalFilename())
                .suffix(FileUtil.extName(file.getOriginalFilename()))
                .filePath(objectName)
                .fileUrl(getFileUrl(objectName))
                .fileSize((long) bytes.length)
                .contentType(getContentType(file))
                .platform(platform)
                .uploadTime(LocalDateTime.now())
                .md5(DigestUtil.md5Hex(bytes))
                .build();
    }

    /**
     * 获取文件的Content-Type，默认值为 application/octet-stream
     */
    protected String getContentType(MultipartFile file) {
        return file.getContentType() != null ? file.getContentType() : "application/octet-stream";
    }

    /**
     * 构建存储对象名称，包含目录和文件名
     */
    protected String buildObjectName(String directory, String fileName) {
        return (StrUtil.isNotBlank(directory) ? directory + FileConstants.SEPARATOR : "")
                + FileConstants.FILE_FOLDER + FileConstants.SEPARATOR + fileName;
    }

    /**
     * 生成唯一的文件名，保留原始文件扩展名
     */
    protected String generateFileName(String originalFileName) {
        String extension = FileUtil.extName(originalFileName);
        return IdUtil.simpleUUID() + FileConstants.POINT + extension;
    }
}

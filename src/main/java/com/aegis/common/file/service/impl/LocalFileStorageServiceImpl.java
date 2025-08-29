package com.aegis.common.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.aegis.common.constant.FileConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.FileUploadProperties;
import com.aegis.common.file.FileUploadResult;
import com.aegis.common.file.StoragePlatform;
import com.aegis.common.file.service.AbstractFileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.Duration;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/27 21:24
 * @Description: 本地文件存储服务实现
 */
@Slf4j
@Service(FileConstants.LOCAL)
@ConditionalOnProperty(prefix = "file.upload", name = "platform", havingValue = "local")
public class LocalFileStorageServiceImpl extends AbstractFileStorageService {

    private final String basePath;

    public LocalFileStorageServiceImpl(FileUploadProperties properties) {
        super(properties);
        this.basePath = properties.getLocal().getPath();
    }

    @Override
    public FileUploadResult upload(MultipartFile file, String directory) {
        try {
            String fullDirectory = basePath + FileConstants.SEPARATOR +
                    (StrUtil.isNotBlank(directory) ? directory + FileConstants.SEPARATOR : "") +
                    FileConstants.FILE_FOLDER;
            FileUtil.mkdir(fullDirectory);

            String fileName = generateFileName(file.getOriginalFilename());
            String filePath = fullDirectory + FileConstants.SEPARATOR + fileName;

            byte[] fileBytes = file.getBytes();
            validateFile(file, fileBytes);

            FileUtil.writeBytes(fileBytes, new File(filePath));

            return buildFileUploadResult(file, fileName, filePath, fileBytes,
                    StoragePlatform.LOCAL.name());

        } catch (Exception e) {
            log.error("本地文件上传失败", e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public InputStream download(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new BusinessException("文件不存在: " + filePath);
            }
            return Files.newInputStream(file.toPath());
        } catch (Exception e) {
            log.error("获取本地文件流失败: {}", filePath, e);
            throw new BusinessException("获取文件流失败: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String filePath) {
        try {
            return FileUtil.del(filePath);
        } catch (Exception e) {
            log.error("删除本地文件失败: {}", filePath, e);
            return false;
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        // 本地文件需要通过web服务访问，这里返回相对路径
        return filePath.replace(basePath, "/files");
    }

    @Override
    public boolean exists(String filePath) {
        return FileUtil.exist(filePath);
    }

    @Override
    public String generatePresignedUploadUrl(String filePath, Duration expiration) {
        log.warn("本地存储不支持预签名上传URL，返回空字符串");
        return "";
    }

    @Override
    public String getTemporaryDownloadUrl(String filePath, Duration expiration) {
        // 本地存储生成带时间戳和签名的临时下载链接
        try {
            if (!exists(filePath)) {
                throw new BusinessException("文件不存在: " + filePath);
            }

            // 生成带时间戳的临时下载链接
            long timestamp = System.currentTimeMillis() + expiration.toMillis();
            String token = DigestUtil.md5Hex(filePath + timestamp + "secret_key"); // 使用配置的密钥

            String relativePath = filePath.replace(basePath, "/files");
            return relativePath + "?token=" + token + "&expires=" + timestamp;
        } catch (Exception e) {
            log.error("生成本地存储临时下载URL失败: {}", filePath, e);
            throw new BusinessException("生成临时下载URL失败: " + e.getMessage());
        }
    }
}

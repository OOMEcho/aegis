package com.aegis.common.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import com.aegis.common.constant.FileConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.StoragePlatform;
import com.aegis.common.file.config.FileUploadProperties;
import com.aegis.common.file.service.AbstractFileStorageService;
import com.aegis.modules.file.domain.entity.FileMetadata;
import com.aegis.modules.file.mapper.FileMetadataMapper;
import lombok.extern.slf4j.Slf4j;
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
public class LocalFileStorageServiceImpl extends AbstractFileStorageService {

    private final String basePath;

    private final String secretKey;

    public LocalFileStorageServiceImpl(FileUploadProperties properties, FileMetadataMapper fileMetadataMapper) {
        super(properties, fileMetadataMapper);
        this.basePath = properties.getLocal().getPath();
        this.secretKey = properties.getLocal().getSecretKey();
    }

    @Override
    public FileMetadata upload(MultipartFile file, String directory) {
        try {
            directory = FileConstants.sanitizeDirectory(directory);

            String fullDirectory = basePath + FileConstants.SEPARATOR +
                    (directory != null ? directory + FileConstants.SEPARATOR : "") +
                    FileConstants.getFileFolder();
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
            throw new BusinessException("上传失败,请联系系统管理员");
        }
    }

    @Override
    public InputStream download(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new BusinessException("下载失败,请联系系统管理员");
            }
            return Files.newInputStream(file.toPath());
        } catch (Exception e) {
            log.error("获取本地文件流失败: {}", filePath, e);
            throw new BusinessException("下载失败,请联系系统管理员");
        }
    }

    @Override
    public void delete(String filePath) {
        try {
            FileUtil.del(filePath);
        } catch (Exception e) {
            log.error("删除本地文件失败: {}", filePath, e);
            throw new BusinessException("删除失败,请联系系统管理员");
        }
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
                throw new BusinessException("下载失败,请联系系统管理员");
            }

            // 生成带时间戳的临时下载链接
            long timestamp = System.currentTimeMillis() + expiration.toMillis();

            String token = SecureUtil.hmacSha256(secretKey).digestHex(filePath + timestamp);

            String relativePath = filePath.replace(basePath, "/file/localDownload");
            return relativePath + "?token=" + token + "&expires=" + timestamp;
        } catch (Exception e) {
            log.error("生成本地存储临时下载URL失败: {}", filePath, e);
            throw new BusinessException("下载失败,请联系系统管理员");
        }
    }
}

package com.aegis.common.file.service.impl;

import com.aegis.common.constant.FileConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.FileUploadProperties;
import com.aegis.common.file.FileUploadResult;
import com.aegis.common.file.StoragePlatform;
import com.aegis.common.file.service.AbstractFileStorageService;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/27 21:43
 * @Description: Minio文件存储服务实现
 */
@Slf4j
@Service(FileConstants.MINIO)
@ConditionalOnProperty(prefix = "file.upload", name = "platform", havingValue = "minio")
public class MinioFileStorageServiceImpl extends AbstractFileStorageService {

    private final MinioClient minioClient;

    private final FileUploadProperties.MinioConfig config;

    public MinioFileStorageServiceImpl(FileUploadProperties properties, MinioClient minioClient) {
        super(properties);
        this.minioClient = minioClient;
        this.config = properties.getMinio();
    }

    @Override
    public FileUploadResult upload(MultipartFile file, String directory) {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            String objectName = buildObjectName(directory, fileName);

            byte[] fileBytes = file.getBytes();
            validateFile(file, fileBytes);

            try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(config.getBucketName())
                        .object(objectName)
                        .stream(inputStream, fileBytes.length, -1)
                        .contentType(getContentType(file))
                        .build());
            }

            return buildFileUploadResult(file, fileName, objectName, fileBytes,
                    StoragePlatform.MINIO.name());

        } catch (Exception e) {
            log.error("MinIO文件上传失败", e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public InputStream download(String filePath) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(filePath)
                    .build());
        } catch (Exception e) {
            log.error("获取MinIO文件流失败: {}", filePath, e);
            throw new BusinessException("获取文件流失败: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String filePath) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(filePath)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("删除MinIO文件失败: {}", filePath, e);
            return false;
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        return config.getEndpoint() + FileConstants.SEPARATOR + config.getBucketName() + FileConstants.SEPARATOR + filePath;
    }

    @Override
    public boolean exists(String filePath) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(filePath)
                    .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

package com.aegis.common.file.service.impl;

import com.aegis.common.constant.FileConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.StoragePlatform;
import com.aegis.common.file.config.FileUploadProperties;
import com.aegis.common.file.service.AbstractFileStorageService;
import com.aegis.modules.file.domain.entity.FileMetadata;
import com.aegis.modules.file.mapper.FileMetadataMapper;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/27 21:43
 * @Description: Minio文件存储服务实现
 */
@Slf4j
@Service(FileConstants.MINIO)
public class MinioFileStorageServiceImpl extends AbstractFileStorageService {

    private volatile MinioClient minioClient;

    private final FileUploadProperties.MinioConfig config;

    public MinioFileStorageServiceImpl(FileUploadProperties properties, FileMetadataMapper fileMetadataMapper) {
        super(properties, fileMetadataMapper);
        this.config = properties.getMinio();
    }

    @Override
    public FileMetadata upload(MultipartFile file, String directory) {
        try {
            MinioClient client = getMinioClient();
            ensureBucketExists(client);

            String fileName = generateFileName(file.getOriginalFilename());
            String objectName = buildObjectName(directory, fileName);

            byte[] fileBytes = file.getBytes();
            validateFile(file, fileBytes);

            try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
                client.putObject(PutObjectArgs.builder()
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
            throw new BusinessException("上传失败,请联系系统管理员");
        }
    }

    @Override
    public InputStream download(String filePath) {
        try {
            return getMinioClient().getObject(GetObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(filePath)
                    .build());
        } catch (Exception e) {
            log.error("获取MinIO文件流失败: {}", filePath, e);
            throw new BusinessException("下载失败,请联系系统管理员");
        }
    }

    @Override
    public void delete(String filePath) {
        try {
            getMinioClient().removeObject(RemoveObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(filePath)
                    .build());
        } catch (Exception e) {
            log.error("删除MinIO文件失败: {}", filePath, e);
            throw new BusinessException("删除失败,请联系系统管理员");
        }
    }

    @Override
    public boolean exists(String filePath) {
        try {
            getMinioClient().statObject(StatObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(filePath)
                    .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String generatePresignedUploadUrl(String filePath, Duration expiration) {
        try {
            return getMinioClient().getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(config.getBucketName())
                            .object(filePath)
                            .expiry((int) expiration.getSeconds())
                            .build()
            );
        } catch (Exception e) {
            log.error("生成MinIO预签名上传URL失败: {}", filePath, e);
            throw new BusinessException("上传失败,请联系系统管理员");
        }
    }

    @Override
    public String getTemporaryDownloadUrl(String filePath, Duration expiration) {
        try {
            return getMinioClient().getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(config.getBucketName())
                            .object(filePath)
                            .expiry((int) expiration.getSeconds())
                            .build()
            );
        } catch (Exception e) {
            log.error("生成MinIO临时下载URL失败: {}", filePath, e);
            throw new BusinessException("下载失败,请联系系统管理员");
        }
    }

    private MinioClient getMinioClient() {
        if (minioClient != null) {
            return minioClient;
        }
        synchronized (this) {
            if (minioClient == null) {
                minioClient = MinioClient.builder()
                        .endpoint(config.getEndpoint())
                        .credentials(config.getAccessKey(), config.getSecretKey())
                        .build();
            }
            return minioClient;
        }
    }

    private void ensureBucketExists(MinioClient client) throws Exception {
        boolean exists = client.bucketExists(
                BucketExistsArgs.builder().bucket(config.getBucketName()).build()
        );
        if (!exists) {
            client.makeBucket(
                    MakeBucketArgs.builder().bucket(config.getBucketName()).build()
            );
        }
    }
}

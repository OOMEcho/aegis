package com.aegis.common.file.service.impl;

import com.aegis.common.constant.FileConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.StoragePlatform;
import com.aegis.common.file.config.FileUploadProperties;
import com.aegis.common.file.service.AbstractFileStorageService;
import com.aegis.modules.file.domain.entity.FileMetadata;
import com.aegis.modules.file.mapper.FileMetadataMapper;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.Date;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/27 21:45
 * @Description: 腾讯云OSS文件存储服务实现
 */
@Slf4j
@Service(FileConstants.TENCENT)
public class TencentCosFileStorageServiceImpl extends AbstractFileStorageService {

    private volatile COSClient cosClient;

    private final FileUploadProperties.TencentConfig config;

    public TencentCosFileStorageServiceImpl(FileUploadProperties properties, FileMetadataMapper fileMetadataMapper) {
        super(properties, fileMetadataMapper);
        this.config = properties.getTencent();
    }

    @Override
    public FileMetadata upload(MultipartFile file, String directory) {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            String objectName = buildObjectName(directory, fileName);

            byte[] fileBytes = file.getBytes();
            validateFile(file, fileBytes);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileBytes.length);
            metadata.setContentType(getContentType(file));

            try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
                PutObjectRequest putObjectRequest = new PutObjectRequest(
                        config.getBucketName(),
                        objectName,
                        inputStream,
                        metadata
                );
                getCosClient().putObject(putObjectRequest);
            }

            return buildFileUploadResult(file, fileName, objectName, fileBytes,
                    StoragePlatform.TENCENT_COS.name());

        } catch (Exception e) {
            log.error("腾讯云COS文件上传失败", e);
            throw new BusinessException("上传失败,请联系系统管理员");
        }
    }

    @Override
    public InputStream download(String filePath) {
        try {
            return getCosClient().getObject(new GetObjectRequest(config.getBucketName(), filePath)).getObjectContent();
        } catch (Exception e) {
            log.error("获取腾讯云COS文件流失败: {}", filePath, e);
            throw new BusinessException("下载失败,请联系系统管理员");
        }
    }

    @Override
    public void delete(String filePath) {
        try {
            getCosClient().deleteObject(config.getBucketName(), filePath);
        } catch (Exception e) {
            log.error("删除腾讯云COS文件失败: {}", filePath, e);
            throw new BusinessException("删除失败,请联系系统管理员");
        }
    }

    @Override
    public boolean exists(String filePath) {
        try {
            return getCosClient().doesObjectExist(config.getBucketName(), filePath);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String generatePresignedUploadUrl(String filePath, Duration expiration) {
        try {
            Date expirationDate = new Date(System.currentTimeMillis() + expiration.toMillis());
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(
                    config.getBucketName(),
                    filePath,
                    HttpMethodName.PUT
            );
            request.setExpiration(expirationDate);
            request.setContentType("application/octet-stream");

            return getCosClient().generatePresignedUrl(request).toString();
        } catch (Exception e) {
            log.error("生成腾讯云COS预签名上传URL失败: {}", filePath, e);
            throw new BusinessException("上传失败,请联系系统管理员");
        }
    }

    @Override
    public String getTemporaryDownloadUrl(String filePath, Duration expiration) {
        try {
            Date expirationDate = new Date(System.currentTimeMillis() + expiration.toMillis());
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(
                    config.getBucketName(),
                    filePath,
                    HttpMethodName.GET
            );
            request.setExpiration(expirationDate);

            return getCosClient().generatePresignedUrl(request).toString();
        } catch (Exception e) {
            log.error("生成腾讯云COS临时下载URL失败: {}", filePath, e);
            throw new BusinessException("下载失败,请联系系统管理员");
        }
    }

    private COSClient getCosClient() {
        if (cosClient != null) {
            return cosClient;
        }
        synchronized (this) {
            if (cosClient == null) {
                COSCredentials credentials = new BasicCOSCredentials(config.getSecretId(), config.getSecretKey());
                ClientConfig clientConfig = new ClientConfig(new Region(config.getRegion()));
                cosClient = new COSClient(credentials, clientConfig);
            }
            return cosClient;
        }
    }
}

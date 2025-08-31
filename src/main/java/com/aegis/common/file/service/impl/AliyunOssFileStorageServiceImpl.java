package com.aegis.common.file.service.impl;

import com.aegis.common.constant.FileConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.FileUploadProperties;
import com.aegis.common.file.FileUploadResult;
import com.aegis.common.file.StoragePlatform;
import com.aegis.common.file.service.AbstractFileStorageService;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.Date;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/27 21:24
 * @Description: 阿里云OSS文件存储服务实现
 */
@Slf4j
@Service(FileConstants.ALIYUN)
@ConditionalOnProperty(prefix = "file.upload", name = "platform", havingValue = "aliyun_oss")
public class AliyunOssFileStorageServiceImpl extends AbstractFileStorageService {

    private final OSS ossClient;

    private final FileUploadProperties.AliyunConfig config;

    public AliyunOssFileStorageServiceImpl(FileUploadProperties properties, OSS ossClient) {
        super(properties);
        this.ossClient = ossClient;
        this.config = properties.getAliyun();
    }

    @Override
    public FileUploadResult upload(MultipartFile file, String directory) {
        try {
            byte[] fileBytes = file.getBytes();
            validateFile(file, fileBytes);

            String fileName = generateFileName(file.getOriginalFilename());
            String objectName = buildObjectName(directory, fileName);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileBytes.length);
            metadata.setContentType(getContentType(file));

            ossClient.putObject(config.getBucketName(), objectName,
                    new ByteArrayInputStream(fileBytes), metadata);

            return buildFileUploadResult(file, fileName, objectName, fileBytes,
                    StoragePlatform.ALIYUN_OSS.name());

        } catch (Exception e) {
            log.error("阿里云OSS文件上传失败", e);
            throw new BusinessException("上传失败,请联系系统管理员");
        }
    }

    @Override
    public InputStream download(String filePath) {
        try {
            return ossClient.getObject(config.getBucketName(), filePath).getObjectContent();
        } catch (Exception e) {
            log.error("获取阿里云OSS文件流失败: {}", filePath, e);
            throw new BusinessException("下载失败,请联系系统管理员");
        }
    }

    @Override
    public boolean delete(String filePath) {
        try {
            ossClient.deleteObject(config.getBucketName(), filePath);
            return true;
        } catch (Exception e) {
            log.error("删除阿里云OSS文件失败: {}", filePath, e);
            return false;
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        return "https://" + config.getBucketName() + "." +
                config.getEndpoint().replace("https://", "") + FileConstants.SEPARATOR + filePath;
    }

    @Override
    public boolean exists(String filePath) {
        try {
            return ossClient.doesObjectExist(config.getBucketName(), filePath);
        } catch (Exception e) {
            log.error("检查阿里云OSS文件是否存在失败: {}", filePath, e);
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
                    HttpMethod.PUT
            );
            request.setExpiration(expirationDate);
            request.setContentType("application/octet-stream");

            return ossClient.generatePresignedUrl(request).toString();
        } catch (Exception e) {
            log.error("生成阿里云OSS预签名上传URL失败: {}", filePath, e);
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
                    HttpMethod.GET
            );
            request.setExpiration(expirationDate);

            return ossClient.generatePresignedUrl(request).toString();
        } catch (Exception e) {
            log.error("生成阿里云OSS临时下载URL失败: {}", filePath, e);
            throw new BusinessException("下载失败,请联系系统管理员");
        }
    }
}

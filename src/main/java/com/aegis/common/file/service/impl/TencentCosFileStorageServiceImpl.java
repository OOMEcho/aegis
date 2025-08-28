package com.aegis.common.file.service.impl;

import com.aegis.common.constant.FileConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.FileUploadProperties;
import com.aegis.common.file.FileUploadResult;
import com.aegis.common.file.StoragePlatform;
import com.aegis.common.file.service.AbstractFileStorageService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.DeleteObjectRequest;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/27 21:45
 * @Description: 腾讯云OSS文件存储服务实现
 */
@Slf4j
@Service(FileConstants.TENCENT)
@ConditionalOnProperty(prefix = "file.upload", name = "platform", havingValue = "tencent_cos")
public class TencentCosFileStorageServiceImpl extends AbstractFileStorageService {

    private final COSClient cosClient;

    public TencentCosFileStorageServiceImpl(FileUploadProperties properties, COSClient cosClient) {
        super(properties);
        this.cosClient = cosClient;
    }

    @Override
    public FileUploadResult upload(MultipartFile file, String directory) {
        try {
            FileUploadProperties.TencentConfig config = properties.getTencent();

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
                cosClient.putObject(putObjectRequest);
            }

            return buildFileUploadResult(file, fileName, objectName, fileBytes,
                    StoragePlatform.TENCENT_COS.name());

        } catch (Exception e) {
            log.error("腾讯云COS文件上传失败", e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public InputStream download(String filePath) {
        try {
            FileUploadProperties.TencentConfig config = properties.getTencent();
            return cosClient.getObject(new GetObjectRequest(config.getBucketName(), filePath)).getObjectContent();
        } catch (Exception e) {
            log.error("获取腾讯云COS文件流失败: {}", filePath, e);
            throw new BusinessException("获取文件流失败: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String filePath) {
        try {
            FileUploadProperties.TencentConfig config = properties.getTencent();
            cosClient.deleteObject(new DeleteObjectRequest(config.getBucketName(), filePath));
            return true;
        } catch (Exception e) {
            log.error("删除腾讯云COS文件失败: {}", filePath, e);
            return false;
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        FileUploadProperties.TencentConfig config = properties.getTencent();
        return "https://" + config.getBucketName() + ".cos." + config.getRegion() + ".myqcloud.com/" + filePath;
    }

    @Override
    public boolean exists(String filePath) {
        try {
            FileUploadProperties.TencentConfig config = properties.getTencent();
            return cosClient.doesObjectExist(config.getBucketName(), filePath);
        } catch (Exception e) {
            return false;
        }
    }
}

package com.aegis.common.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.aegis.common.constant.Constants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.FileUploadProperties;
import com.aegis.common.file.FileUploadResult;
import com.aegis.common.file.StoragePlatform;
import com.aegis.common.file.service.FileStorageService;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/27 21:43
 * @Description: Minio文件存储服务实现
 */
@Slf4j
@Service("minioFileStorageService")
@RequiredArgsConstructor
public class MinioFileStorageServiceImpl implements FileStorageService {

    private final FileUploadProperties properties;

    private final MinioClient minioClient;

    @Override
    public FileUploadResult upload(MultipartFile file, String directory) {
        try {
            FileUploadProperties.MinioConfig config = properties.getMinio();

            String originalFileName = file.getOriginalFilename();
            String extension = FileUtil.extName(originalFileName);
            String fileName = IdUtil.simpleUUID() + Constants.POINT + extension;

            String objectName = (StrUtil.isNotBlank(directory) ? directory + Constants.SEPARATOR : "")
                    + Constants.FILE_FOLDER + Constants.SEPARATOR + fileName;

            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(config.getBucketName())
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            String md5 = DigestUtil.md5Hex(file.getInputStream());

            return FileUploadResult.builder()
                    .fileName(fileName)
                    .originalFileName(originalFileName)
                    .suffix(extension)
                    .filePath(objectName)
                    .fileUrl(getFileUrl(objectName))
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .platform(StoragePlatform.MINIO.name())
                    .uploadTime(LocalDateTime.now())
                    .md5(md5)
                    .build();

        } catch (Exception e) {
            log.error("MinIO文件上传失败", e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public InputStream download(String filePath) {
        try {
            FileUploadProperties.MinioConfig config = properties.getMinio();
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
            FileUploadProperties.MinioConfig config = properties.getMinio();
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
        FileUploadProperties.MinioConfig config = properties.getMinio();
        return config.getEndpoint() + Constants.SEPARATOR + config.getBucketName() + Constants.SEPARATOR + filePath;
    }

    @Override
    public boolean exists(String filePath) {
        try {
            FileUploadProperties.MinioConfig config = properties.getMinio();
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

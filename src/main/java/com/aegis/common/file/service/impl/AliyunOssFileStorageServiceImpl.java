package com.aegis.common.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.aegis.common.constant.Constants;
import com.aegis.common.constant.FileConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.FileUploadProperties;
import com.aegis.common.file.FileUploadResult;
import com.aegis.common.file.StoragePlatform;
import com.aegis.common.file.service.FileStorageService;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/27 21:24
 * @Description: 阿里云OSS文件存储服务实现
 */
@Slf4j
@Service(FileConstants.ALIYUN)
@RequiredArgsConstructor
public class AliyunOssFileStorageServiceImpl implements FileStorageService {

    private final FileUploadProperties properties;

    private final OSS ossClient;

    @Override
    public FileUploadResult upload(MultipartFile file, String directory) {
        try {
            FileUploadProperties.AliyunConfig config = properties.getAliyun();

            String originalFileName = file.getOriginalFilename();
            String extension = FileUtil.extName(originalFileName);
            String fileName = IdUtil.simpleUUID() + Constants.POINT + extension;

            String objectName = (StrUtil.isNotBlank(directory) ? directory + Constants.SEPARATOR : "")
                    + Constants.FILE_FOLDER + Constants.SEPARATOR + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            ossClient.putObject(config.getBucketName(), objectName, file.getInputStream(), metadata);

            String md5 = DigestUtil.md5Hex(file.getInputStream());

            return FileUploadResult.builder()
                    .fileName(fileName)
                    .originalFileName(file.getOriginalFilename())
                    .suffix(extension)
                    .filePath(objectName)
                    .fileUrl(getFileUrl(objectName))
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .platform(StoragePlatform.ALIYUN_OSS.name())
                    .uploadTime(LocalDateTime.now())
                    .md5(md5)
                    .build();

        } catch (Exception e) {
            log.error("阿里云OSS文件上传失败", e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public InputStream download(String filePath) {
        try {
            FileUploadProperties.AliyunConfig config = properties.getAliyun();
            return ossClient.getObject(config.getBucketName(), filePath).getObjectContent();
        } catch (Exception e) {
            log.error("获取阿里云OSS文件流失败: {}", filePath, e);
            throw new BusinessException("获取文件流失败: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String filePath) {
        try {
            FileUploadProperties.AliyunConfig config = properties.getAliyun();
            ossClient.deleteObject(config.getBucketName(), filePath);
            return true;
        } catch (Exception e) {
            log.error("删除阿里云OSS文件失败: {}", filePath, e);
            return false;
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        FileUploadProperties.AliyunConfig config = properties.getAliyun();
        return "https://" + config.getBucketName() + "." +
                config.getEndpoint().replace("https://", "") + Constants.SEPARATOR + filePath;
    }

    @Override
    public boolean exists(String filePath) {
        try {
            FileUploadProperties.AliyunConfig config = properties.getAliyun();
            return ossClient.doesObjectExist(config.getBucketName(), filePath);
        } catch (Exception e) {
            log.error("检查阿里云OSS文件是否存在失败: {}", filePath, e);
            return false;
        }
    }
}

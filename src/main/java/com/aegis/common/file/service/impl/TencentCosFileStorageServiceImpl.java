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
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.DeleteObjectRequest;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/27 21:45
 * @Description: 腾讯云OSS文件存储服务实现
 */
@Slf4j
@Service(FileConstants.TENCENT)
@RequiredArgsConstructor
public class TencentCosFileStorageServiceImpl implements FileStorageService {

    private final FileUploadProperties properties;

    private final COSClient cosClient;

    @Override
    public FileUploadResult upload(MultipartFile file, String directory) {
        try {
            FileUploadProperties.TencentConfig config = properties.getTencent();

            String originalFileName = file.getOriginalFilename();
            String extension = FileUtil.extName(originalFileName);
            String fileName = IdUtil.simpleUUID() + Constants.POINT + extension;

            String objectName = (StrUtil.isNotBlank(directory) ? directory + Constants.SEPARATOR : "")
                    + Constants.FILE_FOLDER + Constants.SEPARATOR + fileName;

            // 上传文件
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    config.getBucketName(),
                    objectName,
                    file.getInputStream(),
                    new ObjectMetadata()
            );
            putObjectRequest.getMetadata().setContentLength(file.getSize());
            putObjectRequest.getMetadata().setContentType(file.getContentType());

            cosClient.putObject(putObjectRequest);

            String md5 = DigestUtil.md5Hex(file.getInputStream());

            return FileUploadResult.builder()
                    .fileName(fileName)
                    .originalFileName(originalFileName)
                    .suffix(extension)
                    .filePath(objectName)
                    .fileUrl(getFileUrl(objectName))
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .platform(StoragePlatform.TENCENT_COS.name())
                    .uploadTime(LocalDateTime.now())
                    .md5(md5)
                    .build();

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

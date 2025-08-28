package com.aegis.common.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
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
}

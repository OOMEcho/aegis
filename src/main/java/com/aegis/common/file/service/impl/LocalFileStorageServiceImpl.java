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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/27 21:24
 * @Description: 本地文件存储服务实现
 */
@Slf4j
@Service(FileConstants.LOCAL)
@RequiredArgsConstructor
public class LocalFileStorageServiceImpl implements FileStorageService {

    private final FileUploadProperties properties;

    @Override
    public FileUploadResult upload(MultipartFile file, String directory) {
        try {
            String basePath = properties.getLocal().getPath();
            String fullDirectory = basePath + Constants.SEPARATOR + (StrUtil.isNotBlank(directory) ? directory + Constants.SEPARATOR : "") + Constants.FILE_FOLDER;

            // 确保目录存在
            FileUtil.mkdir(fullDirectory);

            // 生成唯一文件名
            String originalFileName = file.getOriginalFilename();
            String extension = FileUtil.extName(originalFileName);
            String fileName = IdUtil.simpleUUID() + Constants.POINT+ extension;

            // 完整文件路径
            String filePath = fullDirectory + Constants.SEPARATOR + fileName;
            File destFile = new File(filePath);

            // 保存文件
            file.transferTo(destFile);

            // 计算MD5
            String md5 = DigestUtil.md5Hex(file.getInputStream());

            return FileUploadResult.builder()
                    .fileName(fileName)
                    .originalFileName(originalFileName)
                    .suffix(extension)
                    .filePath(filePath)
                    .fileUrl(getFileUrl(filePath))
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .platform(StoragePlatform.LOCAL.name())
                    .uploadTime(LocalDateTime.now())
                    .md5(md5)
                    .build();

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
        String basePath = properties.getLocal().getPath();
        return filePath.replace(basePath, "/files");
    }

    @Override
    public boolean exists(String filePath) {
        return FileUtil.exist(filePath);
    }
}

package com.aegis.common.file.service.impl;

import com.aegis.common.file.FileUploadResult;
import com.aegis.common.file.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/27 21:24
 * @Description: 阿里云OSS文件存储服务实现
 */
@Slf4j
@Service("aliyunOssFileStorageService")
@RequiredArgsConstructor
public class AliyunOssFileStorageServiceImpl implements FileStorageService {

    @Override
    public FileUploadResult upload(MultipartFile file, String directory) {
        return null;
    }

    @Override
    public InputStream download(String filePath) {
        return null;
    }

    @Override
    public boolean delete(String filePath) {
        return false;
    }

    @Override
    public String getFileUrl(String filePath) {
        return "";
    }

    @Override
    public boolean exists(String filePath) {
        return false;
    }
}

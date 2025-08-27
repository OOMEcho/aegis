package com.aegis.common.file;

import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/27 21:24
 * @Description: 文件存储服务工厂
 */
@Service
@RequiredArgsConstructor
public class FileStorageServiceFactory {

    private final FileUploadProperties properties;

    private final Map<String, FileStorageService> fileStorageServices;

    public FileStorageService getFileStorageService() {
        return getFileStorageService(properties.getPlatform());
    }

    public FileStorageService getFileStorageService(StoragePlatform platform) {
        String serviceName = getServiceName(platform);
        FileStorageService service = fileStorageServices.get(serviceName);
        if (service == null) {
            throw new BusinessException("不支持的存储平台: " + platform);
        }
        return service;
    }

    private String getServiceName(StoragePlatform platform) {
        switch (platform) {
            case LOCAL:
                return "localFileStorageService";
            case MINIO:
                return "minioFileStorageService";
            case ALIYUN_OSS:
                return "aliyunOssFileStorageService";
            case TENCENT_COS:
                return "tencentCosFileStorageService";
            default:
                throw new BusinessException("不支持的存储平台: " + platform);
        }
    }
}

package com.aegis.common.file;

import lombok.Getter;

/**
 * 存储平台枚举
 *
 * @author xuesong.lei
 * @since 2025/08/27 21:24
 */
@Getter
public enum StoragePlatform {

    LOCAL("本地存储"),
    MINIO("MinIO"),
    ALIYUN_OSS("阿里云OSS"),
    TENCENT_COS("腾讯云COS");

    private final String description;

    StoragePlatform(String description) {
        this.description = description;
    }
}

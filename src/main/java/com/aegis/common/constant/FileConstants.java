package com.aegis.common.constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/28 11:05
 * @Description: 文件常量
 */
public class FileConstants {

    /**
     * 本地存储
     */
    public static final String LOCAL = "localFileStorageService";

    /**
     * MinIO存储
     */
    public static final String MINIO = "minioFileStorageService";

    /**
     * 阿里云存储
     */
    public static final String ALIYUN = "aliyunFileStorageService";

    /**
     * 腾讯云存储
     */
    public static final String TENCENT = "tencentCosFileStorageService";

    /**
     * 分隔符
     */
    public static final String SEPARATOR = "/";

    /**
     * 点
     */
    public static final String POINT = ".";

    /**
     * 文件存储目录
     * 按照日期进行分类存储，格式为yyyy-MM-dd
     */
    public static final String FILE_FOLDER = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    /**
     * 允许的文件类型
     */
    public static final Set<String> ALLOWED_EXTENSIONS  = Stream.of(
            "jpg", "jpeg", "png", "gif", "bmp", "webp",  // 图片
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",  // 文档
            "txt", "md", "csv", "json", "xml",  // 文本
            "mp4", "avi", "mov", "wmv", "flv",  // 视频
            "mp3", "wav", "flac", "aac"  // 音频
    ).collect(Collectors.toSet());

    /**
     * 危险文件类型黑名单
     */
    public static final Set<String> DANGEROUS_EXTENSIONS = Stream.of(
            "exe", "bat", "cmd", "com", "pif", "scr", "vbs", "js", "jar", "sh"
    ).collect(Collectors.toSet());

    /**
     * 文件头魔数验证 - 防止文件类型伪造
     */
    public static final Map<String, byte[]> FILE_SIGNATURES;

    static {
        Map<String, byte[]> map = new HashMap<>();
        map.put("jpg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF});
        map.put("png", new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47});
        map.put("pdf", new byte[]{0x25, 0x50, 0x44, 0x46});
        map.put("zip", new byte[]{0x50, 0x4B, 0x03, 0x04});
        FILE_SIGNATURES = Collections.unmodifiableMap(map);
    }

    public static final String[]  MALICIOUS_PATTERNS= {
            "<script", "javascript:", "vbscript:", "onload=", "onerror=",
            "<?php", "<%", "#!/bin/sh", "#!/bin/bash"
    };
}

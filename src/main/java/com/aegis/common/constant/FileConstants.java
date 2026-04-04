package com.aegis.common.constant;

import cn.hutool.core.util.StrUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文件常量
 *
 * @author xuesong.lei
 * @since 2025-08-28
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
    public static String getFileFolder(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

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
     * 支持预览的图片后缀
     */
    public static final Set<String> IMAGE_EXTENSIONS = Stream.of(
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
    ).collect(Collectors.toSet());

    /**
     * 图片后缀对应的标准Content-Type
     */
    public static final Map<String, String> IMAGE_CONTENT_TYPES;

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

        Map<String, String> imageContentTypes = new HashMap<>();
        imageContentTypes.put("jpg", "image/jpeg");
        imageContentTypes.put("jpeg", "image/jpeg");
        imageContentTypes.put("png", "image/png");
        imageContentTypes.put("gif", "image/gif");
        imageContentTypes.put("bmp", "image/bmp");
        imageContentTypes.put("webp", "image/webp");
        IMAGE_CONTENT_TYPES = Collections.unmodifiableMap(imageContentTypes);
    }

    public static final String[]  MALICIOUS_PATTERNS= {
            "<script", "javascript:", "vbscript:", "onload=", "onerror=",
            "<?php", "<%", "#!/bin/sh", "#!/bin/bash"
    };

    /**
     * 校验并清洗目录参数
     * @return 校验后的目录字符串，如果为空则返回 null
     */
    public static String sanitizeDirectory(String directory) {
        if (directory == null || directory.trim().isEmpty()) {
            return null;
        }
        directory = directory.trim();
        // 去除首尾分隔符
        if (directory.startsWith(SEPARATOR)) {
            directory = directory.substring(1);
        }
        if (directory.endsWith(SEPARATOR)) {
            directory = directory.substring(0, directory.length() - 1);
        }
        if (directory.isEmpty()) {
            return null;
        }
        // 禁止路径遍历和反斜杠
        if (directory.contains("..") || directory.contains("\\")) {
            throw new IllegalArgumentException("目录参数包含非法字符");
        }
        // 白名单：只允许字母、数字、中划线、下划线、正斜杠
        if (!directory.matches("^[a-zA-Z0-9_\\-/]+$")) {
            throw new IllegalArgumentException("目录名称只允许包含字母、数字、中划线、下划线");
        }
        String[] segments = directory.split(SEPARATOR);
        for (String segment : segments) {
            if (segment.isEmpty()) {
                throw new IllegalArgumentException("目录路径格式不正确");
            }
            if (segment.length() > 64) {
                throw new IllegalArgumentException("单级目录名称不能超过64个字符");
            }
        }
        if (segments.length > 5) {
            throw new IllegalArgumentException("目录层级不能超过5层");
        }
        return directory;
    }

    public static String normalizeExtension(String suffix) {
        if (StrUtil.isBlank(suffix)) {
            return "";
        }
        return StrUtil.removePrefix(suffix.trim().toLowerCase(), POINT);
    }

    public static boolean isImageExtension(String suffix) {
        return IMAGE_EXTENSIONS.contains(normalizeExtension(suffix));
    }

    public static String getImageContentType(String suffix) {
        return IMAGE_CONTENT_TYPES.get(normalizeExtension(suffix));
    }
}

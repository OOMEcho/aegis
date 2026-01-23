package com.aegis.utils;

import com.aegis.common.constant.FileConstants;
import com.aegis.common.exception.BusinessException;

import java.util.regex.Pattern;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/23 15:42
 * @Description: 路径工具类
 */
public final class PathUtil {

    private PathUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // 正则：只允许 / 开头，后面可有字母数字、下划线、横杠、单斜杠，最多允许末尾 /** 前缀
    private static final Pattern VALID_PATH_PATTERN = Pattern.compile("^(/[a-zA-Z0-9_-]+)*(?:/\\*{2})?$");

    /**
     * 校验并规范化资源路径
     *
     * @param path 用户输入的路径
     * @return 规范化后的路径
     */
    public static String validateAndNormalize(String path) {
        // 去掉首尾空格，合并连续斜杠
        path = path.trim().replaceAll("/+", FileConstants.SEPARATOR);

        // 保证以 / 开头
        if (!path.startsWith(FileConstants.SEPARATOR)) {
            path = FileConstants.SEPARATOR + path;
        }

        // 禁止全路径通配符 /* 或 /** 或 /*** 等
        if (path.matches("/\\*+")) {
            throw new BusinessException("禁止使用全路径通配符");
        }

        // 校验合法字符和合法前缀通配符
        if (!VALID_PATH_PATTERN.matcher(path).matches()) {
            throw new BusinessException("路径包含非法字符或通配符位置不正确");
        }

        // 删除尾部多余斜杠（保留 /**）
        if (path.endsWith(FileConstants.SEPARATOR) && !path.endsWith("/**")) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }
}

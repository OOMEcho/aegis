package com.aegis.utils;

import cn.hutool.json.JSONUtil;
import com.aegis.common.result.Result;
import com.aegis.common.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 响应工具类
 *
 * @author xuesong.lei
 * @since 2025/9/4 22:55
 */
@Slf4j
public final class ResponseUtils {

    private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    private ResponseUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 设置Excel文件响应头
     *
     * @param response HttpServletResponse
     */
    public static void setExcelResponse(HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + System.currentTimeMillis() + ".xlsx");
    }

    public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()) + "\"");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 输出统一响应体
     *
     * @param response HttpServletResponse
     * @param result   响应结果对象
     */
    public static void write(HttpServletResponse response, Object result) {
        response.setContentType(CONTENT_TYPE_JSON);
        try {
            String jsonStr = JSONUtil.toJsonStr(result);
            response.getWriter().write(jsonStr);
        } catch (IOException e) {
            log.error("Response write error", e);
        }
    }

    /**
     * 输出错误信息
     *
     * @param response       HttpServletResponse
     * @param resultCodeEnum 错误枚举
     */
    public static void writeError(HttpServletResponse response, ResultCodeEnum resultCodeEnum) {
        write(response, Result.error(resultCodeEnum));
    }

    /**
     * 输出错误信息，自定义错误消息
     *
     * @param response HttpServletResponse
     * @param message  错误消息
     */
    public static void writeError(HttpServletResponse response, String message) {
        write(response, Result.error(message));
    }

    /**
     * 输出成功信息
     *
     * @param response HttpServletResponse
     * @param data     返回数据
     */
    public static void writeSuccess(HttpServletResponse response, Object data) {
        write(response, Result.success(data));
    }
}

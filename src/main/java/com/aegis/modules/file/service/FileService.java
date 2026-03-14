package com.aegis.modules.file.service;

import com.aegis.common.file.StoragePlatform;
import com.aegis.modules.file.domain.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/13 9:48
 * @Description: 文件服务业务层
 */
public interface FileService {

    /**
     * 上传单个文件
     *
     * @param file      文件
     * @param directory 目录
     * @return 文件元数据
     */
    FileMetadata uploadFile(MultipartFile file, String directory);

    /**
     * 批量上传文件
     *
     * @param files     文件数组
     * @param directory 目录
     * @return 文件元数据列表
     */
    List<FileMetadata> uploadBatchFiles(MultipartFile[] files, String directory);

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @param response HttpServletResponse
     */
    void download(String filePath, HttpServletResponse response);

    /**
     * 本地文件下载只能通过临时URL下载
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    void localDownload(HttpServletRequest request, HttpServletResponse response);

    /**
     * 文件公开预览（按文件ID）
     *
     * @param id       文件ID
     * @param response HttpServletResponse
     */
    void preview(Long id, HttpServletResponse response);

    /**
     * 指定存储平台上传文件
     *
     * @param platform  存储平台
     * @param file      文件
     * @param directory 目录
     * @return 文件元数据
     */
    FileMetadata uploadFileWithPlatform(StoragePlatform platform, MultipartFile file, String directory);

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 删除结果，成功返回文件路径，失败返回null
     */
    String deleteFile(String filePath);

    /**
     * 获取预签名上传URL
     *
     * @param fileName  文件名
     * @param directory 目录
     * @return 包含上传URL和必要表单字段的Map
     */
    Map<String, String> getPresignedUploadUrl(String fileName, String directory);

    /**
     * 获取文件的临时下载链接
     *
     * @param filePath          文件路径
     * @param expirationSeconds 链接过期时间，单位秒
     * @return 包含下载URL和过期时间的Map
     */
    Map<String, String> getTemporaryDownloadUrl(String filePath, long expirationSeconds);
}

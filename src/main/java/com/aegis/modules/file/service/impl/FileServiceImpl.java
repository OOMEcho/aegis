package com.aegis.modules.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.aegis.common.constant.FileConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.FileStorageServiceFactory;
import com.aegis.common.file.StoragePlatform;
import com.aegis.common.file.service.FileStorageService;
import com.aegis.modules.file.domain.entity.FileMetadata;
import com.aegis.modules.file.mapper.FileMetadataMapper;
import com.aegis.modules.file.service.FileService;
import com.aegis.utils.ResponseUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/13 9:48
 * @Description: 文件服务业务实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileStorageServiceFactory fileStorageServiceFactory;

    private final FileMetadataMapper fileMetadataMapper;

    @Value("${file.upload.local.path}")
    private String basePath;

    @Value("${file.upload.local.secret-key}")
    private String secretKey;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileMetadata uploadFile(MultipartFile file, String directory) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();
        FileMetadata result = storageService.upload(file, directory);
        log.info("文件上传成功: {}", result.getFileName());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FileMetadata> uploadBatchFiles(MultipartFile[] files, String directory) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();
        List<FileMetadata> results = Arrays.stream(files)
                .map(file -> storageService.upload(file, directory))
                .collect(Collectors.toList());
        log.info("批量文件上传成功，共{}个文件", results.size());
        return results;
    }

    @Override
    public void download(String filePath, HttpServletResponse response) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();

        final String fileName = getFileNameByFilePatch(filePath);

        ResponseUtils.setFileDownloadHeader(response, fileName);

        try (InputStream inputStream = storageService.download(filePath);
             ServletOutputStream outputStream = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
        } catch (Exception e) {
            log.error("文件下载失败->{}", e.getMessage());
            throw new BusinessException("文件下载失败");
        }
    }

    @Override
    public void localDownload(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();
        String token = request.getParameter("token");
        String expires = request.getParameter("expires");

        // 检查过期时间
        long now = System.currentTimeMillis();
        if (Long.parseLong(expires) < now) {
            throw new BusinessException("链接已过期");
        }

        // 检查签名
        String filePath = basePath + uri.replace("/file/localDownload", "");
        String expectedToken = SecureUtil.hmacSha256(secretKey).digestHex(filePath + expires);
        if (!expectedToken.equals(token)) {
            throw new BusinessException("无效链接");
        }

        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();

        final String fileName = getFileNameByFilePatch(filePath);

        ResponseUtils.setFileDownloadHeader(response, fileName);

        try (InputStream inputStream = storageService.download(filePath);
             ServletOutputStream outputStream = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
        } catch (Exception e) {
            log.error("本地文件临时下载失败->{}", e.getMessage());
            throw new BusinessException("文件下载失败");
        }
    }

    @Override
    public FileMetadata uploadFileWithPlatform(StoragePlatform platform, MultipartFile file, String directory) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService(platform);
        FileMetadata result = storageService.upload(file, directory);
        log.info("文件上传成功到{}: {}", platform.getDescription(), result.getFileName());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteFile(String filePath) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();
        storageService.delete(filePath);

        // 同步删除文件元数据记录
        LambdaQueryWrapper<FileMetadata> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileMetadata::getFilePath, filePath);
        fileMetadataMapper.delete(queryWrapper);

        return filePath;
    }

    @Override
    public Map<String, String> getPresignedUploadUrl(String fileName, String directory) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();

        String filePath = buildFilePath(directory, fileName);
        Duration expiration = Duration.ofMinutes(10); // 10分钟过期

        String presignedUrl = storageService.generatePresignedUploadUrl(filePath, expiration);

        Map<String, String> response = new HashMap<>();
        response.put("uploadUrl", presignedUrl);
        response.put("filePath", filePath);
        response.put("expiresIn", "600"); // 秒
        return response;
    }

    @Override
    public Map<String, String> getTemporaryDownloadUrl(String filePath, long expirationSeconds) {
        if (expirationSeconds > 60) {
            throw new BusinessException("过期时间不能超过60秒");
        }

        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();

        if (!storageService.exists(filePath)) {
            return null;
        }

        Duration expiration = Duration.ofSeconds(expirationSeconds);
        String temporaryUrl = storageService.getTemporaryDownloadUrl(filePath, expiration);

        Map<String, String> response = new HashMap<>();
        response.put("downloadUrl", temporaryUrl);
        response.put("expiresIn", String.valueOf(expirationSeconds));

        return response;
    }

    private String buildFilePath(String directory, String fileName) {
        if (StrUtil.isNotBlank(directory)) {
            directory = directory.trim();
            if (directory.contains("..") || directory.contains("\\") ||
                    !directory.matches("^[a-zA-Z0-9_\\-/]+$")) {
                throw new BusinessException("目录参数包含非法字符");
            }
        }
        String uniqueFileName = IdUtil.simpleUUID() + FileConstants.POINT + FileUtil.extName(fileName);
        return (StrUtil.isNotBlank(directory) ? directory + FileConstants.SEPARATOR : "")
                + FileConstants.getFileFolder() + FileConstants.SEPARATOR + uniqueFileName;
    }

    private String getFileNameByFilePatch(String filePath) {
        LambdaQueryWrapper<FileMetadata> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileMetadata::getFilePath, filePath);
        FileMetadata fileMetadata = fileMetadataMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNull(fileMetadata)) {
            throw new BusinessException("文件不存在");
        }
        return fileMetadata.getOriginalFileName();
    }
}

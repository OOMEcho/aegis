package com.aegis.modules.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.aegis.common.domain.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.aegis.common.constant.FileConstants;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.FileStorageServiceFactory;
import com.aegis.common.file.StoragePlatform;
import com.aegis.common.file.config.FileUploadProperties;
import com.aegis.common.file.service.FileStorageService;
import com.aegis.modules.file.domain.dto.FileMetadataPageDTO;
import com.aegis.modules.file.domain.dto.PresignedUploadCompleteDTO;
import com.aegis.modules.file.domain.entity.FileMetadata;
import com.aegis.modules.file.mapper.FileMetadataMapper;
import com.aegis.modules.file.service.FileService;
import com.aegis.utils.PageUtils;
import com.aegis.utils.ResponseUtils;
import com.aegis.utils.SecurityUtils;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件服务业务实现层
 *
 * @author xuesong.lei
 * @since 2025-09-13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileStorageServiceFactory fileStorageServiceFactory;

    private final FileMetadataMapper fileMetadataMapper;

    private final FileUploadProperties fileUploadProperties;

    @Value("${file.upload.local.path}")
    private String basePath;

    @Value("${file.upload.local.secret-key}")
    private String secretKey;

    @Override
    public PageVO<FileMetadata> pageList(FileMetadataPageDTO dto) {
        LambdaQueryWrapper<FileMetadata> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(dto.getOriginalFileName()), FileMetadata::getOriginalFileName, dto.getOriginalFileName())
                .eq(StrUtil.isNotBlank(dto.getPlatform()), FileMetadata::getPlatform, dto.getPlatform())
                .like(StrUtil.isNotBlank(dto.getContentType()), FileMetadata::getContentType, dto.getContentType())
                .orderByDesc(FileMetadata::getUploadTime);

        return PageUtils.of(dto).pagingAndConvert(fileMetadataMapper, queryWrapper, metadata -> {
            populateAccessUrl(metadata);
            return metadata;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileMetadata uploadFile(MultipartFile file, String directory) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();
        FileMetadata result = storageService.upload(file, directory);
        populateAccessUrl(result);
        log.info("文件上传成功: {}", result.getFileName());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FileMetadata> uploadBatchFiles(MultipartFile[] files, String directory) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();
        List<FileMetadata> results = Arrays.stream(files).map(file -> {
            FileMetadata metadata = storageService.upload(file, directory);
            populateAccessUrl(metadata);
            return metadata;
        }).collect(Collectors.toList());
        log.info("批量文件上传成功，共{}个文件", results.size());
        return results;
    }

    @Override
    public void download(String filePath, HttpServletResponse response) {
        FileMetadata fileMetadata = getMetadataByFilePath(filePath);
        FileStorageService storageService = resolveStorageService(fileMetadata.getPlatform());

        final String fileName = fileMetadata.getOriginalFileName();

        ResponseUtils.setFileDownloadHeader(response, fileName);

        try (InputStream inputStream = storageService.download(fileMetadata.getFilePath()); ServletOutputStream outputStream = response.getOutputStream()) {

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
        long expiresTime;
        try {
            expiresTime = Long.parseLong(expires);
        } catch (NumberFormatException e) {
            throw new BusinessException("无效链接");
        }
        if (expiresTime < now) {
            throw new BusinessException("链接已过期");
        }

        // 检查签名
        String filePath = basePath + uri.replace("/file/localDownload", "");
        String expectedToken = SecureUtil.hmacSha256(secretKey).digestHex(filePath + expires);
        if (!expectedToken.equals(token)) {
            throw new BusinessException("无效链接");
        }

        FileMetadata fileMetadata = getMetadataByFilePath(filePath);
        if (!StoragePlatform.LOCAL.name().equals(fileMetadata.getPlatform())) {
            throw new BusinessException("无效链接");
        }

        FileStorageService storageService = resolveStorageService(fileMetadata.getPlatform());
        final String fileName = fileMetadata.getOriginalFileName();

        ResponseUtils.setFileDownloadHeader(response, fileName);

        try (InputStream inputStream = storageService.download(fileMetadata.getFilePath());
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
    public void preview(Long id, HttpServletResponse response) {
        if (ObjectUtils.isNull(id)) {
            throw new BusinessException("无效链接");
        }

        FileMetadata fileMetadata = fileMetadataMapper.selectById(id);
        if (ObjectUtils.isNull(fileMetadata)) {
            throw new BusinessException("文件不存在");
        }

        String suffix = FileConstants.normalizeExtension(fileMetadata.getSuffix());
        if (!FileConstants.isImageExtension(suffix)) {
            throw new BusinessException("不支持预览的文件类型");
        }
        String contentType = FileConstants.getImageContentType(suffix);
        if (StrUtil.isBlank(contentType)) {
            throw new BusinessException("不支持预览的文件类型");
        }

        StoragePlatform platform;
        try {
            platform = StoragePlatform.valueOf(fileMetadata.getPlatform());
        } catch (Exception e) {
            throw new BusinessException("文件存储平台不支持");
        }

        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService(platform);

        response.setContentType(contentType);
        response.setHeader("Cache-Control", "public, max-age=3600");

        try (InputStream inputStream = storageService.download(fileMetadata.getFilePath()); ServletOutputStream outputStream = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        } catch (Exception e) {
            log.error("文件预览失败->{}", e.getMessage());
            throw new BusinessException("文件预览失败");
        }
    }

    @Override
    public FileMetadata uploadFileWithPlatform(StoragePlatform platform, MultipartFile file, String directory) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService(platform);
        FileMetadata result = storageService.upload(file, directory);
        populateAccessUrl(result);
        log.info("文件上传成功到{}: {}", platform.getDescription(), result.getFileName());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteFile(String filePath) {
        FileMetadata fileMetadata = getMetadataByFilePath(filePath);
        FileStorageService storageService = resolveStorageService(fileMetadata.getPlatform());

        storageService.delete(fileMetadata.getFilePath());
        fileMetadataMapper.deleteById(fileMetadata.getId());

        return fileMetadata.getFilePath();
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
    @Transactional(rollbackFor = Exception.class)
    public FileMetadata completePresignedUpload(PresignedUploadCompleteDTO dto) {
        String filePath = dto.getFilePath().trim();

        LambdaQueryWrapper<FileMetadata> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileMetadata::getFilePath, filePath);
        FileMetadata existed = fileMetadataMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotNull(existed)) {
            populateAccessUrl(existed);
            return existed;
        }

        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();
        if (!storageService.exists(filePath)) {
            throw new BusinessException("文件不存在或上传未完成");
        }

        Long fileSize = dto.getFileSize();
        if (ObjectUtils.isNull(fileSize) || fileSize < 0) {
            throw new BusinessException("文件大小参数无效");
        }

        String originalFileName = dto.getOriginalFileName().trim();
        String suffix = FileUtil.extName(originalFileName);
        if (StrUtil.isBlank(suffix)) {
            suffix = FileUtil.extName(filePath);
        }

        FileMetadata metadata = new FileMetadata()
                .setCreateBy(SecurityUtils.getUserId())
                .setFileName(FileUtil.getName(filePath))
                .setOriginalFileName(originalFileName)
                .setSuffix(suffix)
                .setFilePath(filePath)
                .setFileSize(fileSize)
                .setContentType(StrUtil.blankToDefault(dto.getContentType(), "application/octet-stream"))
                .setPlatform(fileUploadProperties.getPlatform().name())
                .setUploadTime(new Date());

        fileMetadataMapper.insert(metadata);
        populateAccessUrl(metadata);
        return metadata;
    }

    @Override
    public Map<String, String> getTemporaryDownloadUrl(String filePath, long expirationSeconds) {
        if (expirationSeconds > 60) {
            throw new BusinessException("过期时间不能超过60秒");
        }

        FileMetadata fileMetadata = getMetadataByFilePath(filePath);
        FileStorageService storageService = resolveStorageService(fileMetadata.getPlatform());

        if (!storageService.exists(fileMetadata.getFilePath())) {
            throw new BusinessException("文件不存在");
        }

        Duration expiration = Duration.ofSeconds(expirationSeconds);
        String temporaryUrl = storageService.getTemporaryDownloadUrl(fileMetadata.getFilePath(), expiration);
        temporaryUrl = normalizePublicDownloadUrl(temporaryUrl);

        Map<String, String> response = new HashMap<>();
        response.put("downloadUrl", temporaryUrl);
        response.put("expiresIn", String.valueOf(expirationSeconds));

        return response;
    }

    private String buildFilePath(String directory, String fileName) {
        if (StrUtil.isNotBlank(directory)) {
            directory = directory.trim();
            if (directory.contains("..") || directory.contains("\\") || !directory.matches("^[a-zA-Z0-9_\\-/]+$")) {
                throw new BusinessException("目录参数包含非法字符");
            }
        }
        String uniqueFileName = IdUtil.simpleUUID() + FileConstants.POINT + FileUtil.extName(fileName);
        return (StrUtil.isNotBlank(directory) ? directory + FileConstants.SEPARATOR : "") + FileConstants.getFileFolder() + FileConstants.SEPARATOR + uniqueFileName;
    }

    private FileMetadata getMetadataByFilePath(String filePath) {
        LambdaQueryWrapper<FileMetadata> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FileMetadata::getFilePath, filePath);
        FileMetadata fileMetadata = fileMetadataMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNull(fileMetadata)) {
            throw new BusinessException("文件不存在");
        }
        return fileMetadata;
    }

    private FileStorageService resolveStorageService(String platform) {
        StoragePlatform storagePlatform;
        try {
            storagePlatform = StoragePlatform.valueOf(platform);
        } catch (Exception e) {
            throw new BusinessException("文件存储平台不支持");
        }
        return fileStorageServiceFactory.getFileStorageService(storagePlatform);
    }

    private void populateAccessUrl(FileMetadata metadata) {
        if (ObjectUtils.isNull(metadata) || ObjectUtils.isNull(metadata.getId())) {
            return;
        }
        metadata.setAccessUrl(buildAccessUrl(metadata.getId()));
    }

    private String buildAccessUrl(Long fileId) {
        String publicBaseUrl = fileUploadProperties.getPublicBaseUrl();
        if (StrUtil.isBlank(publicBaseUrl)) {
            throw new BusinessException("请先配置 file.upload.public-base-url");
        }
        String baseUrl = StrUtil.removeSuffix(publicBaseUrl.trim(), "/");
        return baseUrl + "/file/preview/" + fileId;
    }

    private String normalizePublicDownloadUrl(String downloadUrl) {
        if (StrUtil.isBlank(downloadUrl)) {
            return downloadUrl;
        }
        String lowerUrl = downloadUrl.trim().toLowerCase();
        if (lowerUrl.startsWith("http://") || lowerUrl.startsWith("https://")) {
            return downloadUrl;
        }

        String publicBaseUrl = fileUploadProperties.getPublicBaseUrl();
        if (StrUtil.isBlank(publicBaseUrl)) {
            throw new BusinessException("请先配置 file.upload.public-base-url");
        }
        String baseUrl = StrUtil.removeSuffix(publicBaseUrl.trim(), "/");
        String normalizedPath = downloadUrl.startsWith(FileConstants.SEPARATOR)
                ? downloadUrl
                : FileConstants.SEPARATOR + downloadUrl;
        return baseUrl + normalizedPath;
    }
}

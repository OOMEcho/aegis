package com.aegis.modules.file.controller;

import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.FileStorageServiceFactory;
import com.aegis.common.file.FileUploadResult;
import com.aegis.common.file.StoragePlatform;
import com.aegis.common.file.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/28 21:24
 * @Description: 文件服务接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileServiceController {

    private final FileStorageServiceFactory fileStorageServiceFactory;

    @PostMapping("/upload")
    public FileUploadResult uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(value = "directory", required = false) String directory) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();
        FileUploadResult result = storageService.upload(file, directory);
        log.info("文件上传成功: {}", result.getFileName());
        return result;
    }

    @PostMapping("/upload/batch")
    public List<FileUploadResult> uploadFiles(@RequestParam("files") MultipartFile[] files, @RequestParam(value = "directory", required = false) String directory) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();
        List<FileUploadResult> results = Arrays.stream(files)
                .map(file -> storageService.upload(file, directory))
                .collect(Collectors.toList());
        log.info("批量文件上传成功，共{}个文件", results.size());
        return results;
    }

    @GetMapping("/download")
    public void download(@RequestParam("filePath") String filePath, HttpServletResponse response) throws Exception {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();

        // 从 filePath 解析文件名
        String fileName = Paths.get(filePath).getFileName().toString();

        try (InputStream inputStream = storageService.download(filePath);
             ServletOutputStream outputStream = response.getOutputStream()) {

            // 设置响应头
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8)) + "\"");
            // 不设置 Content-Length，浏览器会自动处理流结束

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
        } catch (Exception e) {
            throw new BusinessException("文件下载失败" + e);
        }
    }

    @PostMapping("/upload/{platform}")
    public FileUploadResult uploadFileWithPlatform(@PathVariable StoragePlatform platform, @RequestParam("file") MultipartFile file, @RequestParam(value = "directory", required = false) String directory) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService(platform);
        FileUploadResult result = storageService.upload(file, directory);
        log.info("文件上传成功到{}: {}", platform.getDescription(), result.getFileName());
        return result;
    }

    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam("filePath") String filePath) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();
        boolean deleted = storageService.delete(filePath);
        if (deleted) {
            log.info("文件删除成功: {}", filePath);
            return "文件删除成功";
        } else {
            return "文件删除失败";
        }
    }

    @GetMapping("/exists")
    public String checkFileExists(@RequestParam("filePath") String filePath) {
        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();
        boolean exists = storageService.exists(filePath);
        if (exists) {
            log.info("文件存在: {}", filePath);
            return "存在";
        } else {
            return "不存在";
        }
    }

    @GetMapping("/platforms")
    public List<StoragePlatform> getSupportedPlatforms() {
        return Arrays.asList(StoragePlatform.values());
    }
}

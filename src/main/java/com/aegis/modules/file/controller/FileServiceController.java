package com.aegis.modules.file.controller;

import com.aegis.common.duplicate.PreventDuplicateSubmit;
import com.aegis.common.file.StoragePlatform;
import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.limiter.RateLimiter;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.modules.file.domain.dto.FileMetadataPageDTO;
import com.aegis.modules.file.domain.dto.PresignedUploadCompleteDTO;
import com.aegis.modules.file.domain.entity.FileMetadata;
import com.aegis.modules.file.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 文件服务接口
 *
 * @author xuesong.lei
 * @since 2025/8/28 21:24
 */
@Slf4j
@RestController
@Api(tags = "文件服务接口")
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileServiceController {

    private final FileService fileService;

    @ApiOperation("文件分页列表")
    @GetMapping("/pageList")
    public PageVO<FileMetadata> pageList(FileMetadataPageDTO dto) {
        return fileService.pageList(dto);
    }

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    @PreventDuplicateSubmit
    @RateLimiter(time = 60, count = 20)
    @OperationLog(moduleTitle = "文件上传", businessType = BusinessType.IMPORT)
    public FileMetadata uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(value = "directory", required = false) String directory) {
        return fileService.uploadFile(file, directory);
    }

    @ApiOperation("批量文件上传")
    @PostMapping("/upload/batch")
    @PreventDuplicateSubmit
    @RateLimiter(time = 60, count = 10)
    @OperationLog(moduleTitle = "批量文件上传", businessType = BusinessType.IMPORT)
    public List<FileMetadata> uploadBatchFiles(@RequestParam("files") MultipartFile[] files, @RequestParam(value = "directory", required = false) String directory) {
        return fileService.uploadBatchFiles(files, directory);
    }

    @ApiOperation("获取临时下载URL")
    @GetMapping("/temporary-download-url")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "获取临时下载URL", businessType = BusinessType.EXPORT)
    public Map<String, String> getTemporaryDownloadUrl(@RequestParam String filePath, @RequestParam(required = false, defaultValue = "60") long expirationSeconds) {
        return fileService.getTemporaryDownloadUrl(filePath, expirationSeconds);
    }

    @ApiOperation("文件公开预览")
    @GetMapping("/preview/{id}")
    public void preview(@PathVariable("id") Long id, HttpServletResponse response) {
        fileService.preview(id, response);
    }

    @ApiOperation("文件下载")
    @GetMapping("/download")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "文件下载", businessType = BusinessType.EXPORT)
    public void download(@RequestParam("filePath") String filePath, HttpServletResponse response) {
        fileService.download(filePath, response);
    }

    @ApiOperation("本地文件临时下载")
    @GetMapping("/localDownload/**")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "本地文件临时下载", businessType = BusinessType.EXPORT)
    public void localDownload(HttpServletRequest request, HttpServletResponse response) {
        fileService.localDownload(request, response);
    }

    @ApiOperation("指定存储平台上传文件")
    @PostMapping("/upload/{platform}")
    @PreventDuplicateSubmit
    @RateLimiter(time = 60, count = 20)
    @OperationLog(moduleTitle = "指定存储平台上传文件", businessType = BusinessType.IMPORT)
    public FileMetadata uploadFileWithPlatform(@PathVariable("platform") StoragePlatform platform, @RequestParam("file") MultipartFile file, @RequestParam(value = "directory", required = false) String directory) {
        return fileService.uploadFileWithPlatform(platform, file, directory);
    }

    @ApiOperation("文件删除")
    @DeleteMapping("/delete")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "文件删除", businessType = BusinessType.DELETE)
    public String deleteFile(@RequestParam("filePath") String filePath) {
        return fileService.deleteFile(filePath);
    }

    @ApiOperation("获取预签名上传URL")
    @GetMapping("/presigned-upload-url")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "获取预签名上传URL", businessType = BusinessType.EXPORT)
    public Map<String, String> getPresignedUploadUrl(@RequestParam String fileName, @RequestParam(required = false) String directory) {
        return fileService.getPresignedUploadUrl(fileName, directory);
    }

    @ApiOperation("预签名上传完成入库")
    @PostMapping("/presigned-upload-complete")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "预签名上传完成入库", businessType = BusinessType.IMPORT)
    public FileMetadata presignedUploadComplete(@Validated @RequestBody PresignedUploadCompleteDTO dto) {
        return fileService.completePresignedUpload(dto);
    }
}

package com.aegis.utils;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/24 18:40
 * @Description: MinIO工具类
 */
@Slf4j
public final class MinioUtils {

    private static MinioClient minioClient;

    private static String endpoint;

    private static String bucketName;

    private static String accessKey;

    private static String secretKey;

    private static final String FILE_FOLDER = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private static final String SEPARATOR = "/";

    public MinioUtils(String endpoint, String bucketName, String accessKey, String secretKey) {
        MinioUtils.endpoint = endpoint;
        MinioUtils.bucketName = bucketName;
        MinioUtils.accessKey = accessKey;
        MinioUtils.secretKey = secretKey;
        createMinioClient();
    }

    /**
     * 创建基于Java端的MinioClient
     */
    public void createMinioClient() {
        try {
            if (null == minioClient) {
                log.info("开始创建 MinioClient...");
                minioClient = MinioClient.builder()
                        .endpoint(endpoint)
                        .credentials(accessKey, secretKey)
                        .build();
                createBucket();
                log.info("创建完毕 MinioClient...");
            }
        } catch (Exception e) {
            log.error("MinIO服务器异常", e);
        }
    }

    /**
     * 获取上传文件前缀路径
     */
    public static String getBasisUrl() {
        return endpoint + SEPARATOR + bucketName + SEPARATOR;
    }

    /**
     * 判断Bucket是否存在，true：存在，false：不存在
     */
    public static boolean bucketExists() throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 启动SpringBoot容器的时候初始化Bucket
     * 如果没有Bucket则创建
     */
    private static void createBucket() throws Exception {
        if (!bucketExists()) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 获得所有Bucket列表
     */
    public static List<Bucket> getAllBuckets() throws Exception {
        return minioClient.listBuckets();
    }

    /**
     * 根据bucketName获取其相关信息
     */
    public static Optional<Bucket> getBucket() throws Exception {
        return getAllBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 根据bucketName删除Bucket，true：删除成功； false：删除失败，文件或已不存在
     */
    public static void removeBucket() throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * 获得Bucket的策略
     */
    public static String getBucketPolicy() throws Exception {
        return minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(bucketName).build());
    }

    /**
     * 判断文件夹是否存在
     *
     * @param folderName 文件夹名称
     */
    public static boolean isFolderExist(String folderName) {
        boolean exist = false;
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix(folderName).recursive(false).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.isDir() && folderName.equals(item.objectName())) {
                    exist = true;
                }
            }
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

    /**
     * 创建文件夹或目录
     *
     * @param objectName 目录路径
     */
    public static ObjectWriteResponse createDir(String objectName) throws Exception {
        return minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName 文件名称
     */
    public static boolean isFileExist(String fileName) {
        boolean exist = true;
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

    /**
     * 拷贝文件
     *
     * @param bucketName    存储桶
     * @param objectName    文件名
     * @param srcBucketName 目标存储桶
     * @param srcObjectName 目标文件名
     */
    public static ObjectWriteResponse copyFile(String bucketName, String objectName, String srcBucketName, String srcObjectName) throws Exception {
        return minioClient.copyObject(
                CopyObjectArgs.builder()
                        .source(CopySource.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .build()
                        )
                        .bucket(srcBucketName)
                        .object(srcObjectName)
                        .build());
    }

    /**
     * 删除文件
     *
     * @param objectName 文件名称
     */
    public static void removeFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    /**
     * 批量删除文件
     *
     * @param keys 需要删除的文件列表
     */
    public static void removeFiles(List<String> keys) {
        keys.forEach(s -> {
            try {
                removeFile(s);
            } catch (Exception e) {
                log.error("批量删除失败：{}", s);
            }
        });
    }

    /**
     * 获取文件信息，如果抛出异常则说明文件不存在
     *
     * @param objectName 文件名称
     * @return StatObjectResponse
     */
    public static StatObjectResponse getFileInfo(String objectName) throws Exception {
        return minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    /**
     * 获取路径下文件列表
     *
     * @param prefix    文件名称
     * @param recursive 是否递归查找，false：模拟文件夹结构查找
     * @return 二进制流
     */
    public static Iterable<Result<Item>> listObjects(String prefix, boolean recursive) {
        return minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(prefix)
                        .recursive(recursive)
                        .build());
    }

    /**
     * 根据文件前缀查询文件
     *
     * @param prefix    文件前缀
     * @param recursive 是否递归查找，true：递归查找，false：模拟文件夹结构查找
     */
    public static List<Item> getAllObjectsByPrefix(String prefix, boolean recursive) throws Exception {
        List<Item> list = new ArrayList<>();
        Iterable<Result<Item>> objectsIterator = listObjects(prefix, recursive);
        if (objectsIterator != null) {
            for (Result<Item> o : objectsIterator) {
                Item item = o.get();
                list.add(item);
            }
        }
        return list;
    }

    /**
     * 获取上传文件的URL
     * 此方式无法走数据加密
     *
     * @param uniqueFileName 唯一文件名称
     * @param expires        URL过期时间
     * @return URL
     */
    public static String getPreSignedUrl(String uniqueFileName, Integer expires) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(uniqueFileName)
                        .expiry(expires, TimeUnit.MINUTES)// 设置URL过期时间
                        .build());
    }

    /**
     * 使用MultipartFile进行文件上传
     *
     * @param file        文件名
     * @param objectName  对象名
     * @param contentType 类型
     */
    public static String uploadFile(MultipartFile file, String objectName, String contentType) throws Exception {
        final String filePath = FILE_FOLDER + SEPARATOR + objectName;
        InputStream inputStream = file.getInputStream();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filePath)
                        .contentType(contentType)
                        .stream(inputStream, inputStream.available(), -1)
                        .build());
        return String.format("%s/%s/%s", endpoint, bucketName, filePath);
    }

    /**
     * 上传本地文件
     *
     * @param objectName 对象名称
     * @param fileName   本地文件路径
     */
    public static String uploadFile(String objectName, String fileName) throws Exception {
        final String filePath = FILE_FOLDER + SEPARATOR + objectName;
        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filePath)
                        .filename(fileName)
                        .build());
        return String.format("%s/%s/%s", endpoint, bucketName, filePath);
    }

    /**
     * 通过流上传文件
     *
     * @param objectName  文件对象
     * @param inputStream 文件流
     */
    public static String uploadFile(String objectName, InputStream inputStream) throws Exception {
        final String filePath = FILE_FOLDER + SEPARATOR + objectName;
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filePath)
                        .stream(inputStream, inputStream.available(), -1)
                        .build());
        return String.format("%s/%s/%s", endpoint, bucketName, filePath);
    }

    /**
     * 获取文件流
     */
    public static InputStream getObject(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    /**
     * 断点下载
     *
     * @param objectName 文件名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度
     * @return 二进制流
     */
    public InputStream getObject(String objectName, long offset, long length) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .offset(offset)
                        .length(length)
                        .build());
    }
}


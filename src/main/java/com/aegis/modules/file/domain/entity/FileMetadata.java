package com.aegis.modules.file.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件元数据表
 *
 * @author xuesong.lei
 * @since 2025-09-09 15:53:06
 * @TableName t_file_metadata
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "文件元数据表")
@TableName(value = "t_file_metadata")
public class FileMetadata implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @TableField(value = "create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    @TableField(value = "update_by")
    private Long updateBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 逻辑删除标记(0=正常,1=删除)
     */
    @Schema(description = "逻辑删除标记(0=正常,1=删除)")
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;

    /**
     * 版本号,用于乐观锁
     */
    @Schema(description = "版本号,用于乐观锁")
    @Version
    @TableField(value = "version")
    private Integer version;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @TableField(value = "remark")
    private String remark;

    /**
     * 文件名称
     */
    @Schema(description = "文件名称")
    @TableField(value = "file_name")
    private String fileName;

    /**
     * 原始文件名称
     */
    @Schema(description = "原始文件名称")
    @TableField(value = "original_file_name")
    private String originalFileName;

    /**
     * 文件后缀
     */
    @Schema(description = "文件后缀")
    @TableField(value = "suffix")
    private String suffix;

    /**
     * 文件存储路径
     */
    @Schema(description = "文件存储路径")
    @TableField(value = "file_path")
    private String filePath;

    /**
     * 文件大小,单位字节
     */
    @Schema(description = "文件大小,单位字节")
    @TableField(value = "file_size")
    private Long fileSize;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型")
    @TableField(value = "content_type")
    private String contentType;

    /**
     * 存储平台
     */
    @Schema(description = "存储平台")
    @TableField(value = "platform")
    private String platform;

    /**
     * 上传时间
     */
    @Schema(description = "上传时间")
    @TableField(value = "upload_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    /**
     * 文件MD5值
     */
    @Schema(description = "文件MD5值")
    @TableField(value = "md5")
    private String md5;

    /**
     * 可直接访问的文件URL（仅响应返回，不入库）
     */
    @Schema(description = "文件访问URL")
    @TableField(exist = false)
    private String accessUrl;

}

package com.aegis.modules.log.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: xuesong.lei
 * @Date: 2025-08-21 22:39:10
 * @Description: 操作日志表
 * @TableName t_sys_operate_log
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_sys_operate_log")
public class SysOperateLog implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 模块标题
     */
    @TableField(value = "module_title")
    private String moduleTitle;

    /**
     * 业务类型(0-其它,1-新增,2-修改,3-删除)
     */
    @TableField(value = "business_type")
    private Integer businessType;

    /**
     * 请求地址
     */
    @TableField(value = "request_url")
    private String requestUrl;

    /**
     * 请求IP
     */
    @TableField(value = "request_ip")
    private String requestIp;

    /**
     * 请求地点
     */
    @TableField(value = "request_local")
    private String requestLocal;

    /**
     * 请求方式
     */
    @TableField(value = "request_type")
    private String requestType;

    /**
     * 请求方法
     */
    @TableField(value = "request_method")
    private String requestMethod;

    /**
     * 请求参数
     */
    @TableField(value = "request_args")
    private String requestArgs;

    /**
     * 响应结果
     */
    @TableField(value = "response_result")
    private String responseResult;

    /**
     * 错误响应
     */
    @TableField(value = "error_message")
    private String errorMessage;

    /**
     * 操作用户
     */
    @TableField(value = "operate_user")
    private String operateUser;

    /**
     * 操作时间
     */
    @TableField(value = "operate_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    /**
     * 操作状态(0-成功,1-失败)
     */
    @TableField(value = "operate_status")
    private String operateStatus;

}

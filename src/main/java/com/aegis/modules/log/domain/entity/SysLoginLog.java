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
 * @Date: 2025-08-30 10:48:46
 * @Description: 登录日志表
 * @TableName t_sys_login_log
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_sys_login_log")
public class SysLoginLog implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名
     */
    @TableField(value = "login_username")
    private String loginUsername;

    /**
     * 登录IP地址
     */
    @TableField(value = "login_ip")
    private String loginIp;

    /**
     * 登录地点
     */
    @TableField(value = "login_local")
    private String loginLocal;

    /**
     * 浏览器类型
     */
    @TableField(value = "login_browser")
    private String loginBrowser;

    /**
     * 操作系统
     */
    @TableField(value = "login_os")
    private String loginOs;

    /**
     * 登录时间
     */
    @TableField(value = "login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    /**
     * 操作状态(0-成功,1-失败)
     */
    @TableField(value = "login_status")
    private String loginStatus;

}

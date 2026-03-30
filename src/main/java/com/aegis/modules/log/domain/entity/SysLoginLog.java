package com.aegis.modules.log.domain.entity;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import cn.idev.excel.annotation.write.style.HeadRowHeight;
import cn.idev.excel.annotation.write.style.HeadStyle;
import cn.idev.excel.enums.poi.FillPatternTypeEnum;
import com.aegis.common.excel.StatusConvert;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志表
 *
 * @author xuesong.lei
 * @since 2025-08-30 10:48:46
 * @TableName t_sys_login_log
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("登录日志表")
@TableName(value = "t_sys_login_log")
@ColumnWidth(25)
@HeadRowHeight(30)
@ExcelIgnoreUnannotated
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 17)
public class SysLoginLog implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    @TableField(value = "login_username")
    @ExcelProperty("用户名")
    private String loginUsername;

    /**
     * 登录IP地址
     */
    @ApiModelProperty("登录IP地址")
    @TableField(value = "login_ip")
    @ExcelProperty("登录IP地址")
    private String loginIp;

    /**
     * 登录地点
     */
    @ApiModelProperty("登录地点")
    @TableField(value = "login_local")
    @ExcelProperty("登录地点")
    private String loginLocal;

    /**
     * 浏览器类型
     */
    @ApiModelProperty("浏览器类型")
    @TableField(value = "login_browser")
    @ExcelProperty("浏览器类型")
    private String loginBrowser;

    /**
     * 操作系统
     */
    @ApiModelProperty("操作系统")
    @TableField(value = "login_os")
    @ExcelProperty("操作系统")
    private String loginOs;

    /**
     * 登录时间
     */
    @ApiModelProperty("登录时间")
    @TableField(value = "login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("登录时间")
    private Date loginTime;

    /**
     * 操作状态(0-成功,1-失败)
     */
    @ApiModelProperty("操作状态(0-成功,1-失败)")
    @TableField(value = "login_status")
    @ExcelProperty(value = "操作状态", converter = StatusConvert.class)
    private String loginStatus;

    /**
     * 错误响应
     */
    @ApiModelProperty("错误响应")
    @TableField(value = "error_message")
    @ExcelProperty("错误响应")
    private String errorMessage;
}

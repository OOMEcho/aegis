package com.aegis.modules.log.domain.entity;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import cn.idev.excel.annotation.write.style.HeadRowHeight;
import cn.idev.excel.annotation.write.style.HeadStyle;
import cn.idev.excel.enums.poi.FillPatternTypeEnum;
import com.aegis.common.excel.BusinessTypeConvert;
import com.aegis.common.excel.StatusConvert;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志表
 *
 * @author xuesong.lei
 * @since 2025-08-21
 * @TableName t_sys_operate_log
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel("操作日志表")
@TableName(value = "t_sys_operate_log")
@ColumnWidth(25)
@HeadRowHeight(30)
@ExcelIgnoreUnannotated
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 17)
public class SysOperateLog implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 链路追踪ID
     */
    @ApiModelProperty("链路追踪ID")
    @TableField(value = "trace_id")
    @ExcelProperty("链路追踪ID")
    private String traceId;

    /**
     * 模块标题
     */
    @ApiModelProperty("模块标题")
    @TableField(value = "module_title")
    @ExcelProperty("模块标题")
    private String moduleTitle;

    /**
     * 业务类型(0-其它,1-新增,2-修改,3-删除)
     */
    @ApiModelProperty("业务类型(0-其它,1-新增,2-修改,3-删除)")
    @TableField(value = "business_type")
    @ExcelProperty(value = "业务类型", converter = BusinessTypeConvert.class)
    private Integer businessType;

    /**
     * 请求地址
     */
    @ApiModelProperty("请求地址")
    @TableField(value = "request_url")
    @ExcelProperty("请求地址")
    private String requestUrl;

    /**
     * 请求IP
     */
    @ApiModelProperty("请求IP")
    @TableField(value = "request_ip")
    @ExcelProperty("请求IP")
    private String requestIp;

    /**
     * 请求地点
     */
    @ApiModelProperty("请求地点")
    @TableField(value = "request_local")
    @ExcelProperty("请求地点")
    private String requestLocal;

    /**
     * 请求方式
     */
    @ApiModelProperty("请求方式")
    @TableField(value = "request_type")
    @ExcelProperty("请求方式")
    private String requestType;

    /**
     * 请求方法
     */
    @ApiModelProperty("请求方法")
    @TableField(value = "request_method")
    @ExcelProperty("请求方法")
    private String requestMethod;

    /**
     * 请求参数
     */
    @ApiModelProperty("请求参数")
    @TableField(value = "request_args")
    @ExcelProperty("请求参数")
    private String requestArgs;

    /**
     * 响应结果
     */
    @ApiModelProperty("响应结果")
    @TableField(value = "response_result")
    @ExcelProperty("响应结果")
    private String responseResult;

    /**
     * 错误响应
     */
    @ApiModelProperty("错误响应")
    @TableField(value = "error_message")
    @ExcelProperty("错误响应")
    private String errorMessage;

    /**
     * 操作用户
     */
    @ApiModelProperty("操作用户")
    @TableField(value = "operate_user")
    @ExcelProperty("操作用户")
    private String operateUser;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    @TableField(value = "operate_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty("操作时间")
    private LocalDateTime operateTime;

    /**
     * 消耗时间(单位：毫秒)
     */
    @ApiModelProperty("消耗时间(单位：毫秒)")
    @TableField(value = "deplete_time")
    @ExcelProperty("消耗时间(单位：毫秒)")
    private Long depleteTime;

    /**
     * 操作状态(0-成功,1-失败)
     */
    @ApiModelProperty("操作状态(0-成功,1-失败)")
    @TableField(value = "operate_status")
    @ExcelProperty(value = "操作状态", converter = StatusConvert.class)
    private String operateStatus;

}

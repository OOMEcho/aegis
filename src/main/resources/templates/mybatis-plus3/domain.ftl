package ${domain.packageName};
<#list tableClass.allFields as field>
    <#if !field.fullTypeName?starts_with("java.lang") &&
    !field.columnIsArray &&
    !field.fullTypeName?starts_with("java.util") &&
    field.fullTypeName != "java.io.Serializable">
        import ${field.fullTypeName};
    </#if>
</#list>

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

<#assign hasDateFields = false>
<#list tableClass.allFields as field>
    <#if field.shortTypeName == "Date" || field.shortTypeName == "LocalDateTime" || field.shortTypeName == "LocalDate">
        <#assign hasDateFields = true>
        <#break>
    </#if>
</#list>
<#if hasDateFields>
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
</#if>

import java.io.Serializable;
<#if hasDateFields>
import java.util.Date;
</#if>

/**
* @Author: xuesong.lei
* @Date: ${.now?string('yyyy-MM-dd HH:mm:ss')}
* @Description: ${tableClass.remark!}
* @TableName ${tableClass.tableName}
*/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(value ="${tableClass.tableName}")
public class ${tableClass.shortClassName} implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

<#list tableClass.allFields as field>
    /**
    * ${field.remark!}
    */
    <#-- 增强的 ID 字段处理 -->
    <#if field.fieldName == "id">
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    <#elseif field.fieldName?ends_with("_id") || field.fieldName?ends_with("Id")>
    @TableField(value = "${field.columnName}")
    <#-- 处理常见字段 -->
    <#elseif field.fieldName == "createTime" || field.fieldName == "created_time">
    @TableField(value = "${field.columnName}", fill = FieldFill.INSERT)
    <#elseif field.fieldName == "updateTime" || field.fieldName == "updated_time">
    @TableField(value = "${field.columnName}", fill = FieldFill.INSERT_UPDATE)
    <#elseif field.fieldName == "deleted" || field.fieldName == "isDeleted">
    @TableLogic
    @TableField(value = "${field.columnName}")
    <#elseif field.fieldName == "version">
    @Version
    @TableField(value = "${field.columnName}")
    <#else>
    @TableField(value = "${field.columnName}")
    </#if>
    <#-- 增强的日期格式 -->
    <#if field.shortTypeName == "Date">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    <#elseif field.shortTypeName == "LocalDateTime">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    <#elseif field.shortTypeName == "LocalDate">
    @JsonFormat(pattern = "yyyy-MM-dd")
    </#if>
    private ${field.shortTypeName} ${field.fieldName};

</#list>
}

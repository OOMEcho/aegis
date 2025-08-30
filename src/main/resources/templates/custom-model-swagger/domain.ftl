package ${domain.packageName};
<#list tableClass.allFields as field>
    <#if !field.fullTypeName?starts_with("java.lang") &&
    !field.columnIsArray &&
    !field.fullTypeName?starts_with("java.util") &&
    field.fullTypeName != "java.io.Serializable">
        import ${field.fullTypeName};
    </#if>
</#list>

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
@ApiModel(value = "${tableClass.remark!}", description = "${tableClass.tableName}")
public class ${tableClass.shortClassName} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list tableClass.allFields as field>
    /**
    * ${field.remark!}
    */
   <#-- 增强的日期格式 -->
    <#if field.shortTypeName == "Date">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    <#elseif field.shortTypeName == "LocalDateTime">
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    <#elseif field.shortTypeName == "LocalDate">
    @JsonFormat(pattern = "yyyy-MM-dd")
    </#if>
    @ApiModelProperty("${field.remark!}")
    private ${field.shortTypeName} ${field.fieldName};

</#list>
}

package ${mapperInterface.packageName};

import ${tableClass.fullClassName};
<#if tableClass.pkFields??>
    <#list tableClass.pkFields as field><#assign pkName>${field.shortTypeName}</#assign></#list>
</#if>
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @Author: xuesong.lei
* @Date: ${.now?string('yyyy-MM-dd HH:mm:ss')}
* @Description: 针对表【${tableClass.tableName}<#if tableClass.remark?has_content>(${tableClass.remark!})</#if>】的数据库操作Mapper
* @Entity: ${tableClass.fullClassName}
*/
public interface ${mapperInterface.fileName} extends BaseMapper<${tableClass.shortClassName}> {

}





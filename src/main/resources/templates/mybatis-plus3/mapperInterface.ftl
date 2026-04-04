package ${mapperInterface.packageName};

import ${tableClass.fullClassName};
<#if tableClass.pkFields??>
    <#list tableClass.pkFields as field><#assign pkName>${field.shortTypeName}</#assign></#list>
</#if>
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* 针对表【${tableClass.tableName}<#if tableClass.remark?has_content>(${tableClass.remark!})</#if>】的数据库操作Mapper
*
* @author xuesong.lei
* @since ${.now?string('yyyy-MM-dd')}
* @Entity ${tableClass.fullClassName}
*/
public interface ${mapperInterface.fileName} extends BaseMapper<${tableClass.shortClassName}> {

}





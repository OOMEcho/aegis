package ${mapperInterface.packageName};

import ${tableClass.fullClassName};

/**
* 针对表【${tableClass.tableName}<#if tableClass.remark?has_content>(${tableClass.remark!})</#if>】的数据库操作Mapper
*
* @author xuesong.lei
* @since ${.now?string('yyyy-MM-dd')}
* @Entity ${tableClass.fullClassName}
*/
public interface ${mapperInterface.fileName} {

    int deleteByPrimaryKey(Long id);

    int insert(${tableClass.shortClassName} record);

    int insertSelective(${tableClass.shortClassName} record);

    ${tableClass.shortClassName} selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(${tableClass.shortClassName} record);

    int updateByPrimaryKey(${tableClass.shortClassName} record);

}





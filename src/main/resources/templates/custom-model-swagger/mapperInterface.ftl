package ${mapperInterface.packageName};

import ${tableClass.fullClassName};

/**
* @Author: xuesong.lei
* @Date: ${.now?string('yyyy-MM-dd HH:mm:ss')}
* @Description: 针对表【${tableClass.tableName}<#if tableClass.remark?has_content>(${tableClass.remark!})</#if>】的数据库操作Mapper
* @Entity: ${tableClass.fullClassName}
*/
public interface ${mapperInterface.fileName} {

    int deleteByPrimaryKey(Long id);

    int insert(${tableClass.shortClassName} record);

    int insertSelective(${tableClass.shortClassName} record);

    ${tableClass.shortClassName} selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(${tableClass.shortClassName} record);

    int updateByPrimaryKey(${tableClass.shortClassName} record);

}





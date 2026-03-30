package com.aegis.modules.resource.service;

import com.aegis.modules.resource.domain.dto.ResourceDTO;
import com.aegis.modules.resource.domain.entity.Resource;
import com.aegis.modules.resource.domain.vo.ResourceVO;
import org.mapstruct.Mapper;

/**
 * 资源类型转换类
 *
 * @author xuesong.lei
 * @since 2026/1/12 23:20
 */
@Mapper(componentModel = "spring")
public interface ResourceConvert {

    Resource toResource(ResourceDTO dto);

    ResourceVO toResourceVo(Resource resource);
}

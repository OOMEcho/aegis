package com.aegis.modules.resource.service;

import com.aegis.modules.resource.domain.dto.ResourceDTO;
import com.aegis.modules.resource.domain.entity.Resource;
import com.aegis.modules.resource.domain.vo.ResourceVO;
import org.mapstruct.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/12 23:20
 * @Description: 资源类型转换类
 */
@Mapper(componentModel = "spring")
public interface ResourceConvert {

    Resource toResource(ResourceDTO dto);

    ResourceVO toResourceVo(Resource resource);
}

package com.aegis.modules.resource.mapper;

import com.aegis.modules.resource.domain.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026-01-11 15:58:32
 * @Description: 针对表【t_resource(资源(URL)与权限映射表)】的数据库操作Mapper
 * @Entity: com.aegis.modules.resource.domain.entity.Resource
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 获取所有有效的资源列表（用于鉴权）
     *
     * @return 资源列表
     */
    List<Resource> getAllResource();
}





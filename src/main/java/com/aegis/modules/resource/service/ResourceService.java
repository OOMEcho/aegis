package com.aegis.modules.resource.service;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.modules.resource.domain.dto.ResourceDTO;
import com.aegis.modules.resource.domain.vo.ResourceVO;

/**
 * 资源业务层
 *
 * @author xuesong.lei
 * @since 2026-01-12
 */
public interface ResourceService {

    /**
     * 分页列表
     */
    PageVO<ResourceVO> pageList(ResourceDTO dto);

    /**
     * 详情
     */
    ResourceVO detail(Long id);

    /**
     * 删除
     */
    String delete(Long id);

    /**
     * 新增
     */
    String add(ResourceDTO dto);

    /**
     * 修改
     */
    String update(ResourceDTO dto);
}

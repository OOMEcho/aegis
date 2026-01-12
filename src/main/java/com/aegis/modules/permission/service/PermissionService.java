package com.aegis.modules.permission.service;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.modules.permission.domain.dto.PermissionDTO;
import com.aegis.modules.permission.domain.entity.Permission;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/12 23:18
 * @Description: 权限业务层
 */
public interface PermissionService {

    /**
     * 分页列表
     */
    PageVO<Permission> pageList(PermissionDTO dto);

    /**
     * 全部列表
     */
    List<Permission> list(PermissionDTO dto);

    /**
     * 详情
     */
    Permission detail(Long id);

    /**
     * 修改权限状态
     */
    String effective(Long id);

    /**
     * 新增
     */
    String add(PermissionDTO dto);

    /**
     * 修改
     */
    String update(PermissionDTO dto);
}

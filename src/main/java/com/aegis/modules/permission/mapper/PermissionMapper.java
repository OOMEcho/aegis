package com.aegis.modules.permission.mapper;

import com.aegis.modules.permission.domain.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 针对表【t_permission(功能权限表)】的数据库操作Mapper
 *
 * @author xuesong.lei
 * @since 2026-01-11 15:57:26
 * @Entity: com.aegis.modules.permission.domain.entity.Permission
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据用户ID获取权限编码列表
     *
     * @param userId 用户ID
     * @return 权限编码列表
     */
    List<String> selectPermCodesByUserId(Long userId);
}





package com.aegis.modules.user.mapper;

import com.aegis.modules.role.domain.entity.Role;
import com.aegis.modules.user.domain.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025-08-30 10:49:13
 * @Description: 针对表【t_user_role(用户和角色关联表)】的数据库操作Mapper
 * @Entity: com.aegis.modules.user.domain.entity.UserRole
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户ID获取角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> selectRoleByUserId(Long userId);
}





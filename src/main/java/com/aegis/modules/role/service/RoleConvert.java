package com.aegis.modules.role.service;

import com.aegis.modules.role.domain.dto.RoleDTO;
import com.aegis.modules.role.domain.entity.Role;
import com.aegis.modules.role.domain.vo.RoleVO;
import org.mapstruct.Mapper;

/**
 * 角色类型转换类
 *
 * @author xuesong.lei
 * @since 2025-09-13
 */
@Mapper(componentModel = "spring")
public interface RoleConvert {

    RoleVO toRoleVo(Role role);

    Role toRole(RoleDTO dto);
}

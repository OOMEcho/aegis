package com.aegis.modules.permission.service;

import com.aegis.modules.permission.domain.dto.PermissionDTO;
import com.aegis.modules.permission.domain.entity.Permission;
import org.mapstruct.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/12 23:20
 * @Description: 权限类型转换类
 */
@Mapper(componentModel = "spring")
public interface PermissionConvert {

    Permission toPermission(PermissionDTO dto);
}

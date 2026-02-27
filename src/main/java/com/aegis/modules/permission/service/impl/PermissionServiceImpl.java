package com.aegis.modules.permission.service.impl;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.exception.BusinessException;
import com.aegis.modules.permission.domain.dto.PermissionDTO;
import com.aegis.modules.permission.domain.entity.Permission;
import com.aegis.modules.permission.mapper.PermissionMapper;
import com.aegis.modules.permission.service.PermissionConvert;
import com.aegis.modules.permission.service.PermissionService;
import com.aegis.utils.PageUtils;
import com.aegis.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/12 23:18
 * @Description: 权限业务实现层
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;

    private final PermissionConvert permissionConvert;

    @Override
    public PageVO<Permission> pageList(PermissionDTO dto) {
        LambdaQueryWrapper<Permission> queryWrapper = buildQueryWrapper(dto);
        return PageUtils.of(dto).paging(permissionMapper, queryWrapper);
    }

    @Override
    public List<Permission> list(PermissionDTO dto) {
        LambdaQueryWrapper<Permission> queryWrapper = buildQueryWrapper(dto);
        return permissionMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String effective(Long id) {
        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }

        permission.setStatus(CommonConstants.NORMAL_STATUS.equals(permission.getStatus()) ?
                CommonConstants.DISABLE_STATUS : CommonConstants.NORMAL_STATUS);
        permission.setUpdateBy(SecurityUtils.getUserId());

        permissionMapper.updateById(permission);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(PermissionDTO dto) {
        Permission permission = permissionConvert.toPermission(dto);

        // 检查权限编码是否已存在
        checkPermCodeExists(permission.getPermCode(), null);

        permission.setCreateBy(SecurityUtils.getUserId());
        permissionMapper.insert(permission);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(PermissionDTO dto) {
        Permission permission = permissionConvert.toPermission(dto);

        // 检查权限编码是否已存在
        checkPermCodeExists(permission.getPermCode(), permission.getId());

        permission.setUpdateBy(SecurityUtils.getUserId());
        permissionMapper.updateById(permission);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    private void checkPermCodeExists(String permCode, Long excludeId) {
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Permission::getPermCode, permCode)
                .ne(ObjectUtils.isNotNull(excludeId), Permission::getId, excludeId);

        if (permissionMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("权限编码已存在");
        }
    }

    private LambdaQueryWrapper<Permission> buildQueryWrapper(PermissionDTO dto) {
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dto.getPermCode()), Permission::getPermCode, dto.getPermCode())
                .like(StringUtils.isNotBlank(dto.getPermName()), Permission::getPermName, dto.getPermName())
                .eq(StringUtils.isNotBlank(dto.getPermType()), Permission::getPermType, dto.getPermType())
                .eq(StringUtils.isNotBlank(dto.getStatus()), Permission::getStatus, dto.getStatus());
        return queryWrapper;
    }
}

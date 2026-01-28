package com.aegis.modules.role.service.impl;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.exception.BusinessException;
import com.aegis.modules.dept.domain.dto.DeptDTO;
import com.aegis.modules.dept.service.DeptService;
import com.aegis.modules.role.domain.dto.CancelAllDTO;
import com.aegis.modules.role.domain.dto.CancelDTO;
import com.aegis.modules.role.domain.dto.RoleDTO;
import com.aegis.modules.role.domain.dto.UserAndRoleQueryDTO;
import com.aegis.modules.role.domain.entity.Role;
import com.aegis.modules.role.domain.entity.RoleDept;
import com.aegis.modules.role.domain.entity.RolePermission;
import com.aegis.modules.role.domain.vo.RoleWithMenuOrDeptVO;
import com.aegis.modules.role.mapper.RoleDeptMapper;
import com.aegis.modules.role.mapper.RoleMapper;
import com.aegis.modules.role.mapper.RolePermissionMapper;
import com.aegis.modules.role.service.RoleConvert;
import com.aegis.modules.role.service.RoleService;
import com.aegis.modules.user.domain.entity.UserRole;
import com.aegis.modules.user.domain.vo.UserVO;
import com.aegis.modules.user.mapper.UserRoleMapper;
import com.aegis.utils.PageUtils;
import com.aegis.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/10 14:07
 * @Description: 角色业务实现层
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    private final RoleDeptMapper roleDeptMapper;

    private final RolePermissionMapper rolePermissionMapper;

    private final UserRoleMapper userRoleMapper;

    private final RoleConvert roleConvert;

    private final DeptService deptService;

    @Override
    public PageVO<Role> pageList(RoleDTO dto) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotBlank(dto.getRoleName()), Role::getRoleName, dto.getRoleName())
                .like(StringUtils.isNotBlank(dto.getRoleCode()), Role::getRoleCode, dto.getRoleCode())
                .eq(StringUtils.isNotBlank(dto.getStatus()), Role::getStatus, dto.getStatus())
                .orderBy(true, true, Role::getOrderNum);

        return PageUtils.of(dto).paging(roleMapper, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateStatus(Long id) {
        Role role = roleMapper.selectById(id);
        if (ObjectUtils.isNotNull(role)) {
            // 不能操作超级管理员角色
            checkIsAdminRole(role.getRoleCode());

            if (CommonConstants.NORMAL_STATUS.equals(role.getStatus())) {
                role.setStatus(CommonConstants.DISABLE_STATUS);
            } else {
                role.setStatus(CommonConstants.NORMAL_STATUS);
            }

            roleMapper.updateById(role);

        }
        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(Long id) {
        Role role = roleMapper.selectById(id);
        if (ObjectUtils.isNotNull(role)) {
            // 不能操作超级管理员角色
            checkIsAdminRole(role.getRoleCode());

            LambdaQueryWrapper<UserRole> userRoleWrapper = new LambdaQueryWrapper<>();
            userRoleWrapper.eq(UserRole::getRoleId, id);
            if (userRoleMapper.selectCount(userRoleWrapper) > 0) {
                throw new BusinessException(String.format("%1$s已分配，不能删除", role.getRoleName()));
            }

            // 删除角色与权限关联
            rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, id));

            // 删除角色与部门关联
            roleDeptMapper.delete(new LambdaQueryWrapper<RoleDept>().eq(RoleDept::getRoleId, id));

            roleMapper.deleteById(id);

        }
        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(RoleDTO dto) {
        Role role = roleConvert.toRole(dto);

        // 检查角色名称是否存在
        checkSameRoleName(role);

        // 检查角色编码是否存在
        checkSameRoleCode(role);

        role.setCreateBy(SecurityUtils.getUserId());

        roleMapper.insert(role);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(RoleDTO dto) {
        Role role = roleConvert.toRole(dto);

        // 不能操作超级管理员角色
        checkIsAdminRole(roleMapper.selectById(role.getId()).getRoleCode());

        // 检查角色名称是否存在
        checkSameRoleName(role);

        // 检查角色编码是否存在
        checkSameRoleCode(role);

        role.setUpdateBy(SecurityUtils.getUserId());

        roleMapper.updateById(role);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateRoleDataScope(RoleDTO dto) {
        Role role = roleConvert.toRole(dto);

        // 不能操作超级管理员角色
        checkIsAdminRole(roleMapper.selectById(role.getId()).getRoleCode());

        role.setUpdateBy(SecurityUtils.getUserId());

        roleMapper.updateById(role);

        // 先删除角色与部门关联
        roleDeptMapper.delete(new LambdaQueryWrapper<RoleDept>().eq(RoleDept::getRoleId, role.getId()));

        // 再新增角色与部门关联
        List<RoleDept> roleDeptList = new ArrayList<>();
        for (Long deptId : dto.getDeptIds()) {
            RoleDept roleDept = new RoleDept();
            roleDept.setRoleId(role.getId());
            roleDept.setDeptId(deptId);
            roleDeptList.add(roleDept);
        }
        if (!roleDeptList.isEmpty()) {
            roleDeptMapper.insert(roleDeptList);
        }

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    public PageVO<UserVO> allocatedList(UserAndRoleQueryDTO dto) {
        return PageUtils.of(dto).paging(roleMapper.allocatedList(dto));
    }

    @Override
    public PageVO<UserVO> unallocatedList(UserAndRoleQueryDTO dto) {
        return PageUtils.of(dto).paging(roleMapper.unallocatedList(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String cancel(CancelDTO dto) {
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleId, dto.getRoleId())
                .eq(UserRole::getUserId, dto.getUserId()));

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String cancelAll(CancelAllDTO dto) {
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleId, dto.getRoleId())
                .in(UserRole::getUserId, dto.getUserIds()));

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String selectAll(CancelAllDTO dto) {
        List<UserRole> userRoleList = new ArrayList<>();
        for (Long userId : dto.getUserIds()) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(dto.getRoleId());
            userRoleList.add(userRole);
        }
        if (!userRoleList.isEmpty()) {
            userRoleMapper.insert(userRoleList);
        }

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    public RoleWithMenuOrDeptVO roleWithDeptTree(Long roleId) {
        final Role role = roleMapper.selectById(roleId);

        RoleWithMenuOrDeptVO roleWithMenuOrDeptVO = new RoleWithMenuOrDeptVO();
        roleWithMenuOrDeptVO.setCheckedKeys(roleDeptMapper.selectDeptListByRoleId(roleId, role.getDeptCheckStrictly()));
        roleWithMenuOrDeptVO.setTrees(deptService.tree(new DeptDTO()));

        return roleWithMenuOrDeptVO;
    }

    private void checkIsAdminRole(String roleCode) {
        if (CommonConstants.ADMIN_ROLE.equals(roleCode)) {
            throw new BusinessException("不允许操作超级管理员角色");
        }
    }

    private void checkSameRoleCode(Role role) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getRoleCode, role.getRoleCode())
                .ne(ObjectUtils.isNotEmpty(role.getId()), Role::getId, role.getId());
        if (roleMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("角色编码已存在");
        }
    }

    private void checkSameRoleName(Role role) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getRoleName, role.getRoleName())
                .ne(ObjectUtils.isNotEmpty(role.getId()), Role::getId, role.getId());
        if (roleMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("角色名称已存在");
        }
    }

    @Override
    public List<String> getRolePermissions(Long roleId) {
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermission::getRoleId, roleId);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(queryWrapper);
        List<String> permCodes = new ArrayList<>();
        for (RolePermission rp : rolePermissions) {
            permCodes.add(rp.getPermCode());
        }
        return permCodes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String assignPermissions(Long roleId, List<String> permCodes) {
        // 不能操作超级管理员角色
        Role role = roleMapper.selectById(roleId);
        if (role != null) {
            checkIsAdminRole(role.getRoleCode());
        }

        // 先删除角色原有权限
        rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId));

        // 再新增角色权限关联
        if (permCodes != null && !permCodes.isEmpty()) {
            List<RolePermission> rolePermissionList = new ArrayList<>();
            for (String permCode : permCodes) {
                RolePermission rp = new RolePermission();
                rp.setRoleId(roleId);
                rp.setPermCode(permCode);
                rolePermissionList.add(rp);
            }
            for (RolePermission rp : rolePermissionList) {
                rolePermissionMapper.insert(rp);
            }
        }

        return CommonConstants.SUCCESS_MESSAGE;
    }
}

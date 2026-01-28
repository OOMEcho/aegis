package com.aegis.modules.role.service;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.modules.role.domain.dto.CancelAllDTO;
import com.aegis.modules.role.domain.dto.CancelDTO;
import com.aegis.modules.role.domain.dto.RoleDTO;
import com.aegis.modules.role.domain.dto.UserAndRoleQueryDTO;
import com.aegis.modules.role.domain.entity.Role;
import com.aegis.modules.role.domain.vo.RoleWithMenuOrDeptVO;
import com.aegis.modules.user.domain.vo.UserVO;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/10 14:07
 * @Description: 角色业务层
 */
public interface RoleService {

    /**
     * 分页列表
     *
     * @param dto 角色DTO
     * @return 角色列表
     */
    PageVO<Role> pageList(RoleDTO dto);

    /**
     * 修改角色状态
     *
     * @param id 主键
     * @return 结果
     */
    String updateStatus(Long id);

    /**
     * 删除
     *
     * @param id 主键
     * @return 结果
     */
    String delete(Long id);

    /**
     * 新增
     *
     * @param dto 角色DTO
     * @return 结果
     */
    String add(RoleDTO dto);

    /**
     * 编辑
     *
     * @param dto 角色DTO
     * @return 结果
     */
    String update(RoleDTO dto);

    /**
     * 修改角色数据权限
     *
     * @param dto 角色DTO
     * @return 结果
     */
    String updateRoleDataScope(RoleDTO dto);

    /**
     * 查询已分配用户角色列表
     *
     * @param dto 用户角色查询DTO
     * @return 结果
     */
    PageVO<UserVO> allocatedList(UserAndRoleQueryDTO dto);

    /**
     * 查询未分配用户角色列表
     *
     * @param dto 用户角色查询DTO
     * @return 结果
     */
    PageVO<UserVO> unallocatedList(UserAndRoleQueryDTO dto);

    /**
     * 取消授权用户
     *
     * @param dto dto
     * @return 结果
     */
    String cancel(CancelDTO dto);

    /**
     * 批量取消授权用户
     *
     * @param dto dto
     * @return 结果
     */
    String cancelAll(CancelAllDTO dto);

    /**
     * 批量选择授权用户
     *
     * @param dto dto
     * @return 结果
     */
    String selectAll(CancelAllDTO dto);

    /**
     * 获取角色对应的部门树
     *
     * @param roleId 角色ID
     * @return 角色对应的部门树
     */
    RoleWithMenuOrDeptVO roleWithDeptTree(Long roleId);

    /**
     * 获取角色的权限编码列表
     *
     * @param roleId 角色ID
     * @return 权限编码列表
     */
    List<String> getRolePermissions(Long roleId);

    /**
     * 给角色分配权限
     *
     * @param roleId    角色ID
     * @param permCodes 权限编码列表
     * @return 结果
     */
    String assignPermissions(Long roleId, List<String> permCodes);
}

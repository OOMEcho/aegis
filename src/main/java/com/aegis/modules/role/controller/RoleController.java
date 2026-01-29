package com.aegis.modules.role.controller;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.duplicate.PreventDuplicateSubmit;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.common.validator.ValidGroup;
import com.aegis.modules.role.domain.dto.CancelAllDTO;
import com.aegis.modules.role.domain.dto.CancelDTO;
import com.aegis.modules.role.domain.dto.RoleDTO;
import com.aegis.modules.role.domain.dto.UserAndRoleQueryDTO;
import com.aegis.modules.role.domain.entity.Role;
import com.aegis.modules.role.domain.vo.RoleWithDeptVO;
import com.aegis.modules.role.service.RoleService;
import com.aegis.modules.user.domain.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/10 14:08
 * @Description: 角色接口
 */
@RestController
@Api(tags = "角色接口")
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @ApiOperation("分页列表")
    @GetMapping("/pageList")
    public PageVO<Role> pageList(RoleDTO dto) {
        return roleService.pageList(dto);
    }

    @ApiOperation("修改角色状态")
    @PutMapping("/updateStatus/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改角色状态", businessType = BusinessType.UPDATE)
    public String updateStatus(@PathVariable("id") Long id) {
        return roleService.updateStatus(id);
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除角色", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return roleService.delete(id);
    }

    @ApiOperation("新增角色")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增角色", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody RoleDTO dto) {
        return roleService.add(dto);
    }

    @ApiOperation("修改角色")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改角色", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody RoleDTO dto) {
        return roleService.update(dto);
    }

    @ApiOperation("修改角色数据权限")
    @PutMapping("/updateRoleDataScope")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改角色数据权限", businessType = BusinessType.UPDATE)
    public String updateRoleDataScope(@Validated(ValidGroup.Update.class) @RequestBody RoleDTO dto) {
        return roleService.updateRoleDataScope(dto);
    }

    @ApiOperation("查询已分配用户角色列表")
    @GetMapping("/allocatedList")
    public PageVO<UserVO> allocatedList(UserAndRoleQueryDTO dto) {
        return roleService.allocatedList(dto);
    }

    @ApiOperation("查询未分配用户角色列表")
    @GetMapping("/unallocatedList")
    public PageVO<UserVO> unallocatedList(UserAndRoleQueryDTO dto) {
        return roleService.unallocatedList(dto);
    }

    @ApiOperation("取消授权用户")
    @PutMapping("/cancel")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "取消授权用户", businessType = BusinessType.DELETE)
    public String cancel(@Validated @RequestBody CancelDTO dto) {
        return roleService.cancel(dto);
    }

    @ApiOperation("批量取消授权用户")
    @PutMapping("/cancelAll")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "批量取消授权用户", businessType = BusinessType.DELETE)
    public String cancelAll(@Validated @RequestBody CancelAllDTO dto) {
        return roleService.cancelAll(dto);
    }

    @ApiOperation("批量选择用户授权")
    @PostMapping("/selectAll")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "批量选择用户授权", businessType = BusinessType.INSERT)
    public String selectAll(@Validated @RequestBody CancelAllDTO dto) {
        return roleService.selectAll(dto);
    }

    @ApiOperation("获取对应角色部门树")
    @GetMapping("/roleWithDeptTree/{roleId}")
    public RoleWithDeptVO roleWithDeptTree(@PathVariable("roleId") Long roleId) {
        return roleService.roleWithDeptTree(roleId);
    }

    @ApiOperation("获取角色的权限列表")
    @GetMapping("/{id}/permissions")
    public List<String> getRolePermissions(@PathVariable("id") Long id) {
        return roleService.getRolePermissions(id);
    }

    @ApiOperation("给角色分配权限")
    @PostMapping("/{id}/permissions")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "给角色分配权限", businessType = BusinessType.UPDATE)
    public String assignPermissions(@PathVariable("id") Long id, @RequestBody List<String> permCodes) {
        return roleService.assignPermissions(id, permCodes);
    }
}

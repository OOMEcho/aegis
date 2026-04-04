package com.aegis.modules.permission.controller;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.duplicate.PreventDuplicateSubmit;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.common.validator.ValidGroup;
import com.aegis.modules.permission.domain.dto.PermissionDTO;
import com.aegis.modules.permission.domain.vo.PermissionVO;
import com.aegis.modules.permission.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理接口
 *
 * @author xuesong.lei
 * @since 2026-01-12
 */
@RestController
@Tag(name = "权限管理接口")
@RequiredArgsConstructor
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @Operation(summary = "分页列表")
    @GetMapping("/pageList")
    public PageVO<PermissionVO> pageList(PermissionDTO dto) {
        return permissionService.pageList(dto);
    }

    @Operation(summary = "全部列表")
    @GetMapping("/list")
    public List<PermissionVO> list(PermissionDTO dto) {
        return permissionService.list(dto);
    }

    @Operation(summary = "修改权限状态")
    @PutMapping("/effective/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改权限状态", businessType = BusinessType.UPDATE)
    public String effective(@PathVariable("id") Long id) {
        return permissionService.effective(id);
    }

    @Operation(summary = "新增权限")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增权限", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody PermissionDTO dto) {
        return permissionService.add(dto);
    }

    @Operation(summary = "修改权限")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改权限", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody PermissionDTO dto) {
        return permissionService.update(dto);
    }
}

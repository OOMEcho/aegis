package com.aegis.modules.menu.controller;

import com.aegis.common.domain.vo.TreeVO;
import com.aegis.common.duplicate.PreventDuplicateSubmit;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.common.validator.ValidGroup;
import com.aegis.modules.menu.domain.dto.MenuDTO;
import com.aegis.modules.menu.domain.entity.Menu;
import com.aegis.modules.menu.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/10 14:11
 * @Description: 菜单接口
 */
@RestController
@Api(tags = "菜单接口")
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @ApiOperation("列表")
    @GetMapping("/list")
    public List<Menu> list(MenuDTO dto) {
        return menuService.list(dto);
    }

    @ApiOperation("详情")
    @GetMapping("/detail/{id}")
    public Menu detail(@PathVariable("id") Long id) {
        return menuService.detail(id);
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除菜单", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return menuService.delete(id);
    }

    @ApiOperation("新增")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增菜单", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody MenuDTO dto) {
        return menuService.add(dto);
    }

    @ApiOperation("修改")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改菜单", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody MenuDTO dto) {
        return menuService.update(dto);
    }

    @ApiOperation("获取树形结构菜单")
    @GetMapping("/tree")
    public List<TreeVO> tree(MenuDTO dto) {
        return menuService.tree(dto);
    }

    @ApiOperation("获取菜单的权限列表")
    @GetMapping("/{id}/permissions")
    public List<String> getMenuPermissions(@PathVariable("id") Long id) {
        return menuService.getMenuPermissions(id);
    }

    @ApiOperation("给菜单配置权限")
    @PostMapping("/{id}/permissions")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "给菜单配置权限", businessType = BusinessType.UPDATE)
    public String assignPermissions(@PathVariable("id") Long id, @RequestBody List<String> permCodes) {
        return menuService.assignPermissions(id, permCodes);
    }
}

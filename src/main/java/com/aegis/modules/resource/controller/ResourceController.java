package com.aegis.modules.resource.controller;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.duplicate.PreventDuplicateSubmit;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.common.validator.ValidGroup;
import com.aegis.modules.resource.domain.dto.ResourceDTO;
import com.aegis.modules.resource.domain.vo.ResourceVO;
import com.aegis.modules.resource.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 资源管理接口
 *
 * @author xuesong.lei
 * @since 2026-01-12
 */
@RestController
@Tag(name = "资源管理接口")
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;

    @Operation(summary = "分页列表")
    @GetMapping("/pageList")
    public PageVO<ResourceVO> pageList(ResourceDTO dto) {
        return resourceService.pageList(dto);
    }

    @Operation(summary = "详情")
    @GetMapping("/detail/{id}")
    public ResourceVO detail(@PathVariable("id") Long id) {
        return resourceService.detail(id);
    }

    @Operation(summary = "删除资源")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除资源", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return resourceService.delete(id);
    }

    @Operation(summary = "新增资源")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增资源", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody ResourceDTO dto) {
        return resourceService.add(dto);
    }

    @Operation(summary = "修改资源")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改资源", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody ResourceDTO dto) {
        return resourceService.update(dto);
    }
}

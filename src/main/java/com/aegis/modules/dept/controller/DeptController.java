package com.aegis.modules.dept.controller;

import com.aegis.common.domain.vo.TreeVO;
import com.aegis.common.duplicate.PreventDuplicateSubmit;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.common.validator.ValidGroup;
import com.aegis.modules.dept.domain.dto.DeptDTO;
import com.aegis.modules.dept.domain.vo.DeptVO;
import com.aegis.modules.dept.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门接口
 *
 * @author xuesong.lei
 * @since 2025-09-09
 */
@RestController
@Api(tags = "部门接口")
@RequiredArgsConstructor
@RequestMapping("/dept")
public class DeptController {

    private final DeptService deptService;

    @ApiOperation("列表")
    @GetMapping("/list")
    public List<DeptVO> list(DeptDTO dto) {
        return deptService.list(dto);
    }

    @ApiOperation("详情")
    @GetMapping("/detail/{id}")
    public DeptVO detail(@PathVariable("id") Long id) {
        return deptService.detail(id);
    }

    @ApiOperation("查询部门列表(排除查询节点)")
    @GetMapping("/exclude/{id}")
    public List<DeptVO> exclude(@PathVariable("id") Long id) {
        return deptService.exclude(id);
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除部门", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return deptService.delete(id);
    }

    @ApiOperation("新增部门")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增部门", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody DeptDTO dto) {
        return deptService.add(dto);
    }

    @ApiOperation("修改部门")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改部门", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody DeptDTO dto) {
        return deptService.update(dto);
    }

    @ApiOperation("获取树形结构部门")
    @GetMapping("/tree")
    public List<TreeVO> tree(DeptDTO dto) {
        return deptService.tree(dto);
    }
}

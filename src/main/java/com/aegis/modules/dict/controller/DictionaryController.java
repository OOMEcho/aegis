package com.aegis.modules.dict.controller;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.duplicate.PreventDuplicateSubmit;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.common.validator.ValidGroup;
import com.aegis.modules.dict.domain.dto.DictionaryDTO;
import com.aegis.modules.dict.domain.vo.DictionaryVO;
import com.aegis.modules.dict.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典接口
 *
 * @author xuesong.lei
 * @since 2025-09-08
 */
@RestController
@Api(tags = "字典接口")
@RequiredArgsConstructor
@RequestMapping("/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @ApiOperation("分页列表")
    @GetMapping("/pageList")
    public PageVO<DictionaryVO> pageList(DictionaryDTO dto) {
        return dictionaryService.pageList(dto);
    }

    @ApiOperation("删除字典")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除字典", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return dictionaryService.delete(id);
    }

    @ApiOperation("新增字典")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增字典", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody DictionaryDTO dto) {
        return dictionaryService.add(dto);
    }

    @ApiOperation("修改字典")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改字典", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody DictionaryDTO dto) {
        return dictionaryService.update(dto);
    }

    @ApiOperation("根据类型获取字典列表")
    @GetMapping("/list")
    public List<DictionaryVO> list(@RequestParam("dictType") String dictType) {
        return dictionaryService.list(dictType);
    }
}

package com.aegis.modules.whitelist.controller;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.duplicate.PreventDuplicateSubmit;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.common.validator.ValidGroup;
import com.aegis.modules.whitelist.domain.dto.WhitelistDTO;
import com.aegis.modules.whitelist.domain.vo.WhitelistVO;
import com.aegis.modules.whitelist.service.WhitelistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 白名单接口
 *
 * @author xuesong.lei
 * @since 2025/9/8 10:34
 */
@RestController
@Api(tags = "白名单接口")
@RequiredArgsConstructor
@RequestMapping("/whitelist")
public class WhitelistController {

    private final WhitelistService whitelistService;

    @ApiOperation("分页列表")
    @GetMapping("/pageList")
    public PageVO<WhitelistVO> pageList(WhitelistDTO dto) {
        return whitelistService.pageList(dto);
    }

    @ApiOperation("详情")
    @GetMapping("/detail/{id}")
    public WhitelistVO detail(@PathVariable("id") Long id) {
        return whitelistService.detail(id);
    }

    @ApiOperation("删除白名单")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除白名单", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return whitelistService.delete(id);
    }

    @ApiOperation("新增白名单")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增白名单", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody WhitelistDTO dto) {
        return whitelistService.add(dto);
    }

    @ApiOperation("修改白名单")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改白名单", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody WhitelistDTO dto) {
        return whitelistService.update(dto);
    }
}

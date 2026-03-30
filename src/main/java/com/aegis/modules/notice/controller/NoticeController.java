package com.aegis.modules.notice.controller;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.duplicate.PreventDuplicateSubmit;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.common.validator.ValidGroup;
import com.aegis.modules.notice.domain.dto.NoticeDTO;
import com.aegis.modules.notice.domain.vo.NoticeAdminVO;
import com.aegis.modules.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 通知接口
 *
 * @author xuesong.lei
 * @since 2025/9/10 14:09
 */
@RestController
@Tag(name = "通知接口")
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "分页列表")
    @GetMapping("/pageList")
    public PageVO<NoticeAdminVO> pageList(NoticeDTO dto) {
        return noticeService.pageList(dto);
    }

    @Operation(summary = "详情")
    @GetMapping("/detail/{id}")
    public NoticeAdminVO detail(@PathVariable("id") Long id) {
        return noticeService.detail(id);
    }

    @Operation(summary = "删除通知")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除通知", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return noticeService.delete(id);
    }

    @Operation(summary = "新增通知")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增通知", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody NoticeDTO dto) {
        return noticeService.add(dto);
    }

    @Operation(summary = "修改通知")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改通知", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody NoticeDTO dto) {
        return noticeService.update(dto);
    }

    @Operation(summary = "发布通知")
    @PutMapping("/publish/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "发布通知", businessType = BusinessType.UPDATE)
    public String publish(@PathVariable("id") Long id) {
        return noticeService.publish(id);
    }

    @Operation(summary = "撤销通知")
    @PutMapping("/revoke/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "撤销通知", businessType = BusinessType.UPDATE)
    public String revoke(@PathVariable("id") Long id) {
        return noticeService.revoke(id);
    }
}

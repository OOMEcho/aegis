package com.aegis.modules.notice.controller;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.modules.notice.domain.dto.NoticeUserDTO;
import com.aegis.modules.notice.domain.vo.NoticeVO;
import com.aegis.modules.notice.service.NoticeUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通知用户接口
 *
 * @author xuesong.lei
 * @since 2025/9/16 21:59
 */
@RestController
@Tag(name = "通知用户接口")
@RequestMapping("/notice/user")
@RequiredArgsConstructor
public class NoticeUserController {

    private final NoticeUserService noticeUserService;

    @Operation(summary = "分页列表")
    @GetMapping("/pageList")
    public PageVO<NoticeVO> pageList(NoticeUserDTO dto) {
        return noticeUserService.pageList(dto);
    }

    @Operation(summary = "详情")
    @GetMapping("/detail/{id}")
    public NoticeVO detail(@PathVariable("id") Long id) {
        return noticeUserService.detail(id);
    }

    @Operation(summary = "未读消息数")
    @GetMapping("/unreadCount")
    public Long unreadCount() {
        return noticeUserService.unreadCount();
    }
}

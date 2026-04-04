package com.aegis.modules.notice.service;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.modules.notice.domain.dto.NoticeUserDTO;
import com.aegis.modules.notice.domain.vo.NoticeVO;

/**
 * 通知用户业务层
 *
 * @author xuesong.lei
 * @since 2025-09-16
 */
public interface NoticeUserService {

    /**
     * 分页列表
     *
     * @param dto 查询参数
     * @return 通知分页列表
     */
    PageVO<NoticeVO> pageList(NoticeUserDTO dto);

    /**
     * 详情
     *
     * @param id 通知ID
     * @return 通知详情
     */
    NoticeVO detail(Long id);

    /**
     * 未读消息数
     *
     * @return 未读消息数
     */
    Long unreadCount();
}

package com.aegis.modules.notice.service;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.modules.notice.domain.dto.NoticeDTO;
import com.aegis.modules.notice.domain.vo.NoticeAdminVO;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/10 14:09
 * @Description: 通知业务层
 */
public interface NoticeService {

    /**
     * 分页列表
     *
     * @param dto 查询参数
     * @return 通知分页列表
     */
    PageVO<NoticeAdminVO> pageList(NoticeDTO dto);

    /**
     * 详情
     *
     * @param id 通知ID
     * @return 通知详情
     */
    NoticeAdminVO detail(Long id);

    /**
     * 删除通知
     *
     * @param id 通知ID
     * @return 响应消息
     */
    String delete(Long id);

    /**
     * 新增通知
     *
     * @param dto 通知DTO
     * @return 响应消息
     */
    String add(NoticeDTO dto);

    /**
     * 修改通知
     *
     * @param dto 通知DTO
     * @return 响应消息
     */
    String update(NoticeDTO dto);

    /**
     * 发布通知
     *
     * @param id 通知ID
     * @return 响应消息
     */
    String publish(Long id);

    /**
     * 撤销通知
     *
     * @param id 通知ID
     * @return 响应消息
     */
    String revoke(Long id);

    /**
     * 执行发布
     *
     * @param id     通知ID
     * @param userId 发布人ID(手动发布时为当前用户ID，自动发布时可为null)
     */
    void doPublish(Long id, Long userId);
}

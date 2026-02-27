package com.aegis.modules.notice.task;

import com.aegis.modules.notice.domain.entity.Notice;
import com.aegis.modules.notice.mapper.NoticeMapper;
import com.aegis.modules.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/18 14:15
 * @Description: 通知定时任务器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeScheduledTask {

    private final NoticeMapper noticeMapper;

    private final NoticeService noticeService;

    /**
     * 每分钟执行一次，扫描待发布通知
     * 注意：不在此方法上加 @Transactional，每条通知独立事务（由 noticeService.doPublish 内部管理）
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void autoPublishNotices() {
        log.info("开始扫描待发布通知...");

        List<Notice> pendingNotices = noticeMapper.selectPendingNotices();

        if (pendingNotices.isEmpty()) {
            log.info("无待发布通知，结束任务。");
            return;
        }

        log.info("共检测到 {} 条待发布通知", pendingNotices.size());

        for (Notice notice : pendingNotices) {
            try {
                log.info("自动发布通知，noticeId={}", notice.getId());

                noticeService.doPublish(notice.getId(), null);
            } catch (Exception e) {
                log.error("自动发布通知失败，noticeId={}", notice.getId(), e);
            }
        }

        log.info("本次扫描任务结束。");
    }
}

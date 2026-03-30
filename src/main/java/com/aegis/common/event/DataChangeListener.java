package com.aegis.common.event;

import com.aegis.common.constant.RedisConstants;
import com.aegis.config.security.customize.SecurityMetadataService;
import com.aegis.modules.common.domain.dto.UserRegisterDTO;
import com.aegis.modules.log.domain.entity.SysOperateLog;
import com.aegis.modules.log.mapper.SysOperateLogMapper;
import com.aegis.utils.EmailUtils;
import com.aegis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 数据变更监听器
 *
 * @author xuesong.lei
 * @since 2025/09/07 16:24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataChangeListener {

    private final RedisUtils redisUtils;

    private final EmailUtils emailUtils;

    private final SecurityMetadataService securityMetadataService;

    private final SysOperateLogMapper sysOperateLogMapper;

    @Async("asyncExecutor")
    @EventListener(DataChangeEvent.class)
    public void onDataChange(DataChangeEvent event) {
        switch (event.getType()) {
            case WHITELIST:
                handleWhitelistChange(event);
                break;
            case RESOURCE:
                handleResourceChange(event);
                break;
            case LOG:
                handleLog(event);
                break;
            case EMAIL:
                handleEmail(event);
                break;
            default:
                log.warn("未知事件类型: {}", event.getType());
        }
    }

    /**
     * 处理发送注册成功邮件
     */
    private void handleEmail(DataChangeEvent event) {
        UserRegisterDTO logData = (UserRegisterDTO) event.getPayload();
        emailUtils.sendWelcomeEmail(logData.getEmail(), logData.getUsername(), "aegis");
        log.info("注册成功邮件发送完成，描述: {}", event.getDescription());
    }

    /**
     * 处理白名单变更
     */
    private void handleWhitelistChange(DataChangeEvent event) {
        redisUtils.delete(RedisConstants.WHITELIST);
        securityMetadataService.loadDataSourceAllWhitelist();
        log.info("白名单数据刷新完成，描述: {}", event.getDescription());
    }

    /**
     * 处理资源变更
     */
    private void handleResourceChange(DataChangeEvent event) {
        redisUtils.delete(RedisConstants.RESOURCES);
        securityMetadataService.loadDataSourceAllResource();
        log.info("资源数据刷新完成，描述: {}", event.getDescription());
    }

    /**
     * 处理操作日志入库
     */
    private void handleLog(DataChangeEvent event) {
        SysOperateLog logData = (SysOperateLog) event.getPayload();
        sysOperateLogMapper.insert(logData);
        log.debug("操作日志已保存: {}", logData);
    }
}

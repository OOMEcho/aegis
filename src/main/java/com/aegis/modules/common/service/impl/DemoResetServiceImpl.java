package com.aegis.modules.common.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import com.aegis.common.constant.RedisConstants;
import com.aegis.config.property.DemoResetProperties;
import com.aegis.modules.common.domain.vo.DemoResetVO;
import com.aegis.modules.common.service.DemoResetService;
import com.aegis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 演示数据重置服务实现层
 *
 * @author xuesong.lei
 * @since 2026/2/3 14:42
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "demo.reset.enabled", havingValue = "true")
public class DemoResetServiceImpl implements DemoResetService {

    private final DataSource dataSource;

    private final RedisUtils redisUtils;

    private final DemoResetProperties demoResetProperties;

    /**
     * 需要重置的核心表
     */
    private static final List<String> TABLES_TO_RESET = Arrays.asList(
            "t_user_role",
            "t_role_permission",
            "t_role_dept",
            "t_menu_permission",
            "t_user",
            "t_role",
            "t_dept",
            "t_permission",
            "t_resource",
            "t_menu",
            "t_dictionary",
            "t_whitelist"
    );

    /**
     * 初始化 SQL 内容缓存
     */
    private String initDataSql;

    /**
     * Cron 表达式对象
     */
    private CronExpression cronExpression;

    @PostConstruct
    public void init() {
        // 加载初始化 SQL 文件
        try {
            ClassPathResource resource = new ClassPathResource("script/data.sql");
            try (InputStream inputStream = resource.getInputStream()) {
                initDataSql = IoUtil.read(inputStream, StandardCharsets.UTF_8);
                log.info("演示数据重置服务初始化成功，已加载 data.sql");
            }
        } catch (IOException e) {
            log.error("加载 data.sql 失败", e);
            throw new RuntimeException("无法加载演示数据初始化脚本", e);
        }

        // 解析 cron 表达式
        try {
            cronExpression = CronExpression.parse(demoResetProperties.getCron());
            log.info("演示数据重置 Cron 表达式: {}", demoResetProperties.getCron());
        } catch (Exception e) {
            log.error("解析 Cron 表达式失败: {}", demoResetProperties.getCron(), e);
            // 使用默认的每小时执行
            cronExpression = CronExpression.parse("0 0 * * * ?");
        }
    }

    @Override
    public DemoResetVO countdown() {
        DemoResetVO demoResetVO = new DemoResetVO();
        demoResetVO.setEnabled(isEnabled());
        demoResetVO.setNextResetTime(getNextResetTime());
        demoResetVO.setSecondsToNextReset(getSecondsToNextReset());
        demoResetVO.setLastResetTime(getLastResetTime());
        return demoResetVO;
    }

    /**
     * 执行数据重置
     *
     * @return 重置结果信息
     */
    public String resetData() {
        String lockKey = RedisConstants.DEMO_RESET_LOCK_KEY;
        String lockValue = UUID.randomUUID().toString();

        // 尝试获取分布式锁，防止并发执行
        boolean locked = redisUtils.tryLock(lockKey, lockValue, 5, TimeUnit.MINUTES);
        if (!locked) {
            log.warn("获取演示数据重置锁失败，可能有其他实例正在执行重置");
            return "重置任务正在执行中，请稍后再试";
        }

        try {
            log.info("========== 开始执行演示数据重置 ==========");
            long startTime = System.currentTimeMillis();

            // 1. 重置数据库数据
            resetDatabaseData();

            // 2. 清理 Redis 缓存
            clearRedisCache();

            // 3. 记录重置时间
            String resetTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            redisUtils.set(RedisConstants.DEMO_LAST_RESET_TIME, resetTime);

            long costTime = System.currentTimeMillis() - startTime;
            log.info("========== 演示数据重置完成，耗时: {} ms ==========", costTime);

            return "数据重置完成，已清理缓存并使所有用户下线";
        } catch (Exception e) {
            log.error("演示数据重置失败", e);
            return "数据重置失败: " + e.getMessage();
        } finally {
            redisUtils.unlock(lockKey, lockValue);
        }
    }

    /**
     * 重置数据库数据
     */
    private void resetDatabaseData() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                // 1. 禁用外键检查
                statement.execute("SET FOREIGN_KEY_CHECKS = 0");
                log.info("已禁用外键检查");

                // 2. 清空核心表
                for (String table : TABLES_TO_RESET) {
                    statement.execute("TRUNCATE TABLE " + table);
                    log.info("已清空表: {}", table);
                }

                // 3. 执行初始化 SQL
                // 将 SQL 按分号分割成多条语句执行
                String[] sqlStatements = initDataSql.split(";");
                int successCount = 0;
                for (String sql : sqlStatements) {
                    String trimmedSql = sql.trim();
                    if (!trimmedSql.isEmpty() && !trimmedSql.startsWith("--")) {
                        try {
                            statement.execute(trimmedSql);
                            successCount++;
                        } catch (SQLException e) {
                            log.warn("执行 SQL 失败: {}", trimmedSql.substring(0, Math.min(100, trimmedSql.length())), e);
                        }
                    }
                }
                log.info("已执行 {} 条初始化 SQL", successCount);

                // 4. 启用外键检查
                statement.execute("SET FOREIGN_KEY_CHECKS = 1");
                log.info("已启用外键检查");

                connection.commit();
                log.info("数据库事务已提交");

            } catch (SQLException e) {
                connection.rollback();
                log.error("数据库重置失败，已回滚", e);
                throw e;
            }
        }
    }

    /**
     * 清理 Redis 缓存
     */
    private void clearRedisCache() {
        log.info("开始清理 Redis 缓存...");

        // 1. 清理用户 session（使所有用户下线）
        deleteKeysByPattern(RedisConstants.SESSION + "*");
        log.info("已清理用户 session 缓存");

        // 2. 清理用户 session 反向索引
        deleteKeysByPattern(RedisConstants.USER_SESSION + "*");
        log.info("已清理用户 session 索引缓存");

        // 3. 清理 refresh token
        deleteKeysByPattern(RedisConstants.REFRESH + "*");
        log.info("已清理 refresh token 缓存");

        // 4. 清理用户 refresh 反向索引
        deleteKeysByPattern(RedisConstants.USER_REFRESH + "*");
        log.info("已清理用户 refresh 索引缓存");

        // 5. 清理资源缓存
        redisUtils.delete(RedisConstants.RESOURCES);
        log.info("已清理资源缓存");

        // 6. 清理白名单缓存
        redisUtils.delete(RedisConstants.WHITELIST);
        log.info("已清理白名单缓存");

        // 6. 清理验证码缓存
        deleteKeysByPattern(RedisConstants.SLIDER_CAPTCHA_KEY + "*");
        log.info("已清理验证码缓存");

        log.info("Redis 缓存清理完成");
    }

    /**
     * 根据模式删除 Redis keys
     */
    private void deleteKeysByPattern(String pattern) {
        try {
            Set<String> keys = redisUtils.keys(pattern);
            if (!keys.isEmpty()) {
                redisUtils.delete(keys);
            }
        } catch (Exception e) {
            log.warn("使用模式 {} 清理缓存失败: {}", pattern, e.getMessage());
        }
    }

    /**
     * 获取下次重置时间
     *
     * @return 下次重置时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    private String getNextResetTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextTime = cronExpression.next(now);
        if (nextTime != null) {
            return nextTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return null;
    }

    /**
     * 获取距离下次重置的秒数
     *
     * @return 秒数
     */
    private long getSecondsToNextReset() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextTime = cronExpression.next(now);
        if (nextTime != null) {
            return Duration.between(now, nextTime).getSeconds();
        }
        return -1;
    }

    /**
     * 获取上次重置时间
     *
     * @return 上次重置时间
     */
    private String getLastResetTime() {
        return redisUtils.get(RedisConstants.DEMO_LAST_RESET_TIME);
    }

    /**
     * 是否启用演示重置功能
     */
    private boolean isEnabled() {
        return demoResetProperties.isEnabled();
    }

    /**
     * 定时执行演示数据重置
     * 默认每小时整点执行，可通过配置 demo.reset.cron 修改
     */
    @Scheduled(cron = "${demo.reset.cron:0 0 * * * ?}")
    public void autoReset() {
        log.info("定时任务触发：开始执行演示数据重置...");
        String result = resetData();
        log.info("定时任务完成：{}", result);
    }
}

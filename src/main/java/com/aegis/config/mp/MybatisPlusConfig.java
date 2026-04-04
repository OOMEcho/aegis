package com.aegis.config.mp;

import com.aegis.common.datascope.DataPermissionInterceptor;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-Plus 配置
 *
 * @author xuesong.lei
 * @since 2025-08-23
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new DataPermissionInterceptor());// 数据权限
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));// 分页
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());// 乐观锁
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());// 防止全表更新与删除
        return interceptor;
    }
}

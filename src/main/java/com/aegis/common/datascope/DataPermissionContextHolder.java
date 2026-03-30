package com.aegis.common.datascope;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 数据权限上下文持有者 - 线程本地变量
 *
 * @author xuesong.lei
 * @since 2025/09/11 15:30
 */
public class DataPermissionContextHolder {

    private static final ThreadLocal<DataPermissionInfo> holder = new ThreadLocal<>();

    /**
     * 设置数据权限信息到线程本地变量
     */
    public static void set(DataPermissionContext context, DataPermission annotation) {
        if (context != null && annotation != null) {
            holder.set(new DataPermissionInfo(context, annotation));
        }
    }

    /**
     * 获取当前线程的数据权限信息
     */
    public static DataPermissionInfo get() {
        return holder.get();
    }

    /**
     * 清除当前线程的数据权限信息
     */
    public static void clear() {
        holder.remove();
    }

    /**
     * 数据权限信息封装类
     */
    @Data
    @AllArgsConstructor
    public static class DataPermissionInfo {
        /**
         * 数据权限上下文
         */
        private DataPermissionContext context;

        /**
         * 数据权限注解
         */
        private DataPermission annotation;
    }
}

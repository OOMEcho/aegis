package com.aegis.utils;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/21 15:24
 * @Description: Spring上下文工具类，用于在非Spring管理的类中获取Spring容器中的Bean
 */
@Getter
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 通过name获取Bean
     */
    public static Object getBean(String name) {
        checkApplicationContext();
        return applicationContext.getBean(name);
    }

    /**
     * 通过class获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过name和class获取指定的Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 获取指定类型的所有Bean
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBeansOfType(clazz);
    }

    /**
     * 检查Bean是否存在
     */
    public static boolean containsBean(String name) {
        checkApplicationContext();
        return applicationContext.containsBean(name);
    }

    /**
     * 判断Bean是否为单例
     */
    public static boolean isSingleton(String name) {
        checkApplicationContext();
        return applicationContext.isSingleton(name);
    }

    /**
     * 获取Bean的类型
     */
    public static Class<?> getType(String name) {
        checkApplicationContext();
        return applicationContext.getType(name);
    }

    /**
     * 获取Bean的别名
     */
    public static String[] getAliases(String name) {
        checkApplicationContext();
        return applicationContext.getAliases(name);
    }

    /**
     * 发布事件
     */
    public static void publishEvent(Object event) {
        checkApplicationContext();
        applicationContext.publishEvent(event);
    }

    /**
     * 获取当前环境
     */
    public static String[] getActiveProfiles() {
        checkApplicationContext();
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取配置属性
     */
    public static String getProperty(String key) {
        checkApplicationContext();
        return applicationContext.getEnvironment().getProperty(key);
    }

    /**
     * 获取配置属性，带默认值
     */
    public static String getProperty(String key, String defaultValue) {
        checkApplicationContext();
        return applicationContext.getEnvironment().getProperty(key, defaultValue);
    }

    /**
     * 检查ApplicationContext是否已初始化
     */
    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext未初始化，请检查SpringContextUtil是否已被Spring管理");
        }
    }
}

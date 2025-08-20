package com.aegis.common.result;

/**
 * @Author: xuesong.lei
 * @Date: 2022/11/18 19:00
 * @Description: 提供自定义异常所需的方法
 */
public interface ResultCodeInterface {

    /**
     * 返回状态值
     *
     * @return 状态值
     */
    Integer getCode();

    /**
     * 消息
     *
     * @return 具体信息
     */
    String getMessage();
}

package com.aegis.common.mask;

import lombok.Getter;

/**
 * 脱敏类型枚举
 *
 * @author xuesong.lei
 * @since 2025/9/20 13:45
 */
@Getter
public enum MaskTypeEnum {
    PHONE("phone", "手机号脱敏"),
    ID_CARD("idCard", "身份证脱敏"),
    EMAIL("email", "邮箱脱敏"),
    BANK_CARD("bankCard", "银行卡脱敏"),
    NAME("name", "姓名脱敏"),
    ADDRESS("address", "地址脱敏"),
    CUSTOM("custom", "自定义脱敏");

    private final String type;
    private final String description;

    MaskTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }
}

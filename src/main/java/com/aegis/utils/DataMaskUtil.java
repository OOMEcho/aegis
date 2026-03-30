package com.aegis.utils;

import com.aegis.common.mask.MaskTypeEnum;
import org.springframework.util.StringUtils;

/**
 * 数据脱敏工具类
 *
 * @author xuesong.lei
 * @since 2025/9/20 13:45
 */
public final class DataMaskUtil {

    private static final String DEFAULT_MASK_CHAR = "*";

    private DataMaskUtil() {
    }

    /**
     * 根据类型进行脱敏
     */
    public static String mask(String data, MaskTypeEnum type) {
        return mask(data, type, DEFAULT_MASK_CHAR);
    }

    public static String mask(String data, MaskTypeEnum type, String maskChar) {
        if (!StringUtils.hasText(data)) {
            return data;
        }

        return switch (type) {
            case PHONE -> maskPhone(data, maskChar);
            case ID_CARD -> maskIdCard(data, maskChar);
            case EMAIL -> maskEmail(data, maskChar);
            case BANK_CARD -> maskBankCard(data, maskChar);
            case NAME -> maskName(data, maskChar);
            case ADDRESS -> maskAddress(data, maskChar);
            default -> data;
        };
    }

    /**
     * 自定义脱敏
     */
    public static String maskCustom(String data, int prefixKeep, int suffixKeep, String maskChar) {
        if (!StringUtils.hasText(data)) {
            return data;
        }

        int length = data.length();
        if (prefixKeep + suffixKeep >= length) {
            return data;
        }

        StringBuilder masked = new StringBuilder();

        // 保留前缀
        if (prefixKeep > 0) {
            masked.append(data, 0, prefixKeep);
        }

        // 中间脱敏
        int maskLength = length - prefixKeep - suffixKeep;
        masked.append(repeat(maskChar, maskLength));

        // 保留后缀
        if (suffixKeep > 0) {
            masked.append(data, length - suffixKeep, length);
        }

        return masked.toString();
    }

    /**
     * 手机号脱敏：138****8888
     */
    private static String maskPhone(String phone, String maskChar) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + repeat(maskChar, 4) + phone.substring(7);
    }

    /**
     * 身份证脱敏：110***********1234
     */
    private static String maskIdCard(String idCard, String maskChar) {
        if (idCard == null || idCard.length() < 8) {
            return idCard;
        }
        int length = idCard.length();
        return idCard.substring(0, 3) + repeat(maskChar, length - 7) + idCard.substring(length - 4);
    }

    /**
     * 邮箱脱敏：user***@example.com
     */
    private static String maskEmail(String email, String maskChar) {
        if (email == null || !email.contains("@")) {
            return email;
        }

        String[] parts = email.split("@");
        String localPart = parts[0];

        if (localPart.length() <= 3) {
            return email;
        }

        String maskedLocal = localPart.substring(0, 2) + repeat(maskChar, localPart.length() - 2);
        return maskedLocal + "@" + parts[1];
    }

    /**
     * 银行卡脱敏：6222 **** **** 1234
     */
    private static String maskBankCard(String bankCard, String maskChar) {
        if (bankCard == null || bankCard.length() < 8) {
            return bankCard;
        }

        // 移除空格
        String cleanCard = bankCard.replaceAll("\\s", "");
        int length = cleanCard.length();

        String masked = cleanCard.substring(0, 4) + repeat(maskChar, length - 8) + cleanCard.substring(length - 4);

        // 重新格式化为4位一组
        return formatBankCard(masked);
    }

    /**
     * 姓名脱敏：张*明
     */
    private static String maskName(String name, String maskChar) {
        if (name == null || name.length() < 2) {
            return name;
        }

        if (name.length() == 2) {
            return name.charAt(0) + maskChar;
        }

        return name.charAt(0) + repeat(maskChar, name.length() - 2) + name.charAt(name.length() - 1);
    }

    /**
     * 地址脱敏：北京市朝阳区****
     */
    private static String maskAddress(String address, String maskChar) {
        if (address == null || address.length() <= 6) {
            return address;
        }
        return address.substring(0, 6) + repeat(maskChar, address.length() - 6);
    }

    private static String formatBankCard(String card) {
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < card.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" ");
            }
            formatted.append(card.charAt(i));
        }
        return formatted.toString();
    }

    private static String repeat(String str, int count) {
        if (str == null || count <= 0) {
            return "";
        }
        return str.repeat(count);
    }
}

package com.aegis.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * HTML内容清理工具类
 * 用于清理富文本内容，防止XSS攻击
 *
 * @Author: xuesong.lei
 * @Date: 2025/9/16
 */
public final class HtmlSanitizer {

    /**
     * 富文本白名单配置
     * 基于 Jsoup 的 relaxed 模式扩展，支持常用的格式标签、表格、链接、图片等
     */
    private static final Safelist RICH_TEXT_SAFELIST = Safelist.relaxed()
            // 允许 span 标签（用于样式）
            .addTags("span")
            // 允许 style 属性（用于行内样式）
            .addAttributes(":all", "style", "class")
            // 允许 a 标签的 target 和 rel 属性
            .addAttributes("a", "target", "rel")
            // img 标签只允许 http/https 协议，防止 javascript: 和 data: 协议
            .addProtocols("img", "src", "http", "https")
            // a 标签只允许 http/https/mailto 协议
            .addProtocols("a", "href", "http", "https", "mailto")
            // 允许表格相关属性
            .addAttributes("table", "border", "cellpadding", "cellspacing", "width")
            .addAttributes("td", "colspan", "rowspan", "width", "height")
            .addAttributes("th", "colspan", "rowspan", "width", "height");

    private HtmlSanitizer() {
        // 私有构造函数，防止实例化
    }

    /**
     * 清理富文本HTML内容
     * 移除危险的脚本和不安全的标签/属性，保留安全的富文本格式
     *
     * @param html 原始HTML内容
     * @return 清理后的安全HTML，如果输入为null或空则原样返回
     */
    public static String sanitize(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }
        return Jsoup.clean(html, RICH_TEXT_SAFELIST);
    }

    /**
     * 清理为纯文本（移除所有HTML标签）
     * 适用于需要提取纯文本内容的场景，如搜索索引、摘要生成等
     *
     * @param html 原始HTML内容
     * @return 纯文本内容，如果输入为null或空则原样返回
     */
    public static String toPlainText(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }
        return Jsoup.clean(html, Safelist.none());
    }
}

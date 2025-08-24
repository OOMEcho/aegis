package com.aegis.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/24 15:10
 * @Description: 邮箱工具类, 支持多种邮件发送方式：简单文本、HTML、批量发送等
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailUtils {

    private final JavaMailSender mailSender;

    private final Configuration freemarkerConfig;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("发送简单邮件失败->{}", e.getMessage());
            throw new RuntimeException("发送简单邮件失败", e);
        }
    }

    /**
     * 批量发送邮件
     *
     * @param recipients 收件人列表
     * @param subject    邮件主题
     * @param content    邮件内容
     */
    public void sendBatchEmail(List<String> recipients, String subject, String content) {
        if (recipients == null || recipients.isEmpty()) {
            throw new IllegalArgumentException("收件人列表不能为空");
        }

        for (String recipient : recipients) {
            if (StringUtils.hasText(recipient)) {
                try {
                    sendSimpleEmail(recipient, subject, content);
                } catch (Exception e) {
                    // 记录失败的邮件，但继续发送其他邮件
                    log.error("批量发送简单邮件到 {} 失败: {}", recipient, e.getMessage());
                }
            }
        }
    }

    /**
     * 发送HTML邮件
     *
     * @param to          收件人邮箱
     * @param subject     邮件主题
     * @param htmlContent HTML内容
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送HTML邮件失败->{}", e.getMessage());
            throw new RuntimeException("发送HTML邮件失败", e);
        }
    }

    /**
     * 批量发送HTML邮件
     *
     * @param recipients 收件人列表
     * @param subject    邮件主题
     * @param content    HTML内容
     */
    public void sendBatchHtmlMail(List<String> recipients, String subject, String content) {
        if (recipients == null || recipients.isEmpty()) {
            throw new IllegalArgumentException("收件人列表不能为空");
        }
        for (String recipient : recipients) {
            if (StringUtils.hasText(recipient)) {
                try {
                    sendHtmlEmail(recipient, subject, content);
                } catch (Exception e) {
                    // 记录失败的邮件，但继续发送其他邮件
                    log.error("批量发送HTML邮件到 {} 失败: {}", recipient, e.getMessage());
                }
            }
        }
    }

    /**
     * 发送FreeMarker模板邮件
     *
     * @param to           收件人
     * @param subject      主题内容
     * @param templateName 模板名称
     * @param model        模板数据
     */
    public void sendTemplateMail(String to, String subject, String templateName, Map<String, Object> model) {
        try {
            Template template = freemarkerConfig.getTemplate(templateName);
            String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            sendHtmlEmail(to, subject, htmlContent);
        } catch (IOException | TemplateException e) {
            log.error("发送 {} 模板邮件失败 {}", templateName, e.getMessage());
            throw new RuntimeException("发送模板邮件失败", e);
        }
    }

    /**
     * 批量发送FreeMarker模板邮件
     *
     * @param recipients   收件人列表
     * @param subject      主题内容
     * @param templateName 模板名称
     * @param model        模板数据
     */
    public void sendBatchTemplateMail(List<String> recipients, String subject, String templateName, Map<String, Object> model) {
        try {
            Template template = freemarkerConfig.getTemplate(templateName);
            String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            sendBatchHtmlMail(recipients, subject, htmlContent);
        } catch (IOException | TemplateException e) {
            log.error("批量发送 {} 模板邮件失败 {}", templateName, e.getMessage());
            throw new RuntimeException("发送模板邮件失败", e);
        }
    }

    /**
     * 发送验证码邮件
     *
     * @param to            收件人
     * @param code          验证码
     * @param expireMinutes 过期时间（分钟）
     */
    public void sendVerificationCode(String to, String code, int expireMinutes) {
        Map<String, Object> model = new HashMap<>();
        model.put("email", to);
        model.put("code", code);
        model.put("expireMinutes", expireMinutes);
        sendTemplateMail(to, "邮箱验证码", "verification-code.ftl", model);
    }

    /**
     * 发送欢迎注册邮件
     *
     * @param to          收件人
     * @param username    用户名
     * @param websiteName 网站名称
     */
    public void sendWelcomeEmail(String to, String username, String websiteName) {
        Map<String, Object> model = new HashMap<>();
        model.put("username", username);
        model.put("email", to);
        model.put("websiteName", websiteName);
        model.put("year", String.valueOf(Year.now()));
        sendTemplateMail(to, "欢迎注册 " + websiteName, "welcome-register.ftl", model);
    }

    /**
     * 发送抄送和密送邮件
     *
     * @param to      收件人
     * @param cc      抄送人列表
     * @param bcc     密送人列表
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param isHtml  是否为HTML内容
     */
    public void sendEmailWithCcAndBcc(String to, List<String> cc, List<String> bcc, String subject, String content, boolean isHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, isHtml);

            // 设置抄送
            if (cc != null && !cc.isEmpty()) {
                helper.setCc(cc.toArray(new String[0]));
            }

            // 设置密送
            if (bcc != null && !bcc.isEmpty()) {
                helper.setBcc(bcc.toArray(new String[0]));
            }

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送抄送密送邮件失败->{}", e.getMessage());
            throw new RuntimeException("发送抄送密送邮件失败", e);
        }
    }

    /**
     * 验证邮箱格式
     *
     * @param email 邮箱地址
     * @return 是否有效
     */
    private boolean isValidEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }
}

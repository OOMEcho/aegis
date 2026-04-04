package com.aegis.modules.common.service;

import com.aegis.common.domain.vo.CaptchaVO;
import com.aegis.modules.common.domain.dto.UserRegisterDTO;
import com.aegis.modules.common.domain.dto.UserUpdateDTO;
import com.aegis.modules.user.domain.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 个人业务层
 *
 * @author xuesong.lei
 * @since 2025-09-14
 */
public interface ProfileService {

    /**
     * 生成验证码
     *
     * @return 验证码对象
     */
    CaptchaVO generateCaptcha();

    /**
     * 发送注册验证码
     *
     * @param email 邮箱
     * @return 响应消息
     */
    String sendEmailCode(String email);

    /**
     * 刷新令牌
     *
     * @param request  请求
     * @param response 响应
     * @return 新的令牌
     */
    String refreshToken(HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    UserVO info();

    /**
     * 预览用户头像
     *
     * @param response 响应
     */
    void previewAvatar(HttpServletResponse response);

    /**
     * 注册用户
     *
     * @param dto 用户注册DTO
     * @return 结果
     */
    String register(UserRegisterDTO dto);

    /**
     * 上传用户头像
     *
     * @param file 文件
     * @return 结果
     */
    String uploadUserAvatar(MultipartFile file);

    /**
     * 修改个人信息
     *
     * @param dto 用户修改信息DTO
     * @return 结果
     */
    String updateProfile(UserUpdateDTO dto);

    /**
     * 修改用户密码
     *
     * @param dto 用户修改信息DTO
     * @return 结果
     */
    String updatePassword(UserUpdateDTO dto);
}

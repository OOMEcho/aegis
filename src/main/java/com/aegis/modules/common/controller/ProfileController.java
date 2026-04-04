package com.aegis.modules.common.controller;

import com.aegis.common.domain.vo.CaptchaVO;
import com.aegis.common.duplicate.PreventDuplicateSubmit;
import com.aegis.common.limiter.LimitType;
import com.aegis.common.limiter.RateLimiter;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.modules.common.domain.dto.UserRegisterDTO;
import com.aegis.modules.common.domain.dto.UserUpdateDTO;
import com.aegis.modules.common.service.ProfileService;
import com.aegis.modules.user.domain.vo.UserVO;
import com.aegis.utils.RsaUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 个人接口
 *
 * @author xuesong.lei
 * @since 2025-09-14
 */
@Tag(name = "个人接口")
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @RateLimiter(limitType = LimitType.IP)
    @Operation(summary = "生成验证码")
    @GetMapping("/generate/captcha")
    public CaptchaVO generateCaptcha() {
        return profileService.generateCaptcha();
    }

    @RateLimiter(limitType = LimitType.IP)
    @Operation(summary = "发送注册验证码")
    @GetMapping("/sendEmailCode")
    public String sendEmailCode(@RequestParam("email") String email) {
        return profileService.sendEmailCode(email);
    }

    @Operation(summary = "刷新token")
    @GetMapping("/refreshToken")
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return profileService.refreshToken(request, response);
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public UserVO info() {
        return profileService.info();
    }

    @Operation(summary = "预览用户头像")
    @GetMapping("/avatar/preview")
    public void previewAvatar(HttpServletResponse response) {
        profileService.previewAvatar(response);
    }

    @Operation(summary = "注册用户")
    @PostMapping("/register")
    @PreventDuplicateSubmit
    @RateLimiter(limitType = LimitType.IP)
    @OperationLog(moduleTitle = "注册用户", businessType = BusinessType.INSERT)
    public String register(@Validated @RequestBody UserRegisterDTO dto) {
        return profileService.register(dto);
    }

    @Operation(summary = "上传用户头像")
    @PostMapping("/upload/avatar")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "上传用户头像", businessType = BusinessType.IMPORT)
    public String uploadUserAvatar(@RequestParam("file") MultipartFile file) {
        return profileService.uploadUserAvatar(file);
    }

    @Operation(summary = "修改个人信息")
    @PutMapping("/updateProfile")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改个人信息", businessType = BusinessType.UPDATE)
    public String updateProfile(@RequestBody UserUpdateDTO dto) {
        return profileService.updateProfile(dto);
    }

    @Operation(summary = "修改用户密码")
    @PutMapping("/updatePassword")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改用户密码", businessType = BusinessType.UPDATE)
    public String updatePassword(@Validated @RequestBody UserUpdateDTO dto) {
        return profileService.updatePassword(dto);
    }

    @Operation(summary = "获取RSA公钥")
    @GetMapping("/publicKey")
    public String publicKey() {
        return RsaUtils.getKeyPair().getPublicKey();
    }
}

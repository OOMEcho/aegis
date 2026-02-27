package com.aegis.modules.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aegis.common.constant.CommonConstants;
import com.aegis.common.constant.RedisConstants;
import com.aegis.common.domain.vo.CaptchaVO;
import com.aegis.common.event.DataChangePublisher;
import com.aegis.common.exception.BusinessException;
import com.aegis.common.file.FileStorageServiceFactory;
import com.aegis.common.file.service.FileStorageService;
import com.aegis.common.result.ResultCodeEnum;
import com.aegis.modules.common.domain.dto.UserRegisterDTO;
import com.aegis.modules.common.domain.dto.UserUpdateDTO;
import com.aegis.modules.common.service.EmailService;
import com.aegis.modules.common.service.ProfileService;
import com.aegis.modules.dept.mapper.DeptMapper;
import com.aegis.modules.file.domain.entity.FileMetadata;
import com.aegis.modules.file.service.FileService;
import com.aegis.modules.menu.domain.entity.Menu;
import com.aegis.modules.menu.domain.vo.RouterVo;
import com.aegis.modules.menu.mapper.MenuMapper;
import com.aegis.modules.permission.mapper.PermissionMapper;
import com.aegis.modules.role.domain.entity.Role;
import com.aegis.modules.user.domain.entity.User;
import com.aegis.modules.user.domain.vo.UserVO;
import com.aegis.modules.user.mapper.UserMapper;
import com.aegis.modules.user.mapper.UserRoleMapper;
import com.aegis.modules.user.service.UserConvert;
import com.aegis.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/14 10:52
 * @Description: 个人业务实现层
 */
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "webp", "bmp"
    ));

    private final CaptchaUtils captchaUtils;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserConvert userConvert;

    private final DeptMapper deptMapper;

    private final UserRoleMapper userRoleMapper;

    private final MenuMapper menuMapper;

    private final PermissionMapper permissionMapper;

    private final UserMapper userMapper;

    private final EmailService emailService;

    private final FileService fileService;

    private final FileStorageServiceFactory fileStorageServiceFactory;

    private final DataChangePublisher dataChangePublisher;

    private final RedisUtils redisUtils;

    private final LoginSecurityProperties loginSecurityProperties;

    @Override
    public CaptchaVO generateCaptcha() {
        return captchaUtils.generateCaptcha();
    }

    @Override
    public String sendEmailCode(String email) {
        return emailService.sendEmailCode(email);
    }

    @Override
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new BusinessException(ResultCodeEnum.NOT_LOGGED_IN);
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> CommonConstants.REFRESH_TOKEN_COOKIE.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (StrUtil.isBlank(refreshToken)) {
            throw new BusinessException(ResultCodeEnum.NOT_LOGGED_IN);
        }

        // 验证 refresh_token 签名
        if (!jwtTokenUtil.validateToken(refreshToken) || !jwtTokenUtil.isRefreshToken(refreshToken)) {
            throw new BusinessException(ResultCodeEnum.NOT_LOGGED_IN);
        }

        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
        String refreshJti = jwtTokenUtil.getJti(refreshToken);
        String refreshKey = RedisConstants.USER_REFRESH_JTI + username;
        String currentRefreshJti = redisUtils.get(refreshKey);
        if (StrUtil.isBlank(currentRefreshJti) || !currentRefreshJti.equals(refreshJti)) {
            throw new BusinessException(ResultCodeEnum.NOT_LOGGED_IN);
        }

        JwtTokenUtil.TokenResponse tokenResponse = jwtTokenUtil.refreshAccessToken(refreshToken);
        String accessJti = jwtTokenUtil.getJti(tokenResponse.getAccessToken());
        Long accessExpireSeconds = jwtTokenUtil.getAccessTokenExpireSeconds(tokenResponse.getAccessToken());
        redisUtils.set(RedisConstants.USER_TOKEN_JTI + username, accessJti, accessExpireSeconds, TimeUnit.SECONDS);

        String newRefreshJti = jwtTokenUtil.getJti(tokenResponse.getRefreshToken());
        redisUtils.set(refreshKey, newRefreshJti, jwtTokenUtil.getRefreshTokenExpiration(), TimeUnit.SECONDS);

        Cookie cookie = new Cookie(CommonConstants.REFRESH_TOKEN_COOKIE, tokenResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/api/profile/refreshToken");
        cookie.setSecure(loginSecurityProperties.isCookieSecure());
        cookie.setMaxAge(Math.toIntExact(jwtTokenUtil.getRefreshTokenExpiration()));
        response.addCookie(cookie);

        return tokenResponse.getAccessToken();
    }

    @Override
    public UserVO info() {
        User currentUser = SecurityUtils.getCurrentUser();

        UserVO userVo = userConvert.toUserVo(currentUser);

        if (userVo.getDeptId() != null) {
            String deptName = deptMapper.selectById(userVo.getDeptId()).getDeptName();
            userVo.setDeptName(deptName);
        }

        List<Role> roles = userRoleMapper.selectRoleByUserId(userVo.getId());
        userVo.setRoleList(roles);

        List<String> permissions = permissionMapper.selectPermCodesByUserId(userVo.getId());
        userVo.setPermissions(permissions);

        List<Menu> menuList = menuMapper.selectMenuByUserId(userVo.getId());

        List<Menu> menuTree = TreeUtil.makeTree(
                menuList,
                Menu::getParentId,
                Menu::getId,
                menu -> menu.getParentId() == null || menu.getParentId() == 0L,
                Menu::setChildren);

        userVo.setRouterVoList(buildRouters(menuTree));

        return userVo;
    }

    @Override
    public void previewAvatar(HttpServletResponse response) {
        User currentUser = SecurityUtils.getCurrentUser();
        String avatarPath = currentUser.getAvatar();

        if (StrUtil.isBlank(avatarPath)) {
            return;
        }

        String extension = getFileExtension(avatarPath);
        if (!IMAGE_EXTENSIONS.contains(extension)) {
            throw new BusinessException("头像格式不支持");
        }

        response.setContentType(getImageContentType(extension));
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");

        FileStorageService storageService = fileStorageServiceFactory.getFileStorageService();
        try (InputStream inputStream = storageService.download(avatarPath);
             ServletOutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        } catch (Exception e) {
            throw new BusinessException("头像预览失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String register(UserRegisterDTO dto) {
        // 校验滑块验证码
        if (!captchaUtils.verifyCaptcha(dto.getCaptchaKey(), dto.getSlideX())) {
            throw new BusinessException("验证码校验失败");
        }

        // 校验邮箱验证码
        emailService.validateEmailCode(dto.getEmail(), dto.getCode(), false);

        if (!RsaUtils.decryptByPrivateKey(dto.getPassword()).equals(RsaUtils.decryptByPrivateKey(dto.getConfirmPassword()))) {
            throw new BusinessException("两次密码不一致");
        }

        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getUsername, dto.getUsername())
                .or()
                .eq(User::getEmail, dto.getEmail());
        if (userMapper.selectCount(userQueryWrapper) > 0) {
            throw new BusinessException("用户名或邮箱已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(RsaUtils.decryptByPrivateKey(dto.getPassword())));
        user.setNickname(dto.getUsername());
        user.setEmail(dto.getEmail());

        userMapper.insert(user);

        // 发送注册成功事件
        dataChangePublisher.publishSendRegisterSuccess(dto);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadUserAvatar(MultipartFile file) {
        FileMetadata fileMetadata = fileService.uploadFile(file, null);

        User user = new User();
        user.setId(SecurityUtils.getUserId());
        user.setAvatar(fileMetadata.getFilePath());
        userMapper.updateById(user);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateProfile(UserUpdateDTO dto) {
        User currentUser = SecurityUtils.getCurrentUser();

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(dto.getEmail()), User::getEmail, dto.getEmail())
                .or()
                .eq(StringUtils.isNotBlank(dto.getPhone()), User::getPhone, dto.getPhone())
                .ne(User::getId, currentUser.getId());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("邮箱或手机号已存在");
        }

        User user = new User();
        user.setId(currentUser.getId());
        user.setNickname(dto.getNickname());
        user.setPhone(dto.getPhone());
        user.setSex(dto.getSex());
        user.setEmail(dto.getEmail());

        userMapper.updateById(user);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updatePassword(UserUpdateDTO dto) {
        User currentUser = SecurityUtils.getCurrentUser();
        final String password = currentUser.getPassword();

        if (!SecurityUtils.matchesPassword(RsaUtils.decryptByPrivateKey(dto.getOldPassword()), currentUser.getPassword())) {
            throw new BusinessException("旧密码错误");
        }

        if (SecurityUtils.matchesPassword(RsaUtils.decryptByPrivateKey(dto.getPassword()), password)) {
            throw new BusinessException("新密码不能与旧密码相同");
        }

        if (!RsaUtils.decryptByPrivateKey(dto.getPassword()).equals(RsaUtils.decryptByPrivateKey(dto.getConfirmPassword()))) {
            throw new BusinessException("两次密码不一致");
        }

        User user = new User();
        user.setId(SecurityUtils.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(RsaUtils.decryptByPrivateKey(dto.getPassword())));
        userMapper.updateById(user);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1) {
            return "";
        }
        return filePath.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * 根据扩展名获取图片内容类型
     */
    private String getImageContentType(String extension) {
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            case "bmp":
                return "image/bmp";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * 构建前端路由
     */
    private List<RouterVo> buildRouters(List<Menu> menuTree) {
        List<RouterVo> routers = new ArrayList<>();

        for (Menu menu : menuTree) {
            RouterVo router = new RouterVo();
            router.setName(StrUtil.upperFirst(StringUtils.isNotEmpty(menu.getName()) ? menu.getName() : menu.getPath()));
            router.setPath(menu.getPath());
            router.setTitle(menu.getMenuName());
            router.setIcon(menu.getIcon());
            router.setHidden(menu.getHidden());

            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                router.setChildren(buildRouters(menu.getChildren()));
            }

            routers.add(router);
        }
        return routers;
    }
}

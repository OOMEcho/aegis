package com.aegis.modules.user.service.impl;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.constant.RedisConstants;
import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.exception.BusinessException;
import com.aegis.modules.dept.mapper.DeptMapper;
import com.aegis.modules.user.domain.dto.UserDTO;
import com.aegis.modules.user.domain.entity.User;
import com.aegis.modules.user.domain.entity.UserRole;
import com.aegis.modules.user.domain.vo.UserVO;
import com.aegis.modules.user.mapper.UserMapper;
import com.aegis.modules.user.mapper.UserRoleMapper;
import com.aegis.modules.user.service.UserConvert;
import com.aegis.modules.user.service.UserService;
import com.aegis.utils.PageUtils;
import com.aegis.utils.RedisUtils;
import com.aegis.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/4 22:50
 * @Description: 用户业务实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final DeptMapper deptMapper;

    private final UserRoleMapper userRoleMapper;

    private final UserConvert userConvert;

    private final RedisUtils redisUtils;

    @Override
    public PageVO<UserVO> pageList(UserDTO dto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotBlank(dto.getNickname()), User::getNickname, dto.getNickname())
                .like(StringUtils.isNotBlank(dto.getUsername()), User::getUsername, dto.getUsername())
                .like(StringUtils.isNotBlank(dto.getEmail()), User::getEmail, dto.getEmail())
                .like(StringUtils.isNotBlank(dto.getPhone()), User::getPhone, dto.getPhone())
                .eq(StringUtils.isNotBlank(dto.getStatus()), User::getStatus, dto.getStatus())
                .and(ObjectUtils.isNotNull(dto.getDeptId()), w -> w
                        .eq(User::getDeptId, dto.getDeptId())
                        .or()
                        .apply("dept_id IN (SELECT id FROM t_dept WHERE FIND_IN_SET({0}, ancestors))", dto.getDeptId())
                )
                .between(ObjectUtils.isNotNull(dto.getBeginTime()) && ObjectUtils.isNotNull(dto.getEndTime()),
                        User::getCreateTime,
                        dto.getBeginTime(), dto.getEndTime());

        PageVO<UserVO> pageResult = PageUtils.of(dto).pagingAndConvert(userMapper, queryWrapper, userConvert::toUserVo);
        setOnlineStatus(pageResult.getRecords());
        setDeptInfo(pageResult.getRecords());
        setRoleInfo(pageResult.getRecords());
        return pageResult;
    }

    @Override
    public UserVO detail(Long id) {
        User user = userMapper.selectById(id);
        UserVO userVo = userConvert.toUserVo(user);
        userVo.setRoleList(userRoleMapper.selectRoleByUserId(userVo.getId()));
        userVo.setDept(deptMapper.selectById(user.getDeptId()));
        return userVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateStatus(Long id) {
        checkUserAllowed(id);

        checkCurrentUserAllowed(id);

        User user = userMapper.selectById(id);
        if (ObjectUtils.isNotNull(user)) {
            if (CommonConstants.NORMAL_STATUS.equals(user.getStatus())) {
                user.setStatus(CommonConstants.DISABLE_STATUS);
            } else {
                user.setStatus(CommonConstants.NORMAL_STATUS);
            }

            userMapper.updateById(user);

        }
        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(Long id) {
        checkUserAllowed(id);

        checkCurrentUserAllowed(id);

        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, id));

        userMapper.deleteById(id);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(UserDTO dto) {

        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(User::getUsername, dto.getUsername())
                .or()
                .eq(StringUtils.isNotBlank(dto.getPhone()), User::getPhone, dto.getPhone())
                .or()
                .eq(StringUtils.isNotBlank(dto.getEmail()), User::getEmail, dto.getEmail());

        if (userMapper.selectCount(userQueryWrapper) > 0) {
            throw new BusinessException("用户名、手机号或邮箱已存在");
        }

        User user = userConvert.toUser(dto);
        user.setPassword(SecurityUtils.encryptPassword(CommonConstants.DEFAULT_PASSWORD));
        user.setCreateBy(SecurityUtils.getUserId());
        userMapper.insert(user);

        insertUserRole(user.getId(), dto.getRoleIds());

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(UserDTO dto) {
        dto.setUsername(null);

        checkCurrentUserAllowed(dto.getId());

        checkUserAllowed(dto.getId());

        User user = userConvert.toUser(dto);

        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(StringUtils.isNotBlank(dto.getPhone()), User::getPhone, dto.getPhone())
                .or()
                .eq(StringUtils.isNotBlank(dto.getEmail()), User::getEmail, dto.getEmail());
        if (userMapper.selectCount(userQueryWrapper) > 0) {
            throw new BusinessException("手机号或邮箱已存在");
        }

        user.setUpdateBy(SecurityUtils.getUserId());
        user.setNickname(StringUtils.isNotBlank(dto.getNickname()) ? user.getNickname() : dto.getUsername());
        userMapper.updateById(user);

        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, dto.getId()));

        insertUserRole(dto.getId(), dto.getRoleIds());

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String resetPassword(Long id) {
        checkUserAllowed(id);

        checkCurrentUserAllowed(id);

        User user = new User();
        user.setId(id);
        user.setPassword(SecurityUtils.encryptPassword(CommonConstants.DEFAULT_PASSWORD));
        user.setUpdateBy(SecurityUtils.getUserId());
        userMapper.updateById(user);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String kick(Long id) {
        checkUserAllowed(id);

        checkCurrentUserAllowed(id);

        User user = userMapper.selectById(id);
        if (user == null) {
            return CommonConstants.SUCCESS_MESSAGE;
        }

        String username = user.getUsername();
        String accessKey = RedisConstants.USER_TOKEN_JTI + username;
        String jti = redisUtils.get(accessKey);
        if (StringUtils.isNotBlank(jti)) {
            long expireSeconds = redisUtils.getExpire(accessKey, TimeUnit.SECONDS);
            if (expireSeconds > 0) {
                redisUtils.set(RedisConstants.BLACKLIST_TOKEN + jti, "logout", expireSeconds, TimeUnit.SECONDS);
            }
            redisUtils.delete(accessKey);
        }

        redisUtils.delete(RedisConstants.USER_REFRESH_JTI + username);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    private void insertUserRole(Long userId, List<Long> roleIds) {
        if (ObjectUtils.isNotEmpty(roleIds)) {
            for (Long roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
    }

    private void checkCurrentUserAllowed(Long id) {
        if (SecurityUtils.getUserId().equals(id)) {
            throw new BusinessException("当前用户不允许操作");
        }
    }

    private void checkUserAllowed(Long id) {
        if (CommonConstants.SUPER_ADMIN_ID.equals(id)) {
            throw new BusinessException("超级管理员不允许操作");
        }
    }

    private void setOnlineStatus(List<UserVO> userList) {
        if (ObjectUtils.isEmpty(userList)) {
            return;
        }

        for (UserVO userVo : userList) {
            String key = RedisConstants.USER_TOKEN_JTI + userVo.getUsername();
            userVo.setOnline(redisUtils.hasKey(key));
        }
    }

    private void setRoleInfo(List<UserVO> records) {
        if (ObjectUtils.isEmpty(records)) {
            return;
        }

        for (UserVO userVo : records) {
            userVo.setRoleList(userRoleMapper.selectRoleByUserId(userVo.getId()));
        }
    }

    private void setDeptInfo(List<UserVO> records) {
        if (ObjectUtils.isEmpty(records)) {
            return;
        }

        for (UserVO userVo : records) {
            if (userVo.getDeptId() != null) {
                String deptName = deptMapper.selectById(userVo.getDeptId()).getDeptName();
                userVo.setDeptName(deptName);
            }
            userVo.setDept(deptMapper.selectById(userVo.getDeptId()));
        }
    }
}

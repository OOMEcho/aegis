package com.aegis.common.datascope;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.constant.DataScopeConstants;
import com.aegis.modules.dept.mapper.DeptMapper;
import com.aegis.modules.role.mapper.RoleMapper;
import com.aegis.modules.user.domain.entity.User;
import com.aegis.modules.user.mapper.UserMapper;
import com.aegis.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 数据权限处理器
 *
 * @author xuesong.lei
 * @since 2025/09/10 14:08
 */
@Component
@RequiredArgsConstructor
public class DataPermissionHandler {

    private final UserMapper userMapper;

    private final DeptMapper deptMapper;

    private final RoleMapper roleMapper;

    public DataPermissionContext getCurrentUserDataPermission() {
        String username = SecurityUtils.getUsername();
        if (CommonConstants.ANONYMOUS.equals(username)) {
            return null;
        }

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username);

        User user = userMapper.selectOne(userLambdaQueryWrapper);
        if (user == null) {
            return null;
        }

        DataPermissionContext context = new DataPermissionContext();
        context.setUserId(user.getId());
        context.setDeptId(user.getDeptId());

        String dataScope = roleMapper.getHighestDataScope(user.getId());
        if (dataScope == null) {
            dataScope = DataScopeConstants.DATA_SCOPE_SELF;
        }
        context.setDataScope(dataScope);

        if (DataScopeConstants.DATA_SCOPE_CUSTOM.equals(dataScope)) {
            Set<Long> deptIds = roleMapper.getCustomDeptIds(user.getId());
            context.setDeptIds(deptIds);
        }

        return context;
    }

    public Set<Long> getDeptAndChildrenIds(Long deptId) {
        return deptMapper.getDeptAndChildrenIds(deptId);
    }
}

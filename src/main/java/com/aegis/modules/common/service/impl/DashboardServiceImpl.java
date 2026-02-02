package com.aegis.modules.common.service.impl;

import com.aegis.modules.common.domain.vo.AccessTrendVO;
import com.aegis.modules.common.domain.vo.DashboardVO;
import com.aegis.modules.common.service.DashboardService;
import com.aegis.modules.log.mapper.SysLoginLogMapper;
import com.aegis.modules.notice.domain.entity.Notice;
import com.aegis.modules.notice.mapper.NoticeMapper;
import com.aegis.modules.permission.domain.entity.Permission;
import com.aegis.modules.permission.mapper.PermissionMapper;
import com.aegis.modules.role.domain.entity.Role;
import com.aegis.modules.role.mapper.RoleMapper;
import com.aegis.modules.user.domain.entity.User;
import com.aegis.modules.user.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2026/2/2 15:17
 * @Description: 首页业务实现层
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final PermissionMapper permissionMapper;

    private final NoticeMapper noticeMapper;

    private final SysLoginLogMapper sysLoginLogMapper;

    @Override
    public DashboardVO getStatistics() {
        DashboardVO vo = new DashboardVO();

        // 获取7天前的日期
        Date sevenDaysAgo = getDateBefore(7);

        // 用户统计
        Long userCount = userMapper.selectCount(null);
        Long userCountBefore = userMapper.selectCount(
                new LambdaQueryWrapper<User>().lt(User::getCreateTime, sevenDaysAgo)
        );
        vo.setUserCount(userCount);
        vo.setUserGrowthRate(calculateGrowthRate(userCount, userCountBefore));

        // 角色统计
        Long roleCount = roleMapper.selectCount(null);
        Long roleCountBefore = roleMapper.selectCount(
                new LambdaQueryWrapper<Role>().lt(Role::getCreateTime, sevenDaysAgo)
        );
        vo.setRoleCount(roleCount);
        vo.setRoleGrowthRate(calculateGrowthRate(roleCount, roleCountBefore));

        // 权限统计
        Long permissionCount = permissionMapper.selectCount(null);
        Long permissionCountBefore = permissionMapper.selectCount(
                new LambdaQueryWrapper<Permission>().lt(Permission::getCreateTime, sevenDaysAgo)
        );
        vo.setPermissionCount(permissionCount);
        vo.setPermissionGrowthRate(calculateGrowthRate(permissionCount, permissionCountBefore));

        // 通知统计
        Long noticeCount = noticeMapper.selectCount(null);
        Long noticeCountBefore = noticeMapper.selectCount(
                new LambdaQueryWrapper<Notice>().lt(Notice::getCreateTime, sevenDaysAgo)
        );
        vo.setNoticeCount(noticeCount);
        vo.setNoticeGrowthRate(calculateGrowthRate(noticeCount, noticeCountBefore));

        return vo;
    }

    @Override
    public List<AccessTrendVO> getAccessTrend(Integer days) {
        // 参数校验，只允许7或30，默认7
        if (days == null || (days != 7 && days != 30)) {
            days = 7;
        }
        return sysLoginLogMapper.selectAccessTrend(days);
    }

    /**
     * 获取指定天数之前的日期
     *
     * @param days 天数
     * @return 日期
     */
    private Date getDateBefore(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 计算增长率
     * 公式：(当前总数 - 7天前总数) / 7天前总数 × 100%
     *
     * @param currentCount 当前总数
     * @param beforeCount  7天前总数
     * @return 格式化后的增长率字符串，如 "+2.4%" 或 "-1.5%"
     */
    private String calculateGrowthRate(Long currentCount, Long beforeCount) {
        if (beforeCount == null || beforeCount == 0) {
            // 如果7天前没有数据，则新增数量即为增长
            if (currentCount != null && currentCount > 0) {
                return "+100.0%";
            }
            return "+0.0%";
        }

        long newCount = currentCount - beforeCount;
        BigDecimal rate = BigDecimal.valueOf(newCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(beforeCount), 1, RoundingMode.HALF_UP);

        String prefix = rate.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
        return prefix + rate.toPlainString() + "%";
    }
}

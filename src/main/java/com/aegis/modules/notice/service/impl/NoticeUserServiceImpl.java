package com.aegis.modules.notice.service.impl;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.exception.BusinessException;
import com.aegis.modules.notice.domain.dto.NoticeUserDTO;
import com.aegis.modules.notice.domain.entity.Notice;
import com.aegis.modules.notice.domain.entity.NoticeUser;
import com.aegis.modules.notice.domain.vo.NoticeVO;
import com.aegis.modules.notice.mapper.NoticeMapper;
import com.aegis.modules.notice.mapper.NoticeUserMapper;
import com.aegis.modules.notice.service.NoticeConvert;
import com.aegis.modules.notice.service.NoticeUserService;
import com.aegis.utils.PageUtils;
import com.aegis.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/16 22:00
 * @Description: 通知用户业务实现层
 */
@Service
@RequiredArgsConstructor
public class NoticeUserServiceImpl implements NoticeUserService {

    private final NoticeUserMapper noticeUserMapper;

    private final NoticeMapper noticeMapper;

    private final NoticeConvert noticeConvert;

    @Override
    public PageVO<NoticeVO> pageList(NoticeUserDTO dto) {
        final Long userId = SecurityUtils.getUserId();

        return PageUtils.of(dto).paging(noticeUserMapper.pageList(dto, userId));
    }

    @Override
    public NoticeVO detail(Long id) {
        Notice notice = noticeMapper.selectById(id);
        if (notice == null) {
            throw new BusinessException("通知不存在");
        }

        LambdaUpdateWrapper<NoticeUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(NoticeUser::getNoticeId, id)
                .eq(NoticeUser::getUserId, SecurityUtils.getUserId())
                .set(NoticeUser::getReadFlag, CommonConstants.DISABLE_STATUS)
                .set(NoticeUser::getReadTime, new Date());

        noticeUserMapper.update(updateWrapper);

        return noticeConvert.toNoticeVO(notice);
    }

    @Override
    public Long unreadCount() {
        LambdaQueryWrapper<NoticeUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NoticeUser::getUserId, SecurityUtils.getUserId())
                .eq(NoticeUser::getReadFlag, CommonConstants.NORMAL_STATUS);

        return noticeUserMapper.selectCount(queryWrapper);
    }
}

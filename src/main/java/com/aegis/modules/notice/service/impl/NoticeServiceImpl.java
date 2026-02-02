package com.aegis.modules.notice.service.impl;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.exception.BusinessException;
import com.aegis.modules.notice.domain.dto.NoticeDTO;
import com.aegis.modules.notice.domain.entity.Notice;
import com.aegis.modules.notice.domain.entity.NoticeUser;
import com.aegis.modules.notice.mapper.NoticeMapper;
import com.aegis.modules.notice.mapper.NoticeUserMapper;
import com.aegis.modules.notice.service.NoticeConvert;
import com.aegis.modules.notice.service.NoticeService;
import com.aegis.utils.HtmlSanitizer;
import com.aegis.utils.PageUtils;
import com.aegis.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/10 14:10
 * @Description: 通知业务实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    private final NoticeUserMapper noticeUserMapper;

    private final NoticeConvert noticeConvert;

    @Override
    public PageVO<Notice> pageList(NoticeDTO dto) {
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(dto.getNoticeTitle()), Notice::getNoticeTitle, dto.getNoticeTitle())
                .eq(StringUtils.isNotBlank(dto.getNoticeType()), Notice::getNoticeType, dto.getNoticeType())
                .eq(StringUtils.isNotBlank(dto.getStatus()), Notice::getStatus, dto.getStatus())
                .orderByDesc(Notice::getCreateTime);

        return PageUtils.of(dto).paging(noticeMapper, queryWrapper);
    }

    @Override
    public Notice detail(Long id) {
        return noticeMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(Long id) {
        Notice notice = noticeMapper.selectById(id);
        if (!CommonConstants.NORMAL_STATUS.equals(notice.getStatus())) {
            throw new BusinessException("只能删除未发布的通知");
        }

        noticeMapper.deleteById(id);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(NoticeDTO dto) {
        // 清理富文本内容，防止XSS攻击
        dto.setNoticeContent(HtmlSanitizer.sanitize(dto.getNoticeContent()));

        Notice notice = noticeConvert.toNotice(dto);
        notice.setCreateBy(SecurityUtils.getUserId());

        boolean shouldPublish = shouldPublish(dto, notice);

        noticeMapper.insert(notice);

        if (shouldPublish) {
            publishNotice(notice);
        }

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(NoticeDTO dto) {
        // 清理富文本内容，防止XSS攻击
        dto.setNoticeContent(HtmlSanitizer.sanitize(dto.getNoticeContent()));

        Notice notice = noticeConvert.toNotice(dto);
        notice.setUpdateBy(SecurityUtils.getUserId());

        boolean shouldPublish = shouldPublish(dto, notice);

        noticeMapper.updateById(notice);

        if (shouldPublish) {
            publishNotice(notice);
        }

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String publish(Long id) {
        Long currentUserId = SecurityUtils.getUserId();

        doPublish(id, currentUserId);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String revoke(Long id) {
        Notice notice = noticeMapper.selectById(id);
        if (!CommonConstants.DISABLE_STATUS.equals(notice.getStatus())) {
            throw new BusinessException("只能撤销已发布的通知");
        }
        notice.setStatus("2"); // 撤销状态
        notice.setUpdateBy(SecurityUtils.getUserId());
        noticeMapper.updateById(notice);

        // 删除通知与用户的关联关系
        LambdaQueryWrapper<NoticeUser> queryWrapper = new LambdaQueryWrapper<NoticeUser>()
                .eq(NoticeUser::getNoticeId, id);
        noticeUserMapper.delete(queryWrapper);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    public void doPublish(Long id, Long userId) {
        Notice notice = noticeMapper.selectById(id);
        if (!CommonConstants.NORMAL_STATUS.equals(notice.getStatus())) {
            throw new BusinessException("只能发布未发布的通知");
        }

        if (userId == null) {
            userId = CommonConstants.SUPER_ADMIN_ID;
        }

        notice.setStatus(CommonConstants.DISABLE_STATUS);
        notice.setUpdateBy(userId);
        notice.setPublishTime(new Date());
        noticeMapper.updateById(notice);

        publishNotice(notice);
    }

    /**
     * 判断是否需要发布通知，并设置通知的状态和目标ID字符串
     *
     * @param dto    通知DTO
     * @param notice 通知实体
     * @return 是否需要发布通知
     */
    private boolean shouldPublish(NoticeDTO dto, Notice notice) {
        boolean shouldPublish = false;

        if (ObjectUtils.isNull(dto.getPublishTime()) || !dto.getPublishTime().after(new Date())) {// 立即发布
            notice.setStatus(CommonConstants.DISABLE_STATUS);
            if (ObjectUtils.isNull(dto.getPublishTime())) {
                notice.setPublishTime(new Date());
            }
            shouldPublish = true;
        }

        String targetIdsString = buildTargetIdsString(notice.getTargetType(), dto.getTargetIds());
        notice.setTargetIds(targetIdsString);

        return shouldPublish;
    }

    /**
     * 根据目标类型和目标ID列表，返回用于存储的字符串
     *
     * @param targetType 目标类型(1=全部用户,2=指定用户,3=指定角色,4=指定部门)
     * @param targetIds  目标ID列表
     * @return 目标ID字符串，全部用户返回 null
     */
    private String buildTargetIdsString(Integer targetType, List<Long> targetIds) {
        // 1 = 全部用户，不需要存储ID
        if (targetType == 1) {
            return null;
        }

        // 2、3、4 = 指定用户/角色/部门，必须有ID列表
        if (targetIds == null || targetIds.isEmpty()) {
            throw new BusinessException("目标对象ID列表不能为空");
        }

        // 去重并排序，转为逗号分隔字符串
        return targetIds.stream()
                .distinct()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    /**
     * 发布通知
     *
     * @param notice 通知实体
     */
    private void publishNotice(Notice notice) {
        List<Long> targetUserIds = resolveTargetUsers(notice);

        if (targetUserIds == null || targetUserIds.isEmpty()) {
            log.warn("通知发布成功，但没有匹配到接收用户，noticeId={}", notice.getId());
            return;
        }

        // 保存通知与用户的关联关系
        saveUserNoticeRelation(notice.getId(), targetUserIds);
    }

    /**
     * 解析通知的目标用户ID列表
     *
     * @param notice 通知实体
     * @return 目标用户ID列表
     */
    private List<Long> resolveTargetUsers(Notice notice) {
        final Integer targetType = notice.getTargetType();
        final String targetIdsStr = notice.getTargetIds();

        switch (targetType) {
            case 1: // 全部用户
                return noticeUserMapper.selectAllUserIds();

            case 2: // 指定用户
                return processIds(targetIdsStr, Function.identity());

            case 3: // 指定角色，查询角色对应的用户ID
                return processIds(targetIdsStr, noticeUserMapper::selectUserIdsByRoleIds);

            case 4: // 指定部门，查询部门对应的用户ID
                return processIds(targetIdsStr, noticeUserMapper::selectUserIdsByDeptIds);

            default:
                throw new BusinessException("未知的目标类型: " + targetType);
        }
    }

    private List<Long> processIds(String idsStr, Function<List<Long>, List<Long>> processor) {
        List<Long> result = Optional.ofNullable(idsStr)
                .filter(StringUtils::isNotBlank)
                .map(ids -> Arrays.stream(ids.split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        return processor.apply(result);
    }

    /**
     * 保存通知与用户的关联关系
     *
     * @param noticeId 通知ID
     * @param userIds  用户ID列表
     */
    private void saveUserNoticeRelation(Long noticeId, List<Long> userIds) {
        List<NoticeUser> relations = userIds.stream().map(userId -> {
            NoticeUser relation = new NoticeUser();
            relation.setNoticeId(noticeId);
            relation.setUserId(userId);
            return relation;
        }).collect(Collectors.toList());

        if (!relations.isEmpty()) {
            noticeUserMapper.insert(relations);
        }
    }
}

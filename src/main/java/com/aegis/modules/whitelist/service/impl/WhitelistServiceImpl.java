package com.aegis.modules.whitelist.service.impl;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.event.DataChangePublisher;
import com.aegis.common.exception.BusinessException;
import com.aegis.modules.whitelist.domain.dto.WhitelistDTO;
import com.aegis.modules.whitelist.domain.entity.Whitelist;
import com.aegis.modules.whitelist.mapper.WhitelistMapper;
import com.aegis.modules.whitelist.service.WhitelistConvert;
import com.aegis.modules.whitelist.service.WhitelistService;
import com.aegis.utils.PageUtils;
import com.aegis.utils.PathUtil;
import com.aegis.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/8 10:35
 * @Description: 白名单业务实现层
 */
@Service
@RequiredArgsConstructor
public class WhitelistServiceImpl implements WhitelistService {

    private final WhitelistMapper whitelistMapper;

    private final DataChangePublisher dataChangePublisher;

    private final WhitelistConvert whitelistConvert;

    // 正则：只允许 / 开头，后面可有字母数字、下划线、横杠、单斜杠，最多允许末尾 /** 前缀
    private static final Pattern VALID_PATH_PATTERN = Pattern.compile("^(/[a-zA-Z0-9_-]+)*(?:/\\*{2})?$");

    @Override
    public PageVO<Whitelist> pageList(WhitelistDTO dto) {
        LambdaQueryWrapper<Whitelist> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(StringUtils.isNotBlank(dto.getRequestMethod()), Whitelist::getRequestMethod, dto.getRequestMethod())
                .eq(StringUtils.isNotBlank(dto.getStatus()), Whitelist::getStatus, dto.getStatus())
                .like(StringUtils.isNotBlank(dto.getRequestUri()), Whitelist::getRequestUri, dto.getRequestUri());

        return PageUtils.of(dto).paging(whitelistMapper, queryWrapper);
    }

    @Override
    public Whitelist detail(Long id) {
        return whitelistMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(Long id) {
        // 删除白名单
        whitelistMapper.deleteById(id);

        // 发布白名单变更事件
        dataChangePublisher.publishWhitelistChange("删除白名单,ID: " + id);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(WhitelistDTO dto) {
        Whitelist whitelist = whitelistConvert.toWhitelist(dto);

        final String url = PathUtil.validateAndNormalize(whitelist.getRequestUri());

        // 检查是否有重复的路径和方法
        checkSameWhitelist(whitelist, url);

        whitelist.setCreateBy(SecurityUtils.getUserId());
        whitelistMapper.insert(whitelist);

        dataChangePublisher.publishWhitelistChange("新增白名单");

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(WhitelistDTO dto) {
        Whitelist whitelist = whitelistConvert.toWhitelist(dto);

        final String url = PathUtil.validateAndNormalize(whitelist.getRequestUri());

        // 检查是否有重复的路径和方法
        checkSameWhitelist(whitelist, url);

        whitelist.setUpdateBy(SecurityUtils.getUserId());
        whitelistMapper.updateById(whitelist);

        dataChangePublisher.publishWhitelistChange("更新白名单,ID: " + whitelist.getId());

        return CommonConstants.SUCCESS_MESSAGE;
    }

    private void checkSameWhitelist(Whitelist whitelist, String url) {
        LambdaQueryWrapper<Whitelist> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Whitelist::getRequestUri, url)
                .eq(Whitelist::getRequestMethod, whitelist.getRequestMethod().toUpperCase())
                .ne(ObjectUtils.isNotNull(whitelist.getId()), Whitelist::getId, whitelist.getId());

        if (whitelistMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("相同请求路径和方法的白名单已存在");
        }
    }
}

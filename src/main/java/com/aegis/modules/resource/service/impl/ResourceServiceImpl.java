package com.aegis.modules.resource.service.impl;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.event.DataChangePublisher;
import com.aegis.common.exception.BusinessException;
import com.aegis.modules.resource.domain.dto.ResourceDTO;
import com.aegis.modules.resource.domain.entity.Resource;
import com.aegis.modules.resource.domain.vo.ResourceVO;
import com.aegis.modules.resource.mapper.ResourceMapper;
import com.aegis.modules.resource.service.ResourceConvert;
import com.aegis.modules.resource.service.ResourceService;
import com.aegis.utils.PageUtils;
import com.aegis.utils.PathUtil;
import com.aegis.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: xuesong.lei
 * @Date: 2026/1/12 23:16
 * @Description: 资源业务实现层
 */
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceMapper resourceMapper;

    private final ResourceConvert resourceConvert;

    private final DataChangePublisher dataChangePublisher;

    @Override
    public PageVO<ResourceVO> pageList(ResourceDTO dto) {
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(dto.getRequestMethod()), Resource::getRequestMethod, dto.getRequestMethod())
                .like(StringUtils.isNotBlank(dto.getRequestUri()), Resource::getRequestUri, dto.getRequestUri())
                .like(StringUtils.isNotBlank(dto.getPermCode()), Resource::getPermCode, dto.getPermCode())
                .eq(StringUtils.isNotBlank(dto.getStatus()), Resource::getStatus, dto.getStatus());
        return PageUtils.of(dto).pagingAndConvert(resourceMapper, queryWrapper, resourceConvert::toResourceVo);
    }

    @Override
    public ResourceVO detail(Long id) {
        return resourceConvert.toResourceVo(resourceMapper.selectById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(Long id) {
        resourceMapper.deleteById(id);

        // 发布资源变更事件
        dataChangePublisher.publishResourceChange("删除资源,ID: " + id);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(ResourceDTO dto) {
        Resource resource = resourceConvert.toResource(dto);

        final String url = PathUtil.validateAndNormalize(resource.getRequestUri());
        resource.setRequestUri(url);

        // 检查是否有重复的路径和方法
        checkSameResource(resource, url);

        resource.setCreateBy(SecurityUtils.getUserId());
        resourceMapper.insert(resource);

        // 发布资源变更事件
        dataChangePublisher.publishResourceChange("新增资源");

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(ResourceDTO dto) {
        Resource resource = resourceConvert.toResource(dto);

        final String url = PathUtil.validateAndNormalize(resource.getRequestUri());
        resource.setRequestUri(url);

        // 检查是否有重复的路径和方法
        checkSameResource(resource, url);

        resource.setUpdateBy(SecurityUtils.getUserId());
        resourceMapper.updateById(resource);

        // 发布资源变更事件
        dataChangePublisher.publishResourceChange("更新资源,ID: " + resource.getId());

        return CommonConstants.SUCCESS_MESSAGE;
    }

    private void checkSameResource(Resource resource, String url) {
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resource::getRequestUri, url)
                .eq(Resource::getRequestMethod, resource.getRequestMethod().toUpperCase())
                .ne(ObjectUtils.isNotNull(resource.getId()), Resource::getId, resource.getId());

        if (resourceMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("相同请求路径和方法的资源已存在");
        }
    }
}

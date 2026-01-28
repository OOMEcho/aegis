package com.aegis.modules.dict.service.impl;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.exception.BusinessException;
import com.aegis.modules.dict.domain.dto.DictionaryDTO;
import com.aegis.modules.dict.domain.entity.Dictionary;
import com.aegis.modules.dict.mapper.DictionaryMapper;
import com.aegis.modules.dict.service.DictionaryConvert;
import com.aegis.modules.dict.service.DictionaryService;
import com.aegis.utils.PageUtils;
import com.aegis.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/8 16:08
 * @Description: 字典业务实现层
 */
@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryMapper dictionaryMapper;

    private final DictionaryConvert dictionaryConvert;

    @Override
    public PageVO<Dictionary> pageList(DictionaryDTO dto) {
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.isNotBlank(dto.getDictName()), Dictionary::getDictName, dto.getDictName())
                .like(StringUtils.isNotBlank(dto.getDictType()), Dictionary::getDictType, dto.getDictType())
                .like(StringUtils.isNotBlank(dto.getDictLabel()), Dictionary::getDictLabel, dto.getDictLabel())
                .eq(StringUtils.isNotBlank(dto.getStatus()), Dictionary::getStatus, dto.getStatus());

        return PageUtils.of(dto).paging(dictionaryMapper, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(Long id) {
        dictionaryMapper.deleteById(id);
        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(DictionaryDTO dto) {
        if (isContainUpperCase(dto.getDictType())) {
            throw new BusinessException("字典类型必须为大写");
        }

        Dictionary dictionary = dictionaryConvert.toDictionary(dto);

        // 检查是否存在相同的数据
        checkSameDictionary(dictionary);

        dictionary.setCreateBy(SecurityUtils.getUserId());
        dictionaryMapper.insert(dictionary);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(DictionaryDTO dto) {
        if (isContainUpperCase(dto.getDictType())) {
            throw new BusinessException("字典类型必须为大写");
        }

        Dictionary dictionary = dictionaryConvert.toDictionary(dto);

        // 检查是否存在相同的数据
        checkSameDictionary(dictionary);

        dictionary.setUpdateBy(SecurityUtils.getUserId());
        dictionaryMapper.updateById(dictionary);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    public List<Dictionary> list(String dictType) {
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Dictionary::getDictType, dictType);

        return dictionaryMapper.selectList(queryWrapper);
    }

    private void checkSameDictionary(Dictionary dictionary) {
        LambdaQueryWrapper<Dictionary> sameQueryWrapper = new LambdaQueryWrapper<>();
        sameQueryWrapper.eq(Dictionary::getDictName, dictionary.getDictName())
                .eq(Dictionary::getDictType, dictionary.getDictType())
                .eq(Dictionary::getDictLabel, dictionary.getDictLabel())
                .eq(Dictionary::getDictValue, dictionary.getDictValue())
                .ne(ObjectUtils.isNotNull(dictionary.getId()), Dictionary::getId, dictionary.getId());

        Dictionary oldDictionary = dictionaryMapper.selectOne(sameQueryWrapper);
        if (ObjectUtils.isNotNull(oldDictionary)) {
            throw new BusinessException("存在相同的数据");
        }

        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dictionary::getDictType, dictionary.getDictType())
                .ne(ObjectUtils.isNotEmpty(dictionary.getId()), Dictionary::getId, dictionary.getId());

        List<Dictionary> result = dictionaryMapper.selectList(queryWrapper);

        if (result != null && !result.isEmpty()) {
            result.forEach(item -> {
                if (!item.getDictName().equals(dictionary.getDictName()) || !item.getDictType().equals(dictionary.getDictType())) {
                    throw new BusinessException("字典名称或字典类型与现存的字典数据不匹配");
                }
                if (item.getDictLabel().equals(dictionary.getDictLabel()) && item.getDictValue().equals(dictionary.getDictValue())) {
                    throw new BusinessException("存在相同的数据");
                }
            });
        }
    }

    /**
     * 判断字符串是否全为大写
     *
     * @param str 字符串
     * @return 结果
     */
    private boolean isContainUpperCase(String str) {
        StringBuilder buf = new StringBuilder(str);
        for (int i = 0; i < buf.length(); i++) {
            if (Character.isLowerCase(buf.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}

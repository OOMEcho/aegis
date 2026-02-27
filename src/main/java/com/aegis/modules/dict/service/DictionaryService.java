package com.aegis.modules.dict.service;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.modules.dict.domain.dto.DictionaryDTO;
import com.aegis.modules.dict.domain.vo.DictionaryVO;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/8 16:08
 * @Description: 字典业务层
 */
public interface DictionaryService {

    /**
     * 分页列表
     *
     * @param dto 查询参数
     * @return 字典分页列表
     */
    PageVO<DictionaryVO> pageList(DictionaryDTO dto);

    /**
     * 删除字典
     *
     * @param id 字典ID
     * @return 响应消息
     */
    String delete(Long id);

    /**
     * 新增字典
     *
     * @param dto 字典DTO
     * @return 响应消息
     */
    String add(DictionaryDTO dto);

    /**
     * 修改字典
     *
     * @param dto 字典DTO
     * @return 响应消息
     */
    String update(DictionaryDTO dto);

    /**
     * 根据字典类型获取字典列表
     *
     * @param dictType 字典类型
     * @return 字典列表
     */
    List<DictionaryVO> list(String dictType);
}

package com.aegis.modules.dict.service;

import com.aegis.modules.dict.domain.dto.DictionaryDTO;
import com.aegis.modules.dict.domain.entity.Dictionary;
import com.aegis.modules.dict.domain.vo.DictionaryVO;
import org.mapstruct.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2025/09/08 22:23
 * @Description: 字典实体类转换
 */
@Mapper(componentModel = "spring")
public interface DictionaryConvert {

    Dictionary toDictionary(DictionaryDTO dto);

    DictionaryVO toDictionaryVo(Dictionary dictionary);
}

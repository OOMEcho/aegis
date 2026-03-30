package com.aegis.modules.whitelist.service;

import com.aegis.modules.whitelist.domain.dto.WhitelistDTO;
import com.aegis.modules.whitelist.domain.entity.Whitelist;
import com.aegis.modules.whitelist.domain.vo.WhitelistVO;
import org.mapstruct.Mapper;

/**
 * 白名单实体类转换
 *
 * @author xuesong.lei
 * @since 2025/9/8 22:27
 */
@Mapper(componentModel = "spring")
public interface WhitelistConvert {

    Whitelist toWhitelist(WhitelistDTO dto);

    WhitelistVO toWhitelistVo(Whitelist whitelist);
}

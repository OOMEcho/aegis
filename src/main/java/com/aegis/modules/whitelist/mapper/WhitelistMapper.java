package com.aegis.modules.whitelist.mapper;

import com.aegis.modules.whitelist.domain.entity.Whitelist;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025-09-04 13:46:14
 * @Description: 针对表【t_whitelist(白名单表)】的数据库操作Mapper
 * @Entity: com.aegis.modules.whitelist.domain.entity.Whitelist
 */
public interface WhitelistMapper extends BaseMapper<Whitelist> {

    List<Whitelist> getAllWhitelist();
}





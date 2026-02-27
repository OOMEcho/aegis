package com.aegis.modules.whitelist.service;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.modules.whitelist.domain.dto.WhitelistDTO;
import com.aegis.modules.whitelist.domain.vo.WhitelistVO;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/8 10:35
 * @Description: 白名单业务层
 */
public interface WhitelistService {

    /**
     * 分页列表
     *
     * @param dto 查询参数
     * @return 白名单分页列表
     */
    PageVO<WhitelistVO> pageList(WhitelistDTO dto);

    /**
     * 详情
     *
     * @param id 白名单ID
     * @return 白名单详情
     */
    WhitelistVO detail(Long id);

    /**
     * 删除白名单
     *
     * @param id 白名单ID
     * @return 响应消息
     */
    String delete(Long id);

    /**
     * 新增白名单
     *
     * @param dto 白名单DTO
     * @return 响应消息
     */
    String add(WhitelistDTO dto);

    /**
     * 修改白名单
     *
     * @param dto 白名单DTO
     * @return 响应消息
     */
    String update(WhitelistDTO dto);
}

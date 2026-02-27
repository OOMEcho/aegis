package com.aegis.modules.menu.service;

import com.aegis.modules.menu.domain.dto.MenuDTO;
import com.aegis.modules.menu.domain.vo.MenuVO;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/10 14:11
 * @Description: 菜单业务层
 */
public interface MenuService {

    /**
     * 列表
     *
     * @param dto 查询参数
     * @return 菜单列表
     */
    List<MenuVO> list(MenuDTO dto);

    /**
     * 详情
     *
     * @param id 菜单ID
     * @return 菜单详情
     */
    MenuVO detail(Long id);

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return 响应消息
     */
    String delete(Long id);

    /**
     * 新增菜单
     *
     * @param dto 菜单DTO
     * @return 响应消息
     */
    String add(MenuDTO dto);

    /**
     * 修改菜单
     *
     * @param dto 菜单DTO
     * @return 响应消息
     */
    String update(MenuDTO dto);

    /**
     * 获取菜单的权限编码列表
     *
     * @param menuId 菜单ID
     * @return 权限编码列表
     */
    List<String> getMenuPermissions(Long menuId);

    /**
     * 给菜单配置权限
     *
     * @param menuId    菜单ID
     * @param permCodes 权限编码列表
     * @return 结果
     */
    String assignPermissions(Long menuId, List<String> permCodes);
}

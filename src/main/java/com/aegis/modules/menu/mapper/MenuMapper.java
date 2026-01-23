package com.aegis.modules.menu.mapper;

import com.aegis.modules.menu.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025-08-30 10:47:13
 * @Description: 针对表【t_menu(菜单表)】的数据库操作Mapper
 * @Entity: com.aegis.modules.menu.domain.entity.Menu
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户ID获取菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenuByUserId(Long userId);

    /**
     * 批量更新子菜单的路径信息
     *
     * @param children 子菜单列表
     */
    void updateBatchPath(@Param("children") List<Menu> children);
}





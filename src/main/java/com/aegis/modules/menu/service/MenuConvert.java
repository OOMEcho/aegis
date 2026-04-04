package com.aegis.modules.menu.service;

import com.aegis.modules.menu.domain.dto.MenuDTO;
import com.aegis.modules.menu.domain.entity.Menu;
import com.aegis.modules.menu.domain.vo.MenuVO;
import org.mapstruct.Mapper;

/**
 * 菜单类型转换类
 *
 * @author xuesong.lei
 * @since 2025-09-13
 */
@Mapper(componentModel = "spring")
public interface MenuConvert {

    MenuVO toMenuVo(Menu menu);

    Menu toMenu(MenuDTO dto);
}

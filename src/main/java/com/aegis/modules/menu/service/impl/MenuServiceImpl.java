package com.aegis.modules.menu.service.impl;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.domain.vo.TreeVO;
import com.aegis.common.exception.BusinessException;
import com.aegis.modules.menu.domain.dto.MenuDTO;
import com.aegis.modules.menu.domain.entity.Menu;
import com.aegis.modules.menu.domain.entity.MenuPermission;
import com.aegis.modules.menu.mapper.MenuMapper;
import com.aegis.modules.menu.mapper.MenuPermissionMapper;
import com.aegis.modules.menu.service.MenuConvert;
import com.aegis.modules.menu.service.MenuService;
import com.aegis.utils.SecurityUtils;
import com.aegis.utils.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/10 14:12
 * @Description: 菜单业务实现层
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;

    private final MenuPermissionMapper menuPermissionMapper;

    private final MenuConvert menuConvert;

    @Override
    public List<Menu> list(MenuDTO dto) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotEmpty(dto.getMenuCode()), Menu::getMenuCode, dto.getMenuCode())
                .like(StringUtils.isNotBlank(dto.getMenuName()), Menu::getMenuName, dto.getMenuName())
                .like(StringUtils.isNotBlank(dto.getName()), Menu::getName, dto.getName())
                .like(StringUtils.isNotBlank(dto.getPath()), Menu::getPath, dto.getPath())
                .eq(StringUtils.isNotBlank(dto.getMenuType()), Menu::getMenuType, dto.getMenuType())
                .eq(StringUtils.isNotBlank(dto.getStatus()), Menu::getStatus, dto.getStatus())
                .orderBy(true, true, Menu::getParentId, Menu::getOrderNum);
        return menuMapper.selectList(queryWrapper);
    }

    @Override
    public Menu detail(Long id) {
        return menuMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(Long id) {
        LambdaQueryWrapper<Menu> childMenu = new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, id);
        if (menuMapper.selectCount(childMenu) > 0) {
            throw new BusinessException("存在子菜单，不允许删除");
        }

        // 删除菜单与权限的关联
        menuPermissionMapper.delete(new LambdaQueryWrapper<MenuPermission>().eq(MenuPermission::getMenuId, id));

        menuMapper.deleteById(id);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(MenuDTO dto) {
        Menu menu = menuConvert.toMenu(dto);

        checkSameMune(menu);

        menu.setCreateBy(SecurityUtils.getUserId());
        menuMapper.insert(menu);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(MenuDTO dto) {
        Menu menu = menuConvert.toMenu(dto);

        checkSameMune(menu);

        if (dto.getParentId().equals(dto.getId())) {
            throw new BusinessException("修改菜单'" + dto.getMenuName() + "'失败,上级菜单不能选择自己");
        }

        menu.setUpdateBy(SecurityUtils.getUserId());

        menuMapper.updateById(menu);

        return CommonConstants.SUCCESS_MESSAGE;
    }

    @Override
    public List<TreeVO> tree(MenuDTO dto) {
        List<Menu> menuList = list(dto);

        List<Menu> menuTree = TreeUtil.makeTree(
                menuList,
                Menu::getParentId,
                Menu::getId,
                menu -> menu.getParentId() == null || menu.getParentId() == 0L,
                Menu::setChildren);

        return menuTree.stream().map(TreeVO::new).collect(Collectors.toList());
    }

    @Override
    public List<String> getMenuPermissions(Long menuId) {
        LambdaQueryWrapper<MenuPermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuPermission::getMenuId, menuId);
        List<MenuPermission> menuPermissions = menuPermissionMapper.selectList(queryWrapper);
        return menuPermissions.stream()
                .map(MenuPermission::getPermCode)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String assignPermissions(Long menuId, List<String> permCodes) {
        // 先删除菜单原有权限
        menuPermissionMapper.delete(new LambdaQueryWrapper<MenuPermission>().eq(MenuPermission::getMenuId, menuId));

        // 再新增菜单权限关联
        if (permCodes != null && !permCodes.isEmpty()) {
            for (String permCode : permCodes) {
                MenuPermission mp = new MenuPermission();
                mp.setMenuId(menuId);
                mp.setPermCode(permCode);
                menuPermissionMapper.insert(mp);
            }
        }

        return CommonConstants.SUCCESS_MESSAGE;
    }

    private void checkSameMune(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getMenuName, menu.getMenuName())
                .eq(Menu::getParentId, menu.getParentId())
                .ne(ObjectUtils.isNotEmpty(menu.getId()), Menu::getId, menu.getId());
        if (menuMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("同一层级下存在相同名称的菜单");
        }
    }
}

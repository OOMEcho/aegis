package com.aegis.modules.menu.service.impl;

import com.aegis.common.constant.CommonConstants;
import com.aegis.common.constant.FileConstants;
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

import java.util.ArrayList;
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

        // 检查父菜单状态
        if (menu.getParentId() != null && menu.getParentId() != 0L) {
            Menu parentMenu = menuMapper.selectById(menu.getParentId());
            if (parentMenu == null) {
                throw new BusinessException("父菜单不存在");
            }
            if (CommonConstants.DISABLE_STATUS.equals(parentMenu.getStatus())) {
                throw new BusinessException("父菜单已停用，不允许新增子菜单");
            }
        }

        // 自动处理路由地址
        buildMenuPath(menu);

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

        // 获取修改前的菜单信息
        Menu oldMenu = menuMapper.selectById(dto.getId());
        if (oldMenu == null) {
            throw new BusinessException("菜单不存在");
        }

        // 如果要停用菜单，检查是否有启用的子菜单
        if (CommonConstants.DISABLE_STATUS.equals(menu.getStatus())
                && !CommonConstants.DISABLE_STATUS.equals(oldMenu.getStatus())) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Menu::getParentId, dto.getId())
                    .eq(Menu::getStatus, CommonConstants.NORMAL_STATUS);
            if (menuMapper.selectCount(queryWrapper) > 0) {
                throw new BusinessException("该菜单包含未停用的子菜单，不允许停用");
            }
        }

        // 自动处理路由地址
        String oldPath = oldMenu.getPath();
        buildMenuPath(menu);
        String newPath = menu.getPath();

        menu.setUpdateBy(SecurityUtils.getUserId());
        menuMapper.updateById(menu);

        // 如果路径发生变化，需要级联更新所有子孙菜单的路径
        if (!oldPath.equals(newPath)) {
            updateChildrenPath(dto.getId(), oldPath, newPath);
        }

        // 如果启用菜单，需要级联启用所有父菜单
        if (CommonConstants.NORMAL_STATUS.equals(menu.getStatus())
                && !CommonConstants.NORMAL_STATUS.equals(oldMenu.getStatus())) {
            updateParentMenuStatusNormal(menu);
        }

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

    /**
     * 构建菜单路由地址
     * 规则：
     * 1. 如果是根菜单(parentId为0或null)，path必须以/开头，直接使用
     * 2. 如果是子菜单，自动拼接父菜单的path
     *    - 前端传入相对路径(如"user")
     *    - 后端自动拼接为完整路径(如"/system/user")
     *    - 如果前端传入的已经是完整路径且包含父路径，直接使用
     */
    private void buildMenuPath(Menu menu) {
        // 根菜单，path必须以/开头
        if (menu.getParentId() == null || menu.getParentId() == 0L) {
            if (!menu.getPath().startsWith(FileConstants.SEPARATOR)) {
                throw new BusinessException("根菜单的路由地址必须以/开头");
            }
            return;
        }

        // 子菜单，自动拼接父菜单路径
        Menu parentMenu = menuMapper.selectById(menu.getParentId());
        if (parentMenu == null) {
            throw new BusinessException("父菜单不存在");
        }

        String parentPath = parentMenu.getPath();
        String inputPath = menu.getPath();

        // 如果前端传入的路径已经包含了父路径，直接使用
        if (inputPath.startsWith(parentPath + FileConstants.SEPARATOR) || inputPath.equals(parentPath)) {
            return;
        }

        // 处理相对路径
        String relativePath = inputPath;
        if (relativePath.startsWith(FileConstants.SEPARATOR)) {
            relativePath = relativePath.substring(1);
        }

        // 拼接父菜单路径
        if (parentPath.endsWith(FileConstants.SEPARATOR)) {
            menu.setPath(parentPath + relativePath);
        } else {
            menu.setPath(parentPath + FileConstants.SEPARATOR + relativePath);
        }
    }

    /**
     * 级联更新子孙菜单的路径
     * 当父菜单路径发生变化时，递归更新所有子孙菜单的路径
     *
     * @param parentId 父菜单ID
     * @param oldParentPath 父菜单的旧路径
     * @param newParentPath 父菜单的新路径
     */
    private void updateChildrenPath(Long parentId, String oldParentPath, String newParentPath) {
        // 查询所有直接子菜单
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, parentId);
        List<Menu> children = menuMapper.selectList(queryWrapper);

        if (children == null || children.isEmpty()) {
            return;
        }

        // 收集所有需要更新的子菜单
        List<Menu> updateList = new ArrayList<>();

        // 更新每个子菜单的路径
        for (Menu child : children) {
            String oldChildPath = child.getPath();

            // 将子菜单路径中的旧父路径替换为新父路径
            // 例如：oldParentPath="/system", newParentPath="/admin"
            //      oldChildPath="/system/user" -> newChildPath="/admin/user"
            String newChildPath = oldChildPath.replaceFirst("^" + oldParentPath, newParentPath);

            child.setPath(newChildPath);
            child.setUpdateBy(SecurityUtils.getUserId());
            updateList.add(child);

            // 递归更新孙菜单
            updateChildrenPath(child.getId(), oldChildPath, newChildPath);
        }

        // 批量更新
        menuMapper.updateBatchPath(updateList);
    }

    /**
     * 级联启用所有父菜单
     * 当启用某个菜单时，需要确保其所有父菜单也是启用状态
     *
     * @param menu 当前菜单
     */
    private void updateParentMenuStatusNormal(Menu menu) {
        if (menu.getParentId() == null || menu.getParentId() == 0L) {
            return;
        }

        // 递归启用所有父菜单
        Menu parentMenu = menuMapper.selectById(menu.getParentId());
        if (parentMenu != null && CommonConstants.DISABLE_STATUS.equals(parentMenu.getStatus())) {
            parentMenu.setStatus(CommonConstants.NORMAL_STATUS);
            parentMenu.setUpdateBy(SecurityUtils.getUserId());
            menuMapper.updateById(parentMenu);

            // 继续向上递归
            updateParentMenuStatusNormal(parentMenu);
        }
    }
}

package com.aegis.modules.role.mapper;

import com.aegis.modules.role.domain.dto.UserAndRoleQueryDTO;
import com.aegis.modules.role.domain.entity.Role;
import com.aegis.modules.user.domain.vo.UserVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author: xuesong.lei
 * @Date: 2025-08-30 10:48:14
 * @Description: 针对表【t_role(角色信息表)】的数据库操作Mapper
 * @Entity: com.aegis.modules.role.domain.entity.Role
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 获取自定义部门ID集合
     *
     * @param id 角色ID
     * @return 部门ID集合
     */
    Set<Long> getCustomDeptIds(Long id);

    /**
     * 获取最高数据权限范围
     *
     * @param id 角色ID
     * @return 数据权限范围
     */
    String getHighestDataScope(Long id);

    /**
     * 分页查询已分配用户列表
     *
     * @param dto 查询参数
     * @return 用户列表
     */
    List<UserVO> allocatedList(@Param("dto") UserAndRoleQueryDTO dto);

    /**
     * 分页查询未分配用户列表
     *
     * @param dto 查询参数
     * @return 用户列表
     */
    List<UserVO> unallocatedList(@Param("dto") UserAndRoleQueryDTO dto);
}





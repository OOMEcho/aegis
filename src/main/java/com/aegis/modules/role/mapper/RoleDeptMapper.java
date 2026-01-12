package com.aegis.modules.role.mapper;

import com.aegis.modules.role.domain.entity.RoleDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025-08-30 10:48:29
 * @Description: 针对表【t_role_dept(角色和部门关联表)】的数据库操作Mapper
 * @Entity: com.aegis.modules.role.domain.entity.RoleDept
 */
public interface RoleDeptMapper extends BaseMapper<RoleDept> {

    /**
     * 根据角色ID和部门选择严格标志获取部门ID列表
     *
     * @param roleId            角色ID
     * @param deptCheckStrictly 部门选择严格标志
     * @return 部门ID列表
     */
    List<Long> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") Integer deptCheckStrictly);
}





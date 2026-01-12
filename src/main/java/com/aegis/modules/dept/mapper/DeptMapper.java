package com.aegis.modules.dept.mapper;

import com.aegis.modules.dept.domain.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author: xuesong.lei
 * @Date: 2025-08-30 10:44:18
 * @Description: 针对表【t_dept(部门信息表)】的数据库操作Mapper
 * @Entity: com.aegis.modules.dept.domain.entity.Dept
 */
public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 获取指定部门及其所有子部门的ID集合
     *
     * @param deptId 部门ID
     * @return 部门ID集合
     */
    Set<Long> getDeptAndChildrenIds(Long deptId);

    /**
     * 批量更新子部门的祖先节点信息
     *
     * @param children 子部门列表
     */
    void updateBatchAncestors(@Param("children") List<Dept> children);
}





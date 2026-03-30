package com.aegis.modules.dept.service;

import com.aegis.modules.dept.domain.dto.DeptDTO;
import com.aegis.modules.dept.domain.entity.Dept;
import com.aegis.modules.dept.domain.vo.DeptVO;
import org.mapstruct.Mapper;

/**
 * 部门转换类
 *
 * @author xuesong.lei
 * @since 2025/9/11 21:26
 */
@Mapper(componentModel = "spring")
public interface DeptConvert {

    Dept toDept(DeptDTO dto);

    DeptVO toDeptVo(Dept dept);
}

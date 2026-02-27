package com.aegis.modules.dept.service;

import com.aegis.modules.dept.domain.dto.DeptDTO;
import com.aegis.modules.dept.domain.entity.Dept;
import com.aegis.modules.dept.domain.vo.DeptVO;
import org.mapstruct.Mapper;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/11 21:26
 * @Description: 部门转换类
 */
@Mapper(componentModel = "spring")
public interface DeptConvert {

    Dept toDept(DeptDTO dto);

    DeptVO toDeptVo(Dept dept);
}

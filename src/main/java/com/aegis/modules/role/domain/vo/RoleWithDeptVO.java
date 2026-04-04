package com.aegis.modules.role.domain.vo;

import com.aegis.common.domain.vo.TreeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 角色对应的菜单树或部门树
 *
 * @author xuesong.lei
 * @since 2025-09-13
 */
@Data
@ApiModel("角色对应的菜单树或部门树")
public class RoleWithDeptVO {

    @ApiModelProperty("被选中的节点")
    private List<Long> checkedKeys;

    @ApiModelProperty("树形结构")
    private List<TreeVO> trees;
}

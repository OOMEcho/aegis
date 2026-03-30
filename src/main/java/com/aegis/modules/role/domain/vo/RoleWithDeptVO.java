package com.aegis.modules.role.domain.vo;

import com.aegis.common.domain.vo.TreeVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 角色对应的菜单树或部门树
 *
 * @author xuesong.lei
 * @since 2025/9/13 18:48
 */
@Data
@Schema(description = "角色对应的菜单树或部门树")
public class RoleWithDeptVO {

    @Schema(description = "被选中的节点")
    private List<Long> checkedKeys;

    @Schema(description = "树形结构")
    private List<TreeVO> trees;
}

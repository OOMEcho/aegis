package com.aegis.common.domain.vo;

import com.aegis.modules.dept.domain.entity.Dept;
import com.aegis.modules.menu.domain.entity.Menu;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 树形结构VO
 *
 * @author xuesong.lei
 * @since 2025/9/11 22:10
 */
@Data
@Schema(description = "树形结构VO")
public class TreeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 节点id
     */
    @Schema(description = "节点id")
    private Long id;

    /**
     * 节点名称
     */
    @Schema(description = "节点名称")
    private String label;

    /**
     * 子节点
     */
    @Schema(description = "子节点")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeVO> children;


    public TreeVO() {
    }

    public TreeVO(Dept sysDept) {
        this.id = sysDept.getId();
        this.label = sysDept.getDeptName();
        List<Dept> children = sysDept.getChildren();
        if (children != null && !children.isEmpty()) {
            this.children = children.stream().map(TreeVO::new).collect(Collectors.toList());
        } else {
            this.children = Collections.emptyList();
        }
    }

    public TreeVO(Menu sysMenu) {
        this.id = sysMenu.getId();
        this.label = sysMenu.getMenuName();
        List<Menu> children = sysMenu.getChildren();
        if (children != null && !children.isEmpty()) {
            this.children = children.stream().map(TreeVO::new).collect(Collectors.toList());
        } else {
            this.children = Collections.emptyList();
        }
    }
}

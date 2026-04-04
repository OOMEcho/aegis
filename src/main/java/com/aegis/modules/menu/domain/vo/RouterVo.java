package com.aegis.modules.menu.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 前端路由VO
 *
 * @author xuesong.lei
 * @since 2025-09-14
 */
@Data
@Schema(description = "前端路由VO")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页面/路由唯一标识
     * 对应 t_menu.name
     * 用于前端路由 name、页面唯一 key
     */
    @Schema(description = "路由名称")
    private String name;

    /**
     * 路由路径（相对父级）
     * 对应 t_menu.path
     */
    @Schema(description = "路由地址")
    private String path;

    /**
     * 页面显示标题
     * 对应 t_menu.menu_name
     */
    @Schema(description = "路由标题")
    private String title;

    /**
     * 菜单图标
     * 对应 t_menu.icon
     */
    @Schema(description = "路由图标")
    private String icon;

    /**
     * 是否在菜单中隐藏
     * 对应 t_menu.hidden
     */
    @Schema(description = "是否隐藏路由")
    private Boolean hidden;

    @Schema(description = "子路由")
    private List<RouterVo> children;
}

package com.aegis.modules.menu.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/14 11:00
 * @Description: 前端路由VO
 */
@Data
@ApiModel("前端路由VO")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页面/路由唯一标识
     * 对应 t_menu.name
     * 用于前端路由 name、页面唯一 key
     */
    @ApiModelProperty("路由名称")
    private String name;

    /**
     * 路由路径（相对父级）
     * 对应 t_menu.path
     */
    @ApiModelProperty("路由地址")
    private String path;

    /**
     * 页面显示标题
     * 对应 t_menu.menu_name
     */
    @ApiModelProperty("路由标题")
    private String title;

    /**
     * 菜单图标
     * 对应 t_menu.icon
     */
    @ApiModelProperty("路由图标")
    private String icon;

    /**
     * 是否在菜单中隐藏
     * 对应 t_menu.hidden
     */
    @ApiModelProperty("是否隐藏路由")
    private Boolean hidden;

    @ApiModelProperty("子路由")
    private List<RouterVo> children;
}

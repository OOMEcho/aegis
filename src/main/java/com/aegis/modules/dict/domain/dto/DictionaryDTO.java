package com.aegis.modules.dict.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import com.aegis.common.validator.EnumString;
import com.aegis.common.validator.ValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 字典DTO
 *
 * @author xuesong.lei
 * @since 2025-09-08
 */
@Data
@ApiModel("字典DTO")
@EqualsAndHashCode(callSuper = true)
public class DictionaryDTO extends PageDTO {

    @ApiModelProperty("主键ID")
    @Null(groups = ValidGroup.Create.class, message = "应用ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("字典名称")
    @NotBlank(message = "字典名称不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String dictName;

    @ApiModelProperty("字典类型")
    @NotBlank(message = "字典类型不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String dictType;

    @ApiModelProperty("字典排序")
    private Integer dictSort;

    @ApiModelProperty("字典标签")
    @NotBlank(message = "字典类型不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String dictLabel;

    @ApiModelProperty("字典键值")
    @NotBlank(message = "字典键值不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String dictValue;

    @ApiModelProperty("状态")
    @EnumString(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, value = {"0", "1"}, message = "状态只允许为0或1")
    private String status;
}

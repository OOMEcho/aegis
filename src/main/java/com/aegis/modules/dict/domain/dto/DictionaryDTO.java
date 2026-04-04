package com.aegis.modules.dict.domain.dto;

import com.aegis.common.domain.dto.PageDTO;
import com.aegis.common.validator.EnumString;
import com.aegis.common.validator.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * 字典DTO
 *
 * @author xuesong.lei
 * @since 2025-09-08
 */
@Data
@Schema(description = "字典DTO")
@EqualsAndHashCode(callSuper = true)
public class DictionaryDTO extends PageDTO {

    @Schema(description = "主键ID")
    @Null(groups = ValidGroup.Create.class, message = "应用ID必须为空")
    @NotNull(groups = ValidGroup.Update.class, message = "应用ID不能为空")
    private Long id;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "字典名称")
    @NotBlank(message = "字典名称不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String dictName;

    @Schema(description = "字典类型")
    @NotBlank(message = "字典类型不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String dictType;

    @Schema(description = "字典排序")
    private Integer dictSort;

    @Schema(description = "字典标签")
    @NotBlank(message = "字典类型不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String dictLabel;

    @Schema(description = "字典键值")
    @NotBlank(message = "字典键值不能为空", groups = {ValidGroup.Create.class, ValidGroup.Update.class})
    private String dictValue;

    @Schema(description = "状态")
    @EnumString(groups = {ValidGroup.Create.class, ValidGroup.Update.class}, value = {"0", "1"}, message = "状态只允许为0或1")
    private String status;
}

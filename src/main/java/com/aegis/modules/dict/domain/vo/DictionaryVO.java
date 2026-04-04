package com.aegis.modules.dict.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 字典VO
 *
 * @author xuesong.lei
 * @since 2026-02-27
 */
@Data
@Schema(description = "字典VO")
public class DictionaryVO {

    @Schema(description = "主键ID")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "字典排序")
    private Integer dictSort;

    @Schema(description = "字典标签")
    private String dictLabel;

    @Schema(description = "字典键值")
    private String dictValue;

    @Schema(description = "状态(0-正常,1-停用)")
    private String status;
}

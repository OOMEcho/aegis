package com.aegis.common.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页DTO
 *
 * @author xuesong.lei
 * @since 2025/08/23 18:08
 */
@Data
@Schema(description = "分页DTO")
public class PageDTO {

    /**
     * 页数
     */
    @Schema(description = "页数")
    private Integer pageNum;

    /**
     * 当前页大小
     */
    @Schema(description = "当前页大小")
    private Integer pageSize;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段")
    private String sortField;

    /**
     * 排序方式 ASC DESC
     */
    @Schema(description = "排序方式 ASC DESC")
    private String sortOrder;

    public Integer getPageNum() {
        if (pageNum == null || pageNum <= 0) {
            return 1;
        }
        return pageNum;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize <= 0) {
            return 10;
        }
        // 限制最大页面大小
        return Math.min(pageSize, 1000);
    }
}

package com.aegis.common.page.dto;

import lombok.Data;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/23 18:08
 * @Description: 分页DTO
 */
@Data
public class PageDTO {

    /**
     * 页数
     */
    private Integer pageNum;

    /**
     * 当前页大小
     */
    private Integer pageSize;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式 ASC DESC
     */
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

package com.aegis.common.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/23 18:08
 * @Description: 分页响应VO
 */
@Data
@NoArgsConstructor
public class PageVO<T> {

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Long current;

    /**
     * 每页大小
     */
    private Long size;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;

    /**
     * 是否有下一页
     */
    private Boolean hasNext;

    // 完整构造函数
    public PageVO(List<T> records, Long total, Long current, Long size) {
        this.records = records == null ? Collections.emptyList() : records;
        this.total = total == null ? 0L : total;
        this.current = current == null ? 1L : current;
        this.size = size == null ? 10L : size;

        this.pages = this.size > 0 ? (this.total + this.size - 1) / this.size : 0L;
        this.hasPrevious = this.current > 1;
        this.hasNext = this.current < this.pages;
    }

    public static <T> PageVO<T> of(Page<T> page) {
        if (page == null) {
            return new PageVO<>(Collections.emptyList(), 0L, 1L, 10L);
        }
        return new PageVO<>(
                page.getRecords(),
                page.getTotal(),
                page.getCurrent(),
                page.getSize()
        );
    }
}

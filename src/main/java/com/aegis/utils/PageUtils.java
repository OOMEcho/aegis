package com.aegis.utils;

import com.aegis.common.page.dto.PageDTO;
import com.aegis.common.page.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/23 18:08
 * @Description: 分页工具类
 */
public class PageUtils {

    /**
     * 设置分页参数 - 基础版本
     */
    public static Paging setPage(int pageNum, int pageSize) {
        return new Paging(pageNum, pageSize, null, null);
    }

    /**
     * 设置分页参数 - 带排序
     */
    public static Paging setPage(int pageNum, int pageSize, String sortField, String sortOrder) {
        return new Paging(pageNum, pageSize, sortField, sortOrder);
    }

    /**
     * 从PageDTO创建分页
     */
    public static Paging of(PageDTO pageDTO) {
        return new Paging(
                pageDTO.getPageNum(),
                pageDTO.getPageSize(),
                pageDTO.getSortField(),
                pageDTO.getSortOrder()
        );
    }

    /**
     * 内部分页处理类
     */
    public static class Paging {

        private final int pageNum;

        private final int pageSize;

        private final String sortField;

        private final String sortOrder;

        public Paging(int pageNum, int pageSize, String sortField, String sortOrder) {
            this.pageNum = Math.max(pageNum, 1);
            this.pageSize = Math.min(Math.max(pageSize, 1), 1000);
            this.sortField = sortField;
            this.sortOrder = sortOrder;
        }

        /**
         * 执行分页查询 - 使用Service
         */
        public <T> PageVO<T> paging(IService<T> service) {
            Page<T> page = createPage();
            Page<T> result = service.page(page);
            return convertToPageVO(result);
        }

        /**
         * 执行分页查询 - 使用Service + QueryWrapper
         */
        public <T> PageVO<T> paging(IService<T> service, QueryWrapper<T> queryWrapper) {
            Page<T> page = createPage();
            Page<T> result = service.page(page, queryWrapper);
            return convertToPageVO(result);
        }

        /**
         * 执行分页查询 - 使用Service + LambdaQueryWrapper
         */
        public <T> PageVO<T> paging(IService<T> service, LambdaQueryWrapper<T> queryWrapper) {
            Page<T> page = createPage();
            Page<T> result = service.page(page, queryWrapper);
            return convertToPageVO(result);
        }

        /**
         * 执行分页查询 - 使用Mapper
         */
        public <T> PageVO<T> paging(BaseMapper<T> mapper) {
            Page<T> page = createPage();
            Page<T> result = mapper.selectPage(page, null);
            return convertToPageVO(result);
        }

        /**
         * 执行分页查询 - 使用Mapper + QueryWrapper
         */
        public <T> PageVO<T> paging(BaseMapper<T> mapper, QueryWrapper<T> queryWrapper) {
            Page<T> page = createPage();
            Page<T> result = mapper.selectPage(page, queryWrapper);
            return convertToPageVO(result);
        }

        /**
         * 执行分页查询 - 使用Mapper + LambdaQueryWrapper
         */
        public <T> PageVO<T> paging(BaseMapper<T> mapper, LambdaQueryWrapper<T> queryWrapper) {
            Page<T> page = createPage();
            Page<T> result = mapper.selectPage(page, queryWrapper);
            return convertToPageVO(result);
        }

        /**
         * Service数据转换分页 - 在分页后进行数据转换
         */
        public <T, R> PageVO<R> pagingAndConvert(IService<T> service, Function<T, R> converter) {
            PageVO<T> pageResult = paging(service);
            List<R> convertedRecords = pageResult.getRecords().stream()
                    .map(converter)
                    .collect(Collectors.toList());
            return new PageVO<>(convertedRecords, pageResult.getTotal(),
                    pageResult.getCurrent(), pageResult.getSize());
        }

        /**
         * Service数据转换分页 - 带条件查询
         */
        public <T, R> PageVO<R> pagingAndConvert(IService<T> service, QueryWrapper<T> queryWrapper,
                                                 Function<T, R> converter) {
            PageVO<T> pageResult = paging(service, queryWrapper);
            List<R> convertedRecords = pageResult.getRecords().stream()
                    .map(converter)
                    .collect(Collectors.toList());
            return new PageVO<>(convertedRecords, pageResult.getTotal(),
                    pageResult.getCurrent(), pageResult.getSize());
        }

        /**
         * Service数据转换分页 - 带Lambda条件查询
         */
        public <T, R> PageVO<R> pagingAndConvert(IService<T> service, LambdaQueryWrapper<T> queryWrapper,
                                                 Function<T, R> converter) {
            PageVO<T> pageResult = paging(service, queryWrapper);
            List<R> convertedRecords = pageResult.getRecords().stream()
                    .map(converter)
                    .collect(Collectors.toList());
            return new PageVO<>(convertedRecords, pageResult.getTotal(),
                    pageResult.getCurrent(), pageResult.getSize());
        }

        /**
         * Mapper数据转换分页 - 在分页后进行数据转换
         */
        public <T, R> PageVO<R> pagingAndConvert(BaseMapper<T> mapper, Function<T, R> converter) {
            PageVO<T> pageResult = paging(mapper);
            List<R> convertedRecords = pageResult.getRecords().stream()
                    .map(converter)
                    .collect(Collectors.toList());
            return new PageVO<>(convertedRecords, pageResult.getTotal(),
                    pageResult.getCurrent(), pageResult.getSize());
        }

        /**
         * Mapper数据转换分页 - 带条件查询
         */
        public <T, R> PageVO<R> pagingAndConvert(BaseMapper<T> mapper, QueryWrapper<T> queryWrapper,
                                                 Function<T, R> converter) {
            PageVO<T> pageResult = paging(mapper, queryWrapper);
            List<R> convertedRecords = pageResult.getRecords().stream()
                    .map(converter)
                    .collect(Collectors.toList());
            return new PageVO<>(convertedRecords, pageResult.getTotal(),
                    pageResult.getCurrent(), pageResult.getSize());
        }

        /**
         * Mapper数据转换分页 - 带Lambda条件查询
         */
        public <T, R> PageVO<R> pagingAndConvert(BaseMapper<T> mapper, LambdaQueryWrapper<T> queryWrapper,
                                                 Function<T, R> converter) {
            PageVO<T> pageResult = paging(mapper, queryWrapper);
            List<R> convertedRecords = pageResult.getRecords().stream()
                    .map(converter)
                    .collect(Collectors.toList());
            return new PageVO<>(convertedRecords, pageResult.getTotal(),
                    pageResult.getCurrent(), pageResult.getSize());
        }

        /**
         * 处理已有数据列表的分页（内存分页，不推荐大数据量使用）
         */
        public <T> PageVO<T> paging(List<T> dataList) {
            if (dataList == null || dataList.isEmpty()) {
                return new PageVO<>(Collections.emptyList(), 0L, (long) pageNum, (long) pageSize);
            }

            int total = dataList.size();
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);

            List<T> pageData;
            if (fromIndex >= total) {
                pageData = Collections.emptyList();
            } else {
                pageData = dataList.subList(fromIndex, toIndex);
            }

            return new PageVO<>(pageData, (long) total, (long) pageNum, (long) pageSize);
        }

        /**
         * 创建Page对象
         */
        private <T> Page<T> createPage() {
            Page<T> page = new Page<>(pageNum, pageSize);

            // 处理排序
            if (StringUtils.hasText(sortField)) {
                boolean isAsc = "asc".equalsIgnoreCase(sortOrder);
                OrderItem orderItem = new OrderItem();
                orderItem.setColumn(sortField);
                orderItem.setAsc(isAsc);
                page.addOrder(orderItem);
            }

            return page;
        }

        /**
         * 转换为PageVO
         */
        private <T> PageVO<T> convertToPageVO(Page<T> page) {
            return new PageVO<>(
                    page.getRecords(),
                    page.getTotal(),
                    page.getCurrent(),
                    page.getSize()
            );
        }
    }
}

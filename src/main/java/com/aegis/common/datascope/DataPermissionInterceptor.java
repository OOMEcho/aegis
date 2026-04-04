package com.aegis.common.datascope;

import com.aegis.common.constant.DataScopeConstants;
import com.aegis.utils.SpringContextUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据权限拦截器
 *
 * @author xuesong.lei
 * @since 2025-09-10
 */
@Slf4j
public class DataPermissionInterceptor implements InnerInterceptor {

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        if (!SqlCommandType.SELECT.equals(ms.getSqlCommandType())) {
            return;
        }

        // 从线程本地变量获取数据权限信息
        DataPermissionContextHolder.DataPermissionInfo permissionInfo = DataPermissionContextHolder.get();
        if (permissionInfo == null) {// 没有权限信息，说明方法没有添加@DataPermission注解，直接放行
            return;
        }

        DataPermission dataPermission = permissionInfo.getAnnotation();
        DataPermissionContext context = permissionInfo.getContext();

        // 检查注解是否启用
        if (!dataPermission.enable()) {
            return;
        }

        String originalSql = boundSql.getSql();

        try {
            // 解析 SQL
            Statement statement = CCJSqlParserUtil.parse(originalSql);
            if (!(statement instanceof Select select)) {
                return;
            }

            if (select.getPlainSelect() == null) {
                return;
            }

            PlainSelect plainSelect = select.getPlainSelect();

            Expression filterExpression = buildDataPermissionExpression(plainSelect, dataPermission, context);
            if (filterExpression == null) {
                return;
            }

            // 合并 WHERE 条件
            if (plainSelect.getWhere() == null) {
                plainSelect.setWhere(filterExpression);
            } else {
                plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), filterExpression));
            }

            // 替换 SQL
            String newSql = plainSelect.toString();
            PluginUtils.mpBoundSql(boundSql).sql(newSql);

            if (log.isDebugEnabled()) {
                log.debug("应用数据权限过滤器，原SQL:\n{}\n新SQL:\n{}", originalSql, newSql);
            }
        } catch (Exception e) {
            log.error("应用数据权限过滤器失败，SQL: {}", originalSql, e);
        }
    }

    /**
     * 构建数据权限 SQL 条件
     */
    private Expression buildDataPermissionExpression(PlainSelect plainSelect, DataPermission dataPermission, DataPermissionContext context) {

        if (context == null || context.getDataScope() == null) {
            return null;
        }

        String dataScope = context.getDataScope();
        if (DataScopeConstants.DATA_SCOPE_ALL.equals(dataScope)) {
            return null; // 全部数据权限
        }

        String tablePrefix = getMainTableAlias(plainSelect, dataPermission);
        String deptField = getDeptField(dataPermission);
        String userField = getUserField(dataPermission);

        switch (dataScope) {
            case DataScopeConstants.DATA_SCOPE_CUSTOM:
                Set<Long> deptIds = context.getDeptIds();
                if (deptIds != null && !deptIds.isEmpty()) {
                    return buildInExpression(tablePrefix, deptField, deptIds);
                }
                return null;

            case DataScopeConstants.DATA_SCOPE_DEPT:
                Long deptId = context.getDeptId();
                if (deptId != null) {
                    return buildEqualsExpression(tablePrefix, deptField, deptId);
                }
                return null;

            case DataScopeConstants.DATA_SCOPE_DEPT_AND_CHILD:
                DataPermissionHandler handler = SpringContextUtil.getBean(DataPermissionHandler.class);
                Set<Long> allDeptIds = handler.getDeptAndChildrenIds(context.getDeptId());
                if (allDeptIds != null && !allDeptIds.isEmpty()) {
                    return buildInExpression(tablePrefix, deptField, allDeptIds);
                }
                return null;

            case DataScopeConstants.DATA_SCOPE_SELF:
                Long userId = context.getUserId();
                if (userId == null) {
                    return null;
                }
                return buildEqualsExpression(tablePrefix, userField, userId);

            default:
                return null;
        }
    }

    /**
     * 获取主表别名，如果注解指定则优先使用
     */
    private String getMainTableAlias(PlainSelect plainSelect, DataPermission dataPermission) {
        if (dataPermission != null && StringUtils.hasText(dataPermission.tableAlias())) {
            return dataPermission.tableAlias();
        }

        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table table) {
            return table.getAlias() != null ? table.getAlias().getName() : table.getName();
        }

        log.warn("数据权限不支持的SQL结构: {}", plainSelect);
        return "";
    }

    /**
     * 获取部门字段，优先使用注解指定的字段
     */
    private String getDeptField(DataPermission dataPermission) {
        return (dataPermission != null && StringUtils.hasText(dataPermission.deptField()))
                ? dataPermission.deptField()
                : DataScopeConstants.DEFAULT_DEPT_FIELD;
    }

    /**
     * 获取用户字段，优先使用注解指定的字段
     */
    private String getUserField(DataPermission dataPermission) {
        return (dataPermission != null && StringUtils.hasText(dataPermission.userField()))
                ? dataPermission.userField()
                : DataScopeConstants.DEFAULT_USER_FIELD;
    }

    /**
     * 构建等于条件
     */
    private Expression buildEqualsExpression(String tablePrefix, String field, Object value) {
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(buildColumn(tablePrefix, field));
        if (value instanceof Number) {
            equalsTo.setRightExpression(new LongValue(value.toString()));
        } else {
            equalsTo.setRightExpression(new StringValue(value.toString()));
        }
        return equalsTo;
    }

    /**
     * 构建 IN 条件
     */
    private Expression buildInExpression(String tablePrefix, String field, Set<Long> values) {
        InExpression in = new InExpression();
        in.setLeftExpression(buildColumn(tablePrefix, field));

        List<Expression> list = values.stream().map(LongValue::new).collect(Collectors.toList());
        in.setRightExpression(new ExpressionList<>(list));
        return in;
    }

    /**
     * 使用 Column 避免 SQL 注入
     */
    private Column buildColumn(String tablePrefix, String field) {
        if (StringUtils.hasText(tablePrefix)) {
            return new Column(tablePrefix + "." + field);
        }
        return new Column(field);
    }
}

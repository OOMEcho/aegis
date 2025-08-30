DROP TABLE IF EXISTS `t_table_name`;
CREATE TABLE `t_table_name`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   VARCHAR(64)              DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(64)              DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='示例表';

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`       VARCHAR(64)              DEFAULT NULL COMMENT '创建人',
    `update_by`       VARCHAR(64)              DEFAULT NULL COMMENT '更新人',
    `create_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`         INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`          VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `dept_id`         BIGINT                   DEFAULT NULL COMMENT '部门ID',
    `username`        VARCHAR(100)    NOT NULL COMMENT '用户名',
    `password`        VARCHAR(255)    NOT NULL COMMENT '密码',
    `nickname`        VARCHAR(50)              DEFAULT NULL COMMENT '呢称',
    `email`           VARCHAR(50)     NOT NULL COMMENT '邮箱',
    `sex`             CHAR(1)                  DEFAULT '0' COMMENT '性别(0-男,1-女)',
    `phone`           VARCHAR(11)              DEFAULT NULL COMMENT '电话',
    `avatar`          VARCHAR(100)             DEFAULT NULL COMMENT '头像',
    `status`          CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1-停用)',
    `last_login_ip`   VARCHAR(64)              DEFAULT NULL COMMENT '最后登录IP',
    `last_login_time` DATETIME                 DEFAULT NULL COMMENT '最后登录时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_username` (`username`) USING BTREE,
    UNIQUE KEY `uk_email` (`email`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户信息表';

DROP TABLE IF EXISTS `t_dept`;
CREATE TABLE `t_dept`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   VARCHAR(64)              DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(64)              DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`      VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `ancestors`   VARCHAR(50)              DEFAULT NULL COMMENT '祖级列表',
    `parent_id`   BIGINT          NOT NULL DEFAULT 0 COMMENT '父部门ID',
    `dept_name`   VARCHAR(64)     NOT NULL COMMENT '部门名称',
    `order_num`   INT             NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `leader`      VARCHAR(20)              DEFAULT NULL COMMENT '负责人',
    `phone`       VARCHAR(11)              DEFAULT NULL COMMENT '联系电话',
    `email`       VARCHAR(50)              DEFAULT NULL COMMENT '邮箱',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '部门状态(0-正常,1-停用)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '部门信息表';

DROP TABLE IF EXISTS `t_post`;
CREATE TABLE `t_post`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   VARCHAR(64)              DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(64)              DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`      VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `post_code`   VARCHAR(64)     NOT NULL COMMENT '岗位编码',
    `post_name`   VARCHAR(50)     NOT NULL COMMENT '岗位名称',
    `post_sort`   INT             NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1-停用)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='岗位信息表';

DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`
(
    `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`           VARCHAR(64)              DEFAULT NULL COMMENT '创建人',
    `update_by`           VARCHAR(64)              DEFAULT NULL COMMENT '更新人',
    `create_time`         DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`             TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`             INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`              VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `role_name`           VARCHAR(30)     NOT NULL COMMENT '角色名称',
    `role_code`           VARCHAR(100)    NOT NULL COMMENT '角色权限字符串',
    `role_sort`           INT             NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `data_scope`          CHAR(1)         NOT NULL DEFAULT '1' COMMENT '数据范围(1-全部数据权限,2-自定数据权限,3-本部门数据权限,4-本部门及以下数据权限)',
    `menu_check_strictly` INT                      DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
    `dept_check_strictly` INT                      DEFAULT 1 COMMENT '部门树选择项是否关联显示',
    `status`              CHAR(1)         NOT NULL DEFAULT '0' COMMENT '角色状态(0-正常,1-停用)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '角色信息表';

DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   VARCHAR(64)              DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(64)              DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`      VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `menu_name`   VARCHAR(50)     NOT NULL COMMENT '菜单名称',
    `parent_id`   BIGINT          NOT NULL DEFAULT 0 COMMENT '父菜单ID',
    `order_num`   INT             NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `request_url` VARCHAR(255)             DEFAULT NULL COMMENT '请求地址',
    `path`        VARCHAR(200)             DEFAULT NULL COMMENT '路由地址',
    `component`   VARCHAR(255)             DEFAULT NULL COMMENT '组件路径',
    `is_frame`    INT                      DEFAULT 1 COMMENT '是否为外链(0-是,1-否)',
    `is_cache`    INT                      DEFAULT 0 COMMENT '是否缓存(0-缓存,1-不缓存)',
    `menu_type`   CHAR(1)                  DEFAULT NULL COMMENT '菜单类型(D-目录,M-菜单,B-按钮)',
    `visible`     CHAR(1)                  DEFAULT '0' COMMENT '菜单状态(0-显示,1-隐藏)',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '菜单状态(0-正常,1-停用)',
    `perms`       VARCHAR(100)             DEFAULT NULL COMMENT '权限标识',
    `icon`        VARCHAR(100)             DEFAULT '#' COMMENT '菜单图标',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '菜单权限表';

DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`
(
    `id`      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT          NOT NULL COMMENT '用户ID',
    `role_id` BIGINT          NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY uk_user_role (`user_id`, `role_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_role_id (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户和角色关联表';

DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu`
(
    `id`      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT          NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT          NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY uk_role_menu (`role_id`, `menu_id`),
    INDEX idx_role_id (`role_id`),
    INDEX idx_menu_id (`menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '角色和菜单关联表';

DROP TABLE IF EXISTS `t_role_dept`;
CREATE TABLE `t_role_dept`
(
    `id`      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT          NOT NULL COMMENT '角色ID',
    `dept_id` BIGINT          NOT NULL COMMENT '部门ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY uk_role_dept (`role_id`, `dept_id`),
    INDEX idx_role_id (`role_id`),
    INDEX idx_dept_id (`dept_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '角色和部门关联表';

DROP TABLE IF EXISTS `t_user_post`;
CREATE TABLE `t_user_post`
(
    `id`      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT          NOT NULL COMMENT '用户ID',
    `post_id` BIGINT          NOT NULL COMMENT '岗位ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY uk_user_post (`user_id`, `post_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_post_id (`post_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户与岗位关联表';

DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`      VARCHAR(64)              DEFAULT NULL COMMENT '创建人',
    `update_by`      VARCHAR(64)              DEFAULT NULL COMMENT '更新人',
    `create_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`        TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`        INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`         VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `notice_title`   VARCHAR(50)     NOT NULL COMMENT '公告标题',
    `notice_type`    CHAR(1)         NOT NULL COMMENT '公告类型(1-通知,2-公告)',
    `notice_content` LONGTEXT        NOT NULL COMMENT '公告内容',
    `target_type`    TINYINT         NOT NULL DEFAULT 1 COMMENT '目标类型(1=全部用户,2=指定用户)',
    `status`         CHAR(1)         NOT NULL DEFAULT '0' COMMENT '公告状态(0-正常,1-关闭)',
    `publish_time`   DATETIME                 DEFAULT NULL COMMENT '发布时间',
    `expire_time`    DATETIME                 DEFAULT NULL COMMENT '过期时间,可为空',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '通知公告表';

DROP TABLE IF EXISTS `t_notice_user`;
CREATE TABLE `t_notice_user`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   VARCHAR(64)              DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(64)              DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `notice_id`   BIGINT          NOT NULL COMMENT '通知ID',
    `user_id`     BIGINT          NOT NULL COMMENT '用户ID',
    `read_flag`   TINYINT         NOT NULL DEFAULT 0 COMMENT '是否已读(0=未读,1=已读)',
    `read_time`   DATETIME                 DEFAULT NULL COMMENT '阅读时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY uk_notice_user (notice_id, user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '通知接收记录表';

DROP TABLE IF EXISTS `t_dictionary`;
CREATE TABLE `t_dictionary`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   VARCHAR(64)              DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(64)              DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`      VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `dict_name`   VARCHAR(100)    NOT NULL COMMENT '字典名称',
    `dict_type`   VARCHAR(100)    NOT NULL COMMENT '字典类型',
    `dict_sort`   INT                      DEFAULT 0 COMMENT '字典排序',
    `dict_label`  VARCHAR(100)    NOT NULL COMMENT '字典标签',
    `dict_value`  VARCHAR(100)    NOT NULL COMMENT '字典键值',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1停用)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '字典表';

DROP TABLE IF EXISTS `t_sys_operate_log`;
CREATE TABLE `t_sys_operate_log`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `module_title`    VARCHAR(64)              DEFAULT NULL COMMENT '模块标题',
    `business_type`   INT                      DEFAULT NULL COMMENT '业务类型(0-其它,1-新增,2-修改,3-删除)',
    `request_url`     VARCHAR(128)             DEFAULT NULL COMMENT '请求地址',
    `request_ip`      VARCHAR(64)              DEFAULT NULL COMMENT '请求IP',
    `request_local`   VARCHAR(64)              DEFAULT NULL COMMENT '请求地点',
    `request_type`    VARCHAR(32)              DEFAULT NULL COMMENT '请求方式',
    `request_method`  VARCHAR(255)             DEFAULT NULL COMMENT '请求方法',
    `request_args`    LONGTEXT                 DEFAULT NULL COMMENT '请求参数',
    `response_result` LONGTEXT                 DEFAULT NULL COMMENT '响应结果',
    `error_message`   LONGTEXT                 DEFAULT NULL COMMENT '错误响应',
    `operate_user`    VARCHAR(64)              DEFAULT NULL COMMENT '操作用户',
    `operate_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `deplete_time`    BIGINT                   DEFAULT 0 COMMENT '消耗时间(单位：毫秒)',
    `operate_status`  CHAR(1)                  DEFAULT '0' COMMENT '操作状态(0-成功,1-失败)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志表';

DROP TABLE IF EXISTS `t_sys_login_log`;
CREATE TABLE `t_sys_login_log`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `login_username` VARCHAR(64)              DEFAULT NULL COMMENT '用户名',
    `login_ip`       VARCHAR(64)              DEFAULT NULL COMMENT '登录IP地址',
    `login_local`    VARCHAR(255)             DEFAULT NULL COMMENT '登录地点',
    `login_browser`  VARCHAR(50)              DEFAULT NULL COMMENT '浏览器类型',
    `login_os`       VARCHAR(50)              DEFAULT NULL COMMENT '操作系统',
    `login_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    `login_status`   CHAR(1)         NOT NULL DEFAULT '0' COMMENT '操作状态(0-成功,1-失败)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='登录日志表';

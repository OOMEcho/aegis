DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`       BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`       BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`         INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`          VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `dept_id`         BIGINT                   DEFAULT NULL COMMENT '部门ID',
    `username`        VARCHAR(16)     NOT NULL COMMENT '用户名',
    `password`        VARCHAR(255)    NOT NULL COMMENT '密码',
    `nickname`        VARCHAR(16)              DEFAULT NULL COMMENT '呢称',
    `email`           VARCHAR(32)     NOT NULL COMMENT '邮箱',
    `sex`             CHAR(1)                  DEFAULT '0' COMMENT '性别(0-男,1-女)',
    `phone`           VARCHAR(11)              DEFAULT NULL COMMENT '电话',
    `avatar`          VARCHAR(255)             DEFAULT NULL COMMENT '头像',
    `status`          CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1-停用)',
    `last_login_ip`   VARCHAR(32)              DEFAULT NULL COMMENT '最后登录IP',
    `last_login_time` DATETIME                 DEFAULT NULL COMMENT '最后登录时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_username` (`username`) USING BTREE,
    UNIQUE KEY `uk_email` (`email`) USING BTREE,
    INDEX `idx_user_phone` (`phone`),
    INDEX `idx_user_dept_id` (`dept_id`),
    INDEX `idx_user_deleted_create_time` (`deleted`, `create_time`),
    INDEX `idx_user_create_by` (`create_by`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户信息表';

DROP TABLE IF EXISTS `t_dept`;
CREATE TABLE `t_dept`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`      VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `ancestors`   VARCHAR(255)             DEFAULT NULL COMMENT '祖级列表',
    `parent_id`   BIGINT          NOT NULL DEFAULT 0 COMMENT '父部门ID',
    `dept_name`   VARCHAR(64)     NOT NULL COMMENT '部门名称',
    `order_num`   INT             NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `leader`      VARCHAR(16)              DEFAULT NULL COMMENT '负责人',
    `phone`       VARCHAR(11)              DEFAULT NULL COMMENT '联系电话',
    `email`       VARCHAR(32)              DEFAULT NULL COMMENT '邮箱',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '部门状态(0-正常,1-停用)',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_dept_parent_id_order_num` (`parent_id`, `order_num`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '部门信息表';

DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`
(
    `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`           BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`           BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time`         DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`             TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`             INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`              VARCHAR(128)             DEFAULT NULL COMMENT '备注',
    `role_name`           VARCHAR(32)     NOT NULL COMMENT '角色名称',
    `role_code`           VARCHAR(16)     NOT NULL COMMENT '角色编码',
    `order_num`           INT             NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `data_scope`          CHAR(1)         NOT NULL DEFAULT '1' COMMENT '数据范围(1-全部数据权限,2-自定数据权限,3-本部门数据权限,4-本部门及以下数据权限,5-仅本人数据权限)',
    `dept_check_strictly` INT                      DEFAULT 1 COMMENT '部门树选择项是否关联显示',
    `status`              CHAR(1)         NOT NULL DEFAULT '0' COMMENT '角色状态(0-正常,1-停用)',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_role_deleted_status` (`deleted`, `status`),
    INDEX `idx_role_role_code` (`role_code`),
    INDEX `idx_role_role_name` (`role_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '角色信息表';

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

DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `perm_code`   VARCHAR(64)     NOT NULL COMMENT '权限编码(全局唯一，如 system:user:add)',
    `perm_name`   VARCHAR(64)     NOT NULL COMMENT '权限名称',
    `perm_type`   CHAR(1)         NOT NULL COMMENT '权限类型(M=页面,B=按钮,A=API)',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1-停用)',
    `remark`      VARCHAR(128)             DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_perm_code` (`perm_code`),
    INDEX `idx_perm_deleted_status` (`deleted`, `status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='功能权限表';

DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission`
(
    `id`        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id`   BIGINT          NOT NULL COMMENT '角色ID',
    `perm_code` VARCHAR(64)     NOT NULL COMMENT '权限编码',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_perm` (`role_id`, `perm_code`),
    INDEX `idx_rp_perm_code` (`perm_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色与权限关联表';

DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`      BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`      BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`        TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`        INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `request_method` VARCHAR(16)     NOT NULL COMMENT '请求方法(GET/POST/PUT/DELETE/ALL)',
    `request_uri`    VARCHAR(255)    NOT NULL COMMENT 'URI匹配模式(Ant风格)',
    `perm_code`      VARCHAR(64)     NOT NULL COMMENT '关联权限编码',
    `status`         CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1-停用)',
    `remark`         VARCHAR(128)             DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    INDEX `idx_method_uri` (`request_method`, `request_uri`),
    INDEX `idx_perm_code` (`perm_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='资源(URL)与权限映射表';

DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`      VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `menu_code`   VARCHAR(64)     NOT NULL COMMENT '菜单编码(唯一,前端组件映射用)',
    `menu_name`   VARCHAR(50)     NOT NULL COMMENT '菜单名称',
    `parent_id`   BIGINT          NOT NULL DEFAULT 0 COMMENT '父菜单ID',
    `order_num`   INT             NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `name`        VARCHAR(50)              DEFAULT NULL COMMENT '前端路由名称',
    `path`        VARCHAR(128)             DEFAULT NULL COMMENT '前端路由路径',
    `menu_type`   CHAR(1)         NOT NULL COMMENT '菜单类型(D-目录,M-菜单)',
    `icon`        VARCHAR(64)              DEFAULT '#' COMMENT '菜单图标',
    `hidden`      TINYINT                  DEFAULT 0 COMMENT '菜单状态(0-显示,1-隐藏)',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '菜单状态(0-正常,1-停用)',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_menu_code` (`menu_code`),
    UNIQUE KEY `uk_menu_route_name` (`name`),
    INDEX `idx_menu_parent_id_order_num` (`parent_id`, `order_num`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '菜单表';

DROP TABLE IF EXISTS `t_menu_permission`;
CREATE TABLE `t_menu_permission`
(
    `id`        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `menu_id`   BIGINT          NOT NULL COMMENT '菜单ID',
    `perm_code` VARCHAR(64)     NOT NULL COMMENT '权限编码',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_menu_perm` (`menu_id`, `perm_code`),
    INDEX `idx_mp_perm_code` (`perm_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单与权限关联表';

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

DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`      BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`      BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`        TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`        INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`         VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `notice_title`   VARCHAR(64)     NOT NULL COMMENT '通知标题',
    `notice_type`    CHAR(1)         NOT NULL COMMENT '通知类型(1=系统通知,2=公告,3=提醒)',
    `notice_content` LONGTEXT        NOT NULL COMMENT '通知内容',
    `target_type`    TINYINT         NOT NULL DEFAULT 1 COMMENT '目标类型(1=全部用户,2=指定用户,3=指定角色,4=指定部门))',
    `target_ids`     LONGTEXT                 DEFAULT NULL COMMENT '目标对象ID列表,逗号分隔(根据target_type解释含义)',
    `status`         CHAR(1)         NOT NULL DEFAULT '0' COMMENT '通知状态(0=待发布,1=已发布,2=已撤回)',
    `publish_time`   DATETIME                 DEFAULT NULL COMMENT '计划发布时间,为空则立即发布',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_notice_status_deleted_publish_time` (`status`, `deleted`, `publish_time`),
    INDEX `idx_notice_deleted_create_time` (`deleted`, `create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '通知公告表';

DROP TABLE IF EXISTS `t_notice_user`;
CREATE TABLE `t_notice_user`
(
    `id`        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `notice_id` BIGINT          NOT NULL COMMENT '通知ID',
    `user_id`   BIGINT          NOT NULL COMMENT '用户ID',
    `read_flag` TINYINT         NOT NULL DEFAULT 0 COMMENT '是否已读(0=未读,1=已读)',
    `read_time` DATETIME                 DEFAULT NULL COMMENT '阅读时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY uk_notice_user (notice_id, user_id),
    INDEX `idx_nu_user_id_read_flag` (`user_id`, `read_flag`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '通知接收记录表';

DROP TABLE IF EXISTS `t_dictionary`;
CREATE TABLE `t_dictionary`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`      VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `dict_name`   VARCHAR(32)     NOT NULL COMMENT '字典名称',
    `dict_type`   VARCHAR(32)     NOT NULL COMMENT '字典类型',
    `dict_sort`   INT                      DEFAULT 0 COMMENT '字典排序',
    `dict_label`  VARCHAR(32)     NOT NULL COMMENT '字典标签',
    `dict_value`  VARCHAR(32)     NOT NULL COMMENT '字典键值',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1停用)',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_dict_dict_type` (`dict_type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '字典表';

DROP TABLE IF EXISTS `t_whitelist`;
CREATE TABLE `t_whitelist`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`      BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`      BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`        TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`        INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`         VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `request_method` VARCHAR(8)      NOT NULL COMMENT '请求方法,GET,POST,PUT,DELETE,ALL=不限制',
    `request_uri`    VARCHAR(255)    NOT NULL COMMENT 'URI匹配模式,支持Ant风格,比如/api/user/**',
    `description`    VARCHAR(128)             DEFAULT NULL COMMENT '描述',
    `status`         CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1停用)',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_whitelist_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '白名单表';

DROP TABLE IF EXISTS `t_sys_operate_log`;
CREATE TABLE `t_sys_operate_log`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `trace_id`        VARCHAR(64)              DEFAULT NULL COMMENT '链路追踪ID',
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
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_trace_id` (`trace_id`) USING BTREE COMMENT '链路追踪ID索引',
    INDEX `idx_operate_log_operate_time` (`operate_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志表';

DROP TABLE IF EXISTS `t_sys_login_log`;
CREATE TABLE `t_sys_login_log`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `login_username` VARCHAR(64)              DEFAULT NULL COMMENT '用户名',
    `login_ip`       VARCHAR(64)              DEFAULT NULL COMMENT '登录IP地址',
    `login_local`    VARCHAR(128)             DEFAULT NULL COMMENT '登录地点',
    `login_browser`  VARCHAR(64)              DEFAULT NULL COMMENT '浏览器类型',
    `login_os`       VARCHAR(64)              DEFAULT NULL COMMENT '操作系统',
    `login_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    `login_status`   CHAR(1)         NOT NULL DEFAULT '0' COMMENT '操作状态(0-成功,1-失败)',
    `error_message`  LONGTEXT                 DEFAULT NULL COMMENT '错误响应',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_login_log_time_status` (`login_time`, `login_status`),
    INDEX `idx_login_log_login_username` (`login_username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='登录日志表';

DROP TABLE IF EXISTS `t_file_metadata`;
CREATE TABLE `t_file_metadata`
(
    `id`                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`          BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`          BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`            TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`            INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`             VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `file_name`          VARCHAR(255)    NOT NULL COMMENT '文件名称',
    `original_file_name` VARCHAR(255)    NOT NULL COMMENT '原始文件名称',
    `suffix`             VARCHAR(32)              DEFAULT NULL COMMENT '文件后缀',
    `file_path`          VARCHAR(255)    NOT NULL COMMENT '文件存储路径',
    `file_size`          BIGINT          NOT NULL COMMENT '文件大小,单位字节',
    `content_type`       VARCHAR(128)             DEFAULT NULL COMMENT '文件类型',
    `platform`           VARCHAR(16)              DEFAULT NULL COMMENT '存储平台',
    `upload_time`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `md5`                VARCHAR(64)              DEFAULT NULL COMMENT '文件MD5值',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_file_file_path` (`file_path`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '文件元数据表';


DROP TABLE IF EXISTS `boss_user_info`;
CREATE TABLE `boss_user_info`
(
    id           bigint unsigned primary key auto_increment comment '自增编号',
    user_id      varchar(30)  NOT NULL COMMENT '用户编号',
    user_name    varchar(30)  NOT NULL COMMENT '用户名',
    account      varchar(30)  NOT NULL COMMENT '登陆帐户',
    password     varchar(50)  NOT NULL COMMENT '密码',
    sex          varchar(2)   NOT NULL default '0' COMMENT '性别(0:未知;1:男;2:女)',
    dept_id      varchar(100) NOT NULL COMMENT '部门编号',
    locked       varchar(2)   NOT NULL default '0' COMMENT '锁定标志(1:锁定;0:激活)',
    mobile_area  varchar(8)   NOT NULL default '86' comment '手机号区号',
    mobile_phone varchar(20)           default null comment '手机号',
    google_key   varchar(128)          comment 'google验证',
    remark       varchar(50)           default NULL COMMENT '备注',
    user_type    varchar(2)            default '1' COMMENT '人员类型(1:业务员;2:管理员;3:系统内置人员)',
    enable       varchar(2)   NOT NULL default '1' COMMENT '启用状态',
    create_time  datetime     not null default current_timestamp comment '创建时间',
    update_time  datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    unique KEY uniq_user_id (`user_id`),
    unique KEY uniq_account (account)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户信息表';

DROP TABLE IF EXISTS `boss_role`;
CREATE TABLE `boss_role`
(
    id          bigint unsigned primary key auto_increment comment '自增编号',
    `role_id`   varchar(30) NOT NULL COMMENT '角色编号',
    `role_name` varchar(50) NOT NULL COMMENT '角色名称',
    `role_type` varchar(2)  NOT NULL default '1' COMMENT '角色类型(1:业务角色;2:管理角色 ;3:系统内置角色)',
    role_grade  varchar(50) comment '角色等级',
    `remark`    varchar(50)          default NULL COMMENT '备注',
    `locked`    varchar(2)           default '0' COMMENT '锁定标志(1:锁定;0:激活)',
    create_time datetime    not null default current_timestamp comment '创建时间',
    update_time datetime    not null default current_timestamp on update current_timestamp comment '更新时间',
    unique KEY uniq_role_id (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色信息表';

DROP TABLE IF EXISTS `boss_role_authorize`;
CREATE TABLE `boss_role_authorize`
(
    id                bigint unsigned primary key auto_increment comment '自增编号',
    `role_id`         varchar(30) NOT NULL COMMENT '角色编号',
    `menu_id`         varchar(60) NOT NULL COMMENT '菜单编号',
    `authorize_level` varchar(2)  NOT NULL default '1' COMMENT '权限级别(1:访问权限;2:管理权限)',
    create_time       datetime    not null default current_timestamp comment '创建时间',
    update_time       datetime    not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uniq_role_menu (role_id, menu_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色授权表';

DROP TABLE IF EXISTS `boss_user_authorize`;
CREATE TABLE `boss_user_authorize`
(
    id          bigint unsigned primary key auto_increment comment '自增编号',
    `user_id`   varchar(30) NOT NULL COMMENT '用户编号',
    `role_id`   varchar(30) NOT NULL COMMENT '角色编号',
    create_time datetime    not null default current_timestamp comment '创建时间',
    update_time datetime    not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uniq_user_role (user_id, role_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户授权表';

DROP TABLE IF EXISTS `boss_menu`;
CREATE TABLE `boss_menu`
(
    id          bigint unsigned primary key auto_increment comment '自增编号',
    menu_id     varchar(60) NOT NULL COMMENT '菜单编号',
    menu_name   varchar(50) NOT NULL COMMENT '菜单名称',
    parent_id   varchar(60) NOT NULL COMMENT '上级菜单编号',
    icon_cls    varchar(50)          default NULL COMMENT '节点图标CSS类名',
    expanded    varchar(2)           default '0' COMMENT '展开状态(1:展开;0:收缩)',
    request     varchar(200)         default NULL COMMENT '请求地址',
    menu_level  varchar(2)  NOT NULL default '0' COMMENT '菜单级别(0:树枝节点;1:叶子节点;2:按钮级别)',
    sort_no     int(4)               default NULL COMMENT '排序号',
    remark      varchar(200)         default NULL COMMENT '备注',
    icon        varchar(50)          default NULL COMMENT '节点图标',
    menu_type   varchar(2)           default '0' COMMENT '菜单类型(1:系统菜单;0:业务菜单)',
    menu_source varchar(2)           default '1' COMMENT '菜单来源(1:系统初始化;2:动态增加)',
    create_time datetime    not null default current_timestamp comment '创建时间',
    update_time datetime    not null default current_timestamp on update current_timestamp comment '更新时间',
    unique KEY uniq_menu_id (`menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单资源信息表';

drop table if exists boss_operation_log;
create table boss_operation_log
(
    id             bigint unsigned primary key auto_increment comment '自增编号',
    user_id        varchar(30) not null comment '操作人编号',
    user_name      varchar(30) comment '操作人姓名',
    operation_biz  varchar(60) not null comment '操作业务',
    biz_id         varchar(120) comment '业务编号',
    operation_type varchar(10) not null comment '操作类型 ADD/DELETE/UPDATE',
    operation_time datetime    not null default current_timestamp on update current_timestamp comment '操作时间',
    remark         varchar(1200) comment '操作说明',
    key idx_oper_user_id (user_id),
    key idx_biz (operation_biz, biz_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志表';

drop table if exists boss_login_log;
create table boss_login_log
(
    id         bigint unsigned primary key auto_increment comment '自增编号',
    user_id    varchar(30) not null comment '登录用户',
    login_time datetime    not null default current_timestamp on update current_timestamp comment '登录时间',
    login_ip   varchar(30) comment '登录ip',
    terminal   varchar(30) comment '登录终端',
    remark     varchar(120) comment '登录说明',
    key idx_login_user_id (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志表';

DROP TABLE IF EXISTS `boss_code`;
CREATE TABLE `boss_code`
(
    id          bigint unsigned primary key auto_increment comment '自增编号',
    code_id     varchar(30)  NOT NULL COMMENT '对照ID',
    field       varchar(80)  NOT NULL COMMENT '对照字段',
    field_name  varchar(20)  NOT NULL COMMENT '对照字段名称',
    code        varchar(120) NOT NULL COMMENT '代码',
    code_desc   varchar(100) NOT NULL COMMENT '代码描述',
    enabled     varchar(2)   NOT NULL default '1' COMMENT '启用状态(0:禁用;1:启用)',
    edit_mode   varchar(2)   NOT NULL default '1' COMMENT '编辑模式(0:只读;1:可编辑)',
    sort_no     int(4)                default NULL COMMENT '排序号',
    remark      varchar(200)          default NULL COMMENT '备注',
    create_time datetime     not null default current_timestamp comment '创建时间',
    update_time datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    unique KEY uniq_code_id (code_id),
    unique KEY uniq_field_code (field, code)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='代码对照表';

drop table if exists boss_check_log;
create table boss_check_log
(
    id              bigint unsigned primary key auto_increment comment '自增编号',
    biz_code        varchar(50) not null comment '业务代码',
    biz_id          varchar(50) not null comment '业务编号',
    check_type      tinyint     not null comment '审核类型 0初次提交 1初审 2复审',
    check_result    tinyint     not null comment '审核结果 1通过 2不通过',
    check_user_id   varchar(30) not null comment '审核人编号',
    check_user_name varchar(30) not null comment '审核人姓名',
    remark          varchar(240) comment '备注',
    create_time     datetime    not null default current_timestamp comment '审核时间',
    key idx_biz_id (biz_id) using btree
) engine = InnoDB
  DEFAULT CHARSET = utf8mb4 comment ='业务审核日志表';

drop table if exists boss_kick_out_log;
create table boss_kick_out_log (
    id              bigint unsigned primary key auto_increment     comment '自增编号',
    user_id         varchar(30) not null                           comment '用户编号',
    session_id      varchar(48) not null                           comment 'session编号',
    ip              varchar(24)                                    comment '登录ip地址',
    terminate_ip    varchar(24)                                    comment '终结ip(谁踢出的)',
    create_time     datetime    not null default current_timestamp comment '创建时间',
    key idx_session(session_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='踢出日志表';

drop table if exists boss_login_session;
create table boss_login_session (
    id              bigint unsigned primary key auto_increment comment '自增编号',
    user_id         varchar(30) not null                       comment '用户变化',
    ip              varchar(24)                                comment '登录ip地址',
    session_id      varchar(48) not null                       comment '用户登录的session编号',
    create_time  datetime   not null default current_timestamp comment '创建时间',
    update_time  datetime   not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uniq_user_id(user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='用户登录信息表';

drop table if exists ip_address;
create table ip_address (
    id        bigint unsigned primary key auto_increment comment '自增编号',
    ip        varchar(24) not null                       comment 'ip',
    address   varchar(1024)                              comment 'ip地址详细信息',
    create_time  datetime   not null default current_timestamp comment '创建时间',
    update_time  datetime   not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uniq_ip(ip)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='ip地址信息表';

drop table if exists boss_user_lock_info;
create table boss_user_lock_info (
    id        bigint unsigned primary key auto_increment comment '自增编号',
    user_id   varchar(30) not null                       comment '用户编号',
    remark    varchar(128) not null                      comment '锁定原因',
    lock_type tinyint unsigned not null                  comment '锁定类型 1:系统自动锁定 2:管理员锁定',
    operator_id varchar(30) not null                     comment '锁定操作用户 系统自动:SYSTEM',
    operator_name varchar(30) not null                   comment '锁定操作用户名 系统自动:SYSTEM',
    create_time  datetime   not null default current_timestamp comment '创建时间',
    update_time  datetime   not null default current_timestamp on update current_timestamp comment '更新时间',
    key idx_user_id(user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='锁定信息';



drop table if exists id_gen;
create table id_gen (
    table_name  varchar(64) primary key comment '表名',
    seq_no bigint not null comment '序号',
    incr int not null comment '步进',
    version bigint not null comment '版本',
    create_time  datetime     not null default current_timestamp comment '创建时间',
    update_time  datetime     not null default current_timestamp on update current_timestamp comment '更新时间'
) engine = InnoDB default charset = utf8mb4 comment ='编号生成表';

insert into id_gen(table_name, seq_no, incr, version) values ('user_info', 9012421, 3, 1);

drop table if exists user_info;
create table user_info (
    id                    bigint unsigned primary key comment '用户编号',
    account               varchar(64)       not null comment '账号',
    login_account         varchar(64)       not null comment '登入账号',
    user_name             varchar(64)       not null comment '名称',
    email                 varchar(64)                comment '邮箱',
    mobile_area           varchar(6)            null comment '手机区号',
    mobile_no             varchar(18)           null comment '手机号',
    password              varchar(32)       not null comment '密码',
    change_password_flag  tinyint           not null comment '修改密码标志',
    lock_password         varchar(32)           null comment '锁屏密码',
    secret_password       varchar(32)           null comment '安全密码',
    user_type             tinyint           not null comment '用户类型  1:会员 2:代理',
    proxy_account         varchar(64)       not null comment '代理账号',
    proxy_user_id         bigint            not null comment '代理用户编号',
    proxy_info            varchar(128)          null comment '代理拼接信息',
    balance_mode          varchar(24)           null comment '额度模式',
    status                tinyint           not null comment '用户状态 1:正常 0:锁定',
    last_login            bigint unsigned            comment '最后登录时间',
    create_time  datetime     not null default current_timestamp comment '创建时间',
    update_time  datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uniq_account(account, user_type),
    unique key uniq_login_acc(login_account, user_type)
) engine = InnoDB default charset = utf8mb4 comment ='用户信息表';

drop table if exists user_login_session;
create table user_login_session (
    id              bigint unsigned primary key auto_increment comment '自增编号',
    user_id         bigint unsigned not null                   comment '用户编号',
    ip              varchar(24)                                comment '登录ip地址',
    session_id      varchar(48)     not null                   comment '用户登录的session编号',
    create_time     datetime        not null default current_timestamp comment '创建时间',
    update_time     datetime        not null default current_timestamp on update current_timestamp comment '更新时间',
    unique key uniq_user_id(user_id)
) engine = InnoDB default charset = utf8mb4 comment ='用户session信息管理';

drop table if exists user_login_log;
create table user_login_log (
    id              bigint unsigned primary key auto_increment     comment '自增编号',
    user_id         bigint unsigned not null   comment '用户编号',
    session_id      varchar(48)     not null   comment 'session编号',
    ip              varchar(24)                comment '登录ip地址',
    status          tinyint         not null   comment '状态 1:正常 0:被踢出',
    terminate_sid   varchar(48)         null   comment '终结session编号',
    terminate_ip    varchar(24)                comment '终结ip(谁踢出的)',
    create_time     datetime        not null default current_timestamp comment '创建时间',
    update_time     datetime        not null default current_timestamp on update current_timestamp comment '更新时间',
    key idx_session(session_id),
    key idx_user(user_id)
)  engine = InnoDB default charset = utf8mb4 comment ='用户登录日志';

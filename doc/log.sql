drop table if exists operation_log;
create table operation_log(
    id                bigint unsigned primary key auto_increment comment '自增编号',
    operation_uid     bigint unsigned not null comment '操作用户编号',
    operation_account varchar(64)     not null comment '操作账户',
    biz_type          varchar(64)     not null comment '业务类型',
    biz_child         varchar(64)     not null comment '子类型',
    biz_id            varchar(64)     not null comment '业务编号',
    remark            varchar(500)             comment '操作说明',
    operation_time    datetime        not null default current_timestamp comment '操作时间',
    key idx_biz_type(biz_type, biz_child, biz_id),
    key idx_uid(operation_uid)
) engine = InnoDB default charset = utf8mb4 comment ='操作日志表';

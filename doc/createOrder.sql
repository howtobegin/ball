DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`
(
    id              bigint unsigned primary key auto_increment comment '自增编号',
    order_id        varchar(30)  NOT NULL COMMENT '订单编号',
    user_id         bigint  NOT NULL COMMENT '用户编号',
    user_name       varchar(30)  NULL COMMENT '用户名',
    proxy1          bigint   NULL COMMENT '一级代理',
    proxy2          bigint   NULL COMMENT '二级代理',
    proxy3          bigint   NULL COMMENT '三级代理',

    match_id        varchar(30)  NOT NULL COMMENT '比赛ID',
    company_id      varchar(256) null comment '1: Macauslot,3: Crown',
    handicap_type   varchar(30)  NOT NULL COMMENT '盘口类型,让球，欧赔，大小，波胆，参考：HandicapType',
    bet_option      varchar(30)  NOT NULL COMMENT '投注选项，选队伍的就是队伍ID，大小就是OVER或UNDER',

    bet_odds        decimal(12,2) NOT NULL COMMENT '投注赔率',
    bet_amount      decimal(12,2) NOT NULL COMMENT '投注金额',
    bet_result      varchar(30) default 'UNSETTLED' not null comment '投注结果:UNSETTLED 未结算；LOSE 输；WIN 赢；LOSE_HALF 输一半；WIN_HALF 赢一半；DRAW 平',
    result_amount   decimal(12,2) default 0 COMMENT '投注结果金额（包含本金）',
    win_amount      decimal(12,2) default 0 COMMENT '赢金额（不包含）',
    lose_amount     decimal(12,2) default 0 COMMENT '输金额',

    home_current_score    int   not null COMMENT '主队当前得分（来自schedule表）',
    away_current_score    int   not null COMMENT '客队当前得分（来自schedule表）',
    home_last_score       int    null COMMENT '主队最后得分（来自schedule表）',
    away_last_score       int    null COMMENT '客队最后得分（来自schedule表）',

    odds_data       text        not null comment '赔率JSON数据，根据不同type解析',

    schedule_status tinyint null comment '比赛状态 0: Not started 1: First half 2: Half-time break 3: Second half 4: Extra time 5: Penalty -1: Finished -10: Cancelled -11: TBD -12: Terminated -13: Interrupted -14: Postponed',
    settle_status   tinyint  default 0 COMMENT '结算状态:0 未结算；1 已结算',
    status          varchar(30)  default 'INIT' COMMENT '状态:INIT 初始化；CONFIRM 确认；FINISH 完成；CANCEL/MATCH_CANCEL 取消',

    create_time     datetime     not null default current_timestamp comment '创建时间',
    update_time     datetime     not null default current_timestamp on update current_timestamp comment '更新时间',

    unique KEY uniq_order_id (`order_id`),
    key idx_user_order(user_id,order_id),
    key idx_match(match_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='订单表';

DROP TABLE IF EXISTS `order_history`;
CREATE TABLE `order_history`
(
    id              bigint unsigned primary key auto_increment comment '自增编号',
    order_id        varchar(30)  NOT NULL COMMENT '订单编号',
    bet_result      varchar(15)  default 'UNSETTLED' COMMENT '结果:UNSETTLED 未结算；LOSE 输；WIN 赢；LOSE_HALF 输一半；WIN_HALF 赢一半；DRAW 平',
    win_amount      decimal(3,2) NOT NULL COMMENT '投注结果金额，包含本金，输的情况下就是负',
    status          varchar(30)  NOT NULL COMMENT '状态',
    key idx_order(order_id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='订单历史表';
drop table if exists message_template;
create table message_template (
  id              int unsigned     primary key auto_increment comment '主键编号',
  subject         varchar(256)         null comment '标题',
  content         text             not null comment '内容',
  template_code   varchar(64)      not null comment '代码',
  media           tinyint unsigned not null comment '媒体类型 1:手机 2:邮箱',
  lang            varchar(100)     not null comment '语言 中文:zh-CN 英文:en-US',
  create_time     datetime         not null default current_timestamp comment '创建时间',
  update_time     datetime         not null default current_timestamp on update current_timestamp comment '更新时间',
  unique key uniq_code_info(template_code,media,lang)
) engine=InnoDB comment='短信邮件模板';


insert into message_template(subject, content,template_code, media, lang) values ('验证码通知', '您的验证码为：${code}，10分钟内有效，请勿泄露。', 'VERIFY_CODE', 1, 'zh-CN');
insert into message_template(subject, content,template_code, media, lang) values ('验证码通知', '<p>您的验证码为：${code}，10分钟内有效，请勿泄露。</p>', 'VERIFY_CODE', 2, 'zh-CN');
insert into message_template(subject, content,template_code, media, lang) values ('Verification code notification', 'Your verification code is ${code}, valid for 10 minutes. Please do not disclose it.', 'VERIFY_CODE', 1, 'en-US');
insert into message_template(subject, content,template_code, media, lang) values ('Verification code notification', '<p>Your verification code is ${code}, valid for 10 minutes. Please do not disclose it. </p>', 'VERIFY_CODE', 2, 'en-US');

insert into boss_user_info (user_id, user_name, account, password, dept_id, mobile_phone, user_type)
values ('1', 'admin', 'admin', '5e795203aa8dd370080663d79595a95e', 'SYSTEM', '18888888888', 3);

insert into boss_login_session(user_id, ip, session_id) values('1', '127.0.0.1', 'SYSTEM');

-- 系统管理模块
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('1', '系统管理', 'root', 'el-icon-s-tools', null, '0', 30, '1', '1');

insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('2', '人员管理', '1', null, '/system/user', '1', 3, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('2001', '新增用户', '2', null, '/boss/system/user/add', '2', 3, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('2002', '授权用户', '2', null, '/boss/system/user/authorizeRole', '2', 3, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('2003', '修改用户', '2', null, '/boss/system/user/update', '2', 3, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('2004', '删除用户', '2', null, '/boss/system/user/delete', '2', 3, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('2005', '用户锁定解锁', '2', null, '/boss/system/user/changeLock', '2', 3, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('2006', '查询用户', '2', null, '/boss/system/user/queryById', '2', 3, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('2007', '分页查询用户', '2', null, '/boss/system/user/queryPaging', '2', 3, '1', '1');

insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('3', '角色管理', '1', null, '/system/role', '1', 6, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('3001', '新增角色', '3', null, '/boss/system/role/add', '2', 6, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('3002', '修改角色', '3', null, '/boss/system/role/update', '2', 6, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('3003', '删除角色', '3', null, '/boss/system/role/delete', '2', 6, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('3004', '角色锁定解锁', '3', null, '/boss/system/role/changeLock', '2', 6, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('3005', '查询所有角色', '3', null, '/boss/system/role/queryAll', '2', 6, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('3006', '分页查询角色', '3', null, '/boss/system/role/queryPaging', '2', 6, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('3007', '查询角色', '3', null, '/boss/system/role/queryById', '2', 6, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('3008', '授权角色', '3', null, '/boss/system/role/authorizeMenu', '2', 6, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('3009', '查询角色明细', '3', null, '/boss/system/role/queryDetail', '2', 6, '1', '1');



insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('4', '菜单管理', '1', null, '/system/menu', '1', 9, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('4001', '新增菜单', '4', null, '/boss/system/menu/add', '2', 9, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('4002', '修改菜单', '4', null, '/boss/system/menu/update', '2', 9, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('4003', '删除菜单', '4', null, '/boss/system/menu/delete', '2', 9, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('4004', '查询所有菜单', '4', null, '/boss/system/menu/queryAll', '2', 9, '1', '1');

insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('5', '字典管理', '1', null, '/system/dictionary', '1', 15, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('5002', '新增字典', '5', null, '/boss/system/dictionary/add', '2', 15, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('5003', '修改字典', '5', null, '/boss/system/dictionary/update', '2', 15, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('5004', '删除字典', '5', null, '/boss/system/dictionary/delete', '2', 15, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('5005', '查询字典', '5', null, '/boss/system/dictionary/getByField', '2', 15, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('5006', '查询所有字典', '5', null, '/boss/system/dictionary/getAll', '2', 15, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('5007', '查询单个字典', '5', null, '/boss/system/dictionary/getById', '2', 15, '1', '1');
insert into boss_menu(menu_id, menu_name, parent_id, icon_cls, request, menu_level, sort_no, menu_type, menu_source)
values ('5008', '分页查询字典', '5', null, '/boss/system/dictionary/queryPaging', '2', 15, '1', '1');

-- 插入角色信息和角色授权信息
insert into boss_role(role_id, role_name, role_type, remark, locked) values ('1', '系统管理员', '3', '管理所有模块', 0);

insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '1', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '2', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '2001', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '2002', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '2003', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '2004', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '2005', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '2006', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '2007', '2');


insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '3', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '3001', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '3002', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '3003', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '3004', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '3005', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '3006', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '3007', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '3008', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '3009', '2');

insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '4', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '4001', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '4002', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '4003', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '4004', '2');


insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '5', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '5001', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '5002', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '5003', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '5004', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '5005', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '5006', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '5007', '2');
insert into boss_role_authorize(role_id, menu_id, authorize_level) values ('1', '5008', '2');

-- 用户角色授权
insert into boss_user_authorize(user_id, role_id) values('1', '1');


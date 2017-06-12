insert into t_user (id,username, password, full_name) values (1,'super', '$2a$10$zialNqCgFI3QPFNTiiLGlup7q5BzXQVNXtgdPWGgDOvduxfRcfiQO', '上帝');
insert into t_user (id,username, password, full_name) values (2,'user',  '$2a$10$zialNqCgFI3QPFNTiiLGlup7q5BzXQVNXtgdPWGgDOvduxfRcfiQO', '平民');
insert into t_user (id,username, password, full_name) values (3,'admin', '$2a$10$zialNqCgFI3QPFNTiiLGlup7q5BzXQVNXtgdPWGgDOvduxfRcfiQO', '国王');

insert into t_role(id,`name`) values(1,'ROLE_SUPER_ADMIN');
insert into t_role(id,`name`) values(2,'ROLE_NORMAL_USER');
insert into t_role(id,`name`) values(3,'ROLE_ADMIN');

insert into t_user_role(user_id,role_id) values(1,1);
insert into t_user_role(user_id,role_id) values(2,2);
insert into t_user_role(user_id,role_id) values(3,3);

insert into t_permission(id, pid, `name`, url, `description`) values(1, null, 'PRIVILEGE_DASHBOARD', '/dashboard',   '菜单-控制台页面');
insert into t_permission(id, pid, `name`, url, `description`) values(2, null, 'PRIVILEGE_USER',      '/user',        '菜单-用户管理页面');
insert into t_permission(id, pid, `name`, url, `description`) values(3, null, 'PRIVILEGE_PROFILE',   '/profile',     '菜单-用户profile页面');
insert into t_permission(id, pid, `name`, url, `description`) values(4, 2,    'PRIVILEGE_USER_A',    '/user/add',    '');
insert into t_permission(id, pid, `name`, url, `description`) values(5, 2,    'PRIVILEGE_USER_D',    '/user/delete', '');
insert into t_permission(id, pid, `name`, url, `description`) values(6, 2,    'PRIVILEGE_USER_M',    '/user/modify', '');
insert into t_permission(id, pid, `name`, url, `description`) values(7, 2,    'PRIVILEGE_USER_V',    '/user/view',   '');

insert into t_role_permission(role_id, permission_id) values(1, 1);
insert into t_role_permission(role_id, permission_id) values(1, 2);
insert into t_role_permission(role_id, permission_id) values(1, 3);
insert into t_role_permission(role_id, permission_id) values(1, 4);
insert into t_role_permission(role_id, permission_id) values(1, 5);
insert into t_role_permission(role_id, permission_id) values(1, 6);
insert into t_role_permission(role_id, permission_id) values(1, 7);

insert into t_role_permission(role_id, permission_id) values(2, 1);
insert into t_role_permission(role_id, permission_id) values(2, 3);

insert into t_role_permission(role_id, permission_id) values(3, 1);
insert into t_role_permission(role_id, permission_id) values(3, 2);
insert into t_role_permission(role_id, permission_id) values(3, 4);
insert into t_role_permission(role_id, permission_id) values(3, 7);

-- INSERT INTO t_permission(id, `name`, `description`, url, pid) VALUES (1, 'PRIVILEGE_HOME', 'home', '/dashboard', null);
-- INSERT INTO t_permission(id, `name`, `description`, url, pid) VALUES (2, 'PRIVILEGE_USER', 'user', '/user', null);
-- INSERT INTO t_permission(id, `name`, `description`, url, pid) VALUES (3, 'PRIVILEGE_ADMIN', 'admin', '/admin', null);

-- INSERT INTO t_role_permission(role_id, permission_id) VALUES (1, 1);
-- INSERT INTO t_role_permission(role_id, permission_id) VALUES (1, 2);
-- INSERT INTO t_role_permission(role_id, permission_id) VALUES (1, 3);
--
-- INSERT INTO t_role_permission(role_id, permission_id) VALUES (2, 1);
-- INSERT INTO t_role_permission(role_id, permission_id) VALUES (2, 2);
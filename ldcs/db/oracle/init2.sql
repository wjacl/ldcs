INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('liver.entryStatus','入职状态','liver.entryStatus',6,'0','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('les1','未入职','N',1,'liver.entryStatus','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('les2','已入职','Y',2,'liver.entryStatus','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('les3','已离职','L',3,'liver.entryStatus','s',1);

INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('liver.signStatus','签约状态','liver.signStatus',7,'0','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('lss1','未签约','N',1,'liver.signStatus','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('lss2','已签约','Y',2,'liver.signStatus','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('lss3','已解约','L',3,'liver.signStatus','s',1);

INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liver','主播个人信息管理','/liver/manage','sys','1',1,9111);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('sys-role-query','查询','/role/query','sys-role','0',1,9112);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('sys-role-add','新增','/role/add','sys-role','0',1,9113);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('sys-role-update','修改','/role/update','sys-role','0',1,9114);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('sys-role-delete','新增','/role/delete','sys-role','0',1,9115);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('sys-role-get','获取','/role/get','sys-role','0',1,9116);
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','sys-role');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','sys-role-query');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','sys-role-add');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','sys-role-update');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','sys-role-delete');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','sys-role-get');

INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('sys-org','机构管理','/org/manage','sys','1',1,9130);
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','sys-org');

INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('sys-param','参数设置','/param/manage','sys','1',1,9850);
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','sys-param');
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('sys-param-query','查询','/param/query','sys','0',1,9851);
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','sys-param-query');
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('sys-param-save','修改','/param/save','sys','0',1,9852);
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','sys-param-save');

INSERT INTO t_sys_param(ID,name,VALUE,remark)
VALUES('login.try.max.times','登录最大尝试次数','6','登录最大尝试次数，当超过时，将被锁定。');

INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('person','个人信息','/person/info',NULL,'1',1,8990);
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','person');
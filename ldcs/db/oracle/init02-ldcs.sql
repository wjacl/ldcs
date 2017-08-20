

INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('user.type.broker','经纪人','B',4,'user.type','s',1);

INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('liver.entryStatus','入职状态','liver.entryStatus',6,'0','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('les1','未入职','N',1,'liver.entryStatus','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('les2','在职','Y',2,'liver.entryStatus','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('les3','离职','L',3,'liver.entryStatus','s',1);

INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('liver.signStatus','签约状态','liver.signStatus',7,'0','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('lss1','未签约','N',1,'liver.signStatus','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('lss2','已签约','Y',2,'liver.signStatus','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('lss3','已解约','L',3,'liver.signStatus','s',1);

INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('liver.liveStatus','直播状态','liver.liveStatus',8,'0','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('lls1','开播','Y',1,'liver.liveStatus','s',1);
INSERT INTO t_sys_dict(Id,name,value,ordno,pid,type,valid) VALUES('lls2','休假','N',2,'liver.liveStatus','s',1);

INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs','信息录入',NULL,NULL,'2',1,1100);

INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs');

INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liver','主播信息管理','/liver/manage','ldcs','1',1,1171);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liver-query','查询','/liver/query','ldcs-liver','0',1,1172);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liver-add','新增','/liver/add','ldcs-liver','0',1,1173);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liver-update','修改','/liver/update','ldcs-liver','0',1,1174);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liver-delete','删除','/liver/delete','ldcs-liver','0',1,1175);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liver-get','获取','/liver/get','ldcs-liver','0',1,1176);
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liver');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liver-query');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liver-add');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liver-update');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liver-delete');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liver-get');


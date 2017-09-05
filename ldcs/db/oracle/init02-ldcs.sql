

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

INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveData','主播数据管理','/liveData/manage','ldcs','1',1,1111);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveData-query','查询','/liveData/query','ldcs-liveData','0',1,1112);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveData-add','新增','/liveData/add','ldcs-liveData','0',1,1113);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveData-update','修改','/liveData/update','ldcs-liveData','0',1,1114);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveData-delete','删除','/liveData/delete','ldcs-liveData','0',1,1115);
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveData');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveData-query');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveData-add');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveData-update');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveData-delete');

INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveData-badd','主播数据录入','/liveData/batchadd','ldcs','1',1,1101);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveData-badd-query','查询','/liveData/query','ldcs-liveData-badd','0',1,1102);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveData-badd-add','新增','/liveData/add','ldcs-liveData-badd','0',1,1103);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveData-badd-update','修改','/liveData/update','ldcs-liveData-badd','0',1,1104);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveData-badd-delete','删除','/liveData/delete','ldcs-liveData-badd','0',1,1105);
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveData-badd');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveData-badd-query');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveData-badd-add');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveData-badd-update');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveData-badd-delete');

INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveGoal','主播月度目标管理','/liveGoal/manage','ldcs','1',1,1121);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveGoal-query','查询','/liveGoal/query','ldcs-liveGoal','0',1,1122);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveGoal-add','新增','/liveGoal/add','ldcs-liveGoal','0',1,1123);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveGoal-update','修改','/liveGoal/update','ldcs-liveGoal','0',1,1124);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveGoal-delete','删除','/liveGoal/delete','ldcs-liveGoal','0',1,1125);
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveGoal');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveGoal-query');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveGoal-add');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveGoal-update');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveGoal-delete');

INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-perfStandard','绩效评分标准管理','/perfStandard/manage','ldcs','1',1,1201);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-perfStandard-query','查询','/perfStandard/query','ldcs-perfStandard','0',1,1202);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-perfStandard-add','新增','/perfStandard/add','ldcs-perfStandard','0',1,1203);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-perfStandard-update','修改','/perfStandard/update','ldcs-perfStandard','0',1,1204);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-perfStandard-delete','删除','/perfStandard/delete','ldcs-perfStandard','0',1,1205);
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-perfStandard');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-perfStandard-query');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-perfStandard-add');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-perfStandard-update');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-perfStandard-delete');

INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveData-tj','主播数据统计','/liveData/tj','ldcs','1',1,1117);
INSERT INTO t_sys_privilege(pr_id,pr_name,Path,p_pr_id,pr_type,valid,Order_No)
VALUES('ldcs-liveData-tj-query','查询','/liveData/tjQuery','ldcs-liveData-tj','0',1,1118);
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveData-tj');
INSERT INTO t_sys_role_priv(r_id,pr_id) VALUES('admin','ldcs-liveData-tj-query');

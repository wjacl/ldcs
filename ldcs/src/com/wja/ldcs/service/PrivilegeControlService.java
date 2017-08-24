package com.wja.ldcs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wja.base.system.entity.Org;
import com.wja.base.system.entity.User;
import com.wja.base.system.service.OrgService;
import com.wja.base.system.service.UserService;
import com.wja.base.web.RequestThreadLocal;

@Service
public class PrivilegeControlService
{
    
    @Autowired
    private OrgService orgService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 向查询条件中添加数据权限控制
     * 
     * @param params
     * @see [类、类#方法、类#成员]
     */
    public void addDataAuthori(Map<String, Object> params)
    {
        User user = RequestThreadLocal.currUser.get();
        if (user != null)
        {
            List<Org> orgs = new ArrayList<>();
            orgs.add(user.getOrg());
            
            if (User.TYPE_LEADER.equals(user.getType()))
            {
                // 领导用户，可以查看所在部门及子部门
                this.orgService.getAllChildOrg(user.getOrg(), orgs, null, null);
                List<String> ids = new ArrayList<>();
                for (Org org : orgs)
                {
                    ids.add(org.getId());
                }
                Map<String, Object> p = new HashMap<String, Object>();
                p.put("org.id_in_String", ids);
                List<User> us = this.userService.query(params);
                
                ids.clear();
                for (User u : us)
                {
                    ids.add(u.getId());
                }
                params.put("brokerId_in_String", ids);
            }
            else if (User.TYPE_NORMAL_STAFF.equals(user.getType()))
            {
                // 普通员工用户，只可查看所在部门人员，不可查看子部门
                params.put("broker.org.id_eq_String", user.getOrg().getId());
            }
            else
            {
                // 经纪人，只可查看所负责的主播
                params.put("broker.id_eq_String", user.getId());
            }
        }
    }
    
    public void liveDataaddDataAuthori(Map<String, Object> params)
    {
        User user = RequestThreadLocal.currUser.get();
        if (user != null)
        {
            List<Org> orgs = new ArrayList<>();
            orgs.add(user.getOrg());
            
            if (User.TYPE_LEADER.equals(user.getType()))
            {
                // 领导用户，可以查看所在部门及子部门
                this.orgService.getAllChildOrg(user.getOrg(), orgs, null, null);
                List<String> orgIds = new ArrayList<>();
                for (Org org : orgs)
                {
                    orgIds.add(org.getId());
                }
                params.put("broker.org.id_in_String", orgIds);
            }
            else if (User.TYPE_NORMAL_STAFF.equals(user.getType()))
            {
                // 普通员工用户，只可查看所在部门人员，不可查看子部门
                params.put("broker.org.id_eq_String", user.getOrg().getId());
            }
            else
            {
                // 经纪人，只可查看所负责的主播
                params.put("broker.id_eq_String", user.getId());
            }
        }
    }
}

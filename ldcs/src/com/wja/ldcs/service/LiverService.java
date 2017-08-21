package com.wja.ldcs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wja.base.common.CommSpecification;
import com.wja.base.common.service.CommService;
import com.wja.base.system.entity.Org;
import com.wja.base.system.entity.User;
import com.wja.base.system.service.OrgService;
import com.wja.base.system.service.UserService;
import com.wja.base.util.CollectionUtil;
import com.wja.base.util.Page;
import com.wja.base.util.Sort;
import com.wja.base.web.RequestThreadLocal;
import com.wja.ldcs.dao.LiverDao;
import com.wja.ldcs.entity.Liver;

@Service
public class LiverService extends CommService<Liver> {

    @Autowired
    private LiverDao liverDao;

    @Autowired
    private PrivilegeControlService privilegeControlService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private UserService userService;

    public Page<Liver> pageQuery(Map<String, Object> params, Page<Liver> page) {

	privilegeControlService.addDataAuthori(params);
	return page.setPageData(this.liverDao.findAll(new CommSpecification<Liver>(params), page.getPageRequest()));
    }

    public List<Liver> query(Map<String, Object> params, Sort sort) {
	privilegeControlService.addDataAuthori(params);
	return this.liverDao.findAll(new CommSpecification<Liver>(params), sort.getSpringSort());
    }

    public List<Object> treeQuery(Map<String, Object> params, Sort sort) {
	privilegeControlService.addDataAuthori(params);
	List<Liver> livers = this.liverDao.findAll(new CommSpecification<Liver>(params), sort.getSpringSort());

	List<Object> treeList = new ArrayList<>();
	if (CollectionUtil.isNotEmpty(livers)) {
	    User user = RequestThreadLocal.currUser.get();
	    if (user != null) {
		List<Org> orgs = new ArrayList<>();
		orgs.add(user.getOrg());

		if (User.TYPE_LEADER.equals(user.getType())) {
		    // 领导用户，可以查看所在部门及子部门
		    this.orgService.getAllChildOrg(user.getOrg(), orgs, null, null);
		} else if (User.TYPE_NORMAL_STAFF.equals(user.getType())) {
		    // 普通员工用户，只可查看所在部门人员，不可查看子部门
		} else {
		    orgs.remove(0);
		}

		if (orgs.size() > 0) { // 非经纪人，需查询部门下的人员
		    // 查询人员
		    List<String> orgIds = new ArrayList<>();
		    for (Org org : orgs) {
			orgIds.add(org.getId());
		    }

		    Map<String, Object> pars = new HashMap<String, Object>();
		    pars.put("org.id_in_String", orgIds);

		    List<User> users = this.userService.query(pars);

		    // 将组织、人员组合成一个List
		    treeList.addAll(orgs);

		    Map<String, Object> ma;
		    for (User u : users) {
			ma = new HashMap<>();
			ma.put("id", u.getId());
			ma.put("name", u.getName());
			ma.put("pid", u.getOrg().getId());
			ma.put("type", "user");
			ma.put("userType", u.getType());
			ma.put("iconCls", "icon-man");
			treeList.add(ma);
		    }
		} else {
		    Map<String, Object> ma = new HashMap<>();
		    ma.put("id", user.getId());
		    ma.put("name", user.getName());
		    ma.put("pid", user.getOrg().getId());
		    ma.put("type", "user");
		    ma.put("userType", user.getType());
		    ma.put("iconCls", "icon-man");
		    treeList.add(ma);
		}

		// 将主播加到treeList中
		Map<String, Object> ma;
		for (Liver u : livers) {
		    ma = new HashMap<>();
		    ma.put("id", u.getId());
		    ma.put("name", u.getName());
		    ma.put("pid", u.getBroker().getId());
		    ma.put("type", "liver");
		    ma.put("iconCls", "icon-man");
		    ma.put("origin", u);
		    treeList.add(ma);
		}
	    }
	}

	return treeList;
    }

}

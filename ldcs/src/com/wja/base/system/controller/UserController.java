package com.wja.base.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wja.base.common.OpResult;
import com.wja.base.system.dao.RoleDao;
import com.wja.base.system.entity.Org;
import com.wja.base.system.entity.Role;
import com.wja.base.system.entity.User;
import com.wja.base.system.service.OrgService;
import com.wja.base.system.service.UserService;
import com.wja.base.util.BeanUtil;
import com.wja.base.util.MD5;
import com.wja.base.util.Page;
import com.wja.base.web.AppContext;
import com.wja.base.web.RequestThreadLocal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private OrgService orgService;

    @RequestMapping("mana")
    public String manage() {
	return "system/user";
    }

    @RequestMapping("memberTree")
    @ResponseBody
    public List<?> memberTree() {
	User user = RequestThreadLocal.currUser.get();
	List<Object> treeList = new ArrayList<>();
	if (user != null) {
	    List<Org> orgs = new ArrayList<>();
	    orgs.add(user.getOrg());

	    if (User.TYPE_LEADER.equals(user.getType())) {
		// 领导用户，可以查看所在部门及子部门
		this.orgService.getAllChildOrg(user.getOrg(), orgs, null, null);
	    } else {
		// 普通员工用户，只可查看所在部门人员，不可查看子部门
	    }

	    // 查询人员
	    List<String> orgIds = new ArrayList<>();
	    for (Org org : orgs) {
		orgIds.add(org.getId());
	    }

	    Map<String, Object> params = new HashMap<String, Object>();
	    params.put("org.id_in_String", orgIds);

	    List<User> users = this.userService.query(params);

	    // 将组织、人员组合成一个List
	    treeList.addAll(orgs);
	    for (User u : users) {
		Map<String, Object> ma = new HashMap<>();
		ma.put("id", u.getId());
		ma.put("name", u.getName());
		ma.put("pid", u.getOrg().getId());
		ma.put("type", "user");
		ma.put("userType", u.getType());
		ma.put("iconCls", "icon-man");
		treeList.add(ma);
	    }
	}
	return treeList;
    }

    @RequestMapping("oldPwdCheck")
    @ResponseBody
    public boolean oldPwdCheck(String pwd) {
	return RequestThreadLocal.currUser.get().getPassword().equals(MD5.encode(pwd));
    }

    @RequestMapping("pwdupdate")
    @ResponseBody
    public Object updatePwd(String oldpassword, String password) {
	User user = RequestThreadLocal.currUser.get();
	if (!user.getPassword().equals(MD5.encode(oldpassword))) {
	    return OpResult.updateError(AppContext.getMessage("user.oldpwd.error"), null);
	}

	if (StringUtils.isBlank(password)) {
	    return OpResult.updateError(AppContext.getMessage("user.pwd.notnull"), null);
	}

	this.userService.updatePwd(user.getId(), password);
	return OpResult.updateOk();
    }

    @RequestMapping(value = "regist", method = RequestMethod.GET)
    public String toRegist() {
	return "system/regist";
    }

    @RequestMapping(value = "regist", method = RequestMethod.POST)
    @ResponseBody
    public Object regist(User user) {
	if (this.userService.getUserByUsername(user.getUsername()) != null) {
	    return OpResult.error("unameExits", null);
	}

	user.setStatus(User.STATUS_LOCK);
	this.userService.addUser(user);

	return OpResult.ok();
    }

    @RequestMapping("unameCheck")
    @ResponseBody
    public boolean checkUsername(String username) {
	User user = this.userService.getUserByUsername(username);
	return user == null;
    }

    @RequestMapping("add")
    @ResponseBody
    public Object addUser(User user, String[] roleIds) {

	if (roleIds != null && roleIds.length > 1) {
	    Set<Role> roles = new HashSet<>();
	    roles.addAll(this.roleDao.findAll(roleIds));
	    user.setRoles(roles);
	}
	user.setOrg(this.orgService.get(user.getOrg().getId()));
	this.userService.addUser(user);

	return OpResult.addOk();

    }

    @RequestMapping("remove")
    @ResponseBody
    public Object removeUser(String[] id) {
	this.userService.deleteUser(id);
	return OpResult.deleteOk();
    }

    @RequestMapping("update")
    @ResponseBody
    public Object updateUser(User user, String[] roleIds) {
	if (roleIds != null && roleIds.length > 0) {
	    Set<Role> roles = new HashSet<>();
	    roles.addAll(this.roleDao.findAll(roleIds));
	    user.setRoles(roles);
	}
	user.setOrg(this.orgService.get(user.getOrg().getId()));
	this.userService.updateUser(user);
	return OpResult.updateOk();
    }

    @RequestMapping("query")
    @ResponseBody
    public Object query(@RequestParam Map<String, Object> params, Page<User> page) {
	this.userService.query(params, page);
	BeanUtil.setCollFieldValues(page.getRows());
	return page;
    }

    @RequestMapping("find")
    @ResponseBody
    public List<User> find(@RequestParam Map<String, Object> params) {
	return this.userService.query(params);
    }

    @RequestMapping("privs")
    @ResponseBody
    public Object getCurrUserPrivTree() {
	User user = RequestThreadLocal.currUser.get();
	if (user == null) {
	    return null;
	} else {
	    return this.userService.getUserPrivileges(user.getId());
	}
    }

    @RequestMapping("roles")
    @ResponseBody
    public Object getCurrUserRoles() {
	User user = RequestThreadLocal.currUser.get();
	user = this.userService.getUser(user.getId());
	Set<Role> roles = this.roleDao.findByCreateUser(user.getId());
	roles.addAll(user.getRoles());
	return roles;
    }
}

package com.wja.ldcs.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wja.base.common.OpResult;
import com.wja.base.system.service.UserService;
import com.wja.base.util.Page;
import com.wja.ldcs.entity.Liver;
import com.wja.ldcs.service.LiverService;

@Controller
@RequestMapping("/liver")
public class LiverController {
    @Autowired
    private LiverService liverService;

    @Autowired
    private UserService userService;

    @RequestMapping("manage")
    public String manage() {
	return "ldcs/liver";
    }

    @RequestMapping({ "add", "update" })
    @ResponseBody
    public OpResult save(Liver c) {
	boolean add = StringUtils.isBlank(c.getId());
	c.setBroker(this.userService.getUser(c.getBroker().getId()));
	if (add) {
	    this.liverService.add(c);
	    return OpResult.addOk(c);
	} else {
	    this.liverService.update(c);
	    return OpResult.updateOk(c);
	}
    }

    @RequestMapping("query")
    @ResponseBody
    public Page<Liver> pageQuery(@RequestParam Map<String, Object> params, Page<Liver> page) {
	return this.liverService.pageQuery(params, page);
    }

    @RequestMapping("delete")
    @ResponseBody
    public OpResult delete(String[] id) {
	this.liverService.delete(Liver.class, id);
	return OpResult.deleteOk();
    }
}

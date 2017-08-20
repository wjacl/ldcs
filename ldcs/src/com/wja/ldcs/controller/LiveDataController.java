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
import com.wja.ldcs.entity.LiveData;
import com.wja.ldcs.entity.Liver;
import com.wja.ldcs.service.LiveDataService;
import com.wja.ldcs.service.LiverService;

@Controller
@RequestMapping("/liveData")
public class LiveDataController {
    @Autowired
    private LiverService liverService;

    @Autowired
    private UserService userService;

    @Autowired
    private LiveDataService liveDataService;

    @RequestMapping("manage")
    public String manage() {
	return "ldcs/live_data";
    }

    @RequestMapping({ "add", "update" })
    @ResponseBody
    public OpResult save(LiveData c) {
	boolean add = StringUtils.isBlank(c.getId());
	this.replenishLiveData(c);
	if (add) {
	    this.liveDataService.add(c);
	    return OpResult.addOk(c);
	} else {
	    this.liveDataService.update(c);
	    return OpResult.updateOk(c);
	}
    }

    private void replenishLiveData(LiveData c) {
	c.setLiver(this.liverService.get(Liver.class, c.getLiver().getId()));
	if (c.getBroker() == null || StringUtils.isBlank(c.getBroker().getId())) {
	    c.setBroker(c.getLiver().getBroker());
	}
	if (StringUtils.isBlank(c.getPlatform())) {
	    c.setPlatform(c.getLiver().getPlatform());
	}
	if (StringUtils.isBlank(c.getRoomNo())) {
	    c.setRoomNo(c.getLiver().getRoomNo());
	}
	if (StringUtils.isBlank(c.getLiveName())) {
	    c.setLiveName(c.getLiver().getLiveName());
	}
    }

    @RequestMapping("query")
    @ResponseBody
    public Page<LiveData> pageQuery(@RequestParam Map<String, Object> params, Page<LiveData> page) {
	return this.liveDataService.pageQuery(params, page);
    }

    @RequestMapping("delete")
    @ResponseBody
    public OpResult delete(String[] id) {
	this.liveDataService.delete(LiveData.class, id);
	return OpResult.deleteOk();
    }
}

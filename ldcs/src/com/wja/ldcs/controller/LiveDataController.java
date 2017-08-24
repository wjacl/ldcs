package com.wja.ldcs.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wja.base.common.OpResult;
import com.wja.base.util.BeanUtil;
import com.wja.base.util.Page;
import com.wja.ldcs.entity.LiveData;
import com.wja.ldcs.service.LiveDataService;

@Controller
@RequestMapping("/liveData")
public class LiveDataController
{
    @Autowired
    private LiveDataService liveDataService;
    
    @RequestMapping("manage")
    public String manage()
    {
        return "ldcs/live_data";
    }
    
    @RequestMapping({"add", "update"})
    @ResponseBody
    public OpResult save(LiveData c)
    {
        boolean add = StringUtils.isBlank(c.getId());
        if (add)
        {
            this.liveDataService.add(c);
            return OpResult.addOk(c);
        }
        else
        {
            this.liveDataService.update(c);
            return OpResult.updateOk(c);
        }
    }
    
    @RequestMapping("query")
    @ResponseBody
    public Page<LiveData> pageQuery(@RequestParam Map<String, Object> params, Page<LiveData> page)
    {
        Page<LiveData> p = this.liveDataService.pageQuery(params, page);
        BeanUtil.setCollFieldValues(p.getRows());
        return p;
    }
    
    @RequestMapping("delete")
    @ResponseBody
    public OpResult delete(String[] id)
    {
        this.liveDataService.delete(LiveData.class, id);
        return OpResult.deleteOk();
    }
}

package com.wja.ldcs.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wja.base.common.OpResult;
import com.wja.base.util.Page;
import com.wja.base.util.Sort;
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
    
    @RequestMapping("batchadd")
    public String batchAdd()
    {
        return "ldcs/live_data_in";
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
        return this.liveDataService.pageQuery(params, page);
    }
    
    @RequestMapping("list")
    @ResponseBody
    public List<LiveData> listQuery(Map<String, Object> params, Sort sort)
    {
        return this.liveDataService.listQuery(params, sort);
    }
    
    @RequestMapping("delete")
    @ResponseBody
    public OpResult delete(String[] id)
    {
        this.liveDataService.delete(LiveData.class, id);
        return OpResult.deleteOk();
    }
}

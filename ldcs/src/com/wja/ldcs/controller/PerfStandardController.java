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
import com.wja.ldcs.entity.PerfStandard;
import com.wja.ldcs.service.PerfStandardService;

@Controller
@RequestMapping("/perfStandard")
public class PerfStandardController
{
    @Autowired
    private PerfStandardService perfStandardService;
    
    @RequestMapping("manage")
    public String manage()
    {
        return "ldcs/perf_standard";
    }
    
    @RequestMapping({"add", "update"})
    @ResponseBody
    public OpResult save(PerfStandard c)
    {
        PerfStandard dbld = this.perfStandardService.findByMonth(c.getMonth());
        boolean add = StringUtils.isBlank(c.getId());
        if (add)
        {
            if (dbld != null)
            {
                return OpResult.addError("该月份数据已存在，不能重复录入，请修改！", c);
            }
            this.perfStandardService.add(c);
            return OpResult.addOk(c);
        }
        else
        {
            if (dbld != null && !dbld.getId().equals(c.getId()))
            {
                return OpResult.updateError("该月份数据已存在，不能重复录入，请修改！", c);
            }
            this.perfStandardService.update(c);
            return OpResult.updateOk(c);
        }
    }
    
    @RequestMapping("query")
    @ResponseBody
    public Page<PerfStandard> pageQuery(@RequestParam Map<String, Object> params, Page<PerfStandard> page)
    {
        return this.perfStandardService.pageQuery(params, page);
    }
    
    @RequestMapping("list")
    @ResponseBody
    public List<PerfStandard> listQuery(@RequestParam Map<String, Object> params, Sort sort)
    {
        return this.perfStandardService.listQuery(params, sort);
    }
    
    @RequestMapping("delete")
    @ResponseBody
    public OpResult delete(String[] id)
    {
        this.perfStandardService.delete(PerfStandard.class, id);
        return OpResult.deleteOk();
    }
    
    @RequestMapping("getByMonth")
    @ResponseBody
    public PerfStandard delete(Integer month)
    {
        return this.perfStandardService.findByMonth(month);
    }
}

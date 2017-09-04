package com.wja.ldcs.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.wja.base.common.OpResult;
import com.wja.base.util.Page;
import com.wja.base.util.PoiExcelUtil;
import com.wja.base.util.PoiExcelUtil.CellParse;
import com.wja.base.util.Sort;
import com.wja.base.web.view.PoiExcelView;
import com.wja.ldcs.entity.LiveGoal;
import com.wja.ldcs.entity.Liver;
import com.wja.ldcs.service.LiveGoalService;
import com.wja.ldcs.service.LiverService;

@Controller
@RequestMapping("/liveGoal")
public class LiveGoalController
{
    @Autowired
    private LiveGoalService liveGoalService;
    
    @Autowired
    private LiverService liverService;
    
    @RequestMapping("manage")
    public String manage()
    {
        return "ldcs/live_goal";
    }
    
    @RequestMapping("batchadd")
    public String batchAdd()
    {
        return "ldcs/live_data_in";
    }
    
    @RequestMapping({"add", "update"})
    @ResponseBody
    public OpResult save(LiveGoal c)
    {
        LiveGoal dbld = this.liveGoalService.findByLiverIdAndMonth(c.getLiverId(), c.getMonth());
        boolean add = StringUtils.isBlank(c.getId());
        if (add)
        {
            if (dbld != null)
            {
                return OpResult.addError("该月份数据已存在，不能重复录入，请修改！", c);
            }
            this.liveGoalService.add(c);
            return OpResult.addOk(c);
        }
        else
        {
            if (dbld != null && !dbld.getId().equals(c.getId()))
            {
                return OpResult.updateError("该月份数据已存在，不能重复录入，请修改！", c);
            }
            this.liveGoalService.update(c);
            return OpResult.updateOk(c);
        }
    }
    
    @RequestMapping("query")
    @ResponseBody
    public Page<LiveGoal> pageQuery(@RequestParam Map<String, Object> params, Page<LiveGoal> page)
    {
        return this.liveGoalService.pageQuery(params, page);
    }
    
    @RequestMapping("import")
    @ResponseBody
    public List<LiveGoal> doImport(@RequestPart("myfile") MultipartFile myfile)
    {
        
        // 读取excel数据
        Map<String, Object> model = new HashMap<>();
        model.put("firstRowIndex", 1);
        model.put("firstColumnIndex", 1);
        String[] fieldNames =
            {"liverName", "brokerName", "month", "giftEarning", "liveDuration", "remark", "liverId", "brokerId"};
        model.put("fieldNames", fieldNames);
        Map<String, CellParse> fieldCellParseMap = new HashMap<>();
        model.put("fieldCellParseMap", fieldCellParseMap);
        fieldCellParseMap.put("month", CellParse.Integer_CELL_PARSE);
        fieldCellParseMap.put("giftEarning", CellParse.BigDecimal_CELL_PARSE);
        fieldCellParseMap.put("liveDuration", CellParse.Integer_CELL_PARSE);
        fieldCellParseMap.put("remark", CellParse.String_CELL_PARSE);
        
        List<LiveGoal> list = null;
        try
        {
            list = PoiExcelUtil.read(myfile.getInputStream(), LiveGoal.class, model);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        // 入库数据
        if (list != null)
        {
            return this.liveGoalService.batchSave(list);
        }
        else
        {
            return new ArrayList<LiveGoal>();
        }
    }
    
    @RequestMapping("export")
    public ModelAndView export(@RequestParam Map<String, Object> params, Sort sort)
    {
        Map<String, Object> model = new HashMap<>();
        model.put("filename", "主播月度目标.xls");
        model.put("sheetName", "主播月度目标");
        String[] headers = {"序号", "主播", "经纪人", "月份", "礼物收益目标(元)", "直播时长目标(分钟)", "备注"};
        model.put("headers", headers);
        model.put("hasSerialColumn", true);
        String[] fieldNames = {"liverName", "brokerName", "month", "giftEarning", "liveDuration", "remark"};
        model.put("fieldNames", fieldNames);
        
        List<LiveGoal> data = this.liveGoalService.listQuery(params, sort);
        model.put("data", data);
        return new ModelAndView(new PoiExcelView(), model);
    }
    
    @RequestMapping("exportTemplate")
    public ModelAndView exportTemplate(@RequestParam Map<String, Object> params, Sort sort)
    {
        
        Map<String, Object> model = new HashMap<>();
        model.put("filename", "主播月度目标.xls");
        model.put("sheetName", "主播月度目标");
        String[] headers = {"序号", "主播", "经纪人", "月份", "礼物收益目标(元)", "直播时长目标(分钟)", "备注", "主播id", "经纪人id"};
        model.put("headers", headers);
        model.put("hasSerialColumn", true);
        String[] fieldNames =
            {"liverName", "brokerName", "month", "giftEarning", "liveDuration", "remark", "liverId", "brokerId"};
        model.put("fieldNames", fieldNames);
        
        Map<String, Object> liverQueryParams = new HashMap<>();
        
        String borkerIds = (String)params.get("brokerId_in_String");
        if (StringUtils.isNotBlank(borkerIds))
        {
            liverQueryParams.put("broker.id_in_String", borkerIds);
        }
        
        String liverIds = (String)params.get("liverId_in_String");
        if (StringUtils.isNotBlank(liverIds))
        {
            liverQueryParams.put("id_in_String", liverIds);
        }
        
        sort.setSort("broker.org.ordno,broker.id,id");
        sort.setOrder("ASC,ASC,ASC");
        
        String monthStr = (String)params.get("month_gte_intt");
        Calendar ca = Calendar.getInstance();
        int month = ca.get(Calendar.YEAR) * 100 + ca.get(Calendar.MONTH) + 1;
        if (StringUtils.isNotBlank(monthStr))
        {
            try
            {
                month = Integer.parseInt(monthStr);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        List<Liver> livers = this.liverService.query(liverQueryParams, sort);
        List<LiveGoal> data = new ArrayList<>();
        LiveGoal ld = null;
        for (Liver liver : livers)
        {
            ld = new LiveGoal();
            data.add(ld);
            ld.setLiverId(liver.getId());
            ld.setLiverName(liver.getName());
            ld.setBrokerId(liver.getBroker().getId());
            ld.setBrokerName(liver.getBroker().getName());
            ld.setMonth(month);
        }
        model.put("data", data);
        return new ModelAndView(new PoiExcelView(), model);
    }
    
    @RequestMapping("list")
    @ResponseBody
    public List<LiveGoal> listQuery(@RequestParam Map<String, Object> params, Sort sort)
    {
        return this.liveGoalService.listQuery(params, sort);
    }
    
    @RequestMapping("delete")
    @ResponseBody
    public OpResult delete(String[] id)
    {
        this.liveGoalService.delete(LiveGoal.class, id);
        return OpResult.deleteOk();
    }
}

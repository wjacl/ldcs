package com.wja.ldcs.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wja.base.common.OpResult;
import com.wja.base.util.DateUtil;
import com.wja.base.util.Page;
import com.wja.base.util.PoiExcelUtil.DataFormat;
import com.wja.base.util.Sort;
import com.wja.base.web.view.PoiExcelView;
import com.wja.ldcs.entity.EvaLiverMonth;
import com.wja.ldcs.entity.LiveData;
import com.wja.ldcs.service.LiveDataService;
import com.wja.ldcs.service.LiverService;

@Controller
@RequestMapping("/perfEval")
public class PerfEvalController
{
    @Autowired
    private LiveDataService liveDataService;
    
    @Autowired
    private LiverService liverService;
    
    @RequestMapping("manage")
    public String manage()
    {
        return "ldcs/perf_eval";
    }
    
    @RequestMapping("tjQuery")
    @ResponseBody
    public Page<EvaLiverMonth> tongJiPageQuery(@RequestParam Map<String, Object> params, Page<EvaLiverMonth> page)
    {
        return this.liveDataService.tongJiPageQuery(params, page);
    }
    
    @RequestMapping({"add", "update"})
    @ResponseBody
    public OpResult save(LiveData c)
    {
        LiveData dbld = this.liveDataService.findByLiverIdAndDate(c.getLiverId(), c.getDate());
        boolean add = StringUtils.isBlank(c.getId());
        if (add)
        {
            if (dbld != null)
            {
                return OpResult.addError("该日期数据已存在，不能重复录入，请进行修改！", c);
            }
            this.liveDataService.add(c);
            return OpResult.addOk(c);
        }
        else
        {
            if (dbld != null && !dbld.getId().equals(c.getId()))
            {
                return OpResult.updateError("该日期数据已存在，不能重复录入，请修改！", c);
            }
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
    
    @RequestMapping("tjexport")
    public ModelAndView tjexport(@RequestParam Map<String, Object> params, Sort sort)
    {
        Map<String, Object> model = new HashMap<>();
        model.put("filename", "主播直播月度统计数据.xls");
        model.put("sheetName", "主播直播月度统计数据");
        String[] headers = {"序号", "主播", "经纪人", "月份", "礼物收益(元)", "礼物收益目标(元)", "礼物完成率", "礼物差距", "直播时长(分钟)", "直播时长目标(分钟)",
            "时长完成率", "时长差距"};
        model.put("headers", headers);
        model.put("hasSerialColumn", true);
        String[] fieldNames = {"ev.liverName", "ev.brokerName", "ev.month", "ev.giftEarning", "ev.giftEarningGoal",
            "giftLv", "giftCj", "ev.liveDuration", "ev.liveDurationGoal", "duraLv", "duraCj"};
        model.put("fieldNames", fieldNames);
        
        List<EvaLiverMonth> data = this.liveDataService.tongJiListQuery(params, sort);
        List<Map<String, Object>> realList = new ArrayList<>();
        model.put("data", realList);
        Map<String, Object> map = null;
        for (EvaLiverMonth ev : data)
        {
            map = new HashMap<>();
            realList.add(map);
            map.put("ev", ev);
            
            String giftLv = "";
            String giftCj = "";
            
            // 礼物完成率 差距计算
            if (ev.getGiftEarning() != null && ev.getGiftEarningGoal() != null)
            {
                try
                {
                    giftLv =
                        Math.round(ev.getGiftEarning().doubleValue() / ev.getGiftEarningGoal().doubleValue() * 10000)
                            / 100.0 + "%";
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                
                if (ev.getGiftEarning().compareTo(ev.getGiftEarningGoal()) < 0)
                {
                    giftCj = ev.getGiftEarningGoal().subtract(ev.getGiftEarning()).toString();
                }
            }
            map.put("giftLv", giftLv);
            map.put("giftCj", giftCj);
            
            String duraLv = "";
            String duraCj = "";
            
            // 时长完成率 差距计算
            if (ev.getLiveDuration() != null && ev.getLiveDurationGoal() != null)
            {
                try
                {
                    duraLv =
                        Math.round(((double)ev.getLiveDuration()) / ev.getLiveDurationGoal() * 10000) / 100.0 + "%";
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                
                if (ev.getLiveDuration() < ev.getLiveDurationGoal())
                {
                    duraCj = ev.getLiveDurationGoal() - ev.getLiveDuration() + "";
                }
            }
            map.put("duraLv", duraLv);
            map.put("duraCj", duraCj);
        }
        return new ModelAndView(new PoiExcelView(), model);
    }
    
    @RequestMapping("export")
    public ModelAndView export(@RequestParam Map<String, Object> params, Sort sort)
    {
        Map<String, Object> model = new HashMap<>();
        model.put("filename", "主播直播数据.xls");
        model.put("sheetName", "主播直播数据");
        String[] headers =
            {"序号", "主播", "经纪人", "所在平台", "直播房间号", "直播名", "日期", "订阅", "订阅增幅", "人气", "礼物收益(元)", "直播时长(分钟)", "备注"};
        model.put("headers", headers);
        model.put("hasSerialColumn", true);
        String[] fieldNames = {"liverName", "brokerName", "platform", "roomNo", "liveName", "date", "rss",
            "rssGrowRate", "popularity", "giftEarning", "liveDuration", "remark"};
        model.put("fieldNames", fieldNames);
        
        Map<String, DataFormat> dataFormatMap = new HashMap<>();
        model.put("dataFormatMap", dataFormatMap);
        dataFormatMap.put("date", new DataFormat()
        {
            @Override
            public String format(Object v)
            {
                if (v != null)
                {
                    return DateUtil.DEFAULT_DF.format((Date)v);
                }
                return "";
            }
        });
        
        List<LiveData> data = this.liveDataService.listQuery(params, sort);
        model.put("data", data);
        return new ModelAndView(new PoiExcelView(), model);
    }
    
    @RequestMapping("evalList")
    @ResponseBody
    public List<EvaLiverMonth> evalListQuery(@RequestParam Map<String, Object> params, Sort sort)
    {
        return this.liveDataService.tongJiListQuery(params, sort);
    }
    
    @RequestMapping("delete")
    @ResponseBody
    public OpResult delete(String[] id)
    {
        this.liveDataService.delete(LiveData.class, id);
        return OpResult.deleteOk();
    }
    
}

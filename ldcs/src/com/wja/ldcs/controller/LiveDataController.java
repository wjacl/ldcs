package com.wja.ldcs.controller;

import java.io.IOException;
import java.text.ParseException;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.wja.base.common.OpResult;
import com.wja.base.util.DateUtil;
import com.wja.base.util.Page;
import com.wja.base.util.PoiExcelUtil;
import com.wja.base.util.PoiExcelUtil.CellParse;
import com.wja.base.util.PoiExcelUtil.DataFormat;
import com.wja.base.util.Sort;
import com.wja.base.web.view.PoiExcelView;
import com.wja.ldcs.entity.EvaLiverMonth;
import com.wja.ldcs.entity.LiveData;
import com.wja.ldcs.entity.Liver;
import com.wja.ldcs.service.LiveDataService;
import com.wja.ldcs.service.LiverService;

@Controller
@RequestMapping("/liveData")
public class LiveDataController
{
    @Autowired
    private LiveDataService liveDataService;
    
    @Autowired
    private LiverService liverService;
    
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
    
    @RequestMapping("tj")
    public String tongJi()
    {
        return "ldcs/live_data_tj";
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
    
    @RequestMapping("import")
    @ResponseBody
    public List<LiveData> doImport(@RequestPart("myfile") MultipartFile myfile)
    {
        
        // 读取excel数据
        Map<String, Object> model = new HashMap<>();
        model.put("firstRowIndex", 1);
        model.put("firstColumnIndex", 1);
        String[] fieldNames = {"liverName", "brokerName", "platform", "roomNo", "liveName", "date", "rss",
            "rssGrowRate", "popularity", "giftEarning", "liveDuration", "remark", "liverId", "brokerId"};
        model.put("fieldNames", fieldNames);
        Map<String, CellParse> fieldCellParseMap = new HashMap<>();
        model.put("fieldCellParseMap", fieldCellParseMap);
        fieldCellParseMap.put("date", CellParse.Date_YYYYMMDD_CELL_PARSE);
        fieldCellParseMap.put("rss", CellParse.Integer_CELL_PARSE);
        fieldCellParseMap.put("rssGrowRate", CellParse.BigDecimal_CELL_PARSE);
        fieldCellParseMap.put("popularity", CellParse.Integer_CELL_PARSE);
        fieldCellParseMap.put("giftEarning", CellParse.BigDecimal_CELL_PARSE);
        fieldCellParseMap.put("liveDuration", CellParse.Integer_CELL_PARSE);
        
        List<LiveData> list = null;
        try
        {
            list = PoiExcelUtil.read(myfile.getInputStream(), LiveData.class, model);
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
            return this.liveDataService.batchSave(list);
        }
        else
        {
            return new ArrayList<LiveData>();
        }
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
    
    @RequestMapping("exportTemplate")
    public ModelAndView exportTemplate(@RequestParam Map<String, Object> params, Sort sort)
    {
        
        Map<String, Object> model = new HashMap<>();
        model.put("filename", "主播直播数据导入模板.xls");
        model.put("sheetName", "主播直播数据");
        String[] headers = {"序号", "主播", "经纪人", "所在平台", "直播房间号", "直播名", "日期", "订阅", "订阅增幅", "人气", "礼物收益(元)", "直播时长(分钟)",
            "备注", "主播id", "经纪人id"};
        model.put("headers", headers);
        model.put("hasSerialColumn", true);
        String[] fieldNames = {"liverName", "brokerName", "platform", "roomNo", "liveName", "date", "rss",
            "rssGrowRate", "popularity", "giftEarning", "liveDuration", "remark", "liverId", "brokerId"};
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
        
        String liverIds = (String)params.remove("liverId_in_String");
        if (StringUtils.isNotBlank(liverIds))
        {
            params.put("id_in_String", liverIds);
        }
        if (StringUtils.isBlank(sort.getSort()))
        {
            sort.setSort("broker.org.ordno,broker.id,id");
            sort.setOrder("ASC,ASC,ASC");
        }
        
        String dt = (String)params.remove("date_eq_date");
        Date date = new Date();
        if (StringUtils.isNotBlank(dt))
        {
            try
            {
                date = DateUtil.DEFAULT_DF.parse(dt);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        
        List<Liver> livers = this.liverService.query(params, sort);
        List<LiveData> data = new ArrayList<>();
        LiveData ld = null;
        for (Liver liver : livers)
        {
            ld = new LiveData();
            data.add(ld);
            ld.setLiverId(liver.getId());
            ld.setLiverName(liver.getName());
            ld.setBrokerId(liver.getBroker().getId());
            ld.setBrokerName(liver.getBroker().getName());
            ld.setDate(date);
            ld.setPlatform(liver.getPlatform());
            ld.setRoomNo(liver.getRoomNo());
            ld.setLiveName(liver.getLiveName());
            
        }
        model.put("data", data);
        return new ModelAndView(new PoiExcelView(), model);
    }
    
    @RequestMapping("list")
    @ResponseBody
    public List<LiveData> listQuery(@RequestParam Map<String, Object> params, Sort sort)
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

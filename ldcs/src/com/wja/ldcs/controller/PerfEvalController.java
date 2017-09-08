package com.wja.ldcs.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wja.base.common.OpResult;
import com.wja.base.util.BeanUtil;
import com.wja.base.util.CollectionUtil;
import com.wja.base.util.DateUtil;
import com.wja.base.util.Page;
import com.wja.base.util.PoiExcelUtil.DataFormat;
import com.wja.base.util.Sort;
import com.wja.base.web.view.PoiExcelView;
import com.wja.ldcs.entity.EvaBrokerMonth;
import com.wja.ldcs.entity.EvaLiverMonth;
import com.wja.ldcs.entity.LiveData;
import com.wja.ldcs.entity.PerfStandard;
import com.wja.ldcs.service.EvaBrokerMonthService;
import com.wja.ldcs.service.LiveDataService;
import com.wja.ldcs.service.PerfStandardService;

@Controller
@RequestMapping("/perfEval")
public class PerfEvalController
{
    @Autowired
    private LiveDataService liveDataService;
    
    @Autowired
    private EvaBrokerMonthService evaBrokerMonthService;
    
    @Autowired
    private PerfStandardService perfStandardService;
    
    @RequestMapping("manage")
    public String manage()
    {
        return "ldcs/perf_eval";
    }
    
    @RequestMapping({"add", "update"})
    @ResponseBody
    public OpResult save(EvaBrokerMonth c)
    {
        c.setId(null);
        EvaBrokerMonth dbld = this.evaBrokerMonthService.findByBrokerIdAndMonth(c.getBrokerId(), c.getMonth());
        if (dbld == null)
        {
            this.evaBrokerMonthService.add(c);
            return OpResult.addOk(c);
        }
        else
        {
            BeanUtil.copyPropertiesIgnoreNull(c, dbld);
            this.evaBrokerMonthService.update(dbld);
            return OpResult.updateOk(dbld);
        }
    }
    
    @RequestMapping("getBrokerPerfLiverDetail")
    @ResponseBody
    public List<EvaLiverMonth> getBrokerlistLiverMonthData(String brokerId, Integer month)
    {
        return this.evaBrokerMonthService.findEvaLiverMonthData(brokerId, month);
    }
    
    @RequestMapping("greateMonthEvaData")
    @ResponseBody
    public OpResult greateMonthEvaData(Integer month)
    {
        PerfStandard pd = this.perfStandardService.findByMonth(month);
        if (pd == null)
        {
            return OpResult.error(month + "月的绩效标准尚未制定，不可生成绩效数据！", null);
        }
        OpResult opr = OpResult.ok();
        opr.setMess("绩效数据已生成！");
        if (this.evaBrokerMonthService.getMonthCount(month) == 0)
        {
            Map<String, Object> params = new HashMap<>();
            params.put("month_eq_intt", month + "");
            Sort sort = new Sort("brokerId,liverId", "ASC,ASC");
            List<EvaLiverMonth> list = this.liveDataService.tongJiListQuery(params, sort);
            if (CollectionUtil.isNotEmpty(list))
            {
                this.greateAndSaveEvalData(list, month, pd);
            }
            else
            {
                opr.setMess("没有" + month + "月的直播数据，不能生成绩效数据！");
            }
        }
        return opr;
    }
    
    private void computeGiftComm(EvaLiverMonth row, PerfStandard pd)
    {
        row.setComm(0.0);
        if (row.getGiftEarningGoal() == null)
        {
            row.setCommMess("没有定目标！");
            return;
        }
        
        if (row.getGiftEarning() < row.getGiftEarningGoal())
        {
            row.setCommMess("未完成目标!");
        }
        else
        {
            int count = 0;
            double goal = row.getGiftEarningGoal();
            double levelInterval = goal * pd.getCommLevelInterval() / 100;
            double commLevelPropInterval = pd.getCommLevelPropInterval().doubleValue();
            double compValue = goal;
            double commProp = pd.getCommLevelBaseProp().doubleValue();
            double gift = row.getGiftEarning();
            double comm = 0.0;
            String mess = "";
            while (gift > 0)
            {
                if (count == 0)
                {
                    comm = goal * commProp / 100;
                    mess = "(" + goal + " * " + commProp + "%) ";
                }
                else
                {
                    if (gift > levelInterval)
                    {
                        compValue = levelInterval;
                    }
                    else
                    {
                        compValue = gift;
                    }
                    commProp += commLevelPropInterval;
                    comm += compValue * commProp / 100;
                    mess += " + (" + compValue + " * " + commProp + "%) ";
                }
                
                gift -= compValue;
                count++;
            }
            mess += " = " + comm;
            row.setComm(comm);
            row.setCommMess(mess);
        }
    }
    
    private void greateAndSaveEvalData(List<EvaLiverMonth> list, Integer month, PerfStandard pd)
    {
        String lastPid = null;
        EvaLiverMonth lastRow = null;
        int count = 0;
        int giftOkCount = 0;
        int duraOKCount = 0;
        double giftComm = 0;
        EvaBrokerMonth ebm = null;
        List<EvaBrokerMonth> brokers = new ArrayList<>();
        for (EvaLiverMonth row : list)
        {
            row.setGflv(0.0);
            if (row.getGiftEarning() != null && row.getGiftEarningGoal() != null)
            {
                row.setGflv(Math.round(row.getGiftEarning() / row.getGiftEarningGoal() * 10000) / 100.0);
                row.setGflvText(row.getGflv() + "%");
            }
            row.setDulv(0.0);
            if (row.getLiveDuration() != null && row.getLiveDurationGoal() != null)
            {
                row.setDulv(Math.round(row.getLiveDuration() / row.getLiveDurationGoal() * 10000) / 100.0);
                row.setDulvText(row.getDulv() + "%");
            }
            // 礼物提成
            this.computeGiftComm(row, pd);
            
            if (!row.getBrokerId().equals(lastPid))
            {
                if (lastPid != null)
                {
                    double xgflv = Math.round(1.0 * giftOkCount / count * 10000) / 100.0;
                    double xdulv = Math.round(1.0 * duraOKCount / count * 10000) / 100.0;
                    double xperfEval = xgflv * pd.getGiftProp().doubleValue() / 100
                        + xdulv * pd.getDurationProp().doubleValue() / 100 + pd.getGradeProp().doubleValue();
                    ebm = new EvaBrokerMonth();
                    brokers.add(ebm);
                    ebm.setBrokerId(lastRow.getBrokerId());
                    ebm.setBrokerName(lastRow.getBrokerName());
                    ebm.setMonth(month);
                    ebm.setGflv(xgflv);
                    ebm.setGflvText(giftOkCount + " / " + count + " = " + xgflv + "%");
                    ebm.setDulv(xdulv);
                    ebm.setDulvText(duraOKCount + " / " + count + " = " + xdulv + "%");
                    ebm.setComm(giftComm);
                    ebm.setCommMess(giftComm + "");
                    ebm.setGradeProp(pd.getGradeProp().doubleValue());
                    ebm.setPerfEval(xperfEval);
                    ebm.setPerfEvalText("(" + xgflv + "% * " + pd.getGiftProp() + "%) + (" + xdulv + "% * "
                        + pd.getDurationProp() + "%) + " + pd.getGradeProp() + "% = " + xperfEval + "%");
                    ebm.setPerfComm(Math.round(giftComm * xperfEval) / 100.0);
                }
                
                lastPid = row.getBrokerId();
                giftOkCount = 0;
                duraOKCount = 0;
                count = 0;
                giftComm = 0;
            }
            
            count++;
            giftComm += row.getComm();
            if (row.getGflv() >= 100)
            {
                giftOkCount++;
            }
            if (row.getDulv() >= 100)
            {
                duraOKCount++;
            }
            lastRow = row;
            
        }
        
        if (count > 0)
        {
            double xgflv = Math.round(1.0 * giftOkCount / count * 10000) / 100.0;
            double xdulv = Math.round(1.0 * duraOKCount / count * 10000) / 100.0;
            double xperfEval = xgflv * pd.getGiftProp().doubleValue() / 100
                + xdulv * pd.getDurationProp().doubleValue() / 100 + pd.getGradeProp().doubleValue();
            ebm = new EvaBrokerMonth();
            brokers.add(ebm);
            ebm.setBrokerId(lastRow.getBrokerId());
            ebm.setBrokerName(lastRow.getBrokerName());
            ebm.setMonth(month);
            ebm.setGflv(xgflv);
            ebm.setGflvText(giftOkCount + " / " + count + " = " + xgflv + "%");
            ebm.setDulv(xdulv);
            ebm.setDulvText(duraOKCount + " / " + count + " = " + xdulv + "%");
            ebm.setComm(giftComm);
            ebm.setCommMess(giftComm + "");
            ebm.setGradeProp(pd.getGradeProp().doubleValue());
            ebm.setPerfEval(xperfEval);
            ebm.setPerfEvalText("(" + xgflv + "% * " + pd.getGiftProp() + "%) + (" + xdulv + "% * "
                + pd.getDurationProp() + "%) + " + pd.getGradeProp() + "% = " + xperfEval + "%");
            ebm.setPerfComm(Math.round(giftComm * xperfEval) / 100.0);
        }
        this.evaBrokerMonthService.batchSave(brokers);
        this.evaBrokerMonthService.batchSaveLiverMonthData(list);
    }
    
    @RequestMapping("query")
    @ResponseBody
    public Page<EvaBrokerMonth> pageQuery(@RequestParam Map<String, Object> params, Page<EvaBrokerMonth> page)
    {
        return this.evaBrokerMonthService.pageQuery(params, page);
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
                    // giftCj =
                    // ev.getGiftEarningGoal().subtract(ev.getGiftEarning()).toString();
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
    public Page<EvaLiverMonth> evalListQuery(@RequestParam Map<String, Object> params, Sort sort)
    {
        List<EvaLiverMonth> list = this.liveDataService.tongJiListQuery(params, sort);
        Page<EvaLiverMonth> page = new Page<>();
        page.setTotal((long)list.size());
        page.setRows(list);
        return page;
    }
    
    @RequestMapping("delete")
    @ResponseBody
    public OpResult delete(String[] id)
    {
        this.liveDataService.delete(LiveData.class, id);
        return OpResult.deleteOk();
    }
    
}

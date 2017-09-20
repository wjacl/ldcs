package com.wja.ldcs.controller;

import java.util.ArrayList;
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
import com.wja.base.util.Page;
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
    public OpResult greateMonthEvaData(String brokerId_in_String, Integer month)
    {
        PerfStandard pd = this.perfStandardService.findByMonth(month);
        if (pd == null)
        {
            return OpResult.error(month + "月的绩效标准尚未制定，不可生成绩效数据！", null);
        }
        OpResult opr = OpResult.ok();
        opr.setMess("绩效数据已生成！");
        Map<String, Object> params = new HashMap<>();
        params.put("month_eq_intt", month + "");
        params.put("brokerId_in_String", brokerId_in_String);
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
    
    private synchronized void greateAndSaveEvalData(List<EvaLiverMonth> list, Integer month, PerfStandard pd)
    {
        String lastPid = null;
        EvaLiverMonth lastRow = null;
        int count = 0;
        int giftOkCount = 0;
        int duraOKCount = 0;
        double giftComm = 0;
        EvaBrokerMonth ebm = null;
        List<EvaBrokerMonth> brokers = new ArrayList<>();
        int index = 0;
        for (EvaLiverMonth row : list)
        {
            row.setGflv(0.0);
            if (row.getGiftEarning() != null && row.getGiftEarningGoal() != null)
            {
                row.setGflv(Math.round(row.getGiftEarning() / row.getGiftEarningGoal() * 10000) / 100.0);
                row.setGflvText((row.getGflv() + "%").replace(".0%", "%"));
            }
            row.setDulv(0.0);
            if (row.getLiveDuration() != null && row.getLiveDurationGoal() != null)
            {
                row.setDulv(Math.round(row.getLiveDuration() / row.getLiveDurationGoal() * 10000) / 100.0);
                row.setDulvText((row.getDulv() + "%").replace(".0%", "%"));
            }
            // 礼物提成
            this.computeGiftComm(row, pd);
            if (row.getCommMess() != null)
            {
                row.setCommMess(row.getCommMess().replace(".0%", "%"));
            }
            EvaLiverMonth dbelm =
                this.evaBrokerMonthService.findByBrokerIdAndLiverIdAndMonth(row.getBrokerId(), row.getLiverId(), month);
            if (dbelm != null)
            {
                BeanUtil.copyPropertiesIgnoreNull(row, dbelm);
                list.set(index, dbelm);
            }
            
            index++;
            
            if (!row.getBrokerId().equals(lastPid))
            {
                if (lastPid != null)
                {
                    double xgflv = Math.round(1.0 * giftOkCount / count * 10000) / 100.0;
                    double xdulv = Math.round(1.0 * duraOKCount / count * 10000) / 100.0;
                    ebm = this.evaBrokerMonthService.findByBrokerIdAndMonth(lastRow.getBrokerId(), month);
                    if (ebm == null)
                    {
                        ebm = new EvaBrokerMonth();
                    }
                    brokers.add(ebm);
                    ebm.setBrokerId(lastRow.getBrokerId());
                    ebm.setBrokerName(lastRow.getBrokerName());
                    ebm.setMonth(month);
                    ebm.setGflv(xgflv);
                    ebm.setGflvText((giftOkCount + " / " + count + " = " + xgflv + "%").replace(".0%", "%"));
                    ebm.setDulv(xdulv);
                    ebm.setDulvText((duraOKCount + " / " + count + " = " + xdulv + "%").replace(".0%", "%"));
                    ebm.setComm(giftComm);
                    ebm.setCommMess(giftComm + "");
                    if (ebm.getGradeProp() == null)
                    {
                        ebm.setGradeProp(pd.getGradeProp().doubleValue());
                    }
                    
                    double xperfEval = xgflv * pd.getGiftProp().doubleValue() / 100
                        + xdulv * pd.getDurationProp().doubleValue() / 100 + ebm.getGradeProp();
                    ebm.setPerfEval(xperfEval);
                    
                    String pft = "(" + xgflv + "% * " + pd.getGiftProp() + "%) + (" + xdulv + "% * "
                        + pd.getDurationProp() + "%) + " + ebm.getGradeProp() + "% = " + xperfEval + "%";
                    ebm.setPerfEvalText(pft.replace(".00%", "%").replace(".0%", "%"));
                    
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
            ebm = this.evaBrokerMonthService.findByBrokerIdAndMonth(lastRow.getBrokerId(), month);
            if (ebm == null)
            {
                ebm = new EvaBrokerMonth();
            }
            brokers.add(ebm);
            ebm.setBrokerId(lastRow.getBrokerId());
            ebm.setBrokerName(lastRow.getBrokerName());
            ebm.setMonth(month);
            ebm.setGflv(xgflv);
            ebm.setGflvText((giftOkCount + " / " + count + " = " + xgflv + "%").replace(".0%", "%"));
            ebm.setDulv(xdulv);
            ebm.setDulvText((duraOKCount + " / " + count + " = " + xdulv + "%").replace(".0%", "%"));
            ebm.setComm(giftComm);
            ebm.setCommMess(giftComm + "");
            if (ebm.getGradeProp() == null)
            {
                ebm.setGradeProp(pd.getGradeProp().doubleValue());
            }
            
            double xperfEval = xgflv * pd.getGiftProp().doubleValue() / 100
                + xdulv * pd.getDurationProp().doubleValue() / 100 + ebm.getGradeProp();
            ebm.setPerfEval(xperfEval);
            
            String pft = "(" + xgflv + "% * " + pd.getGiftProp() + "%) + (" + xdulv + "% * " + pd.getDurationProp()
                + "%) + " + ebm.getGradeProp() + "% = " + xperfEval + "%";
            ebm.setPerfEvalText(pft.replace(".00%", "%").replace(".0%", "%"));
            
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
    
    @RequestMapping("export")
    public ModelAndView export(@RequestParam Map<String, Object> params, Sort sort)
    {
        Map<String, Object> model = new HashMap<>();
        model.put("filename", "经纪人绩效数据.xls");
        model.put("sheetName", "经纪人绩效数据");
        String[] headers = {"序号", "经纪人", "主播", "月份", "部门评分", "评分说明", "权重得分", "礼物提成", "绩效提成", "礼物收益(元)", "礼物收益目标(元)",
            "礼物完成率", "直播时长(分钟)", "直播时长目标(分钟)", "直播时长完成率", "礼物提成明细"};
        model.put("headers", headers);
        model.put("hasSerialColumn", true);
        String[] fieldNames = {"brokerName", "liverName", "month", "gradeProp", "remark", "perfEvalText", "comm",
            "perfComm", "giftEarning", "giftEarningGoal", "gflvText", "liveDuration", "liveDurationGoal", "dulvText",
            "commMess"};
        model.put("fieldNames", fieldNames);
        
        List<EvaBrokerMonth> ebmList = this.evaBrokerMonthService.listQuery(params, sort);
        List<Object> data = new ArrayList<>();
        model.put("data", data);
        for (EvaBrokerMonth ebm : ebmList)
        {
            data.add(ebm);
            List<EvaLiverMonth> elList =
                this.evaBrokerMonthService.findEvaLiverMonthData(ebm.getBrokerId(), ebm.getMonth());
            if (CollectionUtil.isNotEmpty(elList))
            {
                for (EvaLiverMonth el : elList)
                {
                    el.setBrokerName("");
                }
                data.addAll(elList);
            }
        }
        return new ModelAndView(new PoiExcelView(), model);
    }
    
    @RequestMapping("delete")
    @ResponseBody
    public OpResult delete(String[] id)
    {
        this.liveDataService.delete(LiveData.class, id);
        return OpResult.deleteOk();
    }
    
}

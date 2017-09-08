package com.wja.ldcs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wja.base.common.CommSpecification;
import com.wja.base.common.service.CommService;
import com.wja.base.util.BeanUtil;
import com.wja.base.util.Page;
import com.wja.base.util.Sort;
import com.wja.ldcs.dao.EvaBrokerMonthDao;
import com.wja.ldcs.dao.EvaLiverMonthDao;
import com.wja.ldcs.entity.EvaBrokerMonth;
import com.wja.ldcs.entity.EvaLiverMonth;

@Service
public class EvaBrokerMonthService extends CommService<EvaBrokerMonth>
{
    @Autowired
    private EvaBrokerMonthDao evaBrokerMonthDao;
    
    @Autowired
    private EvaLiverMonthDao evaLiverMonthDao;
    
    @Autowired
    private PrivilegeControlService privilegeControlService;
    
    public List<EvaLiverMonth> findEvaLiverMonthData(String brokerId, Integer month)
    {
        List<EvaLiverMonth> list = this.evaLiverMonthDao.findByBrokerIdAndMonth(brokerId, month);
        BeanUtil.setCollFieldValues(list);
        return list;
    }
    
    public void batchSaveLiverMonthData(List<EvaLiverMonth> list)
    {
        this.evaLiverMonthDao.save(list);
    }
    
    public void batchSave(List<EvaBrokerMonth> list)
    {
        this.evaBrokerMonthDao.save(list);
    }
    
    public long getMonthCount(Integer month)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("month_eq_intt", month);
        return this.evaBrokerMonthDao.count(new CommSpecification<>(params));
    }
    
    public EvaBrokerMonth findByBrokerIdAndMonth(String brokerId, Integer month)
    {
        return this.evaBrokerMonthDao.findByBrokerIdAndMonth(brokerId, month);
    }
    
    public Page<EvaBrokerMonth> pageQuery(Map<String, Object> params, Page<EvaBrokerMonth> page)
    {
        this.privilegeControlService.liveDataAddDataAuthori(params);
        page.setPageData(
            this.evaBrokerMonthDao.findAll(new CommSpecification<EvaBrokerMonth>(params), page.getPageRequest()));
        return page;
    }
    
    public List<EvaBrokerMonth> listQuery(Map<String, Object> params, Sort sort)
    {
        this.privilegeControlService.liveDataAddDataAuthori(params);
        List<EvaBrokerMonth> list =
            this.evaBrokerMonthDao.findAll(new CommSpecification<EvaBrokerMonth>(params), sort.getSpringSort());
        return list;
    }
}

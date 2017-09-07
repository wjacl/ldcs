package com.wja.ldcs.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wja.base.common.CommSpecification;
import com.wja.base.common.service.CommService;
import com.wja.base.util.Page;
import com.wja.base.util.Sort;
import com.wja.ldcs.dao.EvaBrokerMonthDao;
import com.wja.ldcs.entity.EvaBrokerMonth;

@Service
public class EvaBrokerMonthService extends CommService<EvaBrokerMonth>
{
    @Autowired
    private EvaBrokerMonthDao evaBrokerMonthDao;
    
    public EvaBrokerMonth findByBrokerIdAndMonth(String brokerId, Integer month)
    {
        return this.evaBrokerMonthDao.findByBrokerIdAndMonth(brokerId, month);
    }
    
    public Page<EvaBrokerMonth> pageQuery(Map<String, Object> params, Page<EvaBrokerMonth> page)
    {
        page.setPageData(
            this.evaBrokerMonthDao.findAll(new CommSpecification<EvaBrokerMonth>(params), page.getPageRequest()));
        return page;
    }
    
    public List<EvaBrokerMonth> listQuery(Map<String, Object> params, Sort sort)
    {
        List<EvaBrokerMonth> list =
            this.evaBrokerMonthDao.findAll(new CommSpecification<EvaBrokerMonth>(params), sort.getSpringSort());
        return list;
    }
}

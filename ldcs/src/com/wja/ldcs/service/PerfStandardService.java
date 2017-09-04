package com.wja.ldcs.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wja.base.common.CommSpecification;
import com.wja.base.common.service.CommService;
import com.wja.base.util.Page;
import com.wja.base.util.Sort;
import com.wja.ldcs.dao.PerfStandardDao;
import com.wja.ldcs.entity.PerfStandard;

@Service
public class PerfStandardService extends CommService<PerfStandard> {
    @Autowired
    private PerfStandardDao perfStandardDao;

    public PerfStandard findByMonth(Integer month) {
	return this.perfStandardDao.findByMonth(month);
    }

    public Page<PerfStandard> pageQuery(Map<String, Object> params, Page<PerfStandard> page) {
	page.setPageData(
		this.perfStandardDao.findAll(new CommSpecification<PerfStandard>(params), page.getPageRequest()));
	return page;
    }

    public List<PerfStandard> listQuery(Map<String, Object> params, Sort sort) {
	List<PerfStandard> list = this.perfStandardDao.findAll(new CommSpecification<PerfStandard>(params),
		sort.getSpringSort());
	return list;
    }
}

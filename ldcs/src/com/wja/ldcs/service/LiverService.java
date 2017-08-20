package com.wja.ldcs.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wja.base.common.CommSpecification;
import com.wja.base.common.service.CommService;
import com.wja.base.util.Page;
import com.wja.base.util.Sort;
import com.wja.ldcs.dao.LiverDao;
import com.wja.ldcs.entity.Liver;

@Service
public class LiverService extends CommService<Liver> {

    @Autowired
    private LiverDao liverDao;

    @Autowired
    private PrivilegeControlService privilegeControlService;

    public Page<Liver> pageQuery(Map<String, Object> params, Page<Liver> page) {

	privilegeControlService.addDataAuthori(params);
	return page.setPageData(this.liverDao.findAll(new CommSpecification<Liver>(params), page.getPageRequest()));
    }

    public List<Liver> query(Map<String, Object> params, Sort sort) {
	privilegeControlService.addDataAuthori(params);
	return this.liverDao.findAll(new CommSpecification<Liver>(params), sort.getSpringSort());
    }
}

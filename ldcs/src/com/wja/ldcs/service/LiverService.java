package com.wja.ldcs.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wja.base.common.CommSpecification;
import com.wja.base.common.service.CommService;
import com.wja.base.util.Page;
import com.wja.ldcs.dao.LiverDao;
import com.wja.ldcs.entity.Liver;

@Service
public class LiverService extends CommService<Liver>
{
    
    @Autowired
    private LiverDao liverDao;
    
    public Page<Liver> pageQuery(Map<String, Object> params, Page<Liver> page)
    {
        return page.setPageData(this.liverDao.findAll(new CommSpecification<Liver>(params), page.getPageRequest()));
    }
}

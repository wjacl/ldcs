package com.wja.ldcs.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wja.base.common.CommSpecification;
import com.wja.base.common.service.CommService;
import com.wja.base.util.Page;
import com.wja.ldcs.dao.LiveDataDao;
import com.wja.ldcs.entity.LiveData;

@Service
public class LiveDataService extends CommService<LiveData>
{
    @Autowired
    private LiveDataDao liveDataDao;
    
    @Autowired
    private PrivilegeControlService privilegeControlService;
    
    public Page<LiveData> pageQuery(Map<String, Object> params, Page<LiveData> page)
    {
        return page
            .setPageData(this.liveDataDao.findAll(new CommSpecification<LiveData>(params), page.getPageRequest()));
    }
}

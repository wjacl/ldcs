package com.wja.ldcs.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wja.base.common.CommSpecification;
import com.wja.base.common.service.CommService;
import com.wja.base.util.BeanUtil;
import com.wja.base.util.Page;
import com.wja.base.util.Sort;
import com.wja.ldcs.dao.LiveDataDao;
import com.wja.ldcs.dao.LiveDataTJDao;
import com.wja.ldcs.entity.EvaLiverMonth;
import com.wja.ldcs.entity.LiveData;

@Service
public class LiveDataService extends CommService<LiveData>
{
    @Autowired
    private LiveDataDao liveDataDao;
    
    @Autowired
    private PrivilegeControlService privilegeControlService;
    
    @Autowired
    private LiveDataTJDao liveDataTJDao;
    
    public LiveData findByLiverIdAndDate(String liverId, Date date)
    {
        return this.liveDataDao.findByLiverIdAndDate(liverId, date);
    }
    
    /**
     * 批量保存直播数据，注意：对已存在日期的数据进行的是修改。
     * 
     * @param list
     * @return 返回保存过后的数据list
     * @see [类、类#方法、类#成员]
     */
    public List<LiveData> batchSave(List<LiveData> list)
    {
        if (list == null)
        {
            return null;
        }
        LiveData dbLiveData = null;
        LiveData ld = null;
        for (int i = 0; i < list.size(); i++)
        {
            dbLiveData = null;
            ld = list.get(i);
            if (StringUtils.isNotBlank(ld.getLiverId()) && ld.getDate() != null)
            {
                dbLiveData = this.liveDataDao.findByLiverIdAndDate(ld.getLiverId(), ld.getDate());
            }
            if (dbLiveData != null)
            {
                BeanUtil.copyPropertiesIgnoreNull(ld, dbLiveData);
                this.update(dbLiveData);
            }
            else
            {
                dbLiveData = ld;
                this.add(dbLiveData);
            }
            
            list.set(i, dbLiveData);
        }
        
        return list;
    }
    
    public Page<LiveData> pageQuery(Map<String, Object> params, Page<LiveData> page)
    {
        privilegeControlService.liveDataAddDataAuthori(params);
        page.setPageData(this.liveDataDao.findAll(new CommSpecification<LiveData>(params), page.getPageRequest()));
        BeanUtil.setCollFieldValues(page.getRows());
        return page;
    }
    
    public List<LiveData> listQuery(Map<String, Object> params, Sort sort)
    {
        privilegeControlService.liveDataAddDataAuthori(params);
        List<LiveData> list = this.liveDataDao.findAll(new CommSpecification<LiveData>(params), sort.getSpringSort());
        BeanUtil.setCollFieldValues(list);
        return list;
    }
    
    public Page<EvaLiverMonth> tongJiPageQuery(Map<String, Object> params, Page<EvaLiverMonth> page)
    {
        privilegeControlService.liveDataAddDataAuthori(params);
        this.liveDataTJDao.tongJiPageQuery(params, page);
        BeanUtil.setCollFieldValues(page.getRows());
        return page;
    }
    
    public List<EvaLiverMonth> tongJiListQuery(Map<String, Object> params, Sort sort)
    {
        privilegeControlService.liveDataAddDataAuthori(params);
        List<EvaLiverMonth> list = this.liveDataTJDao.listQuery(params, sort);
        BeanUtil.setCollFieldValues(list);
        return list;
    }
}

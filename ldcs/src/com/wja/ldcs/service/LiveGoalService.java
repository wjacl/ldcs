package com.wja.ldcs.service;

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
import com.wja.ldcs.dao.LiveGoalDao;
import com.wja.ldcs.entity.LiveGoal;

@Service
public class LiveGoalService extends CommService<LiveGoal> {
    @Autowired
    private LiveGoalDao liveGoalDao;

    @Autowired
    private PrivilegeControlService privilegeControlService;

    public LiveGoal findByLiverIdAndMonth(String liverId, Integer month) {
	return this.liveGoalDao.findByLiverIdAndMonth(liverId, month);
    }

    /**
     * 批量保存直播数据，注意：对已存在日期的数据进行的是修改。
     * 
     * @param list
     * @return 返回保存过后的数据list
     * @see [类、类#方法、类#成员]
     */
    public List<LiveGoal> batchSave(List<LiveGoal> list) {
	if (list == null) {
	    return null;
	}
	LiveGoal dbLiveGoal = null;
	LiveGoal ld = null;
	for (int i = 0; i < list.size(); i++) {
	    dbLiveGoal = null;
	    ld = list.get(i);
	    if (StringUtils.isNotBlank(ld.getLiverId()) && ld.getMonth() != null) {
		dbLiveGoal = this.liveGoalDao.findByLiverIdAndMonth(ld.getLiverId(), ld.getMonth());
	    }
	    if (dbLiveGoal != null) {
		BeanUtil.copyPropertiesIgnoreNull(ld, dbLiveGoal);
		this.update(dbLiveGoal);
	    } else {
		dbLiveGoal = ld;
		this.add(dbLiveGoal);
	    }

	    list.set(i, dbLiveGoal);
	}

	return list;
    }

    public Page<LiveGoal> pageQuery(Map<String, Object> params, Page<LiveGoal> page) {
	privilegeControlService.liveDataAddDataAuthori(params);
	page.setPageData(this.liveGoalDao.findAll(new CommSpecification<LiveGoal>(params), page.getPageRequest()));
	BeanUtil.setCollFieldValues(page.getRows());
	return page;
    }

    public List<LiveGoal> listQuery(Map<String, Object> params, Sort sort) {
	privilegeControlService.liveDataAddDataAuthori(params);
	List<LiveGoal> list = this.liveGoalDao.findAll(new CommSpecification<LiveGoal>(params), sort.getSpringSort());
	BeanUtil.setCollFieldValues(list);
	return list;
    }
}

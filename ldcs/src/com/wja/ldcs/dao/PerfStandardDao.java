package com.wja.ldcs.dao;

import org.springframework.stereotype.Repository;

import com.wja.base.common.CommRepository;
import com.wja.ldcs.entity.PerfStandard;

@Repository
public interface PerfStandardDao extends CommRepository<PerfStandard, String> {

    PerfStandard findByMonth(Integer month);
}

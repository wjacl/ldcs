package com.wja.ldcs.dao;

import org.springframework.stereotype.Repository;

import com.wja.base.common.CommRepository;
import com.wja.ldcs.entity.LiveData;

@Repository
public interface LiveDataDao extends CommRepository<LiveData, String> {

}

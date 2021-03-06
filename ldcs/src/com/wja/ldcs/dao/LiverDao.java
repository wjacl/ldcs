package com.wja.ldcs.dao;

import org.springframework.stereotype.Repository;

import com.wja.base.common.CommRepository;
import com.wja.ldcs.entity.Liver;

@Repository
public interface LiverDao extends CommRepository<Liver, String> {

}

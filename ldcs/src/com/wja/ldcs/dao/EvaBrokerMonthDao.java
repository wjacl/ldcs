package com.wja.ldcs.dao;

import org.springframework.stereotype.Repository;

import com.wja.base.common.CommRepository;
import com.wja.ldcs.entity.EvaBrokerMonth;

@Repository
public interface EvaBrokerMonthDao extends CommRepository<EvaBrokerMonth, String>
{
    
    EvaBrokerMonth findByBrokerIdAndMonth(String brokerId, Integer month);
}

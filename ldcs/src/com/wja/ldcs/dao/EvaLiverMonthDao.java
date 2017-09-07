package com.wja.ldcs.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wja.base.common.CommRepository;
import com.wja.ldcs.entity.EvaLiverMonth;

@Repository
public interface EvaLiverMonthDao extends CommRepository<EvaLiverMonth, String> {

    List<EvaLiverMonth> findByBrokerIdAndMonth(String brokerId, Integer month);

    void deleteByMonth(Integer month);
}

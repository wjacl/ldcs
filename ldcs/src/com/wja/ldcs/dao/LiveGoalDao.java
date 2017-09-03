package com.wja.ldcs.dao;

import org.springframework.stereotype.Repository;

import com.wja.base.common.CommRepository;
import com.wja.ldcs.entity.LiveGoal;

@Repository
public interface LiveGoalDao extends CommRepository<LiveGoal, String> {

    LiveGoal findByLiverIdAndMonth(String liverId, Integer month);
}

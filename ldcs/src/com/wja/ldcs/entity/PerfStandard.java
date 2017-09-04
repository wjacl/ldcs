package com.wja.ldcs.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.wja.base.common.CommConstants;
import com.wja.base.common.CommEntity;

@Entity
@Table(name = "t_ldcs_perf_standard")
@Where(clause = " valid = " + CommConstants.DATA_VALID)
public class PerfStandard extends CommEntity {
    /**
     * 月份
     */
    private Integer month;

    /**
     * 礼物占比
     */
    @Column(name = "gift_prop", precision = 5, scale = 2)
    private BigDecimal giftProp;

    /**
     * 时长占比
     */
    @Column(name = "duration_prop", precision = 5, scale = 2)
    private BigDecimal durationProp;

    /**
     * 部门调控评分占比
     */
    @Column(name = "grade_prop", precision = 5, scale = 2)
    private BigDecimal gradeProp;

    /**
     * 完成礼物目标提成基数等级值
     */
    @Column(name = "comm_level_base")
    private Integer commLevelBase;

    /**
     * 提成基数等级提成比例
     */
    @Column(name = "comm_level_base_prop", precision = 5, scale = 2)
    private BigDecimal commLevelBaseProp;

    /**
     * 完成礼物目标提成等级间隔值
     */
    @Column(name = "comm_level_interval")
    private Integer commLevelInterval;

    /**
     * 等级提成比例间隔值
     */
    @Column(name = "comm_level_prop_Interval", precision = 5, scale = 2)
    private BigDecimal commLevelPropInterval;

    @Column(length = 200)
    private String remark;

    public Integer getMonth() {
	return month;
    }

    public void setMonth(Integer month) {
	this.month = month;
    }

    public String getRemark() {
	return remark;
    }

    public void setRemark(String remark) {
	this.remark = remark;
    }

}

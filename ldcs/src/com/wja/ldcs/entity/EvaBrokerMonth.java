package com.wja.ldcs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.wja.base.common.CommConstants;
import com.wja.base.common.CommEntity;

@Entity
@Table(name = "t_ldcs_eva_Broker_month")
@Where(clause = " valid = " + CommConstants.DATA_VALID)
public class EvaBrokerMonth extends CommEntity {
    /**
     * 经纪人
     */
    @Column(name = "broker_id", length = 32)
    private String brokerId;

    @Column(name = "broker_name", length = 40)
    private String brokerName;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 礼物完成率
     */
    @Column(precision = 10, scale = 2)
    private Double gflv;

    /**
     * 礼物完成率计算式
     */
    @Column(name = "gflv_text", length = 40)
    private String gflvText;

    /**
     * 直播时长完成率
     */
    @Column(precision = 10, scale = 2)
    private Double dulv;

    /**
     * 直播时长完成率计算式
     */
    @Column(name = "dulv_text", length = 40)
    private String dulvText;

    /**
     * 礼物提成
     */
    @Column(precision = 10, scale = 2)
    private Double comm;

    /**
     * 礼物提成计算式
     */
    @Column(name = "comm_mess", length = 400)
    private String commMess;

    /**
     * 部门评分
     */
    @Column(name = "grade_prop", precision = 5, scale = 2)
    private Double gradeProp;

    /**
     * 评分说明
     */
    @Column(length = 200)
    private String remark;

    /**
     * 权重得分
     */
    @Column(name = "perf_eval", precision = 5, scale = 2)
    private Double perfEval;

    /**
     * 权重计算式
     */
    @Column(name = "perf_eval_text", length = 400)
    private String perfEvalText;

    /**
     * 绩效提成
     */
    @Column(name = "perf_comm", precision = 10, scale = 2)
    private Double perfComm;

    public String getBrokerName() {
	return brokerName;
    }

    public void setBrokerName(String brokerName) {
	this.brokerName = brokerName;
    }

    public String getBrokerId() {
	return brokerId;
    }

    public void setBrokerId(String brokerId) {
	this.brokerId = brokerId;
    }

    public Integer getMonth() {
	return month;
    }

    public void setMonth(Integer month) {
	this.month = month;
    }

    public Double getGflv() {
	return gflv;
    }

    public void setGflv(Double gflv) {
	this.gflv = gflv;
    }

    public String getGflvText() {
	return gflvText;
    }

    public void setGflvText(String gflvText) {
	this.gflvText = gflvText;
    }

    public Double getDulv() {
	return dulv;
    }

    public void setDulv(Double dulv) {
	this.dulv = dulv;
    }

    public String getDulvText() {
	return dulvText;
    }

    public void setDulvText(String dulvText) {
	this.dulvText = dulvText;
    }

    public Double getComm() {
	return comm;
    }

    public void setComm(Double comm) {
	this.comm = comm;
    }

    public String getCommMess() {
	return commMess;
    }

    public void setCommMess(String commMess) {
	this.commMess = commMess;
    }

    public Double getGradeProp() {
	return gradeProp;
    }

    public void setGradeProp(Double gradeProp) {
	this.gradeProp = gradeProp;
    }

    public String getRemark() {
	return remark;
    }

    public void setRemark(String remark) {
	this.remark = remark;
    }

    public Double getPerfEval() {
	return perfEval;
    }

    public void setPerfEval(Double perfEval) {
	this.perfEval = perfEval;
    }

    public String getPerfEvalText() {
	return perfEvalText;
    }

    public void setPerfEvalText(String perfEvalText) {
	this.perfEvalText = perfEvalText;
    }

    public Double getPerfComm() {
	return perfComm;
    }

    public void setPerfComm(Double perfComm) {
	this.perfComm = perfComm;
    }
}

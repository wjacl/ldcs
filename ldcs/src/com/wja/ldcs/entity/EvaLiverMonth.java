package com.wja.ldcs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.wja.base.common.CommConstants;
import com.wja.base.common.CommEntity;
import com.wja.base.system.entity.User;
import com.wja.base.util.SetValue;

@Entity
@Table(name = "t_ldcs_eva_liver_month")
@Where(clause = " valid = " + CommConstants.DATA_VALID)
public class EvaLiverMonth extends CommEntity {

    /**
     * 主播ID
     */
    @Column(name = "liver_id", length = 32)
    private String liverId;

    /**
     * 经纪人
     */
    @Column(name = "broker_id", length = 32)
    private String brokerId;

    @Transient
    @SetValue(clazz = Liver.class, id = "liverId", field = "name")
    private String liverName;

    @Transient
    @SetValue(clazz = User.class, id = "brokerId", field = "name")
    private String brokerName;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 礼物收益
     */
    @Column(name = "gift_earning", precision = 10, scale = 2)
    private Double giftEarning;

    /**
     * 直播时长(分钟)
     */
    @Column(name = "live_duration")
    private Integer liveDuration;

    /**
     * 礼物收益目标
     */
    @Column(name = "gift_earning_goal", precision = 10, scale = 2)
    private Double giftEarningGoal;

    /**
     * 直播时长(分钟)目标
     */
    @Column(name = "live_duration_goal")
    private Integer liveDurationGoal;

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

    public String getLiverName() {
	return liverName;
    }

    public void setLiverName(String liverName) {
	this.liverName = liverName;
    }

    public String getBrokerName() {
	return brokerName;
    }

    public void setBrokerName(String brokerName) {
	this.brokerName = brokerName;
    }

    public String getLiverId() {
	return liverId;
    }

    public void setLiverId(String liverId) {
	this.liverId = liverId;
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

    public Double getGiftEarning() {
	return giftEarning;
    }

    public void setGiftEarning(Double giftEarning) {
	this.giftEarning = giftEarning;
    }

    public Integer getLiveDuration() {
	return liveDuration;
    }

    public void setLiveDuration(Integer liveDuration) {
	this.liveDuration = liveDuration;
    }

    public Double getGiftEarningGoal() {
	return giftEarningGoal;
    }

    public void setGiftEarningGoal(Double giftEarningGoal) {
	this.giftEarningGoal = giftEarningGoal;
    }

    public Integer getLiveDurationGoal() {
	return liveDurationGoal;
    }

    public void setLiveDurationGoal(Integer liveDurationGoal) {
	this.liveDurationGoal = liveDurationGoal;
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
}

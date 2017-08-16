package com.wja.ldcs.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.wja.base.common.CommConstants;
import com.wja.base.common.CommConstants.User;
import com.wja.base.common.CommEntity;
import com.wja.base.util.DateUtil;

@Entity
@Table(name = "t_ldcs_liver")
@Where(clause = " valid = " + CommConstants.DATA_VALID)
public class Liver extends CommEntity {

    /**
     * 主播姓名
     */
    @Column(length = 30)
    private String name;

    /**
     * 主播艺名
     */
    @Column(name = "stage_name", length = 30)
    private String stageName;

    /**
     * 主播联系电话
     */
    @Column(length = 30)
    private String phone;

    /**
     * 入职状态
     */
    @Column(name = "entry_status", length = 1, nullable = false)
    private String entryStatus;

    /**
     * 入职日期
     */
    @DateTimeFormat(pattern = DateUtil.DATE)
    @JSONField(format = DateUtil.DATE)
    @Column(name = "entry_date")
    private Date entryDate;

    /**
     * 离职日期
     */
    @DateTimeFormat(pattern = DateUtil.DATE)
    @JSONField(format = DateUtil.DATE)
    @Column(name = "leave_date")
    private Date leaveDate;

    /**
     * 签约状态
     */
    @Column(name = "sign_status", length = 1, nullable = false)
    private String signStatus;

    /**
     * 签约日期
     */
    @DateTimeFormat(pattern = DateUtil.DATE)
    @JSONField(format = DateUtil.DATE)
    @Column(name = "sign_date")
    private Date signDate;

    /**
     * 解约日期
     */
    @DateTimeFormat(pattern = DateUtil.DATE)
    @JSONField(format = DateUtil.DATE)
    @Column(name = "break_date")
    private Date breakDate;

    /**
     * 经纪人
     */
    @ManyToOne
    private User broker;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getStageName() {
	return stageName;
    }

    public void setStageName(String stageName) {
	this.stageName = stageName;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getEntryStatus() {
	return entryStatus;
    }

    public void setEntryStatus(String entryStatus) {
	this.entryStatus = entryStatus;
    }

    public Date getEntryDate() {
	return entryDate;
    }

    public void setEntryDate(Date entryDate) {
	this.entryDate = entryDate;
    }

    public Date getLeaveDate() {
	return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
	this.leaveDate = leaveDate;
    }

    public String getSignStatus() {
	return signStatus;
    }

    public void setSignStatus(String signStatus) {
	this.signStatus = signStatus;
    }

    public Date getSignDate() {
	return signDate;
    }

    public void setSignDate(Date signDate) {
	this.signDate = signDate;
    }

    public Date getBreakDate() {
	return breakDate;
    }

    public void setBreakDate(Date breakDate) {
	this.breakDate = breakDate;
    }

    public User getBroker() {
	return broker;
    }

    public void setBroker(User broker) {
	this.broker = broker;
    }
}

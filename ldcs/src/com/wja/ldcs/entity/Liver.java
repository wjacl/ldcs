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
import com.wja.base.common.CommEntity;
import com.wja.base.system.entity.User;
import com.wja.base.util.DateUtil;

@Entity
@Table(name = "t_ldcs_liver")
@Where(clause = " valid = " + CommConstants.DATA_VALID)
public class Liver extends CommEntity {

    /**
     * 主播姓名
     */
    @Column(length = 30, nullable = false)
    private String name;

    /**
     * 经纪人
     */
    @ManyToOne
    private User broker;

    /**
     * 平台
     */
    @Column(length = 40)
    private String platform;

    /**
     * 房间号
     */
    @Column(name = "room_no", length = 30)
    private String roomNo;

    /**
     * 主播直播名
     */
    @Column(name = "live_name", length = 60)
    private String liveName;

    /**
     * 主播联系电话
     */
    @Column(length = 30)
    private String phone;

    @Column(length = 1)
    private String sex;

    @DateTimeFormat(pattern = DateUtil.DATE)
    @JSONField(format = DateUtil.DATE)
    private Date birthday;

    /**
     * 身份证号
     */
    @Column(length = 30)
    private String idCardNo;

    /**
     * 籍贯
     */
    @Column(name = "native_place", length = 100)
    private String nativePlace;

    /**
     * 特长
     */
    @Column(length = 200)
    private String speciality;

    /**
     * 备注
     */
    @Column(length = 200)
    private String remark;

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
     * 开播状态
     */
    @Column(name = "live_status", length = 1, nullable = false)
    private String liveStatus;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
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

    public String getPlatform() {
	return platform;
    }

    public void setPlatform(String platform) {
	this.platform = platform;
    }

    public String getRoomNo() {
	return roomNo;
    }

    public void setRoomNo(String roomNo) {
	this.roomNo = roomNo;
    }

    public String getLiveName() {
	return liveName;
    }

    public void setLiveName(String liveName) {
	this.liveName = liveName;
    }

    public String getSex() {
	return sex;
    }

    public void setSex(String sex) {
	this.sex = sex;
    }

    public Date getBirthday() {
	return birthday;
    }

    public void setBirthday(Date birthday) {
	this.birthday = birthday;
    }

    public String getIdCardNo() {
	return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
	this.idCardNo = idCardNo;
    }

    public String getNativePlace() {
	return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
	this.nativePlace = nativePlace;
    }

    public String getSpeciality() {
	return speciality;
    }

    public void setSpeciality(String speciality) {
	this.speciality = speciality;
    }

    public String getRemark() {
	return remark;
    }

    public void setRemark(String remark) {
	this.remark = remark;
    }

    public String getLiveStatus() {
	return liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
	this.liveStatus = liveStatus;
    }

}

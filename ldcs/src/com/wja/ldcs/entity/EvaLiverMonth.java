package com.wja.ldcs.entity;

import java.math.BigDecimal;

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
public class EvaLiverMonth extends CommEntity
{
    
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
    private BigDecimal giftEarning;
    
    /**
     * 直播时长(分钟)
     */
    @Column(name = "live_duration")
    private Integer liveDuration;
    
    /**
     * 礼物收益
     */
    @Column(name = "gift_earning_goal", precision = 10, scale = 2)
    private BigDecimal giftEarningGoal;
    
    /**
     * 直播时长(分钟)
     */
    @Column(name = "live_duration_goal")
    private Integer liveDurationGoal;
    
    public String getLiverName()
    {
        return liverName;
    }
    
    public void setLiverName(String liverName)
    {
        this.liverName = liverName;
    }
    
    public String getBrokerName()
    {
        return brokerName;
    }
    
    public void setBrokerName(String brokerName)
    {
        this.brokerName = brokerName;
    }
    
    public String getLiverId()
    {
        return liverId;
    }
    
    public void setLiverId(String liverId)
    {
        this.liverId = liverId;
    }
    
    public String getBrokerId()
    {
        return brokerId;
    }
    
    public void setBrokerId(String brokerId)
    {
        this.brokerId = brokerId;
    }
    
    public Integer getMonth()
    {
        return month;
    }
    
    public void setMonth(Integer month)
    {
        this.month = month;
    }
    
    public BigDecimal getGiftEarning()
    {
        return giftEarning;
    }
    
    public void setGiftEarning(BigDecimal giftEarning)
    {
        this.giftEarning = giftEarning;
    }
    
    public Integer getLiveDuration()
    {
        return liveDuration;
    }
    
    public void setLiveDuration(Integer liveDuration)
    {
        this.liveDuration = liveDuration;
    }
    
    public BigDecimal getGiftEarningGoal()
    {
        return giftEarningGoal;
    }
    
    public void setGiftEarningGoal(BigDecimal giftEarningGoal)
    {
        this.giftEarningGoal = giftEarningGoal;
    }
    
    public Integer getLiveDurationGoal()
    {
        return liveDurationGoal;
    }
    
    public void setLiveDurationGoal(Integer liveDurationGoal)
    {
        this.liveDurationGoal = liveDurationGoal;
    }
    
}

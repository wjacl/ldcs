package com.wja.ldcs.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.wja.base.common.CommConstants;
import com.wja.base.common.CommEntity;
import com.wja.base.system.entity.User;
import com.wja.base.util.DateUtil;
import com.wja.base.util.SetValue;

@Entity
@Table(name = "t_ldcs_live_data")
@Where(clause = " valid = " + CommConstants.DATA_VALID)
public class LiveData extends CommEntity
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
     * 直播日期
     */
    @DateTimeFormat(pattern = DateUtil.DATE)
    @JSONField(format = DateUtil.DATE)
    @Column(nullable = false)
    private Date date;
    
    /**
     * 订阅数
     */
    private Integer rss;
    
    /**
     * 订阅增幅
     */
    @Column(name = "rss_grow_rate", precision = 7, scale = 4)
    private BigDecimal rssGrowRate;
    
    /**
     * 人气
     */
    private Integer popularity;
    
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
    
    @Column(length = 200)
    private String remark;
    
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
    
    public String getPlatform()
    {
        return platform;
    }
    
    public void setPlatform(String platform)
    {
        this.platform = platform;
    }
    
    public String getRoomNo()
    {
        return roomNo;
    }
    
    public void setRoomNo(String roomNo)
    {
        this.roomNo = roomNo;
    }
    
    public String getLiveName()
    {
        return liveName;
    }
    
    public void setLiveName(String liveName)
    {
        this.liveName = liveName;
    }
    
    public Date getDate()
    {
        return date;
    }
    
    public void setDate(Date date)
    {
        this.date = date;
    }
    
    public Integer getRss()
    {
        return rss;
    }
    
    public void setRss(Integer rss)
    {
        this.rss = rss;
    }
    
    public BigDecimal getRssGrowRate()
    {
        return rssGrowRate;
    }
    
    public void setRssGrowRate(BigDecimal rssGrowRate)
    {
        this.rssGrowRate = rssGrowRate;
    }
    
    public Integer getPopularity()
    {
        return popularity;
    }
    
    public void setPopularity(Integer popularity)
    {
        this.popularity = popularity;
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
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
}

package com.wja.ldcs.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.wja.base.common.CommConstants;
import com.wja.base.common.CommEntity;

@Entity
@Table(name = "t_ldcs_eva_Broker_month")
@Where(clause = " valid = " + CommConstants.DATA_VALID)
public class EvaBrokerMonth extends CommEntity
{
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
    private BigDecimal gflv;
    
    /**
     * 礼物完成率计算式
     */
    @Column(name = "gflv_text", length = 40)
    private String gflvText;
    
    /**
     * 直播时长完成率
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal dulv;
    
    /**
     * 直播时长完成率计算式
     */
    @Column(name = "dulv_text", length = 40)
    private String dulvText;
    
    /**
     * 礼物提成
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal comm;
    
    /**
     * 礼物提成计算式
     */
    @Column(name = "comm_mess", length = 400)
    private String commMess;
    
    /**
     * 部门评分
     */
    @Column(name = "grade_prop", precision = 5, scale = 2)
    private BigDecimal gradeProp;
    
    /**
     * 评分说明
     */
    @Column(length = 200)
    private String remark;
    
    /**
     * 权重得分
     */
    @Column(name = "perf_eval", precision = 5, scale = 2)
    private BigDecimal perfEval;
    
    /**
     * 权重计算式
     */
    @Column(name = "perf_eval_text", length = 400)
    private String perfEvalText;
    
    /**
     * 绩效提成
     */
    @Column(name = "perf_comm", precision = 10, scale = 2)
    private BigDecimal perfComm;
    
    public String getBrokerName()
    {
        return brokerName;
    }
    
    public void setBrokerName(String brokerName)
    {
        this.brokerName = brokerName;
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
    
    public BigDecimal getGflv()
    {
        return gflv;
    }
    
    public void setGflv(BigDecimal gflv)
    {
        this.gflv = gflv;
    }
    
    public String getGflvText()
    {
        return gflvText;
    }
    
    public void setGflvText(String gflvText)
    {
        this.gflvText = gflvText;
    }
    
    public BigDecimal getDulv()
    {
        return dulv;
    }
    
    public void setDulv(BigDecimal dulv)
    {
        this.dulv = dulv;
    }
    
    public String getDulvText()
    {
        return dulvText;
    }
    
    public void setDulvText(String dulvText)
    {
        this.dulvText = dulvText;
    }
    
    public BigDecimal getComm()
    {
        return comm;
    }
    
    public void setComm(BigDecimal comm)
    {
        this.comm = comm;
    }
    
    public String getCommMess()
    {
        return commMess;
    }
    
    public void setCommMess(String commMess)
    {
        this.commMess = commMess;
    }
    
    public BigDecimal getGradeProp()
    {
        return gradeProp;
    }
    
    public void setGradeProp(BigDecimal gradeProp)
    {
        this.gradeProp = gradeProp;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public BigDecimal getPerfEval()
    {
        return perfEval;
    }
    
    public void setPerfEval(BigDecimal perfEval)
    {
        this.perfEval = perfEval;
    }
    
    public String getPerfEvalText()
    {
        return perfEvalText;
    }
    
    public void setPerfEvalText(String perfEvalText)
    {
        this.perfEvalText = perfEvalText;
    }
    
    public BigDecimal getPerfComm()
    {
        return perfComm;
    }
    
    public void setPerfComm(BigDecimal perfComm)
    {
        this.perfComm = perfComm;
    }
}

package com.wja.ldcs.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.wja.base.common.CommConstants;
import com.wja.base.util.CollectionUtil;
import com.wja.base.util.Page;
import com.wja.base.util.Sort;
import com.wja.ldcs.entity.EvaLiverMonth;

@Repository
public class LiveDataTJDao
{
    @PersistenceContext
    private EntityManager em;
    
    public Page<EvaLiverMonth> tongJiPageQuery(Map<String, Object> params, Page<EvaLiverMonth> page)
    {
        StringBuilder sql = this.greateQuerySql(params);
        StringBuilder sqlCount = new StringBuilder();
        sqlCount.append("select count(1) from (");
        
        sqlCount.append(sql).append(") res");
        
        this.addOrderBy(sql, page.getSort(), page.getOrder());
        
        sql.append(" limit " + (page.getStartNum() - 1) + "," + page.getSize());
        
        Query cquery = em.createNativeQuery(sqlCount.toString());
        Long total = ((BigInteger)cquery.getSingleResult()).longValue();
        page.setTotal(total);
        List<EvaLiverMonth> rows = new ArrayList<>();
        page.setRows(rows);
        if (total >= page.getStartNum())
        {
            Query query = em.createNativeQuery(sql.toString());
            List<Object[]> list = query.getResultList();
            
            this.resultListToEvaLiverMonthList(list, rows);
        }
        return page;
    }
    
    public List<EvaLiverMonth> listQuery(Map<String, Object> params, Sort sortObj)
    {
        StringBuilder sql = this.greateQuerySql(params);
        this.addOrderBy(sql, sortObj.getSort(), sortObj.getOrder());
        List<EvaLiverMonth> rows = new ArrayList<>();
        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> list = query.getResultList();
        
        this.resultListToEvaLiverMonthList(list, rows);
        
        return rows;
    }
    
    private void addOrderBy(StringBuilder sql, String sort, String order)
    {
        if (StringUtils.isNotBlank(sort))
        {
            String[] ss = sort.split(",");
            String[] oss = new String[ss.length];
            if (StringUtils.isNotBlank(order))
            {
                String[] os = order.split(",");
                System.arraycopy(os, 0, oss, 0, os.length);
            }
            
            for (int i = 0; i < ss.length; i++)
            {
                if (i == 0)
                {
                    if ("month".equals(ss[i]))
                    {
                        sql.append(" order by aa.month " + (oss[i] == null ? "" : oss[i]));
                    }
                    else if ("brokerId".equals(ss[i]))
                    {
                        sql.append(" order by aa.broker_id " + (oss[i] == null ? "" : oss[i]));
                    }
                    else if ("liverId".equals(ss[i]))
                    {
                        sql.append(" order by aa.liver_id " + (oss[i] == null ? "" : oss[i]));
                    }
                }
                else
                {
                    if ("month".equals(ss[i]))
                    {
                        sql.append(" ,aa.month " + (oss[i] == null ? "" : oss[i]));
                    }
                    else if ("brokerId".equals(ss[i]))
                    {
                        sql.append(" ,aa.broker_id " + (oss[i] == null ? "" : oss[i]));
                    }
                    else if ("liverId".equals(ss[i]))
                    {
                        sql.append(" ,aa.liver_id " + (oss[i] == null ? "" : oss[i]));
                    }
                }
            }
        }
    }
    
    private StringBuilder greateQuerySql(Map<String, Object> params)
    {
        StringBuilder sql = new StringBuilder();
        sql.append(" select aa.*,b.gift_earning ggoal,b.live_duration dgoal ");
        sql.append(" from ( ");
        sql.append(" select a.broker_id,a.liver_id,DATE_FORMAT(a.date,'%Y%m') month, ");
        sql.append("    sum(a.gift_earning) gift,sum(a.live_duration) dura ");
        sql.append(" from t_ldcs_live_data a  ");
        sql.append(" where a.valid = " + CommConstants.DATA_VALID);
        
        List<String> borkerIds = (List<String>)params.get("brokerId_in_string");
        if (CollectionUtil.isNotEmpty(borkerIds))
        {
            StringBuilder stb = new StringBuilder();
            for (String s : borkerIds)
            {
                stb.append("'" + s + "',");
            }
            sql.append(" and a.broker_id in (" + stb.substring(0, stb.length() - 1) + ") ");
        }
        
        String borkerId = (String)params.get("brokerId_eq_String");
        if (StringUtils.isNotBlank(borkerId))
        {
            sql.append(" and a.broker_id = '" + borkerId + "' ");
        }
        
        String liverIds = (String)params.get("liverId_in_String");
        if (StringUtils.isNotBlank(liverIds))
        {
            sql.append(" and a.liver_id in ('" + liverIds.replace(",", "','") + "') ");
        }
        
        String monthGte = (String)params.get("month_gte_intt");
        if (StringUtils.isNotBlank(monthGte))
        {
            sql.append(" and DATE_FORMAT(a.date,'%Y%m') >= " + monthGte);
        }
        
        String monthLte = (String)params.get("month_lte_intt");
        if (StringUtils.isNotBlank(monthLte))
        {
            sql.append(" and DATE_FORMAT(a.date,'%Y%m') <= " + monthLte);
        }
        
        String month = (String)params.get("month_eq_intt");
        if (StringUtils.isNotBlank(monthLte))
        {
            sql.append(" and DATE_FORMAT(a.date,'%Y%m') = " + month);
        }
        
        sql.append(" group by a.broker_id,a.liver_id,DATE_FORMAT(a.date,'%Y%m')) aa");
        sql.append(" LEFT JOIN t_ldcs_live_goal b ");
        sql.append("    on (aa.liver_id = b.liver_id and aa.month = b.month ");
        sql.append("   and b.valid = " + CommConstants.DATA_VALID + ") ");
        
        return sql;
    }
    
    private void resultListToEvaLiverMonthList(List<Object[]> list, List<EvaLiverMonth> rows)
    {
        if (CollectionUtil.isEmpty(list))
        {
            return;
        }
        
        EvaLiverMonth eva = null;
        int i;
        for (Object[] os : list)
        {
            eva = new EvaLiverMonth();
            rows.add(eva);
            i = 0;
            eva.setBrokerId((String)os[i++]);
            eva.setLiverId((String)os[i++]);
            eva.setMonth(os[i] == null ? null : Integer.valueOf((String)os[i]));
            i++;
            eva.setGiftEarning((BigDecimal)os[i++]);
            eva.setLiveDuration(os[i] == null ? null : ((BigDecimal)os[i]).intValue());
            i++;
            eva.setGiftEarningGoal((BigDecimal)os[i++]);
            eva.setLiveDurationGoal((Integer)os[i]);
        }
    }
}

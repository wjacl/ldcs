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
import com.wja.base.util.Page;
import com.wja.ldcs.entity.EvaLiverMonth;

@Repository
public class LiveDataTJDao {
    @PersistenceContext
    private EntityManager em;

    public Page<EvaLiverMonth> tongJiPageQuery(Map<String, Object> params, Page<EvaLiverMonth> page) {
	StringBuilder sql = new StringBuilder();
	StringBuilder sqlCount = new StringBuilder();
	sqlCount.append("select count(1) from (");
	sql.append(" select a.broker_id,a.liver_id,DATE_FORMAT(a.date,'%Y%m'), ");
	sql.append("    sum(a.gift_earning),sum(a.live_duration), ");
	sql.append("    b.gift_earning ggoal,b.live_duration dgoal ");
	sql.append(" from t_ldcs_live_data a  ");
	sql.append(" LEFT JOIN t_ldcs_live_goal b ");
	sql.append("    on (DATE_FORMAT(a.date,'%Y%m') = b.month) ");
	sql.append(" where a.valid = " + CommConstants.DATA_VALID);
	sql.append("   and b.valid = " + CommConstants.DATA_VALID);

	String liverIds = (String) params.get("liverId_in_String");
	if (StringUtils.isNotBlank(liverIds)) {
	    sql.append(" and a.liver_id in ('" + liverIds.replace(",", "','") + "') ");
	}

	String monthGte = (String) params.get("month_gte_intt");
	if (StringUtils.isNotBlank(monthGte)) {
	    sql.append(" and DATE_FORMAT(a.date,'%Y%m') >= " + monthGte);
	}

	String monthLte = (String) params.get("month_lte_intt");
	if (StringUtils.isNotBlank(monthLte)) {
	    sql.append(" and DATE_FORMAT(a.date,'%Y%m') <= " + monthLte);
	}
	sql.append(" group by a.broker_id,a.liver_id,DATE_FORMAT(a.date,'%Y%m')");

	sqlCount.append(sql).append(") res");

	String sort = page.getSort();
	if (StringUtils.isNotBlank(sort)) {
	    String[] ss = sort.split(",");
	    String[] oss = new String[ss.length];
	    String order = page.getOrder();
	    if (StringUtils.isNotBlank(order)) {
		String[] os = order.split(",");
		System.arraycopy(os, 0, oss, 0, os.length);
	    }

	    for (int i = 0; i < ss.length; i++) {
		if (i == 0) {
		    if ("month".equals(ss[i])) {
			sql.append(" order by DATE_FORMAT(a.date,'%Y%m') " + (oss[i] == null ? "" : oss[i]));
		    } else if ("brokerId".equals(ss[i])) {
			sql.append(" order by broker_id " + (oss[i] == null ? "" : oss[i]));
		    } else if ("liverId".equals(ss[i])) {
			sql.append(" order by liver_id " + (oss[i] == null ? "" : oss[i]));
		    }
		} else {
		    if ("month".equals(ss[i])) {
			sql.append(" ,DATE_FORMAT(a.date,'%Y%m') " + (oss[i] == null ? "" : oss[i]));
		    } else if ("brokerId".equals(ss[i])) {
			sql.append(" ,broker_id " + (oss[i] == null ? "" : oss[i]));
		    } else if ("liverId".equals(ss[i])) {
			sql.append(" ,liver_id " + (oss[i] == null ? "" : oss[i]));
		    }
		}
	    }
	}
	sql.append(" limit " + (page.getStartNum() - 1) + "," + page.getSize());

	Query cquery = em.createNativeQuery(sqlCount.toString());
	Long total = ((BigInteger) cquery.getSingleResult()).longValue();
	page.setTotal(total);

	if (total >= page.getStartNum()) {
	    Query query = em.createNativeQuery(sql.toString());
	    List<Object[]> list = query.getResultList();
	    List<EvaLiverMonth> rows = new ArrayList<>();
	    EvaLiverMonth eva = null;
	    int i;
	    for (Object[] os : list) {
		eva = new EvaLiverMonth();
		rows.add(eva);
		i = 0;
		eva.setLiverId((String) os[i++]);
		eva.setBrokerId((String) os[i++]);
		eva.setMonth(os[i] == null ? null : Integer.valueOf((String) os[i]));
		i++;
		eva.setGiftEarning((BigDecimal) os[i++]);
		eva.setLiveDuration(os[i] == null ? null : ((BigDecimal) os[i]).intValue());
		i++;
		eva.setGiftEarningGoal((BigDecimal) os[i++]);
		eva.setLiveDurationGoal((Integer) os[i]);
	    }
	    page.setRows(rows);
	}
	return page;
    }
}

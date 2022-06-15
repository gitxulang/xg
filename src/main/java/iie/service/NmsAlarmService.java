package iie.service;

import iie.pojo.*;
import iie.tools.*;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class NmsAlarmService {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public boolean deleteAlarm(List<String> list) {
		boolean res = true;
		String sql = "UPDATE nms_alarm SET d_status = 4 WHERE id = "
				+ list.get(0);
		if (list != null && list.size() > 0) {
			for (int i = 1; i < list.size(); i++) {
				sql += " OR id = " + list.get(i);
			}
		}

		Session session = hibernateTemplate.getSessionFactory().openSession();
		Transaction tran = session.beginTransaction();
		try {
			int num = session.createSQLQuery(sql).executeUpdate();
		} catch (Exception e) {
			res = false;
		}
		tran.commit();
		session.close();
		return res;
	}

	public boolean updateAlarmDeal(Integer id, String dPeople) {
		DetachedCriteria criteria = DetachedCriteria.forClass(NmsAlarm.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("DStatus", 0));
		criteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
		List<NmsAlarm> nmsAlarmList = (List<NmsAlarm>) hibernateTemplate
				.findByCriteria(criteria);
		if (nmsAlarmList == null || nmsAlarmList.size() == 0) {
			return false;
		}
		try {
			for (NmsAlarm nmsAlarm : nmsAlarmList) {
				nmsAlarm.setDPeople(dPeople);
				nmsAlarm.setDStatus(1);
				nmsAlarm.setDTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date()));
				hibernateTemplate.update(nmsAlarm);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean addAlarmReportAndUpdateAlarm(Integer id, String rPeople,
			String rContent, String dTime) {
		DetachedCriteria criteria = DetachedCriteria.forClass(NmsAlarm.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("DStatus", 1));
		criteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
		List<NmsAlarm> nmsAlarmList = (List<NmsAlarm>) hibernateTemplate
				.findByCriteria(criteria);
		if (nmsAlarmList == null || nmsAlarmList.size() == 0) {
			return false;
		}
		try {
			for (NmsAlarm nmsAlarm : nmsAlarmList) {
				nmsAlarm.setId(id);
				nmsAlarm.setDStatus(2);
				nmsAlarm.setDPeople(rPeople);
				nmsAlarm.setDTime(dTime);
				hibernateTemplate.update(nmsAlarm);
				NmsAlarmReport nmsAlarmReport = new NmsAlarmReport();
				nmsAlarmReport.setNmsAlarm(nmsAlarm);
				nmsAlarmReport.setDTime(dTime);
				nmsAlarmReport.setRContent(rContent);
				nmsAlarmReport.setRPeople(rPeople);
				nmsAlarmReport.setItime(nmsAlarm.getItime());
				nmsAlarmReport.setRTime(new Timestamp(System
						.currentTimeMillis()));
				hibernateTemplate.save(nmsAlarmReport);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public PageBean<NmsAlarmInfoDetail> listAlarmPageByCondition(
			String orderKey, int orderValue, Integer ALevel,
			Integer nmsAssetId, String AIp, Integer DStatus, Short MStatus, String AContent,
			String AName, String startDate, String endDate, int begin,
			int offset) {

		DetachedCriteria criteria = DetachedCriteria.forClass(NmsAlarm.class);
		criteria.add(Restrictions.ne("DStatus", 4));
		
		if (ALevel != null) {
			criteria.add(Restrictions.eq("ALevel", ALevel));
		}
		criteria.createAlias("nmsAsset", "nmsAsset");
		if (nmsAssetId != null) {
			criteria.add(Restrictions.eq("nmsAsset.id", nmsAssetId));
		}
		if (AIp != null && AIp.length() > 0) {
			criteria.add(Restrictions.like("nmsAsset.AIp", "%" + AIp + "%"));
		}
		if (MStatus != null) {
			criteria.add(Restrictions.eq("nmsAsset.colled", MStatus));
		}		
		if (AContent != null && AContent.length() > 0) {
			criteria.add(Restrictions.like("AContent", "%" + AContent + "%"));
		}
		if (AName != null && AName.length() > 0) {
			criteria.add(Restrictions.like("nmsAsset.AName", "%" + AName + "%"));
		}
		if (DStatus != null) {
			criteria.add(Restrictions.eq("DStatus", DStatus));
		}
		if (startDate != null && startDate.length() > 0) {
			criteria.add(Restrictions.gt("ATime", startDate));
		}
		if (endDate != null && endDate.length() > 0) {
			criteria.add(Restrictions.lt("ATime", endDate));
		}
		
		if (orderKey == null || orderKey.equals("")) { 
			criteria.addOrder(Order.desc("itime"));
		} else {
			if (orderValue == 0) {
				criteria.addOrder(Order.desc(orderKey));
			} else {
				criteria.addOrder(Order.asc(orderKey));
			}
		}

		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) hibernateTemplate.findByCriteria(criteria)
				.get(0);

		criteria.setProjection(null);
		criteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
		List<NmsAlarm> nmsAlarmList = (List<NmsAlarm>) hibernateTemplate
				.findByCriteria(criteria, (begin - 1) * offset, offset);

		List<NmsAlarmInfoDetail> nmsAlarmInfoDetailList = new ArrayList<NmsAlarmInfoDetail>(
				0);
		if (nmsAlarmList != null && nmsAlarmList.size() > 0) {
			for (int i = 0; i < nmsAlarmList.size(); i++) {
				NmsAlarm nmsAlarm = nmsAlarmList.get(i);
				int dStatus = nmsAlarm.getDStatus();
				Integer alarmId = nmsAlarm.getId();
				/* Map<String, Object> map = queryAlarmRule(alarmId); */

				NmsAlarmInfoDetail nmsAlarmInfoDetail = new NmsAlarmInfoDetail();
				nmsAlarmInfoDetail.setNmsAlarm(nmsAlarm);
				/*
				 * nmsAlarmInfoDetail.setRuleId((Integer) map.get("ruleId"));
				 * nmsAlarmInfoDetail.setRuleTableName((String) map
				 * .get("ruleTableName"));
				 */
				String action;
				if (dStatus == 0) {
					action = "接受处理";
				} else if (dStatus == 1) {
					action = "填写报告";
				} else if (dStatus == 2) {
					action = "查看报告";
				} else if (dStatus == 3) {
					action = "无需处理";
				} else if (dStatus == 4) {
					action = "无需处理";
				} else {
					if (alarmReportExists(nmsAlarm)) {
						action = "查看报告";
					} else {
						action = "填写报告";
					}
				}
				nmsAlarmInfoDetail.setAction(action);
				nmsAlarmInfoDetailList.add(nmsAlarmInfoDetail);
			}
		}

		int totalPage = 1;
		if (totalCount == 0) {
			totalPage = totalCount.intValue() / offset + 1;
		} else if (totalCount % offset == 0) {
			totalPage = totalCount.intValue() / offset;
		} else {
			totalPage = totalCount.intValue() / offset + 1;
		}

		PageBean<NmsAlarmInfoDetail> page = new PageBean<NmsAlarmInfoDetail>();

		page.setOrderKey(orderKey);
		page.setOrderValue(orderValue);
		page.setTotalCount(totalCount.intValue());
		page.setPage(begin);
		page.setTotalPage(totalPage);
		page.setList(nmsAlarmInfoDetailList);
		return page;
	}

	public List<NmsAlarmInfoDetail> listAlarmPageByConditionExportExcel(
			String orderKey, int orderValue, Integer ALevel,
			Integer nmsAssetId, String AIp, Integer DStatus, String AContent,
			String AName, String startDate, String endDate) {

		DetachedCriteria criteria = DetachedCriteria.forClass(NmsAlarm.class);
		criteria.add(Restrictions.lt("DStatus", 3));
		
		if (ALevel != null) {
			criteria.add(Restrictions.eq("ALevel", ALevel));
		}
		criteria.createAlias("nmsAsset", "nmsAsset");
		if (nmsAssetId != null) {
			criteria.add(Restrictions.eq("nmsAsset.id", nmsAssetId));
		}
		if (AIp != null && AIp.length() > 0) {
			criteria.add(Restrictions.eq("nmsAsset.AIp", AIp));
		}
		if (AContent != null && AContent.length() > 0) {
			criteria.add(Restrictions.like("AContent", "%" + AContent + "%"));
		}
		if (AName != null && AName.length() > 0) {
			criteria.add(Restrictions.like("AName", "%" + AName + "%"));
		}
		if (DStatus != null) {
			criteria.add(Restrictions.gt("DStatus", DStatus));
		}
		if (startDate != null && startDate.length() > 0) {
			criteria.add(Restrictions.gt("ATime", startDate));
		}
		if (endDate != null && endDate.length() > 0) {
			criteria.add(Restrictions.lt("ATime", endDate));
		}
		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "ATime";
		}
		if (orderValue == 0) {
			criteria.addOrder(Order.desc(orderKey));
		} else {
			criteria.addOrder(Order.asc(orderKey));
		}
		criteria.addOrder(Order.desc("DStatus"));

		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) hibernateTemplate.findByCriteria(criteria).get(0);

		criteria.setProjection(null);
		criteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
		List<NmsAlarm> nmsAlarmList = (List<NmsAlarm>) hibernateTemplate.findByCriteria(criteria);

		List<NmsAlarmInfoDetail> nmsAlarmInfoDetailList = new ArrayList<NmsAlarmInfoDetail>(0);
		if (nmsAlarmList != null && nmsAlarmList.size() > 0) {
			for (int i = 0; i < nmsAlarmList.size(); i++) {
				NmsAlarm nmsAlarm = nmsAlarmList.get(i);
				int dStatus = nmsAlarm.getDStatus();
				Integer alarmId = nmsAlarm.getId();
				NmsAlarmInfoDetail nmsAlarmInfoDetail = new NmsAlarmInfoDetail();
				nmsAlarmInfoDetail.setNmsAlarm(nmsAlarm);
				nmsAlarmInfoDetailList.add(nmsAlarmInfoDetail);
			}
		}
		return nmsAlarmInfoDetailList;
	}

	public Map<String, Object> queryAlarmRule(Integer nmsAlarmId) {

		Map<String, Object> data = new HashMap<String, Object>();
		NmsAlarm nmsAlarm = new NmsAlarm();
		nmsAlarm.setId(nmsAlarmId);

		nmsAlarm = (NmsAlarm) hibernateTemplate.findByExample(nmsAlarm).get(0);

		NmsAsset nmsAsset = nmsAlarm.getNmsAsset();
		String RName = nmsAlarm.getAName();

		String AIndex = nmsAlarm.getAIndex();
		if (AIndex != null && AIndex.length() > 0) {
			NmsRuleAssetIndex nmsRuleAssetIndex = new NmsRuleAssetIndex();
			nmsRuleAssetIndex.setIndexId(AIndex);
			nmsRuleAssetIndex.setNmsAsset(nmsAsset);
			nmsRuleAssetIndex.setRName(RName);
			List<NmsRuleAssetIndex> nmsRuleAssetIndexList = (List<NmsRuleAssetIndex>) hibernateTemplate
					.findByExample(nmsRuleAssetIndex);
			if (nmsRuleAssetIndexList != null
					&& nmsRuleAssetIndexList.size() > 0) {
				data.put("ruleTableName", "nms_rule_asset_index");
				data.put("ruleId", nmsRuleAssetIndexList.get(0).getId());
				return data;
			}
		}
		NmsRuleAsset nmsRuleAsset = new NmsRuleAsset();
		nmsRuleAsset.setNmsAsset(nmsAsset);
		nmsRuleAsset.setRName(RName);
		List<NmsRuleAsset> nmsRuleAssetList = (List<NmsRuleAsset>) hibernateTemplate
				.findByExample(nmsRuleAsset);
		if (nmsRuleAssetList != null && nmsRuleAssetList.size() > 0) {
			data.put("ruleTableName", nmsRuleAsset);
			data.put("ruleId", nmsRuleAssetList.get(0).getId());
			return data;
		}

		NmsRule nmsRule = new NmsRule();
		nmsRule.setNmsAssetType(nmsAsset.getNmsAssetType());
		nmsRule.setRName(RName);
		List<NmsRule> nmsRuleList = (List<NmsRule>) hibernateTemplate
				.findByExample(nmsRule);
		if (nmsRuleList != null && nmsRuleList.size() > 0) {
			data.put("ruleTableName", "nms_rule");
			data.put("ruleId", nmsRuleList.get(0).getId());
		}
		return data;
	}

	@SuppressWarnings("rawtypes")
	public PageBean<NmsAlarmReport> listAlarmReportByCondition(String orderKey,
			int orderValue, Integer alarmId, Integer alarmReportId,
			String rPeople, String dTimeStart, String dTimeEnd,
			String rTimeStart, String rTimeEnd, int begin, int offset) {

		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(NmsAlarmReport.class);
		if (alarmId != null) {
			detachedCriteria.createAlias("nmsAlarm", "nmsAlarm");
			detachedCriteria.add(Restrictions.eq("nmsAlarm.id", alarmId));
		}
		if (alarmReportId != null) {
			detachedCriteria.add(Restrictions.eq("id", alarmReportId));
		}
		if (rPeople != null && rPeople.length() > 0) {
			detachedCriteria.add(Restrictions.eq("RPeople", rPeople));
		}
		if (dTimeStart != null && dTimeStart.length() > 0) {
			detachedCriteria.add(Restrictions.ge("DTime", dTimeStart));
		}
		if (dTimeEnd != null && dTimeEnd.length() > 0) {
			detachedCriteria.add(Restrictions.lt("DTime", dTimeEnd));
		}
		if (rTimeStart != null && rTimeStart.length() > 0) {
			detachedCriteria.add(Restrictions.ge("RTime",
					Timestamp.valueOf(rTimeStart)));
		}
		if (rTimeEnd != null && rTimeEnd.length() > 0) {
			detachedCriteria.add(Restrictions.lt("RTime",
					Timestamp.valueOf(rTimeEnd)));

		}

		// 排序条件限制（可选）
		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "RTime";
		}
		if (orderValue == 0) {
			detachedCriteria.addOrder(Order.asc(orderKey));
		} else {
			detachedCriteria.addOrder(Order.desc(orderKey));
		}
		detachedCriteria.addOrder(Order.desc("id"));

		detachedCriteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) hibernateTemplate.findByCriteria(
				detachedCriteria).get(0);

		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
		List list = hibernateTemplate.findByCriteria(detachedCriteria,
				(begin - 1) * offset, offset);

		int totalPage = 1;
		if (totalCount == 0) {
			totalPage = totalCount.intValue() / offset + 1;
		} else if (totalCount % offset == 0) {
			totalPage = totalCount.intValue() / offset;
		} else {
			totalPage = totalCount.intValue() / offset + 1;
		}

		// 创建PageBean对象返回数据
		PageBean<NmsAlarmReport> page = new PageBean<NmsAlarmReport>();
		page.setOrderKey(orderKey);
		page.setOrderValue(orderValue);
		page.setTotalCount(totalCount.intValue());
		page.setPage(begin);
		page.setTotalPage(totalPage);
		page.setList(list);
		return page;
	}

	@SuppressWarnings("unchecked")
	public NmsAlarmReport findByAlarmId(Integer alarmId) {
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(NmsAlarmReport.class);
		if (alarmId != null) {
			detachedCriteria.createAlias("nmsAlarm", "nmsAlarm");
			detachedCriteria.add(Restrictions.eq("nmsAlarm.id", alarmId));
		}
		detachedCriteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
		
		List<NmsAlarmReport> nmsAlarmReports = new ArrayList<NmsAlarmReport>();
		
		nmsAlarmReports = (List<NmsAlarmReport>) hibernateTemplate
				.findByCriteria(detachedCriteria, 0, 1);
		
		if (nmsAlarmReports.size() > 0) {
			return nmsAlarmReports.get(0);
		} 
		return null;
	}

	public PageBean<NmsAlarmStaticsDetail> staticsNmsAssetAlarm(
			String nmsAssetName, String nmsAssetIp, Integer nmsAssetId,
			String nmsAssetTypeName, Integer nmsAssetTypeId,
			Integer alarmLevel, String alarmStartDate, String alarmEndDate,
			int begin, int offset, String orderKey, String orderValue) {

		String firstSql = "select a.id,a.a_name,"
				+ "a.a_ip,b.totalCount,"
				+ "b.undealTotalCount,b.dealIngTotalCount,"
				+ "b.dealedTotalCount,b.autoTotalCount,b.deletedTotalCount,"
				+ "k.undealTotalCountLevelOne,k.dealingTotalCountLevelOne,k.dealedTotalCountLevelOne,"
				+ "k.autoTotalCountLevelOne,k.deletedTotalCountLevelOne,"
				+ "k.undealTotalCountLevelTwo,k.dealingTotalCountLevelTwo,k.dealedCountLevelTwo,k.autoTotalCountLevelTwo,"
				+ "k.deletedCountLevelTwo,"
				+ "k.undealTotalCountLevelThree,k.dealingTotalCountLevelThree,k.dealedCountLevelThree,"
				+ "k.autoTotalCountLevelThree,k.deletedCountLevelThree,t.ch_type,t.ch_subtype "
				+ "from nms_asset a "
				+ "left join nms_asset_type t on a.type_id= t.id "
				+ "left join ( ";
		String firstSqlCount = "select count(*) " + "from nms_asset a "
				+ "left join nms_asset_type t on a.type_id= t.id "
				+ "left join ( ";
		String secondSql = "select asset_id,count(id) totalCount," + "sum("
				+ "case when d_status = 0 then 1 " + "else 0 end"
				+ ")  as undealTotalCount, " + "sum("
				+ "case when d_status = 1 then 1 " + "else 0 end"
				+ ") as dealIngTotalCount," + "sum("
				+ "case when d_status = 2 then 1 " + "else 0 end"
				+ ") as dealedTotalCount, " + "sum("
				+ "case when d_status = 3 then 1 " + "else 0 end"
				+ ") as autoTotalCount, " + "sum("
				+ "case when d_status = 4 then 1 " + "else 0 end "
				+ ") as deletedTotalCount " + "from nms_alarm " + " where 1=1 ";

		String threeSql = " group by asset_id ) b on a.id = b.asset_id "
				+ " left join ( ";
		String fourSql = "select asset_id," + "sum("
				+ "case when d_status = 0  and a_level=1 then 1 "
				+ "else 0 end" + ") as undealTotalCountLevelOne," + "sum("
				+ "case when d_status = 1 and a_level=1 then 1 " + "else 0 end"
				+ ") as dealingTotalCountLevelOne," + "sum("
				+ "case when d_status = 2 and a_level=1 then 1 " + "else 0 end"
				+ ") as dealedTotalCountLevelOne," + "sum("
				+ "case when d_status = 3 and a_level=1 then 1 " + "else 0 end"
				+ ") as autoTotalCountLevelOne," + "sum("
				+ "case when d_status = 4 and a_level=1 then 1 " + "else 0 end"
				+ ") as deletedTotalCountLevelOne," + "sum("
				+ "case when d_status = 0 and a_level=2 then 1 " + "else 0 end"
				+ ") as undealTotalCountLevelTwo," + "sum("
				+ "case when d_status = 1 and a_level=2 then 1 " + "else 0 end"
				+ ")  as dealingTotalCountLevelTwo," + "sum("
				+ "case when d_status = 2 and a_level=2 then 1 " + "else 0 end"
				+ ") as dealedCountLevelTwo," + "sum("
				+ "case when d_status = 3 and a_level=2 then 1 " + "else 0 end"
				+ ") as autoTotalCountLevelTwo," + "sum("
				+ "case when d_status = 4 and a_level=2 then 1 " + "else 0 end"
				+ ") as deletedCountLevelTwo," + "sum("
				+ "case when d_status = 0 and a_level=3 then 1 " + "else 0 end"
				+ ") as undealTotalCountLevelThree," + "sum("
				+ "case when d_status = 1 and a_level=3 then 1 " + "else 0 end"
				+ ")  as dealingTotalCountLevelThree," + "sum("
				+ "case when d_status = 2 and a_level=3 then 1 " + "else 0 end"
				+ ") as dealedCountLevelThree," + "sum("
				+ "case when d_status = 3 and a_level=3 then 1 " + "else 0 end"
				+ ") as autoTotalCountLevelThree," + "sum("
				+ "case when d_status = 4 and a_level=3 then 1 " + "else 0 end"
				+ ") as deletedCountLevelThree " + " from nms_alarm "
				+ " where 1=1 ";
		String endSql = " group by asset_id " + ") k on k.asset_id = a.id "
				+ "where 1=1 and a.deled=0 ";
		if (nmsAssetName != null && nmsAssetName.length() > 0) {
			endSql = endSql + "and a.a_name like '%" + nmsAssetName + "%' ";
		}
		if (nmsAssetIp != null && nmsAssetIp.length() > 0) {
			endSql = endSql + "and a.a_ip like'%" + nmsAssetIp + "%' ";
		}
		if (nmsAssetId != null) {
			endSql = endSql + "and a.id =" + nmsAssetId + " ";
		}
		if (nmsAssetTypeName != null && nmsAssetTypeName.length() > 0) {
			endSql = endSql + " and t.ch_type like '%" + nmsAssetTypeName
					+ "%' ";
		}
		if (nmsAssetTypeId != null) {
			endSql = endSql + " and t.id =" + nmsAssetTypeId + " ";
		}
		if (alarmLevel != null) {
			secondSql = secondSql + "and  a_level = " + alarmLevel + " ";
			fourSql = fourSql + " and a_level = " + alarmLevel + " ";
		}
		if (alarmStartDate != null && alarmStartDate.length() > 0) {
			secondSql = secondSql + " and a_time >= '" + alarmStartDate + "' ";
			fourSql = fourSql + " and a_time >= '" + alarmStartDate + "' ";
		}
		if (alarmEndDate != null && alarmEndDate.length() > 0) {
			secondSql = secondSql + " and a_time <= '" + alarmEndDate + "' ";
			fourSql = fourSql + " and a_time <= '" + alarmEndDate + "' ";
		}

		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "id";
		}
		if (orderValue == null || orderValue.length() == 0
				|| orderValue.equals("1")) {
			orderValue = "1";
			orderKey = orderKey + " desc";
		} else {
			orderKey = orderKey + " asc";
		}		
		
		String endSqlLimit = " order by " + orderKey + " limit " + (begin - 1) * offset + "," + offset;
		String sql = firstSql + secondSql + threeSql + fourSql + endSql + endSqlLimit;
		String sqlcount = firstSqlCount + secondSql + threeSql + fourSql + endSql;
		
		List list = null;
		Integer count = 0;
		
		try {
			Session session = hibernateTemplate.getSessionFactory().openSession();
			SQLQuery sqlQuery = session.createSQLQuery(sql);
			list = sqlQuery.list();

			SQLQuery sqlQueryCount = session.createSQLQuery(sqlcount);
			count = Integer.valueOf(sqlQueryCount.list().get(0).toString());
			session.close();
		} catch (Exception e) {
			e.toString();
		}

		List<NmsAlarmStaticsDetail> result = new ArrayList<NmsAlarmStaticsDetail>(0);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objects = (Object[]) list.get(i);
				NmsAlarmStaticsDetail nmsAlarmStaticsDetail = new NmsAlarmStaticsDetail();
				NmsAlarmLevelStaticsDetail levelOne = new NmsAlarmLevelStaticsDetail();
				NmsAlarmLevelStaticsDetail levelTwo = new NmsAlarmLevelStaticsDetail();
				NmsAlarmLevelStaticsDetail levelThree = new NmsAlarmLevelStaticsDetail();
				nmsAlarmStaticsDetail.setNmsAssetId((Integer) objects[0]);
				nmsAlarmStaticsDetail.setNmsAssetName((String) objects[1]);
				nmsAlarmStaticsDetail.setNmsAssetIp((String) objects[2]);
				if (objects[3] == null) {
					nmsAlarmStaticsDetail.setAlarmTotalCount(0L);
				} else {
					nmsAlarmStaticsDetail.setAlarmTotalCount(Long
							.valueOf(objects[3].toString()));
				}
				if (objects[4] == null) {
					nmsAlarmStaticsDetail.setUnDealTotalCount(0L);
				} else {
					nmsAlarmStaticsDetail.setUnDealTotalCount(Long
							.valueOf(objects[4].toString()));
				}
				if (objects[5] == null) {
					nmsAlarmStaticsDetail.setDealingTotalCount(0L);
				} else {
					nmsAlarmStaticsDetail.setDealingTotalCount(Long
							.valueOf(objects[5].toString()));
				}
				if (objects[6] == null) {
					nmsAlarmStaticsDetail.setDealedTotalCount(0L);
				} else {
					nmsAlarmStaticsDetail.setDealedTotalCount(Long
							.valueOf(objects[6].toString()));
				}
				if (objects[7] == null) {
					nmsAlarmStaticsDetail.setAutoRecoverTotalCount(0L);
				} else {
					nmsAlarmStaticsDetail.setAutoRecoverTotalCount(Long
							.valueOf(objects[7].toString()));
				}
				if (objects[8] == null) {
					nmsAlarmStaticsDetail.setDeleteTotalCount(0L);
				} else {
					nmsAlarmStaticsDetail.setDeleteTotalCount(Long
							.valueOf(objects[8].toString()));
				}

				if (objects[9] == null) {
					levelOne.setUnDealTotalCount(0L);
				} else {
					levelOne.setUnDealTotalCount(Long.valueOf(objects[9]
							.toString()));
				}
				if (objects[10] == null) {
					levelOne.setDealingTotalCount(0L);
				} else {
					levelOne.setDealingTotalCount(Long.valueOf(objects[10]
							.toString()));
				}
				if (objects[11] == null) {
					levelOne.setDealedTotalCount(0L);
				} else {
					levelOne.setDealedTotalCount(Long.valueOf(objects[11]
							.toString()));
				}
				if (objects[12] == null) {
					levelOne.setAutoRecoverTotalCount(0L);
				} else {
					levelOne.setAutoRecoverTotalCount(Long.valueOf(objects[12]
							.toString()));
				}
				if (objects[13] == null) {
					levelOne.setDeleteTotalCount(0L);
				} else {
					levelOne.setDeleteTotalCount(Long.valueOf(objects[13]
							.toString()));
				}

				if (objects[14] == null) {
					levelTwo.setUnDealTotalCount(0L);
				} else {
					levelTwo.setUnDealTotalCount(Long.valueOf(objects[14]
							.toString()));
				}
				if (objects[15] == null) {
					levelTwo.setDealingTotalCount(0L);
				} else {
					levelTwo.setDealingTotalCount(Long.valueOf(objects[15]
							.toString()));
				}
				if (objects[16] == null) {
					levelTwo.setDealedTotalCount(0L);
				} else {
					levelTwo.setDealedTotalCount(Long.valueOf(objects[16]
							.toString()));
				}
				if (objects[17] == null) {
					levelTwo.setAutoRecoverTotalCount(0L);
				} else {
					levelTwo.setAutoRecoverTotalCount(Long.valueOf(objects[17]
							.toString()));
				}
				if (objects[18] == null) {
					levelTwo.setDeleteTotalCount(0L);
				} else {
					levelTwo.setDeleteTotalCount(Long.valueOf(objects[18]
							.toString()));
				}

				if (objects[19] == null) {
					levelThree.setUnDealTotalCount(0L);
				} else {
					levelThree.setUnDealTotalCount(Long.valueOf(objects[19]
							.toString()));
				}
				if (objects[20] == null) {
					levelThree.setDealingTotalCount(0L);
				} else {
					levelThree.setDealingTotalCount(Long.valueOf(objects[20]
							.toString()));
				}
				if (objects[21] == null) {
					levelThree.setDealedTotalCount(0L);
				} else {
					levelThree.setDealedTotalCount(Long.valueOf(objects[21]
							.toString()));
				}
				if (objects[22] == null) {
					levelThree.setAutoRecoverTotalCount(0L);
				} else {
					levelThree.setAutoRecoverTotalCount(Long
							.valueOf(objects[22].toString()));
				}
				if (objects[23] == null) {
					levelThree.setDeleteTotalCount(0L);
				} else {
					levelThree.setDeleteTotalCount(Long.valueOf(objects[23]
							.toString()));
				}
				
				// 重置告警总数=待处理+处理中+已处理
				nmsAlarmStaticsDetail.setAlarmTotalCount(nmsAlarmStaticsDetail
						.getUnDealTotalCount()
						+ nmsAlarmStaticsDetail.getDealingTotalCount()
						+ nmsAlarmStaticsDetail.getDealedTotalCount());		
				
				nmsAlarmStaticsDetail.setNmsAssetType((String) objects[24]);
				nmsAlarmStaticsDetail.setNmsAssetSubType((String) objects[25]);
				nmsAlarmStaticsDetail.setLevelOne(levelOne);
				nmsAlarmStaticsDetail.setLevelTwo(levelTwo);
				nmsAlarmStaticsDetail.setLevelThree(levelThree);
				result.add(nmsAlarmStaticsDetail);
			}
		}
		
		// 创建PageBean对象返回数据
		PageBean<NmsAlarmStaticsDetail> page = new PageBean<NmsAlarmStaticsDetail>();
		page.setTotalCount(count);
		page.setPage(begin);
		page.setLimit(offset);
		if (count == 0) {
			page.setTotalPage(count / offset + 1);
		} else if (count % offset == 0) {
			page.setTotalPage(count / offset);
		} else {
			page.setTotalPage(count / offset + 1);
		}
		page.setList(result);
		return page;
	}

	public Map<String, List<NmsAlarmDeptDetail>> staticsDeptNmsAlarm() {
		String sql = "select d.d_name,at.ch_type,at.asset_total_count,am.alarm_total_count from nms_department d "
				+ "left join  "
				+ "( "
				+ "select a.dept_id as dept_id,t.ch_type as ch_type,count(a.id) as asset_total_count from nms_asset a "
				+ "left join nms_asset_type  t on a.type_id = t.id "
				+ "group by a.dept_id,t.ch_type "
				+ ") at on at.dept_id = d.id "
				+ "left join ( "
				+ "select a.dept_id as dept_id,t.ch_type as ch_type,count(lm.id) as alarm_total_count from nms_alarm lm "
				+ "left join nms_asset a on lm.asset_id = a.id "
				+ "left join nms_asset_type t on a.type_id = t.id "
				+ "group by a.dept_id,t.ch_type "
				+ ") am on d.id = am.dept_id and at.ch_type = am.ch_type";

		Map<String, List<NmsAlarmDeptDetail>> result = new HashMap<String, List<NmsAlarmDeptDetail>>();
		
		
		Session session = hibernateTemplate.getSessionFactory().openSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		session.close();
		
		
		if (list == null || list.size() == 0) {
			return result;
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objects = (Object[]) list.get(i);
				String DName = (String) objects[0];
				List<NmsAlarmDeptDetail> nmsAlarmDeptDetailList = result
						.get(DName);
				if (nmsAlarmDeptDetailList == null) {
					nmsAlarmDeptDetailList = new ArrayList<NmsAlarmDeptDetail>();
				}

				NmsAlarmDeptDetail nmsAlarmDeptDetail = new NmsAlarmDeptDetail();
				nmsAlarmDeptDetail.setChType((String) objects[1]);
				if (objects[2] == null) {
					nmsAlarmDeptDetail.setAssetTotalCount(0L);
				} else {
					nmsAlarmDeptDetail.setAssetTotalCount(Long
							.valueOf(objects[2].toString()));
				}
				if (objects[3] == null) {
					nmsAlarmDeptDetail.setAlarmTotalCount(0L);
				} else {
					nmsAlarmDeptDetail.setAlarmTotalCount(Long
							.valueOf(objects[2].toString()));
				}
				nmsAlarmDeptDetailList.add(nmsAlarmDeptDetail);
				result.put(DName, nmsAlarmDeptDetailList);
			}
		}
		

		return result;
	}

	@SuppressWarnings("unchecked")
	public NmsAlarm findById(int id) {
		String hsql = "from NmsAlarm na where na.id = ?";
		List<NmsAlarm> list = (List<NmsAlarm>) hibernateTemplate.find(hsql, id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new NmsAlarm();
	}

	@SuppressWarnings("unchecked")
	public NmsAlarmReport findByAlarmId(NmsAlarm nmsAlarm) {
		String sql = "from NmsAlarmReport where nmsAlarm = ?";
		List<NmsAlarmReport> list = (List<NmsAlarmReport>) hibernateTemplate
				.find(sql, new Object[] { nmsAlarm });
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new NmsAlarmReport();
	}

	private boolean alarmReportExists(NmsAlarm nmsAlarm) {
		String sql = "from NmsAlarmReport where nmsAlarm = ?";
		List<NmsAlarmReport> list = (List<NmsAlarmReport>) hibernateTemplate
				.find(sql, new Object[] { nmsAlarm });
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	public List<NmsAlarmStaticsDetail> staticsNmsAssetAlarmExportExcel(
			String nmsAssetName, String nmsAssetIp, Integer nmsAssetId,
			String nmsAssetTypeName, Integer nmsAssetTypeId,
			Integer alarmLevel, String alarmStartDate, String alarmEndDate, String orderKey, Integer orderValue) {

		String firstSql = "select a.id,a.a_name,"
				+ "a.a_ip,b.totalCount,"
				+ "b.undealTotalCount,b.dealIngTotalCount,"
				+ "b.dealedTotalCount,b.autoTotalCount,b.deletedTotalCount,"
				+ "k.undealTotalCountLevelOne,k.dealingTotalCountLevelOne,k.dealedTotalCountLevelOne,"
				+ "k.autoTotalCountLevelOne,k.deletedTotalCountLevelOne,"
				+ "k.undealTotalCountLevelTwo,k.dealingTotalCountLevelTwo,k.dealedCountLevelTwo,k.autoTotalCountLevelTwo,"
				+ "k.deletedCountLevelTwo,"
				+ "k.undealTotalCountLevelThree,k.dealingTotalCountLevelThree,k.dealedCountLevelThree,"
				+ "k.autoTotalCountLevelThree,k.deletedCountLevelThree,t.ch_type,t.ch_subtype "
				+ "from nms_asset a "
				+ "left join nms_asset_type t on a.type_id= t.id "
				+ "left join ( ";
		String firstSqlCount = "select count(*) " + "from nms_asset a "
				+ "left join nms_asset_type t on a.type_id= t.id "
				+ "left join ( ";
		String secondSql = "select asset_id,count(id) totalCount," + "sum("
				+ "case when d_status = 0 then 1 " + "else 0 end"
				+ ")  as undealTotalCount, " + "sum("
				+ "case when d_status = 1 then 1 " + "else 0 end"
				+ ") as dealIngTotalCount," + "sum("
				+ "case when d_status = 2 then 1 " + "else 0 end"
				+ ") as dealedTotalCount, " + "sum("
				+ "case when d_status = 3 then 1 " + "else 0 end"
				+ ") as autoTotalCount, " + "sum("
				+ "case when d_status = 4 then 1 " + "else 0 end "
				+ ") as deletedTotalCount " + "from nms_alarm " + " where 1=1 ";

		String threeSql = " group by asset_id ) b on a.id = b.asset_id "
				+ " left join ( ";
		String fourSql = "select asset_id," + "sum("
				+ "case when d_status = 0  and a_level=1 then 1 "
				+ "else 0 end" + ") as undealTotalCountLevelOne," + "sum("
				+ "case when d_status = 1 and a_level=1 then 1 " + "else 0 end"
				+ ") as dealingTotalCountLevelOne," + "sum("
				+ "case when d_status = 2 and a_level=1 then 1 " + "else 0 end"
				+ ") as dealedTotalCountLevelOne," + "sum("
				+ "case when d_status = 3 and a_level=1 then 1 " + "else 0 end"
				+ ") as autoTotalCountLevelOne," + "sum("
				+ "case when d_status = 4 and a_level=1 then 1 " + "else 0 end"
				+ ") as deletedTotalCountLevelOne," + "sum("
				+ "case when d_status = 0 and a_level=2 then 1 " + "else 0 end"
				+ ") as undealTotalCountLevelTwo," + "sum("
				+ "case when d_status = 1 and a_level=2 then 1 " + "else 0 end"
				+ ")  as dealingTotalCountLevelTwo," + "sum("
				+ "case when d_status = 2 and a_level=2 then 1 " + "else 0 end"
				+ ") as dealedCountLevelTwo," + "sum("
				+ "case when d_status = 3 and a_level=2 then 1 " + "else 0 end"
				+ ") as autoTotalCountLevelTwo," + "sum("
				+ "case when d_status = 4 and a_level=2 then 1 " + "else 0 end"
				+ ") as deletedCountLevelTwo," + "sum("
				+ "case when d_status = 0 and a_level=3 then 1 " + "else 0 end"
				+ ") as undealTotalCountLevelThree," + "sum("
				+ "case when d_status = 1 and a_level=3 then 1 " + "else 0 end"
				+ ")  as dealingTotalCountLevelThree," + "sum("
				+ "case when d_status = 2 and a_level=3 then 1 " + "else 0 end"
				+ ") as dealedCountLevelThree," + "sum("
				+ "case when d_status = 3 and a_level=3 then 1 " + "else 0 end"
				+ ") as autoTotalCountLevelThree," + "sum("
				+ "case when d_status = 4 and a_level=3 then 1 " + "else 0 end"
				+ ") as deletedCountLevelThree " + " from nms_alarm "
				+ " where 1=1 ";
		String endSql = " group by asset_id " + ") k on k.asset_id = a.id "
				+ "where 1=1 and a.deled = 0 ";
		if (nmsAssetName != null && nmsAssetName.length() > 0) {
			endSql = endSql + "and a.a_name like '%" + nmsAssetName + "%' ";
		}
		if (nmsAssetIp != null && nmsAssetIp.length() > 0) {
			endSql = endSql + "and a.a_ip like'%" + nmsAssetIp + "%' ";
		}
		if (nmsAssetId != null) {
			endSql = endSql + "and a.id =" + nmsAssetId + " ";
		}
		if (nmsAssetTypeName != null && nmsAssetTypeName.length() > 0) {
			endSql = endSql + " and t.ch_type like '%" + nmsAssetTypeName
					+ "%' ";
		}
		if (nmsAssetTypeId != null) {
			endSql = endSql + " and t.id =" + nmsAssetTypeId + " ";
		}
		if (alarmLevel != null) {
			secondSql = secondSql + "and  a_level = " + alarmLevel + " ";
			fourSql = fourSql + " and a_level = " + alarmLevel + " ";
		}
		if (alarmStartDate != null && alarmStartDate.length() > 0) {
			secondSql = secondSql + " and a_time >= '" + alarmStartDate + "' ";
			fourSql = fourSql + " and a_time >= '" + alarmStartDate + "' ";
		}
		if (alarmEndDate != null && alarmEndDate.length() > 0) {
			secondSql = secondSql + " and a_time <= '" + alarmEndDate + "' ";
			fourSql = fourSql + " and a_time <= '" + alarmEndDate + "' ";
		}
		
		String sql = firstSql + secondSql + threeSql + fourSql + endSql;
		String sqlcount = firstSqlCount + secondSql + threeSql + fourSql + endSql;
		
		if (orderKey == null || orderKey.equals("")) {
			orderKey = "id";
			orderValue = 0;
		}

		if (orderValue == 0) {
			sql += " order by " + orderKey + " desc";
		} else {
			sql += " order by " + orderKey + " asc";
		}
		
		Session session = hibernateTemplate.getSessionFactory().openSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List list = sqlQuery.list();
		SQLQuery sqlQueryCount = session.createSQLQuery(sqlcount);
		Integer count = Integer.valueOf(sqlQueryCount.list().get(0).toString());
		session.close();
		
		
		List<NmsAlarmStaticsDetail> result = new ArrayList<NmsAlarmStaticsDetail>(
				0);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] objects = (Object[]) list.get(i);
				NmsAlarmStaticsDetail nmsAlarmStaticsDetail = new NmsAlarmStaticsDetail();
				NmsAlarmLevelStaticsDetail levelOne = new NmsAlarmLevelStaticsDetail();
				NmsAlarmLevelStaticsDetail levelTwo = new NmsAlarmLevelStaticsDetail();
				NmsAlarmLevelStaticsDetail levelThree = new NmsAlarmLevelStaticsDetail();
				nmsAlarmStaticsDetail.setNmsAssetId((Integer) objects[0]);
				nmsAlarmStaticsDetail.setNmsAssetName((String) objects[1]);
				nmsAlarmStaticsDetail.setNmsAssetIp((String) objects[2]);
				if (objects[3] == null) {
					nmsAlarmStaticsDetail.setAlarmTotalCount(0L);
				} else {
					nmsAlarmStaticsDetail.setAlarmTotalCount(Long
							.valueOf(objects[3].toString()));
				}
				if (objects[4] == null) {
					nmsAlarmStaticsDetail.setUnDealTotalCount(0L);
				} else {
					nmsAlarmStaticsDetail.setUnDealTotalCount(Long
							.valueOf(objects[4].toString()));
				}
				if (objects[5] == null) {
					nmsAlarmStaticsDetail.setDealingTotalCount(0L);
				} else {
					nmsAlarmStaticsDetail.setDealingTotalCount(Long
							.valueOf(objects[5].toString()));
				}
				if (objects[6] == null) {
					nmsAlarmStaticsDetail.setDealedTotalCount(0L);
				} else {
					nmsAlarmStaticsDetail.setDealedTotalCount(Long
							.valueOf(objects[6].toString()));
				}
				if (objects[7] == null) {
					nmsAlarmStaticsDetail.setAutoRecoverTotalCount(0L);
				} else {
					nmsAlarmStaticsDetail.setAutoRecoverTotalCount(Long
							.valueOf(objects[7].toString()));
				}
				if (objects[8] == null) {
					nmsAlarmStaticsDetail.setDeleteTotalCount(0L);
				} else {
					nmsAlarmStaticsDetail.setDeleteTotalCount(Long
							.valueOf(objects[8].toString()));
				}

				if (objects[9] == null) {
					levelOne.setUnDealTotalCount(0L);
				} else {
					levelOne.setUnDealTotalCount(Long.valueOf(objects[9]
							.toString()));
				}
				if (objects[10] == null) {
					levelOne.setDealingTotalCount(0L);
				} else {
					levelOne.setDealingTotalCount(Long.valueOf(objects[10]
							.toString()));
				}
				if (objects[11] == null) {
					levelOne.setDealedTotalCount(0L);
				} else {
					levelOne.setDealedTotalCount(Long.valueOf(objects[11]
							.toString()));
				}
				if (objects[12] == null) {
					levelOne.setAutoRecoverTotalCount(0L);
				} else {
					levelOne.setAutoRecoverTotalCount(Long.valueOf(objects[12]
							.toString()));
				}
				if (objects[13] == null) {
					levelOne.setDeleteTotalCount(0L);
				} else {
					levelOne.setDeleteTotalCount(Long.valueOf(objects[13]
							.toString()));
				}

				if (objects[14] == null) {
					levelTwo.setUnDealTotalCount(0L);
				} else {
					levelTwo.setUnDealTotalCount(Long.valueOf(objects[14]
							.toString()));
				}
				if (objects[15] == null) {
					levelTwo.setDealingTotalCount(0L);
				} else {
					levelTwo.setDealingTotalCount(Long.valueOf(objects[15]
							.toString()));
				}
				if (objects[16] == null) {
					levelTwo.setDealedTotalCount(0L);
				} else {
					levelTwo.setDealedTotalCount(Long.valueOf(objects[16]
							.toString()));
				}
				if (objects[17] == null) {
					levelTwo.setAutoRecoverTotalCount(0L);
				} else {
					levelTwo.setAutoRecoverTotalCount(Long.valueOf(objects[17]
							.toString()));
				}
				if (objects[18] == null) {
					levelTwo.setDeleteTotalCount(0L);
				} else {
					levelTwo.setDeleteTotalCount(Long.valueOf(objects[18]
							.toString()));
				}

				if (objects[19] == null) {
					levelThree.setUnDealTotalCount(0L);
				} else {
					levelThree.setUnDealTotalCount(Long.valueOf(objects[19]
							.toString()));
				}
				if (objects[20] == null) {
					levelThree.setDealingTotalCount(0L);
				} else {
					levelThree.setDealingTotalCount(Long.valueOf(objects[20]
							.toString()));
				}
				if (objects[21] == null) {
					levelThree.setDealedTotalCount(0L);
				} else {
					levelThree.setDealedTotalCount(Long.valueOf(objects[21]
							.toString()));
				}
				if (objects[22] == null) {
					levelThree.setAutoRecoverTotalCount(0L);
				} else {
					levelThree.setAutoRecoverTotalCount(Long
							.valueOf(objects[22].toString()));
				}
				if (objects[23] == null) {
					levelThree.setDeleteTotalCount(0L);
				} else {
					levelThree.setDeleteTotalCount(Long.valueOf(objects[23]
							.toString()));
				}
				
				
				// 重置告警总数=待处理+处理中+已处理
				nmsAlarmStaticsDetail.setAlarmTotalCount(nmsAlarmStaticsDetail
						.getUnDealTotalCount()
						+ nmsAlarmStaticsDetail.getDealingTotalCount()
						+ nmsAlarmStaticsDetail.getDealedTotalCount());				
				
				nmsAlarmStaticsDetail.setNmsAssetType((String) objects[24] + "/"+ (String) objects[25]);
				nmsAlarmStaticsDetail.setNmsAssetSubType((String) objects[25]);
				nmsAlarmStaticsDetail.setLevelOne(levelOne);
				nmsAlarmStaticsDetail.setLevelTwo(levelTwo);
				nmsAlarmStaticsDetail.setLevelThree(levelThree);
				result.add(nmsAlarmStaticsDetail);
			}
		}

		return result;
	}
	
	
	public boolean deleteAlarmByAssetId(String assetId) {
		boolean res = true;
		String sql = "update nms_alarm set d_status = 4 where asset_id = " + assetId;
		Session session = hibernateTemplate.getSessionFactory().openSession();
		try {
			session.createSQLQuery(sql).executeUpdate();
		} catch (Exception e) {
			session.close();
			res = false;
		}
		session.close();
		return res;	
	}
	
	
	public List<NmsAlarmDetail> listPageByConditionExportExcel(
			String orderKey, int orderValue, String AIp, String AName, 
			Integer ALevel, Integer DStatus, String AContent,
			String startDate, String endDate) {

		DetachedCriteria criteria = DetachedCriteria.forClass(NmsAlarm.class);
		if (ALevel != null) {
			criteria.add(Restrictions.eq("ALevel", ALevel));
		}
		
		criteria.add(Restrictions.le("DStatus", 3));
		
		criteria.createAlias("nmsAsset", "nmsAsset");
		if (AIp != null && AIp.length() > 0) {
			criteria.add(Restrictions.like("nmsAsset.AIp", "%" + AIp + "%"));
		}
		
		if (AName != null && AName.length() > 0) {
			criteria.add(Restrictions.like("nmsAsset.AName", "%" + AName + "%"));
		}		
		
		if (AContent != null && AContent.length() > 0) {
			criteria.add(Restrictions.like("AContent", "%" + AContent + "%"));
		}

		if (DStatus != null) {
			criteria.add(Restrictions.eq("DStatus", DStatus));
		}
		
		if (startDate != null && startDate.length() > 0) {
			criteria.add(Restrictions.gt("ATime", startDate));
		}
		
		if (endDate != null && endDate.length() > 0) {
			criteria.add(Restrictions.lt("ATime", endDate));
		}
		
		if (orderKey == null || orderKey.equals("")) { 
			orderKey = "ATime";
			orderValue = 0;
		}
		
		if (orderValue == 0) {
			criteria.addOrder(Order.desc(orderKey));
		} else {
			criteria.addOrder(Order.asc(orderKey));
		}

		criteria.setProjection(Projections.rowCount());
		Long totalCount = (Long) hibernateTemplate.findByCriteria(criteria).get(0);

		criteria.setProjection(null);
		criteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
		
		// 告警报表最多导出10000条记录
		List<NmsAlarm> nmsAlarmList = (List<NmsAlarm>) hibernateTemplate.findByCriteria(criteria, 0, 10000);

		List<NmsAlarmDetail> nmsAlarmDetailList = new ArrayList<NmsAlarmDetail>(0);
		if (nmsAlarmList != null && nmsAlarmList.size() > 0) {
			for (int i = 0; i < nmsAlarmList.size(); i++) {
				NmsAlarm nmsAlarm = nmsAlarmList.get(i);
				NmsAlarmDetail nmsAlarmDetail = new NmsAlarmDetail();
				
				
				int level = nmsAlarm.getALevel();
				int dStatus = nmsAlarm.getDStatus();
				
				if (level == 1) {
					nmsAlarmDetail.setLevel("低风险");
				} else if (level == 2) {
					nmsAlarmDetail.setLevel("中风险");
				} else if (level == 3) {
					nmsAlarmDetail.setLevel("高风险");
				} else {
					nmsAlarmDetail.setLevel("其它");
				}
	
				if (dStatus == 0) {
					nmsAlarmDetail.setStatus("待处理");
				} else if (dStatus == 1) {
					nmsAlarmDetail.setStatus("处理中");
				} else if (dStatus == 2) {
					nmsAlarmDetail.setStatus("已处理");
				} else {
					nmsAlarmDetail.setStatus("其它");
				}				
				
				if (nmsAlarm.getSTime() != null) {
					nmsAlarmDetail.setStime(nmsAlarm.getSTime());
				} else {
					nmsAlarmDetail.setStime("--");
				}

				if (nmsAlarm.getATime() != null) {
					nmsAlarmDetail.setAtime(nmsAlarm.getATime());
				} else {
					nmsAlarmDetail.setAtime("--");
				}
				
				if (nmsAlarm.getACount() != null) {
					nmsAlarmDetail.setAcount(String.valueOf(nmsAlarm.getACount()));
				} else {
					nmsAlarmDetail.setAcount("--");
				}
				
				if (nmsAlarm.getDTime() != null) {
					nmsAlarmDetail.setDtime(nmsAlarm.getDTime());
				} else {
					nmsAlarmDetail.setDtime("--");
				}
				
				if (nmsAlarm.getDTime() != null) {
					nmsAlarmDetail.setIp(nmsAlarm.getNmsAsset().getAIp());
				} else {
					nmsAlarmDetail.setIp("--");
				}	
				
				if (nmsAlarm.getAContent() != null) {
					nmsAlarmDetail.setContent(nmsAlarm.getAContent());
				} else {
					nmsAlarmDetail.setContent("--");
				}	
		
				if (nmsAlarm.getAName() != null) {
					nmsAlarmDetail.setName(nmsAlarm.getNmsAsset().getAName());
				} else {
					nmsAlarmDetail.setName("--");
				}	
				
				nmsAlarmDetailList.add(nmsAlarmDetail);
			}
		}
		return nmsAlarmDetailList;
	}	
}

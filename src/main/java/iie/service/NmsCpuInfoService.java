package iie.service;

import iie.pojo.NmsAsset;
import iie.pojo.NmsCpuInfo;
import iie.pojo.NmsStaticInfo;
import iie.tools.*;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("NmsCpuInfoService")
public class NmsCpuInfoService {

	@Autowired
	NmsAssetService nmsAssetService;

	@Autowired
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	public List<NmsCpuInfo> getAll() {
		String hsql = "from NmsCpuInfo";
		return (List<NmsCpuInfo>) hibernateTemplate.find(hsql);
	}

	public boolean saveCpu(NmsCpuInfo nc) {
		try {
			nc.setId((long) 0);
			hibernateTemplate.save(nc);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	public NmsAsset findById(int id) {
		String hsql = "from NmsAsset na where na.id = ?";
		@SuppressWarnings("unchecked")
		List<NmsAsset> list = (List<NmsAsset>) hibernateTemplate.find(hsql, id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new NmsAsset();
	}

	@SuppressWarnings("unchecked")
	public PageBean<NmsCpuInfo> getPageByDate(String orderKey,
			String orderValue, String startDate, String endDate, int begin,
			int offset, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from nms_cpu_info as rt where rt.asset_id = '" + assetId + "'";
		if (startDate != null && endDate != null) {
			sqlCount += " and rt.itime between '" + startDate + "' and '" + endDate + "'";
		}
		SQLQuery queryCount = session.createSQLQuery(sqlCount);
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());
		session.close();
		
		if (orderKey == null || orderValue == null) {
			orderKey = "id";
			orderValue = "1";
		}
		
		if (orderKey != null && !orderKey.equals("")) {
			
			if (orderValue != null && orderValue.equals("0")) {
				orderKey = orderKey + " asc";
			} else {
				orderKey = orderKey + " desc";
			}
		}

		// 获取list数据
		String sqlList = "select * from nms_cpu_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		sqlList += " order by " + orderKey + " limit " + (begin - 1) * offset
				+ "," + offset;
		
		session = hibernateTemplate.getSessionFactory().openSession();
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsCpuInfo", NmsCpuInfo.class);
		List<NmsCpuInfo> list = queryList.list();
		session.close();

		// 创建PageBean对象返回数据
		PageBean<NmsCpuInfo> page = new PageBean<NmsCpuInfo>();
		page.setOrderKey(orderKey);
		page.setOrderValue(Integer.valueOf(orderValue));
		page.setKey("");
		page.setValue("");

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

		page.setList(list);

		return page;
	}

	/**
	 * 不推荐使用此方法
	 * @param orderKey
	 * @param orderValue
	 * @param startDate
	 * @param endDate
	 * @param begin
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageBean<NmsCpuInfo> getPageByDateV02(String orderKey,
			int orderValue, String startDate, String endDate, int begin,
			int offset) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from (select * from nms_cpu_info order by itime asc) as rt where rt.itime between '"
				+ startDate + "' and '" + endDate + "'";
		SQLQuery queryCount = session.createSQLQuery(sqlCount);
	//	System.out.println(queryCount.list());
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());
	//	System.out.println(count);

		if (orderValue != 0) {
			orderKey = orderKey + " desc";
		}

		// 获取list数据
		String sqlList = "select * from (select * from nms_cpu_info order by itime asc) as rt where rt.itime between '"
				+ startDate
				+ "' and '"
				+ endDate
				+ "' order by "
				+ orderKey
				+ " limit " + (begin - 1) * offset + "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);

		// 序列化数据
		queryList.addEntity("NmsCpuInfo", NmsCpuInfo.class);
		List<NmsCpuInfo> list = queryList.list();
	//	System.out.println(list);

		// 关闭session
		session.close();

		// 创建PageBean对象返回数据
		PageBean<NmsCpuInfo> page = new PageBean<NmsCpuInfo>();
		page.setOrderKey(orderKey);
		page.setOrderValue(orderValue);
		page.setKey("");
		page.setValue("");

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

		page.setList(list);

		return page;
	}

	public NmsCpuStaticsInfo cpuInfoServiceDetailPerformanceInfo(
			Integer assetId, String startDate, String endDate) throws Exception {
		NmsAsset nmsAsset = hibernateTemplate.get(NmsAsset.class, assetId);

		if (nmsAsset == null) {
			return new NmsCpuStaticsInfo();
		}
		NmsStaticInfo nmsStaticInfo = new NmsStaticInfo();
		nmsStaticInfo.setNmsAsset(nmsAsset);
		nmsStaticInfo = hibernateTemplate.findByExample(nmsStaticInfo).get(0);

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();

		StringBuilder currentCpuBuilder = new StringBuilder(
				"select avg(a.cpu_rate) from nms_cpu_info  a where a.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_cpu_info) as b where asset_id= "
						+ assetId + ")");
		currentCpuBuilder.append(" and a.asset_id = ").append(assetId);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		currentCpuBuilder.append(" and itime >='")
				.append(dateFormat.format(calendar.getTime())).append("'");

		
		
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Query query = session.createSQLQuery(currentCpuBuilder.toString());
		List currentCpuRateList = query.list();
		session.close();



		StringBuilder cpuAvgRateSql = new StringBuilder("select freq,avg(cpu_rate) from nms_cpu_info where 1=1 ");
		cpuAvgRateSql.append(" and asset_id = ").append(assetId);
		if (startDate != null && startDate.length() > 0) {
			cpuAvgRateSql.append(" and itime >= '").append(startDate)
					.append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			cpuAvgRateSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		if (endDate != null && endDate.length() > 0) {
			cpuAvgRateSql.append(" and itime <= '").append(endDate).append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			cpuAvgRateSql.append(" and itime <= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		cpuAvgRateSql.append(" group by freq");

		

		session = hibernateTemplate.getSessionFactory().openSession();
		Query cpuRateQuery = session.createSQLQuery(cpuAvgRateSql.toString());
		List cpuRateList = cpuRateQuery.list();
		session.close();
		
		

		StringBuilder cpuMaxRateSql = new StringBuilder(
				"select max(cpu_rate) from nms_cpu_info where 1=1");
		cpuMaxRateSql.append(" and asset_id = ").append(assetId);
		if (startDate != null && startDate.length() > 0) {
			cpuMaxRateSql.append(" and itime >= '").append(startDate)
					.append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			cpuMaxRateSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		if (endDate != null && endDate.length() > 0) {
			cpuMaxRateSql.append(" and itime <= '").append(endDate).append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			cpuMaxRateSql.append(" and itime <= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		
		session = hibernateTemplate.getSessionFactory().openSession();
		Query cpuMaxRateQuery = session.createSQLQuery(cpuMaxRateSql.toString());
		List cpuMaxRateList = cpuMaxRateQuery.list();
		session.close();
		

		StringBuilder threeCpuRateSql = new StringBuilder(
				"select freq,avg(cpu_rate) from nms_cpu_info where 1=1 ");
		threeCpuRateSql.append(" and asset_id = ").append(assetId);

		if (startDate != null && startDate.length() > 0) {
			calendar.setTime(dateFormat.parse(startDate));
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			threeCpuRateSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
			threeCpuRateSql.append(" and itime <= '").append(startDate)
					.append("'");
		} else {
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			threeCpuRateSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}
		threeCpuRateSql.append(" group by freq");

		
		session = hibernateTemplate.getSessionFactory().openSession();
		Query cpuThreeRateQuery = session.createSQLQuery(threeCpuRateSql.toString());
		List cpuThreeRateList = cpuThreeRateQuery.list();
		session.close();
		

		StringBuilder threeMaxCpuRateSql = new StringBuilder(
				"select freq, max(cpu_rate) from nms_cpu_info where 1=1 ");
		threeMaxCpuRateSql.append(" and asset_id = ").append(assetId);

		if (startDate != null && startDate.length() > 0) {
			calendar.setTime(dateFormat.parse(startDate));
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			threeCpuRateSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
			threeMaxCpuRateSql.append(" and itime <= '").append(startDate)
					.append("'");
		} else {
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			threeMaxCpuRateSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}
		
		threeMaxCpuRateSql.append(" group by freq");
		
		
		session = hibernateTemplate.getSessionFactory().openSession();
		Query cpuThreeMaxRateQuery = session.createSQLQuery(threeMaxCpuRateSql.toString());
		List cpuThreeMaxRateList = cpuThreeMaxRateQuery.list();
		session.close();
		

		if (startDate == null || startDate.length() == 0) {
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = dateFormat.format(calendar.getTime());
		}

		if (endDate == null || endDate.length() == 0) {
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			endDate = dateFormat.format(calendar.getTime());
		}

		String allQuerySql = "select itime,cpu_rate from nms_cpu_info where asset_id = "
				+ assetId
				+ " and itime between '"
				+ startDate
				+ "' and '"
				+ endDate + "' order by id desc";

		
		session = hibernateTemplate.getSessionFactory().openSession();
		Query allQuery = session.createSQLQuery(allQuerySql);
		List allCpuInfoList = allQuery.list();
		session.close();
		

		NmsCpuStaticsInfo nmsCpuStaticsInfo = new NmsCpuStaticsInfo();
		nmsCpuStaticsInfo.setAssetId(nmsAsset.getId());
		nmsCpuStaticsInfo.setAssetName(nmsAsset.getAName());
		nmsCpuStaticsInfo.setAssetIp(nmsAsset.getAIp());
		nmsCpuStaticsInfo.setCpuNum(nmsStaticInfo.getCpuNum());

		NmsCpuData cpuData = new NmsCpuData();
		if (currentCpuRateList != null && currentCpuRateList.size() > 0) {
			Double currentCpuRate = (Double) currentCpuRateList.get(0);
			if (currentCpuRate != null) {
				cpuData.setCurrentCpuRate(currentCpuRate.floatValue());
			}
		}

		Double totalAverageCpuRateOfToday = 0.0;
		if (cpuRateList != null && cpuRateList.size() > 0) {
			for (int i = 0; i < cpuRateList.size(); i++) {
				Object[] objects = (Object[]) cpuRateList.get(i);
				Double AverageCpuRateOfToday = (Double) objects[1];
				totalAverageCpuRateOfToday = totalAverageCpuRateOfToday
						+ AverageCpuRateOfToday;
			}
			cpuData.setAverageCpuRateOfToday(totalAverageCpuRateOfToday
					/ cpuRateList.size());
		}
		if (cpuMaxRateList != null && cpuMaxRateList.size() > 0) {
			Object object = cpuMaxRateList.get(0);
			cpuData.setMaxCpuRateOfToday((Float) object);
		}
		if (cpuThreeMaxRateList != null && cpuThreeMaxRateList.size() > 0) {
			Object object = cpuThreeMaxRateList.get(0);
			cpuData.setMaxCpuRateOfTheLastThreeDays((Float) object);
		}

		Double totalAverageCpuRateOfTheLastThreeDays = 0.0;

		if (cpuThreeRateList != null && cpuThreeRateList.size() > 0) {
			for (int i = 0; i < cpuThreeRateList.size(); i++) {
				Object[] objects = (Object[]) cpuThreeRateList.get(i);
				Double AverageCpuRateOfTheLastThreeDays = (Double) objects[1];
				totalAverageCpuRateOfTheLastThreeDays = totalAverageCpuRateOfTheLastThreeDays
						+ AverageCpuRateOfTheLastThreeDays;
			}
			cpuData.setAverageCpuRateOfTheLastThreeDays(totalAverageCpuRateOfTheLastThreeDays
					/ cpuThreeRateList.size());
		}

		List<NmsCpuDetail> allList = new ArrayList<NmsCpuDetail>();
		if (allCpuInfoList != null && allCpuInfoList.size() > 0) {
			for (int i = 0; i < allCpuInfoList.size(); i++) {
				Object[] objects = (Object[]) allCpuInfoList.get(i);
				NmsCpuDetail nmsCpuDetail = new NmsCpuDetail();
				nmsCpuDetail.setItime((String) objects[0]);
				//nmsCpuDetail.setCpuRate((Float) objects[1]);
				nmsCpuDetail.setCpuRate(String.valueOf(new BigDecimal((Double) objects[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()));
				allList.add(nmsCpuDetail);
			}
		}
		cpuData.setData(allList);
		nmsCpuStaticsInfo.setCpuData(cpuData);

		session.close();
		return nmsCpuStaticsInfo;
	}

	
	public NmsCpuStaticsInfo cpuInfoServiceDetailPerformanceInfoV02(
			Integer assetId, String startDate, String endDate) throws Exception {

		NmsAsset nmsAsset = hibernateTemplate.get(NmsAsset.class, assetId);

		if (nmsAsset == null) {
			return new NmsCpuStaticsInfo();
		}

		NmsStaticInfo nmsStaticInfo = new NmsStaticInfo();
		nmsStaticInfo.setNmsAsset(nmsAsset);
	//	nmsStaticInfo = hibernateTemplate.findByExample(nmsStaticInfo).get(0);
		List<NmsStaticInfo> list = hibernateTemplate.findByExample(nmsStaticInfo);
		if (list.size() > 0) {
			nmsStaticInfo = list.get(0);
		} else {
			return new NmsCpuStaticsInfo();
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Calendar calendar = getDateOfTheLastNmsCpuInfo(nmsAsset);

		StringBuilder currentCpuBuilder = new StringBuilder(
				"select avg(a.cpu_rate) from nms_cpu_info  a where a.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_cpu_info) as b where asset_id= "
						+ assetId + ")");
		currentCpuBuilder.append(" and a.asset_id = ").append(assetId);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		currentCpuBuilder.append(" and itime >='")
				.append(dateFormat.format(calendar.getTime())).append("'");

		Session session = hibernateTemplate.getSessionFactory().openSession();
		Query query = session.createSQLQuery(currentCpuBuilder.toString());
		List currentCpuRateList = query.list();
		session.close();

		StringBuilder cpuAvgRateSql = new StringBuilder(
				"select freq,avg(cpu_rate) from nms_cpu_info where 1=1 ");
		cpuAvgRateSql.append(" and asset_id = ").append(assetId);
		if (startDate != null && startDate.length() > 0) {
			cpuAvgRateSql.append(" and itime >= '").append(startDate)
					.append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			cpuAvgRateSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		if (endDate != null && endDate.length() > 0) {
			cpuAvgRateSql.append(" and itime <= '").append(endDate).append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			cpuAvgRateSql.append(" and itime <= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		cpuAvgRateSql.append(" group by freq");

		session = hibernateTemplate.getSessionFactory().openSession();
		Query cpuRateQuery = session.createSQLQuery(cpuAvgRateSql.toString());
		List cpuRateList = cpuRateQuery.list();
		session.close();
		
		StringBuilder cpuMaxRateSql = new StringBuilder(
				"select max(cpu_rate) from nms_cpu_info where 1=1");
		cpuMaxRateSql.append(" and asset_id = ").append(assetId);
		if (startDate != null && startDate.length() > 0) {
			cpuMaxRateSql.append(" and itime >= '").append(startDate)
					.append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			cpuMaxRateSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		if (endDate != null && endDate.length() > 0) {
			cpuMaxRateSql.append(" and itime <= '").append(endDate).append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			cpuMaxRateSql.append(" and itime <= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		session = hibernateTemplate.getSessionFactory().openSession();
		Query cpuMaxRateQuery = session.createSQLQuery(cpuMaxRateSql.toString());
		List cpuMaxRateList = cpuMaxRateQuery.list();
		session.close();

		StringBuilder threeCpuRateSql = new StringBuilder(
				"select freq,avg(cpu_rate) from nms_cpu_info where 1=1 ");
		threeCpuRateSql.append(" and asset_id = ").append(assetId);

		if (startDate != null && startDate.length() > 0) {
			calendar.setTime(dateFormat.parse(startDate));
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			threeCpuRateSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
			threeCpuRateSql.append(" and itime <= '").append(startDate)
					.append("'");
		} else {
			calendar.setTime(getDateOfTheLastNmsCpuInfo(nmsAsset).getTime());
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			threeCpuRateSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		session = hibernateTemplate.getSessionFactory().openSession();
		Query cpuThreeRateQuery = session.createSQLQuery(threeCpuRateSql.toString());
		List cpuThreeRateList = cpuThreeRateQuery.list();
		session.close();
		
		StringBuilder threeMaxCpuRateSql = new StringBuilder(
				"select max(cpu_rate) from nms_cpu_info where 1=1 ");
		threeMaxCpuRateSql.append(" and asset_id = ").append(assetId);

		if (startDate != null && startDate.length() > 0) {
			calendar.setTime(dateFormat.parse(startDate));
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			threeCpuRateSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
			threeMaxCpuRateSql.append(" and itime <= '").append(startDate)
					.append("'");
		} else {
			calendar.setTime(getDateOfTheLastNmsCpuInfo(nmsAsset).getTime());
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			threeMaxCpuRateSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		session = hibernateTemplate.getSessionFactory().openSession();
		Query cpuThreeMaxRateQuery = session.createSQLQuery(threeMaxCpuRateSql.toString());
		List cpuThreeMaxRateList = cpuThreeMaxRateQuery.list();
		session.close();
		
		if (startDate == null || startDate.length() == 0) {
			calendar.setTime(getDateOfTheLastNmsCpuInfo(nmsAsset).getTime());
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = dateFormat.format(calendar.getTime());
		}

		if (endDate == null || endDate.length() == 0) {
			calendar.setTime(getDateOfTheLastNmsCpuInfo(nmsAsset).getTime());
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			endDate = dateFormat.format(calendar.getTime());
		}

		String allQuerySql = "select a.itime,avg(a.cpu_rate) from nms_cpu_info a where a.asset_id = "
				+ assetId
				+ " and a.itime between '"
				+ startDate
				+ "' and '"
				+ endDate + "' group by a.freq";

		session = hibernateTemplate.getSessionFactory().openSession();
		Query allQuery = session.createSQLQuery(allQuerySql);
		List allCpuInfoList = allQuery.list();
		session.close();
		
		NmsCpuStaticsInfo nmsCpuStaticsInfo = new NmsCpuStaticsInfo();
		nmsCpuStaticsInfo.setAssetId(nmsAsset.getId());
		nmsCpuStaticsInfo.setAssetName(nmsAsset.getAName());
		nmsCpuStaticsInfo.setAssetIp(nmsAsset.getAIp());
		nmsCpuStaticsInfo.setOnline(nmsAsset.getOnline());
		nmsCpuStaticsInfo.setCpuNum(nmsStaticInfo.getCpuNum());

		NmsCpuData cpuData = new NmsCpuData();
		if (currentCpuRateList != null && currentCpuRateList.size() > 0 && nmsAssetService.findOnlineById(assetId)) {
			Double currentCpuRate = (Double) currentCpuRateList.get(0);
			if (currentCpuRate != null) {
				cpuData.setCurrentCpuRate(currentCpuRate.floatValue());
			}
		}

		Double totalAverageCpuRateOfToday = 0.0;
		if (cpuRateList != null && cpuRateList.size() > 0) {
			for (int i = 0; i < cpuRateList.size(); i++) {
				Object[] objects = (Object[]) cpuRateList.get(i);
				Double AverageCpuRateOfToday = (Double) objects[1];
				totalAverageCpuRateOfToday = totalAverageCpuRateOfToday
						+ AverageCpuRateOfToday;
			}
			cpuData.setAverageCpuRateOfToday(totalAverageCpuRateOfToday
					/ cpuRateList.size());
		}
		if (cpuMaxRateList != null && cpuMaxRateList.size() > 0) {
			Object object = cpuMaxRateList.get(0);
			cpuData.setMaxCpuRateOfToday((Float) object);
		}
		if (cpuThreeMaxRateList != null && cpuThreeMaxRateList.size() > 0) {
			Object object = cpuThreeMaxRateList.get(0);
			cpuData.setMaxCpuRateOfTheLastThreeDays((Float) object);
		}

		
		Double totalAverageCpuRateOfTheLastThreeDays = 0.0;

		if (cpuThreeRateList != null && cpuThreeRateList.size() > 0) {
			for (int i = 0; i < cpuThreeRateList.size(); i++) {
				Object[] objects = (Object[]) cpuThreeRateList.get(i);
				if (objects[1] != null) {		
					Double AverageCpuRateOfTheLastThreeDays = (Double) objects[1];
					totalAverageCpuRateOfTheLastThreeDays = totalAverageCpuRateOfTheLastThreeDays
							+ AverageCpuRateOfTheLastThreeDays;
				}
			}
			cpuData.setAverageCpuRateOfTheLastThreeDays(totalAverageCpuRateOfTheLastThreeDays
					/ cpuThreeRateList.size());
		}

		List<NmsCpuDetail> allList = new ArrayList<NmsCpuDetail>();
		SimpleDateFormat dateFormat01x = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		
		float maxAvgCpuRate = 0;
		if (allCpuInfoList != null && allCpuInfoList.size() > 0) {
			for (int i = 0; i < allCpuInfoList.size(); i++) {
				Object[] objects = (Object[]) allCpuInfoList.get(i);
				NmsCpuDetail nmsCpuDetail = new NmsCpuDetail();
				nmsCpuDetail.setItime(dateFormat01x.format(objects[0]));
				nmsCpuDetail.setCpuRate(String.valueOf(new BigDecimal((Double) objects[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()));
				
				if (maxAvgCpuRate < new BigDecimal((Double) objects[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) {
					maxAvgCpuRate =  new BigDecimal((Double) objects[1]).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
				}
				
				allList.add(nmsCpuDetail);
			}
		}

		cpuData.setMaxCpuRateOfTheLastThreeDays(maxAvgCpuRate);

		cpuData.setData(allList);
		nmsCpuStaticsInfo.setCpuData(cpuData);

		return nmsCpuStaticsInfo;
	}

	@SuppressWarnings("unchecked")
	private Calendar getDateOfTheLastNmsCpuInfo(NmsAsset nmsAsset) {

		DetachedCriteria currentCriteria = DetachedCriteria
				.forClass(NmsCpuInfo.class);
		currentCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));
		currentCriteria.addOrder(Order.desc("id"));
		List<NmsCpuInfo> nmsCpuInfoList = (List<NmsCpuInfo>) hibernateTemplate
				.findByCriteria(currentCriteria, 0, 1);

		Calendar calendar = Calendar.getInstance();
		Date theDateOfTheLastNmsCpuInfo = new Date();
		if (nmsCpuInfoList != null && nmsCpuInfoList.size() > 0) {
			NmsCpuInfo theLastNmsCpuInfo = nmsCpuInfoList.get(0);
			Timestamp thetimestampOfTheLastNmsFilesysInfo = theLastNmsCpuInfo
					.getItime();
			try {
				theDateOfTheLastNmsCpuInfo = thetimestampOfTheLastNmsFilesysInfo;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		calendar.setTime(theDateOfTheLastNmsCpuInfo);
		return calendar;
	}
	
	@SuppressWarnings("unchecked")
	public List<NmsCpuInfo> getPageByDateExportExcel(String orderKey,
			String orderValue, String startDate, String endDate, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "itime";
		}
		if (orderValue == null || orderValue.length() == 0 || orderValue.equals("1")) {
			orderValue = "1";
			orderKey = orderKey + " asc";
		} else {
			orderKey = orderKey + " desc";
		}

		// 获取list数据
		String sqlList = "select * from nms_cpu_info as a where a.asset_id = " + assetId;
		if (startDate != null && endDate != null) {
			sqlList += " and a.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		
		// 所有原始数据报表最多导出最新的10000条记录
		sqlList += " order by itime desc limit 10000";	
		SQLQuery queryList = session.createSQLQuery(sqlList);

		// 序列化数据
		queryList.addEntity("NmsCpuInfo", NmsCpuInfo.class);
		List<NmsCpuInfo> list = queryList.list();
		
		session.close();
		return list;
	}
	
	
	public boolean deleteByAssetId(String assetId) {
		boolean res = true;
		String sql = "delete from nms_cpu_info where asset_id = " + assetId;
		Session session = hibernateTemplate.getSessionFactory().openSession();
		try {
			session.createSQLQuery(sql).executeUpdate();
		} catch (Exception e) {
			res = false;
		}
		session.close();
		return res;	
	}	
}

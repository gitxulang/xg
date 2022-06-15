package iie.service;

import iie.pojo.NmsAsset;
import iie.pojo.NmsPingInfo;
import iie.tools.NmsPingDetail;
import iie.tools.NmsPingStaticsInfo;
import iie.tools.PageBean;
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

@Service("NmsPingInfoService")
public class NmsPingInfoService {

	@Autowired
	NmsAssetService nmsAssetService;

	@Autowired
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	public NmsPingStaticsInfo pingInfoServiceDetailPerformanceInfo(
			Integer assetId, String startDate, String endDate) throws Exception {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		NmsAsset nmsAsset = hibernateTemplate.get(NmsAsset.class, assetId);

		if (nmsAsset == null) {
			return new NmsPingStaticsInfo();
		}

		DetachedCriteria currentCriteria = DetachedCriteria
				.forClass(NmsPingInfo.class);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		currentCriteria.add(Restrictions.ge("itime", calendar.getTime()));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		currentCriteria.add(Restrictions.le("itime", calendar.getTime()));
		currentCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));
		currentCriteria.addOrder(Order.desc("id"));

		List<NmsPingInfo> nmsPingInfoList = (List<NmsPingInfo>) hibernateTemplate
				.findByCriteria(currentCriteria, 0, 1);

		if (startDate == null || startDate.length() == 0) {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = dateFormat.format(calendar.getTime());

		}
		if (endDate == null || endDate.length() == 0) {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			endDate = dateFormat.format(calendar.getTime());
		}

		String allSql = "select ping_rate,ping_rtt,itime from nms_ping_info where asset_id="
				+ assetId
				+ " and itime between '"
				+ startDate
				+ "' and '"
				+ endDate + "' order by id desc";
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Query allQuery = session.createSQLQuery(allSql);
		List<?> allList = allQuery.list();
		session.close();

		StringBuilder builderSql = new StringBuilder(
				"select avg(ping_rate) avgPingRate,max(ping_rate) maxPingRate,avg(ping_rtt) avgPingRtt,max(ping_rtt) maxPingRtt from nms_ping_info where 1=1 ");
		builderSql.append(" and asset_id = ").append(assetId);
		if (startDate != null && startDate.length() > 0) {
			builderSql.append(" and itime >= '").append(startDate).append("'");
		} else {
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			builderSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}
		if (endDate != null && endDate.length() > 0) {
			builderSql.append(" and itime <= '").append(endDate).append("'");
		} else {
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			builderSql.append(" and itime <= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		session = hibernateTemplate.getSessionFactory().openSession();
		Query query = session.createSQLQuery(builderSql.toString());
		List<?> oneList = query.list();
		session.close();

		StringBuilder threeBuilderSql = new StringBuilder(
				"select avg(ping_rate) avgPingRate,max(ping_rate) maxPingRate,avg(ping_rtt) avgPingRtt,max(ping_rtt) maxPingRtt from nms_ping_info where 1=1 ");

		if (startDate != null && startDate.length() > 0) {
			calendar.setTime(dateFormat.parse(startDate));
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			threeBuilderSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
			threeBuilderSql.append(" and itime <= '").append(startDate)
					.append("'");
		}
		
		session = hibernateTemplate.getSessionFactory().openSession();
		Query threeQuery = session.createSQLQuery(threeBuilderSql.toString());
		List<?> threeList = threeQuery.list();
		session.close();

		NmsPingStaticsInfo nmsPingInfoDetail = new NmsPingStaticsInfo();
		nmsPingInfoDetail.setAssetId(nmsAsset.getId());
		nmsPingInfoDetail.setAssetName(nmsAsset.getAName());
		nmsPingInfoDetail.setAssetIp(nmsAsset.getAIp());

		if (nmsPingInfoList != null && nmsPingInfoList.size() > 0) {
			NmsPingInfo nmsPingInfo = nmsPingInfoList.get(0);
			nmsPingInfoDetail.setCurrentPingRate(nmsPingInfo.getPingRate());
			nmsPingInfoDetail.setCurrentPingRtt(nmsPingInfo.getPingRtt());
		}
		if (oneList != null && oneList.size() > 0) {
			Object[] objects = (Object[]) oneList.get(0);
			nmsPingInfoDetail.setAveragePingRateOfToday((Double) objects[0]);
			nmsPingInfoDetail.setMaxPingRateOfToday((Float) objects[1]);
			nmsPingInfoDetail.setAveragePingRttOfToday((BigDecimal) objects[2]);
			nmsPingInfoDetail.setMaxPingRttOfToday((Integer) objects[3]);
		}
		if (threeList != null && threeList.size() > 0) {
			Object[] objects = (Object[]) oneList.get(0);
			nmsPingInfoDetail
					.setAveragePingRateOfTheLastThreeDays((Double) objects[0]);
			nmsPingInfoDetail
					.setMaxPingRateOfTheLastThreeDays((Float) objects[1]);
			nmsPingInfoDetail
					.setAveragePingRttOfTheLastThreeDays((BigDecimal) objects[2]);
			nmsPingInfoDetail
					.setMaxPingRttOfTheLastThreeDays((Integer) objects[3]);
		}

		List<NmsPingDetail> nmsPingDetailList = new ArrayList<NmsPingDetail>();

		if (allList != null && allList.size() > 0) {
			for (int i = 0; i < allList.size(); i++) {
				Object[] objects = (Object[]) allList.get(i);
				NmsPingDetail nmsPingDetail = new NmsPingDetail();
				nmsPingDetail.setPingRate((Float) objects[0]);
				nmsPingDetail.setPingRtt((Integer) objects[1]);
				nmsPingDetail.setItime(dateFormat
						.format((Timestamp) objects[2]));
				nmsPingDetailList.add(nmsPingDetail);
			}
		}
		nmsPingInfoDetail.setNmsPingInfoList(nmsPingDetailList);
		
		
		return nmsPingInfoDetail;
	}

	/**
	 * 如果不存在当日数据，统计数据库中已经存在的距当日最近的三天数据
	 * 
	 * @param assetId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public NmsPingStaticsInfo pingInfoServiceDetailPerformanceInfoV02(
			Integer assetId, String startDate, String endDate) throws Exception {

		// 1、如果资产id为空，返回空数据。
		NmsAsset nmsAsset = hibernateTemplate.get(NmsAsset.class, assetId);

		if (nmsAsset == null) {
			return new NmsPingStaticsInfo();
		}

		// 2、获取指定设备的最近一条记录
		DetachedCriteria currentCriteria = DetachedCriteria
				.forClass(NmsPingInfo.class);
		currentCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));
		currentCriteria.addOrder(Order.desc("id"));
		List<NmsPingInfo> nmsPingInfoList = (List<NmsPingInfo>) hibernateTemplate
				.findByCriteria(currentCriteria, 0, 1);
		NmsPingInfo theLastnmsPingInfo = null;
		if (nmsPingInfoList != null && nmsPingInfoList.size() > 0) {
			theLastnmsPingInfo = nmsPingInfoList.get(0);
		}
		// 3、根据最近一条记录的时间初始化时间相关对象
		Date theDateOfTheLastnmsPingInfo = new Date();
		if (theLastnmsPingInfo != null) {
			Timestamp thetimestampOfTheLastnmsPingInfo = theLastnmsPingInfo
					.getItime();
			try {
				theDateOfTheLastnmsPingInfo = thetimestampOfTheLastnmsPingInfo;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(theDateOfTheLastnmsPingInfo);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		String startOftheDateOfTheLastnmsPingInfo = dateFormat.format(calendar.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		String endOftheDateOfTheLastnmsPingInfo = dateFormat.format(calendar.getTime());
		if (startDate == null || startDate.length() == 0) {
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = dateFormat.format(calendar.getTime());
		}
		if (endDate == null || endDate.length() == 0) {
			calendar.setTime(theDateOfTheLastnmsPingInfo);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			endDate = dateFormat.format(calendar.getTime());
		}

		//4、获取接口"nmsPingInfoList"对应的结果数据集
		String allSql = "select ping_rate,ping_rtt,itime from nms_ping_info where asset_id="
				+ assetId
				+ " and itime between '"
				+ startDate
				+ "' and '"
				+ endDate + "' order by id desc";
		Session session = hibernateTemplate.getSessionFactory().openSession();
		Query allQuery = session.createSQLQuery(allSql);
		List<?> allList = allQuery.list();
		session.close();
		
        //5、获取最后一条记录当日性能统计数据
		StringBuilder builderSql = new StringBuilder(
				"select avg(ping_rate) avgPingRate,max(ping_rate) maxPingRate,avg(ping_rtt) avgPingRtt,max(ping_rtt) maxPingRtt from nms_ping_info where 1=1 ");
		builderSql.append(" and asset_id = ").append(assetId);
			builderSql.append(" and itime >= '")
					.append(startOftheDateOfTheLastnmsPingInfo).append("'");	
			builderSql.append(" and itime <= '")
					.append(endOftheDateOfTheLastnmsPingInfo).append("'");
			
		session = hibernateTemplate.getSessionFactory().openSession();
		Query query = session.createSQLQuery(builderSql.toString());
		List<?> oneList = query.list();
		session.close();
		
		//6、获取最后一条记录前三日性能统计数据
		StringBuilder threeBuilderSql = new StringBuilder(
				"select avg(ping_rate) avgPingRate,max(ping_rate) maxPingRate,avg(ping_rtt) avgPingRtt,max(ping_rtt) maxPingRtt from nms_ping_info where 1=1 ");
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			threeBuilderSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
			threeBuilderSql.append(" and itime <= '").append(endOftheDateOfTheLastnmsPingInfo)
					.append("'");
			
		session = hibernateTemplate.getSessionFactory().openSession();
		Query threeQuery = session.createSQLQuery(threeBuilderSql.toString());
		List<?> threeList = threeQuery.list();
		session.close();

		NmsPingStaticsInfo nmsPingInfoDetail = new NmsPingStaticsInfo();
		nmsPingInfoDetail.setAssetId(nmsAsset.getId());
		nmsPingInfoDetail.setAssetName(nmsAsset.getAName());
		nmsPingInfoDetail.setAssetIp(nmsAsset.getAIp());
		nmsPingInfoDetail.setOnline(nmsAsset.getOnline());

		if (nmsPingInfoList != null && nmsPingInfoList.size() > 0 && nmsAssetService.findOnlineById(assetId)) {
			NmsPingInfo nmsPingInfo = nmsPingInfoList.get(0);
			nmsPingInfoDetail.setCurrentPingRate(nmsPingInfo.getPingRate());
			nmsPingInfoDetail.setCurrentPingRtt(nmsPingInfo.getPingRtt());
		}
		if (oneList != null && oneList.size() > 0) {
			Object[] objects = (Object[]) oneList.get(0);
			nmsPingInfoDetail.setAveragePingRateOfToday((Double) objects[0]);
			nmsPingInfoDetail.setMaxPingRateOfToday((Float) objects[1]);
			nmsPingInfoDetail.setAveragePingRttOfToday((BigDecimal) objects[2]);
			nmsPingInfoDetail.setMaxPingRttOfToday((Integer) objects[3]);
		}
		if (threeList != null && threeList.size() > 0) {
			Object[] objects = (Object[]) oneList.get(0);
			nmsPingInfoDetail
					.setAveragePingRateOfTheLastThreeDays((Double) objects[0]);
			nmsPingInfoDetail
					.setMaxPingRateOfTheLastThreeDays((Float) objects[1]);
			nmsPingInfoDetail
					.setAveragePingRttOfTheLastThreeDays((BigDecimal) objects[2]);
			nmsPingInfoDetail
					.setMaxPingRttOfTheLastThreeDays((Integer) objects[3]);
		}

		List<NmsPingDetail> nmsPingDetailList = new ArrayList<NmsPingDetail>();

		if (allList != null && allList.size() > 0) {
			for (int i = 0; i < allList.size(); i++) {
				Object[] objects = (Object[]) allList.get(i);
				NmsPingDetail nmsPingDetail = new NmsPingDetail();
				nmsPingDetail.setPingRate((Float) objects[0]);
				nmsPingDetail.setPingRtt((Integer) objects[1]);
				nmsPingDetail.setItime(dateFormat
						.format((Timestamp) objects[2]));
				nmsPingDetailList.add(nmsPingDetail);
			}
		}
		nmsPingInfoDetail.setNmsPingInfoList(nmsPingDetailList);

		return nmsPingInfoDetail;
	}

	@SuppressWarnings("unchecked")
	public List<NmsPingInfo> getAll() {
		String hsql = "from NmsPingInfo";
		return (List<NmsPingInfo>) hibernateTemplate.find(hsql);
	}

	public boolean savePing(NmsPingInfo nc) {
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
	public PageBean<NmsPingInfo> getPageByDate(String orderKey,
			String orderValue, String startDate, String endDate, int begin,
			int offset, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from nms_ping_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlCount += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		SQLQuery queryCount = session.createSQLQuery(sqlCount);
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());

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
		String sqlList = "select * from nms_ping_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		sqlList += " order by " + orderKey + " limit " + (begin - 1) * offset + "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsPingInfo", NmsPingInfo.class);
		List<NmsPingInfo> list = queryList.list();
		session.close();
		
		for (NmsPingInfo nmsPingInfo : list) {
			nmsPingInfo.setPingRate(nmsPingInfo.getPingRate()*100);
		}

		// 创建PageBean对象返回数据
		PageBean<NmsPingInfo> page = new PageBean<NmsPingInfo>();
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
	
	@SuppressWarnings("unchecked")
	public List<NmsPingInfo> getPageByDateExportExcel(String orderKey,
			String orderValue, String startDate, String endDate, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "itime";
		}
		if (orderValue == null || orderValue.length() == 0 || orderValue.equals("1")) {
			orderValue = "1";
			orderKey = orderKey + " desc";
		} else {
			orderKey = orderKey + " asc";
		}

		// 获取list数据
		String sqlList = "select * from nms_ping_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		// 所有原始数据报表最多导出最新的10000条记录
		sqlList += " order by itime desc limit 10000";

		
		SQLQuery queryList = session.createSQLQuery(sqlList);

		// 序列化数据
		queryList.addEntity("NmsPingInfo", NmsPingInfo.class);
		List<NmsPingInfo> list = queryList.list();
		session.close();
		
		for (NmsPingInfo nmsPingInfo : list) {
			nmsPingInfo.setPingRate(nmsPingInfo.getPingRate()*100);
		}

		return list;
	}
	
	public boolean deleteByAssetId(String assetId) {
		boolean res = true;
		String sql = "delete from nms_ping_info where asset_id = " + assetId;
		Session session = hibernateTemplate.getSessionFactory().openSession();
		try {
			session.createSQLQuery(sql).executeUpdate();
		} catch (Exception e) {
			res = false;
		}
		session.close();
		return res;	
	}	
	
	
	@SuppressWarnings("unchecked")
	public float findPingRate(String assetId) {

		Session session = hibernateTemplate.getSessionFactory().openSession();

		String sql = " select * from nms_ping_info where asset_id  = " + assetId + " order by id desc limit 1";
		SQLQuery queryList = session.createSQLQuery(sql);


		queryList.addEntity("NmsPingInfo", NmsPingInfo.class);
		List<NmsPingInfo> list = queryList.list();
		session.close();
		
		float rate = 0;
		if (list.size() > 0) {
			NmsPingInfo obj = (NmsPingInfo)list.get(0);
			rate = obj.getPingRate();
		}

		return rate;
	}
}

package iie.service;

import iie.pojo.NmsAsset;
import iie.pojo.NmsMemInfo;
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

@Service("NmsMemInfoService")
public class NmsMemInfoService {

	@Autowired
	NmsAssetService nmsAssetService;

	@Autowired
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	public List<NmsMemInfo> getAll() {
		String hsql = "from NmsMemInfo";
		return (List<NmsMemInfo>) hibernateTemplate.find(hsql);
	}

	public boolean saveMem(NmsMemInfo nm) {
		try {
			nm.setId((long) 0);
			hibernateTemplate.save(nm);
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
	public PageBean<NmsMemInfo> getPageByDate(String orderKey,
			String orderValue, String startDate, String endDate, int begin,
			int offset, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from nms_mem_info as rt where rt.asset_id = '"
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
		String sqlList = "select * from nms_mem_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		sqlList += " order by " + orderKey + " limit " + (begin - 1) * offset
				+ "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);

		// 序列化数据
		queryList.addEntity("NmsMemInfo", NmsMemInfo.class);
		List<NmsMemInfo> list = queryList.list();
		session.close();

		// 创建PageBean对象返回数据
		PageBean<NmsMemInfo> page = new PageBean<NmsMemInfo>();
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
	 * 
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
	public PageBean<NmsMemInfo> getPageByDateV02(String orderKey,
			int orderValue, String startDate, String endDate, int begin,
			int offset) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from (select * from nms_mem_info order by itime asc) as rt where rt.itime between '"
				+ startDate + "' and '" + endDate + "'";
		SQLQuery queryCount = session.createSQLQuery(sqlCount);
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());

		if (orderValue != 0) {
			orderKey = orderKey + " desc";
		}

		// 获取list数据
		String sqlList = "select * from (select * from nms_mem_info order by itime asc) as rt where rt.itime between '"
				+ startDate
				+ "' and '"
				+ endDate
				+ "' order by "
				+ orderKey
				+ " limit " + (begin - 1) * offset + "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);

		// 序列化数据
		queryList.addEntity("NmsMemInfo", NmsMemInfo.class);
		List<NmsMemInfo> list = queryList.list();
		session.close();

		// 创建PageBean对象返回数据
		PageBean<NmsMemInfo> page = new PageBean<NmsMemInfo>();
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

	public NmsMemStaticsInfo memInfoServiceDetailMemInfo(Integer assetId,
			String startDate, String endDate) throws Exception {

		NmsAsset nmsAsset = hibernateTemplate.get(NmsAsset.class, assetId);

		if (nmsAsset == null) {
			return new NmsMemStaticsInfo();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		DetachedCriteria currentCriteria = DetachedCriteria
				.forClass(NmsMemInfo.class);
		currentCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		currentCriteria.add(Restrictions.ge("itime", calendar.getTime()));
		currentCriteria.addOrder(Order.desc("id"));

		List<NmsMemInfo> currentList = (List<NmsMemInfo>) hibernateTemplate
				.findByCriteria(currentCriteria);

		StringBuilder builderSql = new StringBuilder(
				"select max((mem_total-mem_free)/mem_total),avg((mem_total-mem_free)/mem_total),max((swap_total-swap_free)/swap_total),avg((swap_total-swap_free)/swap_total) from nms_mem_info where 1=1 ");
		builderSql.append(" and asset_id=").append(assetId);
		if (startDate != null && startDate.length() > 0) {
			builderSql.append(" and itime >= '").append(startDate).append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			builderSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}
		if (endDate != null && endDate.length() > 0) {
			builderSql.append(" and itime <= '").append(endDate).append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			builderSql.append(" and itime <= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		Session session = hibernateTemplate.getSessionFactory().openSession();
		Query query = session.createSQLQuery(builderSql.toString());
		List oneList = query.list();
		session.close();

		StringBuilder threeBuilderSql = new StringBuilder(
				"select max((mem_total-mem_free)/mem_total),avg((mem_total-mem_free)/mem_total),max((swap_total-swap_free)/swap_total),avg((swap_total-swap_free)/swap_total) from nms_mem_info where 1=1 ");
		threeBuilderSql.append(" and asset_id=").append(assetId);

		if (startDate != null && startDate.length() > 0) {
			calendar.setTime(dateFormat.parse(startDate));
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			threeBuilderSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
			threeBuilderSql.append(" and itime <= '").append(startDate)
					.append("'");
		} else {
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			threeBuilderSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		session = hibernateTemplate.getSessionFactory().openSession();
		Query threeQuery = session.createSQLQuery(threeBuilderSql.toString());
		List threeList = threeQuery.list();
		session.close();

		DetachedCriteria allMemCriteria = DetachedCriteria
				.forClass(NmsMemInfo.class);
		allMemCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));
		if (startDate != null && startDate.length() > 0) {
			allMemCriteria.add(Restrictions.ge("itime",
					Timestamp.valueOf(startDate)));
		} else {
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			allMemCriteria.add(Restrictions.ge("itime", calendar.getTime()));
		}
		if (endDate != null && endDate.length() > 0) {
			allMemCriteria.add(Restrictions.le("itime",
					Timestamp.valueOf(endDate)));
		} else {
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			allMemCriteria.add(Restrictions.le("itime", calendar.getTime()));
		}
		allMemCriteria.addOrder(Order.desc("id"));
		List<NmsMemInfo> allMemInfoList = (List<NmsMemInfo>) hibernateTemplate
				.findByCriteria(allMemCriteria);

		NmsMemStaticsInfo nmsMemStaticsInfo = new NmsMemStaticsInfo();

		nmsMemStaticsInfo.setAssetId(nmsAsset.getId());
		nmsMemStaticsInfo.setAssetName(nmsAsset.getAName());
		nmsMemStaticsInfo.setAssetIp(nmsAsset.getAIp());
		NmsMemData nmsMemData = new NmsMemData();
		NmsSwapData nmsSwapData = new NmsSwapData();
		if (currentList != null && currentList.size() > 0) {
			NmsMemInfo nmsMemInfo = currentList.get(0);
			nmsMemData.setCurrentMemTotal(nmsMemInfo.getMemTotal());
			nmsMemData
					.setCurrentMemRate(((nmsMemInfo.getMemTotal().doubleValue() - nmsMemInfo
							.getMemFree().doubleValue()) / nmsMemInfo
							.getMemTotal().doubleValue()));
			nmsSwapData.setCurrentSwapTotal(nmsMemInfo.getSwapTotal());
			nmsSwapData.setCurrentSwapRate((nmsMemInfo.getSwapTotal()
					.doubleValue() - nmsMemInfo.getSwapFree().doubleValue())
					/ nmsMemInfo.getSwapTotal().doubleValue());
		}

		if (oneList != null && oneList.size() > 0) {
			Object[] objects = (Object[]) oneList.get(0);
			if (objects[0] != null) {
				nmsMemData.setMaxMemRateOfToday(((BigDecimal) objects[0])
						.doubleValue());
				nmsMemData.setAverageMemRateOfToday(((BigDecimal) objects[1])
						.doubleValue());
				nmsSwapData.setMaxSwapRateOfToday(((BigDecimal) objects[2])
						.doubleValue());
				nmsSwapData.setAverageSwapRateOfToday(((BigDecimal) objects[3])
						.doubleValue());
			}
		}

		if (threeList != null && threeList.size() > 0) {
			Object[] objects = (Object[]) threeList.get(0);
			if (objects[0] != null) {
				nmsMemData
						.setMaxMemRateOfTheLastThreeDays(((BigDecimal) objects[0])
								.doubleValue());
				nmsMemData
						.setAverageMemRateOfTheLastThreeDays(((BigDecimal) objects[1])
								.doubleValue());
				nmsSwapData
						.setMaxSwapRateOfTheLastThreeDays(((BigDecimal) objects[2])
								.doubleValue());
				nmsSwapData
						.setAverageSwapRateOfTheLastThreeDays(((BigDecimal) objects[3])
								.doubleValue());
			}
		}
		List<NmsMemDataDetail> nmsMemDataDetailList = new ArrayList<NmsMemDataDetail>();
		List<NmsSwapDataDetail> swapDataDetailList = new ArrayList<NmsSwapDataDetail>();
		if (allMemInfoList != null && allMemInfoList.size() > 0) {
			for (NmsMemInfo nmsMemInfo : allMemInfoList) {
				String itime = dateFormat.format(nmsMemInfo.getItime());
				String swapRate = String
						.valueOf((nmsMemInfo.getMemTotal() - nmsMemInfo
								.getMemFree()) / nmsMemInfo.getMemTotal());
				String memRate = String
						.valueOf((nmsMemInfo.getSwapTotal() - nmsMemInfo
								.getSwapFree()) / nmsMemInfo.getSwapTotal());
				NmsMemDataDetail nmsMemDataDetail = new NmsMemDataDetail();
				NmsSwapDataDetail nmsSwapDataDetail = new NmsSwapDataDetail();
				nmsMemDataDetail.setItime(itime);
				nmsMemDataDetail.setMemRate(memRate);
				nmsSwapDataDetail.setItime(itime);
				nmsSwapDataDetail.setSwapRate(swapRate);
				nmsMemDataDetailList.add(nmsMemDataDetail);
				swapDataDetailList.add(nmsSwapDataDetail);
			}
		}
		nmsMemData.setData(nmsMemDataDetailList);
		nmsSwapData.setData(swapDataDetailList);
		nmsMemStaticsInfo.setMemData(nmsMemData);
		nmsMemStaticsInfo.setNmsSwapData(nmsSwapData);

		return nmsMemStaticsInfo;
	}

	public NmsMemStaticsInfo memInfoServiceDetailMemInfoV02(Integer assetId,
			String startDate, String endDate) throws Exception {

		NmsAsset nmsAsset = hibernateTemplate.get(NmsAsset.class, assetId);

		if (nmsAsset == null) {
			return new NmsMemStaticsInfo();
		}

		Calendar calendar = getDateOfTheLastNmsMemInfo(nmsAsset);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		DetachedCriteria currentCriteria = DetachedCriteria
				.forClass(NmsMemInfo.class);
		currentCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		currentCriteria.add(Restrictions.ge("itime", calendar.getTime()));
		currentCriteria.addOrder(Order.desc("id"));

		List<NmsMemInfo> currentList = (List<NmsMemInfo>) hibernateTemplate
				.findByCriteria(currentCriteria);

		StringBuilder builderSql = new StringBuilder(
				"select max((mem_total-mem_free)/mem_total),avg((mem_total-mem_free)/mem_total),max((swap_total-swap_free)/swap_total),avg((swap_total-swap_free)/swap_total) from nms_mem_info where 1=1 ");
		builderSql.append(" and asset_id=").append(assetId);
		if (startDate != null && startDate.length() > 0) {
			builderSql.append(" and itime >= '").append(startDate).append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			builderSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}
		if (endDate != null && endDate.length() > 0) {
			builderSql.append(" and itime <= '").append(endDate).append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			builderSql.append(" and itime <= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		Session session = hibernateTemplate.getSessionFactory().openSession();
		Query query = session.createSQLQuery(builderSql.toString());
		List oneList = query.list();
		session.close();
		
		StringBuilder threeBuilderSql = new StringBuilder(
				"select max((mem_total-mem_free)/mem_total),avg((mem_total-mem_free)/mem_total),max((swap_total-swap_free)/swap_total),avg((swap_total-swap_free)/swap_total) from nms_mem_info where 1=1 ");
		threeBuilderSql.append(" and asset_id=").append(assetId);

		if (startDate != null && startDate.length() > 0) {
			calendar.setTime(dateFormat.parse(startDate));
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			threeBuilderSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
			threeBuilderSql.append(" and itime <= '").append(startDate)
					.append("'");
		} else {
			calendar.setTime(getDateOfTheLastNmsMemInfo(nmsAsset).getTime());
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			threeBuilderSql.append(" and itime >= '")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		session = hibernateTemplate.getSessionFactory().openSession();
		Query threeQuery = session.createSQLQuery(threeBuilderSql.toString());
		List threeList = threeQuery.list();
		session.close();
		
		DetachedCriteria allMemCriteria = DetachedCriteria
				.forClass(NmsMemInfo.class);
		allMemCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));
		if (startDate != null && startDate.length() > 0) {
			allMemCriteria.add(Restrictions.ge("itime",
					Timestamp.valueOf(startDate)));
		} else {
			calendar.setTime(getDateOfTheLastNmsMemInfo(nmsAsset).getTime());
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			allMemCriteria.add(Restrictions.ge("itime", calendar.getTime()));
		}
		if (endDate != null && endDate.length() > 0) {
			allMemCriteria.add(Restrictions.le("itime",
					Timestamp.valueOf(endDate)));
		} else {
			calendar.setTime(getDateOfTheLastNmsMemInfo(nmsAsset).getTime());
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			allMemCriteria.add(Restrictions.le("itime", calendar.getTime()));
		}
		allMemCriteria.addOrder(Order.desc("id"));
		List<NmsMemInfo> allMemInfoList = (List<NmsMemInfo>) hibernateTemplate.findByCriteria(allMemCriteria);
		NmsMemStaticsInfo nmsMemStaticsInfo = new NmsMemStaticsInfo();

		nmsMemStaticsInfo.setAssetId(nmsAsset.getId());
		nmsMemStaticsInfo.setAssetName(nmsAsset.getAName());
		nmsMemStaticsInfo.setAssetIp(nmsAsset.getAIp());
		nmsMemStaticsInfo.setOnline(nmsAsset.getOnline());
		NmsMemData nmsMemData = new NmsMemData();
		NmsSwapData nmsSwapData = new NmsSwapData();
		if (currentList != null && currentList.size() > 0) {
			NmsMemInfo nmsMemInfo = currentList.get(0);
			if (nmsMemInfo.getMemTotal() != null && nmsMemInfo.getMemFree() != null && nmsAssetService.findOnlineById(assetId)) {
				nmsMemData.setCurrentMemTotal(nmsMemInfo.getMemTotal());
				nmsMemData.setCurrentMemRate(((nmsMemInfo.getMemTotal().doubleValue() - nmsMemInfo.getMemFree().doubleValue()) / nmsMemInfo.getMemTotal().doubleValue()));
				nmsSwapData.setCurrentSwapTotal(nmsMemInfo.getSwapTotal());
				nmsSwapData.setCurrentSwapRate((nmsMemInfo.getSwapTotal().doubleValue() - nmsMemInfo.getSwapFree().doubleValue()) / nmsMemInfo.getSwapTotal().doubleValue());
			} else {
				nmsMemData.setCurrentMemTotal(0L);
				nmsMemData.setCurrentMemRate(0D);
				nmsSwapData.setCurrentSwapTotal(0L);
				nmsSwapData.setCurrentSwapRate(0D);
			}
		}

		if (oneList != null && oneList.size() > 0) {
			Object[] objects = (Object[]) oneList.get(0);

			if (objects[0] != null) {
				nmsMemData.setMaxMemRateOfToday(((BigDecimal) objects[0])
						.doubleValue());
				nmsMemData.setAverageMemRateOfToday(((BigDecimal) objects[1])
						.doubleValue());

			} else {
				nmsMemData.setMaxMemRateOfToday(0.0);
				nmsMemData.setAverageMemRateOfToday(0.0);
			}
			if (objects[2] != null) {
				nmsSwapData.setMaxSwapRateOfToday(((BigDecimal) objects[2])
						.doubleValue());
				nmsSwapData.setAverageSwapRateOfToday(((BigDecimal) objects[3])
						.doubleValue());
			} else {
				nmsSwapData.setMaxSwapRateOfToday(0.0);
				nmsSwapData.setAverageSwapRateOfToday(0.0);
			}
		}

		if (threeList != null && threeList.size() > 0) {
			Object[] objects = (Object[]) threeList.get(0);
			if (objects[0] != null) {
				nmsMemData
						.setMaxMemRateOfTheLastThreeDays(((BigDecimal) objects[0])
								.doubleValue());
				nmsMemData
						.setAverageMemRateOfTheLastThreeDays(((BigDecimal) objects[1])
								.doubleValue());
			} else {
				nmsMemData.setMaxMemRateOfTheLastThreeDays(0.0);
				nmsMemData.setAverageMemRateOfTheLastThreeDays(0.0);
			}
			if (objects[2] != null) {
				nmsSwapData
						.setMaxSwapRateOfTheLastThreeDays(((BigDecimal) objects[2])
								.doubleValue());
				nmsSwapData
						.setAverageSwapRateOfTheLastThreeDays(((BigDecimal) objects[3])
								.doubleValue());
			} else {
				nmsSwapData.setMaxSwapRateOfTheLastThreeDays(0.0);
				nmsSwapData.setAverageSwapRateOfTheLastThreeDays(0.0);
			}
		}
		List<NmsMemDataDetail> nmsMemDataDetailList = new ArrayList<NmsMemDataDetail>();
		List<NmsSwapDataDetail> swapDataDetailList = new ArrayList<NmsSwapDataDetail>();
		if (allMemInfoList != null && allMemInfoList.size() > 0) {
			for (NmsMemInfo nmsMemInfo : allMemInfoList) {
				String itime = dateFormat.format(nmsMemInfo.getItime());
				String memRate = "0", swapRate = "0";
				if (nmsMemInfo != null && nmsMemInfo.getMemTotal() != null &&  nmsMemInfo.getMemFree() != null && nmsMemInfo.getMemTotal() != 0) {
					memRate = String.valueOf(new BigDecimal((nmsMemInfo.getMemTotal() - nmsMemInfo.getMemFree())*100.0 / nmsMemInfo.getMemTotal()*1.0).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				if (nmsMemInfo != null &&  nmsMemInfo.getSwapTotal() != null && nmsMemInfo.getSwapFree() != null && nmsMemInfo.getSwapTotal() != 0) {
					swapRate = String.valueOf(new BigDecimal((nmsMemInfo.getSwapTotal() - nmsMemInfo.getSwapFree())*100.0 / nmsMemInfo.getSwapTotal()*1.0).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				NmsMemDataDetail nmsMemDataDetail = new NmsMemDataDetail();
				NmsSwapDataDetail nmsSwapDataDetail = new NmsSwapDataDetail();
				nmsMemDataDetail.setItime(itime);
				nmsMemDataDetail.setMemRate(memRate);
				nmsSwapDataDetail.setItime(itime);
				nmsSwapDataDetail.setSwapRate(swapRate);
				nmsMemDataDetailList.add(nmsMemDataDetail);
				swapDataDetailList.add(nmsSwapDataDetail);
			}
		}
		nmsMemData.setData(nmsMemDataDetailList);
		nmsSwapData.setData(swapDataDetailList);
		nmsMemStaticsInfo.setMemData(nmsMemData);
		nmsMemStaticsInfo.setNmsSwapData(nmsSwapData);
		
		return nmsMemStaticsInfo;
	}

	@SuppressWarnings("unchecked")
	private Calendar getDateOfTheLastNmsMemInfo(NmsAsset nmsAsset) {
		Calendar calendar = Calendar.getInstance();
		DetachedCriteria currentCriteria = DetachedCriteria
				.forClass(NmsMemInfo.class);
		currentCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));
		currentCriteria.addOrder(Order.desc("id"));
		List<NmsMemInfo> nmsMemInfoList = (List<NmsMemInfo>) hibernateTemplate
				.findByCriteria(currentCriteria, 0, 1);
		Date theDateOfTheLastnmsMemInfo = new Date();
		if (nmsMemInfoList != null && nmsMemInfoList.size() > 0) {
			NmsMemInfo theLastNmsMemInfo = nmsMemInfoList.get(0);
			Timestamp thetimestampOfTheLastNmsMemInfo = theLastNmsMemInfo
					.getItime();
			try {
				theDateOfTheLastnmsMemInfo = thetimestampOfTheLastNmsMemInfo;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		calendar.setTime(theDateOfTheLastnmsMemInfo);
		return calendar;
	}

	@SuppressWarnings("unchecked")
	public List<NmsMemInfo> getPageByDateExportExcel(String orderKey,
			String orderValue, String startDate, String endDate, String assetId)
			throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "itime";
		}
		if (orderValue == null || orderValue.length() == 0
				|| orderValue.equals("1")) {
			orderValue = "1";
			orderKey = orderKey + " asc";
		} else {
			orderKey = orderKey + " desc";
		}

		// 获取list数据
		String sqlList = "select * from nms_mem_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		// 所有原始数据报表最多导出最新的10000条记录
		sqlList += " order by itime desc limit 10000";		
		SQLQuery queryList = session.createSQLQuery(sqlList);

		// 序列化数据
		queryList.addEntity("NmsMemInfo", NmsMemInfo.class);
		List<NmsMemInfo> list = queryList.list();
		session.close();

		return list;
	}

	public boolean deleteByAssetId(String assetId) {
		boolean res = true;
		String sql = "delete from nms_mem_info where asset_id = " + assetId;
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

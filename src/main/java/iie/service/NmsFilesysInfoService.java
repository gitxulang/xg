package iie.service;

import iie.pojo.NmsAsset;
import iie.pojo.NmsFilesysInfo;
import iie.tools.NmsFileDiskData;
import iie.tools.NmsFilePartsData;
import iie.tools.NmsFileSysStaticsInfo;
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
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("NmsFilesysInfoService")
public class NmsFilesysInfoService {

	@Autowired
	HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	public List<NmsFilesysInfo> getAll() {
		String hsql = "from NmsFilesysInfo";
		return (List<NmsFilesysInfo>) hibernateTemplate.find(hsql);
	}

	public boolean saveFilesys(NmsFilesysInfo nf) {
		try {
			nf.setId((long) 0);
			hibernateTemplate.save(nf);
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
	public PageBean<NmsFilesysInfo> getPageByDate(String orderKey,
			String orderValue, String startDate, String endDate, int begin,
			int offset, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from nms_filesys_info as rt where rt.asset_id = '"
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
		String sqlList = "select * from nms_filesys_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		sqlList += " order by " + orderKey + " limit " + (begin - 1) * offset
				+ "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsFilesysInfo", NmsFilesysInfo.class);
		List<NmsFilesysInfo> list = queryList.list();
		session.close();
		
		for (NmsFilesysInfo nmsFilesysInfo : list) {
			nmsFilesysInfo.setPartTotal(new BigDecimal(nmsFilesysInfo.getPartTotal()/1024/1024).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
			nmsFilesysInfo.setPartFree(new BigDecimal(nmsFilesysInfo.getPartFree()/1024/1024).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
		}


		// 创建PageBean对象返回数据
		PageBean<NmsFilesysInfo> page = new PageBean<NmsFilesysInfo>();
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
	public PageBean<NmsFilesysInfo> getPageByDateV02(String orderKey,
			int orderValue, String startDate, String endDate, int begin,
			int offset) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from (select * from nms_filesys_info order by itime asc) as rt where rt.itime between '"
				+ startDate + "' and '" + endDate + "'";
		SQLQuery queryCount = session.createSQLQuery(sqlCount);
		System.out.println(queryCount.list());
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());
	//	System.out.println(count);

		if (orderValue != 0) {
			orderKey = orderKey + " desc";
		}

		// 获取list数据
		String sqlList = "select * from (select * from nms_filesys_info order by itime asc) as rt where rt.itime between '"
				+ startDate
				+ "' and '"
				+ endDate
				+ "' order by "
				+ orderKey
				+ " limit " + (begin - 1) * offset + "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsFilesysInfo", NmsFilesysInfo.class);
		List<NmsFilesysInfo> list = queryList.list();
		session.close();
	
		for (NmsFilesysInfo nmsFilesysInfo : list) {
			nmsFilesysInfo.setPartTotal(nmsFilesysInfo.getPartTotal()/1024/1024);
			nmsFilesysInfo.setPartFree(nmsFilesysInfo.getPartFree()/1024/1024);
		}

		// 创建PageBean对象返回数据
		PageBean<NmsFilesysInfo> page = new PageBean<NmsFilesysInfo>();
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

	public NmsFileSysStaticsInfo filesysInfoServerDetailDiskUtilization(
			Integer assetId, String startDate, String endDate) {

		NmsAsset nmsAsset = hibernateTemplate.get(NmsAsset.class, assetId);

		if (nmsAsset == null) {
			return new NmsFileSysStaticsInfo();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		StringBuilder partBuilder = new StringBuilder(
				"select count(1) from nms_filesys_info a "
						+ "where a.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_cpu_info) as b where asset_id = "
						+ assetId + ") ");
		partBuilder.append(" and a.asset_id = ").append(assetId);

		if (startDate != null && startDate.length() > 0) {
			partBuilder.append(" and itime >='").append(startDate).append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			partBuilder.append(" and itime >='")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		if (endDate != null && endDate.length() > 0) {
			partBuilder.append(" and itime <='").append(endDate).append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			partBuilder.append(" and itime <='")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		Session session = hibernateTemplate.getSessionFactory().openSession();
		Query partQuery = session.createSQLQuery(partBuilder.toString());
		List partList = partQuery.list();

		String fileSysSql = "select distinct file_sys from nms_filesys_info where asset_id=" + assetId;
		Query fileSysQuery = session.createSQLQuery(fileSysSql);
		List fileSysList = fileSysQuery.list();
		session.close();
		
		
		List<NmsFilePartsData> partsDates = new ArrayList<NmsFilePartsData>();
		if (fileSysList != null && fileSysList.size() > 0) {
			for (int i = 0; i < fileSysList.size(); i++) {
				partsDates.add(getNmsFilePartData(nmsAsset,
						(String) fileSysList.get(i), startDate, endDate));
			}
		}

		NmsFileSysStaticsInfo nmsFileSysStaticsInfo = new NmsFileSysStaticsInfo();
		nmsFileSysStaticsInfo.setAssetId(nmsAsset.getId());
		nmsFileSysStaticsInfo.setAssetName(nmsAsset.getAName());
		nmsFileSysStaticsInfo.setAssetIp(nmsAsset.getAIp());
		if (partList != null && partList.size() > 0) {
			BigInteger partNum = (BigInteger) partList.get(0);
			nmsFileSysStaticsInfo.setPartNum(partNum.intValue());
		}

		nmsFileSysStaticsInfo.setPartsDates(partsDates);
		return nmsFileSysStaticsInfo;
	}

	private NmsFilePartsData getNmsFilePartData(NmsAsset nmsAsset,
			String fileSys, String startDate, String endDate) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(NmsFilesysInfo.class);

		detachedCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));
		detachedCriteria.add(Restrictions.eq("fileSys", fileSys));
		if (startDate != null && startDate.length() > 0) {
			detachedCriteria.add(Restrictions.ge("itime",
					Timestamp.valueOf(startDate)));
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			detachedCriteria.add(Restrictions.ge("itime", calendar.getTime()));
		}
		if (endDate != null && endDate.length() > 0) {
			detachedCriteria.add(Restrictions.le("itime",
					Timestamp.valueOf(endDate)));
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			detachedCriteria.add(Restrictions.le("itime", calendar.getTime()));
		}

		detachedCriteria.addOrder(Order.desc("id"));

		List<NmsFilesysInfo> list = (List<NmsFilesysInfo>) hibernateTemplate
				.findByCriteria(detachedCriteria);

		Float currentPartTotal = 0f;
		Float currentDiskUtilization = 0f;

		List<NmsFileDiskData> diskUtilizationData = new ArrayList<NmsFileDiskData>();
		if (list != null && list.size() > 0) {
			// todo
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			NmsFilesysInfo nmsFilesysInfo = list.get(0);
			currentPartTotal = nmsFilesysInfo.getPartTotal();
			currentDiskUtilization = (nmsFilesysInfo.getPartTotal() - nmsFilesysInfo
					.getPartFree()) / nmsFilesysInfo.getPartTotal();
			for (NmsFilesysInfo nmsFilesysInfo1 : list) {
				NmsFileDiskData nmsFileDiskData = new NmsFileDiskData();
				nmsFileDiskData.setPartTotal(nmsFilesysInfo1.getPartTotal());
				nmsFileDiskData.setDiskUtilization((nmsFilesysInfo1
						.getPartTotal() - nmsFilesysInfo1.getPartFree())
						/ nmsFilesysInfo1.getPartTotal());
				nmsFileDiskData.setItime(dateFormat.format(nmsFilesysInfo1
						.getItime()));
				diskUtilizationData.add(nmsFileDiskData);
			}
		}

		NmsFilePartsData nmsFilePartsData = new NmsFilePartsData();
		nmsFilePartsData.setFilesys(fileSys);
		nmsFilePartsData.setCurrentPartTotal(currentPartTotal);
		nmsFilePartsData.setCurrentDiskUtilization(currentDiskUtilization);
		nmsFilePartsData.setDiskUtilizationData(diskUtilizationData);
		return nmsFilePartsData;
	}

	@SuppressWarnings("unchecked")
	public NmsFileSysStaticsInfo filesysInfoServerDetailDiskUtilizationV02(
			Integer assetId, String startDate, String endDate) {

		// 1、如果资产id为空，返回空数据。
		NmsAsset nmsAsset = hibernateTemplate.get(NmsAsset.class, assetId);
		if (nmsAsset == null) {
			return new NmsFileSysStaticsInfo();
		}

		// 2、获取指定设备的最近一条记录
		DetachedCriteria currentCriteria = DetachedCriteria
				.forClass(NmsFilesysInfo.class);
		currentCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));
		currentCriteria.addOrder(Order.desc("id"));
		List<NmsFilesysInfo> nmsFilesysInfoList = (List<NmsFilesysInfo>) hibernateTemplate
				.findByCriteria(currentCriteria, 0, 1);
		NmsFilesysInfo theLastNmsFilesysInfo = new NmsFilesysInfo();
		if (nmsFilesysInfoList != null && nmsFilesysInfoList.size() > 0) {
			theLastNmsFilesysInfo = nmsFilesysInfoList.get(0);
	//		System.out.println("test" + nmsFilesysInfoList.size());
		}
		// 3、根据最近一条记录的时间初始化时间相关对象
		Date theDateOfTheLastNmsFilesysInfo = new Date();

		if (theLastNmsFilesysInfo != null) {
			Timestamp thetimestampOfTheLastNmsFilesysInfo = theLastNmsFilesysInfo
					.getItime();
		//	System.out.println("NULL");
			try {
				theDateOfTheLastNmsFilesysInfo = thetimestampOfTheLastNmsFilesysInfo;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
	//	System.out.println("--DATE--" + theDateOfTheLastNmsFilesysInfo);
		Calendar calendar = Calendar.getInstance();
		if (theDateOfTheLastNmsFilesysInfo == null) {
			theDateOfTheLastNmsFilesysInfo = new Date();
		}
		calendar.setTime(theDateOfTheLastNmsFilesysInfo);
		if (startDate == null || startDate.length() == 0) {
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = dateFormat.format(calendar.getTime());
		}
		if (endDate == null || endDate.length() == 0) {
			calendar.setTime(theDateOfTheLastNmsFilesysInfo);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			endDate = dateFormat.format(calendar.getTime());
		}

		StringBuilder partBuilder = new StringBuilder(
				"select count(1) from nms_filesys_info a "
						+ "where a.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_filesys_info) as b where asset_id = "
						+ assetId + ") ");
		partBuilder.append(" and a.asset_id = ").append(assetId);

		if (startDate != null && startDate.length() > 0) {
			partBuilder.append(" and itime >='").append(startDate).append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			partBuilder.append(" and itime >='")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		if (endDate != null && endDate.length() > 0) {
			partBuilder.append(" and itime <='").append(endDate).append("'");
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			partBuilder.append(" and itime <='")
					.append(dateFormat.format(calendar.getTime())).append("'");
		}

		Session session = hibernateTemplate.getSessionFactory().openSession();
		Query partQuery = session.createSQLQuery(partBuilder.toString());
		List<?> partList = partQuery.list();

		String fileSysSql = "select distinct file_sys from nms_filesys_info where asset_id = " + assetId + " and freq = (select max(freq) from nms_filesys_info where asset_id = " + assetId+ " ) ";		
		Query fileSysQuery = session.createSQLQuery(fileSysSql);
		List<?> fileSysList = fileSysQuery.list();
		session.close();

		List<NmsFilePartsData> partsDates = new ArrayList<NmsFilePartsData>();
		if (fileSysList != null && fileSysList.size() > 0) {
			for (int i = 0; i < fileSysList.size(); i++) {
				partsDates.add(getNmsFilePartDataV02(nmsAsset,
						(String) fileSysList.get(i), startDate, endDate));
			}
		}

		NmsFileSysStaticsInfo nmsFileSysStaticsInfo = new NmsFileSysStaticsInfo();
		nmsFileSysStaticsInfo.setAssetId(nmsAsset.getId());
		nmsFileSysStaticsInfo.setAssetName(nmsAsset.getAName());
		nmsFileSysStaticsInfo.setAssetIp(nmsAsset.getAIp());
		nmsFileSysStaticsInfo.setOnline(nmsAsset.getOnline());
		if (partList != null && partList.size() > 0) {
			BigInteger partNum = (BigInteger) partList.get(0);
			nmsFileSysStaticsInfo.setPartNum(partNum.intValue());
		}
		
		nmsFileSysStaticsInfo.setPartsDates(partsDates);
		return nmsFileSysStaticsInfo;
	}

	@SuppressWarnings("unchecked")
	private NmsFilePartsData getNmsFilePartDataV02(NmsAsset nmsAsset,
			String fileSys, String startDate, String endDate) {

		Calendar calendar = getDateOfTheLastNmsFilesysInfo(nmsAsset);
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(NmsFilesysInfo.class);

		detachedCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));
		detachedCriteria.add(Restrictions.eq("fileSys", fileSys));
		if (startDate != null && startDate.length() > 0) {
			detachedCriteria.add(Restrictions.ge("itime",
					Timestamp.valueOf(startDate)));
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			detachedCriteria.add(Restrictions.ge("itime", calendar.getTime()));
		}
		if (endDate != null && endDate.length() > 0) {
			detachedCriteria.add(Restrictions.le("itime",
					Timestamp.valueOf(endDate)));
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			detachedCriteria.add(Restrictions.le("itime", calendar.getTime()));
		}

		detachedCriteria.addOrder(Order.desc("id"));

		List<NmsFilesysInfo> list = (List<NmsFilesysInfo>) hibernateTemplate
				.findByCriteria(detachedCriteria);

		Float currentPartTotal = 0f;
		Float currentDiskUtilization = 0f;

		List<NmsFileDiskData> diskUtilizationData = new ArrayList<NmsFileDiskData>();
		if (list != null && list.size() > 0) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			NmsFilesysInfo nmsFilesysInfo = list.get(0);
			currentPartTotal = nmsFilesysInfo.getPartTotal();
			if (currentPartTotal != 0f) {
				currentDiskUtilization = (nmsFilesysInfo.getPartTotal() - nmsFilesysInfo
						.getPartFree()) / nmsFilesysInfo.getPartTotal();
			} else {
				currentDiskUtilization = 0.00f;
			}
			if (currentDiskUtilization < 0f) {
				currentDiskUtilization = 0.00f;
			}
			for (NmsFilesysInfo nmsFilesysInfo1 : list) {
				NmsFileDiskData nmsFileDiskData = new NmsFileDiskData();
				nmsFileDiskData.setPartTotal(nmsFilesysInfo1.getPartTotal());
				nmsFileDiskData.setDiskUtilization((nmsFilesysInfo1
						.getPartTotal() - nmsFilesysInfo1.getPartFree())
						/ nmsFilesysInfo1.getPartTotal());
				nmsFileDiskData.setItime(dateFormat.format(nmsFilesysInfo1
						.getItime()));
				diskUtilizationData.add(nmsFileDiskData);
			}
		}

		NmsFilePartsData nmsFilePartsData = new NmsFilePartsData();
		nmsFilePartsData.setFilesys(fileSys);
		nmsFilePartsData.setCurrentPartTotal(currentPartTotal);
		nmsFilePartsData.setCurrentDiskUtilization(currentDiskUtilization);
		nmsFilePartsData.setDiskUtilizationData(diskUtilizationData);
		return nmsFilePartsData;
	}

	@SuppressWarnings("unchecked")
	private Calendar getDateOfTheLastNmsFilesysInfo(NmsAsset nmsAsset) {
		Calendar calendar = Calendar.getInstance();
		DetachedCriteria currentCriteria = DetachedCriteria
				.forClass(NmsFilesysInfo.class);
		currentCriteria.add(Restrictions.eq("nmsAsset", nmsAsset));
		currentCriteria.addOrder(Order.desc("id"));
		List<NmsFilesysInfo> nmsFilesysInfoList = (List<NmsFilesysInfo>) hibernateTemplate
				.findByCriteria(currentCriteria, 0, 1);
		Date theDateOfTheLastNmsFilesysInfo = new Date();
		if (nmsFilesysInfoList != null && nmsFilesysInfoList.size() > 0) {
			NmsFilesysInfo theLastNmsFilesysInfo = nmsFilesysInfoList.get(0);
			Timestamp thetimestampOfTheLastNmsFilesysInfo = theLastNmsFilesysInfo
					.getItime();
			try {
				theDateOfTheLastNmsFilesysInfo = thetimestampOfTheLastNmsFilesysInfo;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		calendar.setTime(theDateOfTheLastNmsFilesysInfo);
		return calendar;
	}

	@SuppressWarnings("unchecked")
	public List<NmsFilesysInfo> getPageByDateExportExcel(String orderKey,
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
		String sqlList = "select * from nms_filesys_info as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}

		// 所有原始数据报表最多导出最新的10000条记录
		sqlList += " order by itime desc limit 10000";
		
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsFilesysInfo", NmsFilesysInfo.class);
		List<NmsFilesysInfo> list = queryList.list();
		session.close();
		
		for (NmsFilesysInfo nmsFilesysInfo : list) {
			nmsFilesysInfo.setPartTotal(new BigDecimal(nmsFilesysInfo.getPartTotal()/1024/1024).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
			nmsFilesysInfo.setPartFree(new BigDecimal(nmsFilesysInfo.getPartFree()/1024/1024).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
		}


		return list;
	}
	
	public boolean deleteByAssetId(String assetId) {
		boolean res = true;
		String sql = "delete from nms_filesys_info where asset_id = " + assetId;
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

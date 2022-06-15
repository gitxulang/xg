package iie.service;

import iie.pojo.NmsAsset;
import iie.pojo.NmsYthApp;
import iie.tools.PageBean;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("NmsAppInfoService")
public class NmsAppInfoService {

	@Autowired
	HibernateTemplate hibernateTemplate;

	public int getSoftNum(int assetId) {
		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sqlCount = "select count(1) from nms_yth_app as rt where rt.asset_id = '"
				+ assetId + "'";
		SQLQuery queryCount = session.createSQLQuery(sqlCount);
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());

		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<NmsYthApp> getAll() {
		String hsql = "from NmsYthApp";
		return (List<NmsYthApp>) hibernateTemplate.find(hsql);
	}

	public boolean saveSoft(NmsYthApp np) {
		try {
			np.setId((long) 0);
			hibernateTemplate.save(np);
		} catch (Exception e) {
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
	public PageBean<NmsYthApp> getPageByDate(String orderKey,
			String orderValue, String startDate, String endDate, int begin,
			int offset, String assetId) throws Exception {

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();

		// 获取count总数
		String sqlCount = "select count(1) from nms_yth_app as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlCount += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		SQLQuery queryCount = session.createSQLQuery(sqlCount);
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());

		
		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "id";
		}
		
		String orderKey1 = "";
		if (orderValue == null || orderValue.length() == 0 || orderValue.equals("1")) {  
			orderKey1 = orderKey + " desc";
			orderValue = "1";
		} else {
			orderKey1 = orderKey + " asc";
			orderValue = "0";
		}

		// 获取list数据
		String sqlList = "select * from nms_yth_app as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		sqlList += " order by " + orderKey1 + " limit " + (begin - 1) * offset + "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsYthApp", NmsYthApp.class);
		List<NmsYthApp> list = queryList.list();
		session.close();
		
		// 创建PageBean对象返回数据
		PageBean<NmsYthApp> page = new PageBean<NmsYthApp>();
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
	public PageBean<NmsYthApp> getPageByAssetId(String orderKey,
			String orderValue, int begin, int offset, String assetId, String appName, String appPort) {

		if (orderKey == null || orderKey.length() == 0) {
			orderKey = "id";
		}
		String orderKey1 = "";
		if (orderValue == null || orderValue.length() == 0 || orderValue.equals("1")) {
			orderKey1 = orderKey + " desc";
			orderValue = "1";
		} else {
			orderKey1 = orderKey + " asc";
			orderValue = "0";
		}

		// 获取session
		Session session = hibernateTemplate.getSessionFactory().openSession();
		// 获取count总数
		StringBuilder sqlCountBuilder = new StringBuilder(
				"select count(1) from nms_yth_app  npi where npi.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_yth_app) as b where asset_id= "
						+ assetId + ")");
		sqlCountBuilder.append(" and npi.asset_id = ").append(assetId);

		
		if (appName != null) {
			sqlCountBuilder.append(" and npi.appName like '%").append(appName).append("%' "); 
		}
		
		if (appPort != null) {
			sqlCountBuilder.append(" and npi.appPort like '%").append(appPort).append("%' "); 
		}
		
		SQLQuery queryCount = session.createSQLQuery(sqlCountBuilder.toString());
		Integer count = Integer.valueOf(queryCount.list().get(0).toString());

		// 获取list数据
		String sqlList = "select * from nms_yth_app  npi where npi.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_yth_app) as b where asset_id= "
				+ assetId + ") and npi.asset_id = " + assetId;
		
		if (appName != null) {
			sqlList += " and npi.appName like '%" + appName + "%' "; 
		}
		
		if (appPort != null) {
			sqlList += " and npi.appPort = '" + appPort + "' "; 
		}
		
		sqlList += " order by " + orderKey1 + " limit " + (begin - 1) * offset
				+ "," + offset;
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsYthApp", NmsYthApp.class);
		List<NmsYthApp> list = queryList.list();
		session.close();

		// 创建PageBean对象返回数据
		PageBean<NmsYthApp> page = new PageBean<NmsYthApp>();
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
	public List<NmsYthApp> getListByAssetId(String assetId, int limit) {
		Session session = hibernateTemplate.getSessionFactory().openSession();
		String sqlList = "select * from nms_yth_app  npi where npi.freq = (select max(freq) from (SELECT DISTINCT asset_id,freq FROM nms_yth_app) as b where asset_id= "
				+ assetId + ") and npi.asset_id = " + assetId + " order by id desc limit " + limit;
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsYthApp", NmsYthApp.class);
		List<NmsYthApp> list = queryList.list();
		session.close();
		if (list == null) {
			list = new ArrayList<NmsYthApp>();
		}
		return list;
	}	
	
	
	@SuppressWarnings("unchecked")
	public List<NmsYthApp> getPageByDateExportExcel(String orderKey,
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
		String sqlList = "select * from nms_yth_app as rt where rt.asset_id = '"
				+ assetId + "'";
		if (startDate != null && endDate != null) {
			sqlList += " and rt.itime between '" + startDate + "' and '"
					+ endDate + "'";
		}
		
		// 所有原始数据报表最多导出最新的10000条记录
		sqlList += " order by itime desc limit 10000";	
		
		SQLQuery queryList = session.createSQLQuery(sqlList);
		queryList.addEntity("NmsYthApp", NmsYthApp.class);
		List<NmsYthApp> list = queryList.list();
		session.close();

		return list;
	}
	
	public boolean deleteByAssetId(String assetId) {
		boolean res = true;
		String sql = "delete from nms_yth_app where asset_id = " + assetId;
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
